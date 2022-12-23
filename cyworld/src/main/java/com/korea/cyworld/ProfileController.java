package com.korea.cyworld;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dao.DiaryDAO;
import dao.GalleryDAO;
import dao.GuestBookDAO;
import dao.MainDAO;
import dao.SignUpDAO;
import util.Common;
import vo.BuyMinimiVO;
import vo.IlchonVO;
import vo.SignUpVO;

@Controller
public class ProfileController {
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
	// 프로필 조회
	@RequestMapping("/profile.do")
	public String profile(int idx, Model model) {
		// 프로필에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 로그인한 유저의 idx와 해당 미니홈피 유저의 idx가 다를경우 - 프로필 변경창은 오로지 미니홈피 주인만 들어갈 수 있다
		if ( (int)session.getAttribute("login") != idx ) {
			// 해당 미니홈피 유저의 메인 페이지로 이동
			return "redirect:main.do?idx=" + idx ;
		}
		
		// 그 다음 idx에 해당하는 프로필 조회
		SignUpVO idxVo = signUp_dao.selectOneIdx(idx);
		// 조회된 프로필 정보를 바인딩
		model.addAttribute("signVo", idxVo);
		// 추가로 세션값도 바인딩
		model.addAttribute("sessionIdx", session.getAttribute("login"));
		
		// 그 다음 일촌 관계를 알아보기 위해 IlchonVO를 생성
		IlchonVO ilchonVo = new IlchonVO();
		
		// 맞일촌 상태를 알리는 ilchonUp을 2로 지정
		ilchonVo.setIlchonUp(2);
		// 일촌 idx에 idx를 지정
		ilchonVo.setIlchonSession((int)session.getAttribute("login"));
		// 그 다음 idx에 해당하는 일촌 조회
		List<IlchonVO> ilchonList = main_dao.selectIlchonList(ilchonVo);
		// 조회된 맞일촌을 리스트 형태로 바인딩
		model.addAttribute("ilchonList", ilchonList);
		
		// 프로필 페이지로 이동
		return Common.P_PATH + "profile.jsp";
	}
	
	// 미니미 수정 팝업 페이지 이동
	@RequestMapping("/profile_minimi_popup.do")
	public String popup(Integer idx, Model model) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// idx에 해당하는 프로필 조회
		SignUpVO svo = signUp_dao.selectOneIdx(idx);
		// 조회된 프로필 정보중 미니미 정보를 바인딩
		model.addAttribute("minimi", svo.getMinimi());
		// 조회된 프로필 정보중 도토리 개수를 바인딩
		model.addAttribute("dotory", svo.getDotoryNum());
		
		// idx에 해당하는 구매한 미니미 조회
		List<BuyMinimiVO> bvo = signUp_dao.selectBuyMinimiList(idx);
		// 조회된 구매한 미니미를 리스트 형태로 바인딩
		model.addAttribute("buyMinimi", bvo);
		
