package com.korea.cyworld;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.DiaryDAO;
import dao.GalleryDAO;
import dao.GuestBookDAO;
import dao.MainDAO;
import dao.SignUpDAO;
import util.Common;
import vo.DiaryVO;
import vo.SignUpVO;

@Controller
public class DiaryController {
	// @Autowired
	@Autowired
	ServletContext app;// 현재 프로젝트의 기본 정보들을 저장하고 있는 클래스

	@Autowired
	HttpServletRequest request;

	// SignUpDAO
	SignUpDAO signUp_dao; // 로그인 및 회원가입 DAO
	MainDAO main_dao; // 메인 페이지 DAO
	GalleryDAO gallery_dao; // 사진첩 DAO
	GuestBookDAO guestbook_dao; // 방명록 DAO
	DiaryDAO diary_dao; // 다이어리 DAO
	
	// SI / CI 방식
	public void setSignUp_dao(SignUpDAO signUp_dao) {
		this.signUp_dao = signUp_dao;
	}
	public void setMain_dao(MainDAO main_dao) {
		this.main_dao = main_dao;
	}
	public void setGallery_dao(GalleryDAO gallery_dao) {
		this.gallery_dao = gallery_dao;
	}
	public void setGuestbook_dao(GuestBookDAO guestbook_dao) {
		this.guestbook_dao = guestbook_dao;
	}
	public void setDiary_dao(DiaryDAO diary_dao) {
	 	this.diary_dao = diary_dao;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 다이어리 조회
	@RequestMapping("/diary.do")
	public String list(int idx,Model model) {
		// 다이어리에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 비회원이 접근할 경우
		if ( (int)session.getAttribute("login") < 0 ) {
			// 해당 미니홈피 유저의 메인 페이지로 이동
			return "redirect:main.do?idx=" + idx ;
		}
		
		// 그 다음 idx에 해당하는 다이어리의 모든 글을 조회
		List<DiaryVO> list = diary_dao.selectList(idx);
		// 조회된 모든 다이어리 글을 리스트 형태로 바인딩
		model.addAttribute("list", list);
		
		// 그 다음 idx에 해당하는 유저 정보를 조회
		SignUpVO svo = signUp_dao.selectOneIdx(idx);
		// 조회된 유저 정보를 바인딩
		model.addAttribute("signVo", svo);
		// 추가로 세션값도 바인딩
		model.addAttribute("sessionIdx", session.getAttribute("login"));
		
		// 다이어리 페이지로 이동
		return Common.DP_PATH + "diary_list.jsp";
	}
	
	// 다이어리 글 작성 페이지로 이동
	@RequestMapping("/diary_insert_form.do")
	public String insert_form() {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 세션값이 있다면 작성 페이지로 이동
		return Common.DP_PATH + "diary_insert_form.jsp";
	}

	// 다이어리 새 글 작성
	@RequestMapping("/diary_insert.do")
	public String insert(DiaryVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 해당 idx의 다이어리에 작성된 글 개수 조회
		int countNum = diary_dao.selectCountNum(vo.getDiaryIdx());
		
		// 다이어리에 글이 한 개도 없을경우
		if ( countNum == 0 ) {
			// 다이어리에 시작번호 1 부여
			vo.setDiaryContentRef(1);
			// 작성한 다이어리 글을 저장
			diary_dao.insert(vo);
			// idx를 들고 다이어리 페이지 URL로 이동
			return "redirect:diary.do?idx=" + vo.getDiaryIdx();
		// 작성된 다이어리 글이 있을 경우
		} else {
			// 가장 최근에 작성한 다이어리 글의 번호에 1 더하기
			vo.setDiaryContentRef(countNum + 1);
			// 작성한 다이어리 글을 저장
			diary_dao.insert(vo);
			// idx를 들고 다이어리 페이지 URL로 이동
			return "redirect:diary.do?idx=" + vo.getDiaryIdx();
		}
	}
	
	// 다이어리 글 삭제
	@RequestMapping("/diary_delete.do")
	@ResponseBody
	public String delete(DiaryVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 삭제할 다이어리의 글에 idx와 ref를 글을 삭제 후에도 계속 사용하기 위해 VO에서 따로 분리해서 변수에 각각 저장한다
		int idx = vo.getDiaryIdx(); // 삭제할 다이어리의 idx
		int ref = vo.getDiaryContentRef(); // 삭제할 다이어리 글 번호
		
		// 그리고 idx와 ref를 사용하기 편하게 Map으로 만들어 놓는다
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("1", idx); // 삭제할 다이어리의 idx 저장
		map.put("2", ref); // 삭제할 다이어리의 글 번호 저장
		
		// DB에 저장된 다이어리 글 중 가져온 정보에 해당하는 다이어리 글 삭제
		int res = diary_dao.delete(vo);
		
		// 삭제 실패할 경우
		String result = "no";
		
		if (res == 1) {
			// 삭제 성공할 경우
			result = "yes";
			
			// 다이어리 글 삭제 후 다이어리 글 번호 재정렬
			// 다이어리 글 삭제 후 삭제한 다이어리 글 번호보다 큰 번호의 다이어리 글들 조회
			List<DiaryVO> list = diary_dao.selectListDelete(map);
			// forEach문
			for ( DiaryVO uref : list ) {
				// 조회된 다이어리 글 번호들을 1씩 감소
				uref.setDiaryContentRef(uref.getDiaryContentRef() - 1);
				// 1씩 감소된 번호들을 다시 갱신
				diary_dao.updateRefMinus(uref);
			}
		}
		
		// 콜백 메소드에 전달
		return result;
	}
	
	// 다이어리 글 수정 페이지로 이동
	@RequestMapping("/diary_modify_form.do")
	public String modify_form(Model model, DiaryVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 해당 idx의 다이어리에 수정할 글을 조회
		DiaryVO updateVo = diary_dao.selectOne(vo);
		if (updateVo != null) {
			// 조회된 다이어리 글을 바인딩
			model.addAttribute("vo", updateVo);
		}
		
		// 수정 페이지로 이동
		return Common.DP_PATH + "diary_modify_form.jsp";
	}
	
	// 다이어리 글 수정하기
	@RequestMapping("/diary_modify.do")
	@ResponseBody
	public String modify(DiaryVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 수정된 다이어리 글로 갱신
		int res = diary_dao.update(vo);
		
		// 갱신 실패할 경우 - JSON형태
		String result = "{'result':'no'}";
		if (res != 0) {
			// 갱신 성공할 경우 - JSON형태
			result = "{'result':'yes'}";
		}
		
		// 콜백 메소드에 전달
		return result;
	}
}