		// 미니미 팝업 페이지로 이동
		return Common.P_PATH + "minimiPopUp.jsp";
	}
	
	// 미니미 변경
	@RequestMapping("/profile_minimi_change.do")
	public String minimiChange(SignUpVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 변경할 미니미 정보로 갱신
		signUp_dao.updateMinimi(vo);
		// idx를 들고 미니미 팝업 페이지 URL로 이동
		return "redirect:profile_minimi_popup.do?idx=" + vo.getIdx();
	}
	
	// 미니미 구매
	@RequestMapping("/profile_minimi_buy.do")
	@ResponseBody
	public String minimiBuy(SignUpVO svo, BuyMinimiVO bvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 미니미를 구매한 idx를 지정
		bvo.setBuyIdx(svo.getIdx());
		
		// 이미 구매한 미니미인지 조회 - 중복 구매 방지
		BuyMinimiVO idxMinimi = signUp_dao.selectIdxBuyMinimi(bvo);
		
		// 이미 구매한 미니미일 경우
		if ( idxMinimi != null ) {
			return "no";
		// 아직 구매하지 않은 미니미일 경우
		} else {
			// 미니미를 구매하고 줄어든 도토리 보유 개수 갱신
			signUp_dao.updateDotoryNum(svo);
			
			// 구매한 미니미를 저장
			signUp_dao.insertBuyMinimi(bvo);
			
			return "yes";
		}
	}
	
	// 프로필 좌측 - 메인 사진 및 메인 소개글 수정
	@RequestMapping("/profile_modify_main.do")
	public String profileModifyMain(SignUpVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 메인 사진 업로드를 위해 상대 경로를 통한 절대 경로를 생성
		String webPath = "/resources/mainphoto/"; // 상대 경로
		String savePath = app.getRealPath(webPath); // 절대 경로
		
		// 메인 사진 업로드를 위해 파라미터로 넘어온 사진의 정보
		MultipartFile mainPhotoFile = vo.getMainPhotoFile();
		
		// 업로드된 사진이 없을 경우 이미 저장되어 있는 사진 이름 지정
		String mainPhoto = vo.getMainPhoto();
		
		// 업로드된 사진이 있을 경우
		if ( !mainPhotoFile.isEmpty() ) {
			// 이미 저장되어 있는 사진 삭제
			File delFile = new File(savePath, vo.getMainPhoto());
			delFile.delete();
			// 업로드된 사진의 원본 이름을 지정
			mainPhoto = mainPhotoFile.getOriginalFilename();
			// 사진 저장할 경로를 지정
			File saveFile = new File(savePath, mainPhoto);
			// 저장할 경로가 없을 경우
			if(!saveFile.exists()) {
				// 경로를 생성
				saveFile.mkdirs();
			// 저정할 경로가 있을 경우
			}else {
				// 사진 이름 중복 방지를 위해 사진 이름 앞에 시간 추가
				long time = System.currentTimeMillis();
				mainPhoto = String.format("%d_%s", time, mainPhoto);
				saveFile = new File(savePath, mainPhoto);
			}
			// 업로드된 사진을 실제로 저장 - try~catch 필요
			try {
				// 업로드된 사진을 실제로 저장해 주는 메소드
				mainPhotoFile.transferTo(saveFile);
				// 사진 이름 지정
				vo.setMainPhoto(mainPhoto);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 업로드된 사진 정보로 갱신
		signUp_dao.updateMain(vo);
		// idx를 들고 프로필 페이지 URL로 이동
		return "redirect:profile.do?idx=" + vo.getIdx();
	}
	
	// 프로필 우측 - 메인 타이틀 및 비밀번호 수정
	@RequestMapping("/profile_modify_userdata.do")
	@ResponseBody
	public String profileModifyUserData(SignUpVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 수정 실패할 경우
		String result = "no";
		
		/* 플랫폼이 cyworld일때와 소셜일 때 구분
		 * cyworld 가입자는 아이디와 비밀번호를 작성해서 가입했기에 비밀번호 변경이 있다
		 * 소셜 가입자는 아이디와 비밀번호를 소셜 플랫폼 것을 가져다 쓰기에 비밀번호 변경이 없다
		 */
		
		// cyworld 가입자
		if ( vo.getPlatform().equals("cyworld") ) {
			// 비밀번호 및 메인 타이틀 수정
			int res = signUp_dao.updateUserData(vo);
			if ( res == 1 ) {
				// 수정 성공할 경우
				result = "yes";
			}
			// 콜백 메소드에 전달
			return result;
		// 소셜 가입자
		} else {
			// 비밀번호 X, 메인 타이틀만 수정
			int res = signUp_dao.updateSocialUserData(vo);
			if ( res == 1 ) {
				// 수정 성공할 경우
				result = "yes";
			}
			// 콜백 메소드에 전달
			return result;
		}
	}
}