package com.korea.cyworld;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import vo.GuestBookLikeVO;
import vo.GuestBookVO;
import vo.IlchonVO;
import vo.SignUpVO;
import vo.ViewsVO;

@Controller
public class GuestBookController {
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
	// 방명록 조회
	@RequestMapping("/guestbook.do")
	public String guestbook_list (Integer idx, Model model) {
		// 방명록에 들어오면 가장 먼저 세션값이 있는지 확인
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
		
		// 조회수 구역 시작 //
		
		// 먼저 접속 날짜를 기록하기 위해 Date객체 사용
		Date date = new Date();
		// Date객체를 그냥 사용하면 뒤에 시간까지 모두 기록되기에 날짜만 따로 뺴는 작업을 한다
		SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
		
		// 그리고 앞으로 사용할 로그인한 유저의 idx와 해당 미니홈피의 idx와 접속 날짜를 편하게 사용하기 위해 Map으로 만들어 둔다
		HashMap<String, Object> todayMap = new HashMap<String, Object>();
		todayMap.put("1", idx); // 로그인한 유저의 idx
		todayMap.put("2", session.getAttribute("login")); // 해당 미니홈피 유저의 idx
		todayMap.put("3", today.format(date)); // 접속 날짜
		
		// 세션값이 비회원이 아닐 경우 - 세션값이 비회원일 경우 조회수 증가 X
		if ( (int)session.getAttribute("login") > 0 ) {
			
			// 세션값과 idx값이 다를 경우 - 타 유저 미니홈피 조회 - 조회수 증가 O
			if ( ( (int)session.getAttribute("login") != idx ) ) {
				
				// 그 다음 로그인한 유저의 현재 날짜로 접속 기록이 있는지 조회
				ViewsVO loginUser = main_dao.selectViewsToday(todayMap);
				
				// 그 다음 idx에 해당하는 미니홈피 유저정보를 조회
				SignUpVO miniUser = signUp_dao.selectOneIdx(idx);
				
				// 로그인한 유저의 조회된 기록이 있을 경우
				if ( loginUser != null ) {
					
					// 로그인한 유저의 조회된 기록 중 접속 날짜가 현재 날짜와 다를 경우
					if ( !loginUser.getTodayDate().equals(today.format(date)) ) {
						
						// 로그인한 유저의 해당 미니홈피 접속 날짜를 현재 날짜로 갱신
						main_dao.updateViewsToday(todayMap);
						
						// 해당 미니홈피 유저의 조회된 기록 중 접속 날짜가 현재 날짜랑 다를 경우
						if ( !miniUser.getToDate().equals(today.format(date)) ) {
							
							// 해당 미니홈피 유저의 일일 조회수를 누적 조회수에 추가
							miniUser.setTotal(miniUser.getTotal() + miniUser.getToday());
							// 해당 미니홈피 유저의 일일 조회수를 0으로 초기화 후 1 증가
							miniUser.setToday(1);
							// 해당 미니홈피 유저의 접속 날짜를 현재 날짜로 갱신
							miniUser.setToDate(today.format(date));
							// 수정된 값들로 해당 미니홈피 유저의 유저 정보 갱신
							main_dao.updateTotalCount(miniUser);
							
						// 해당 미니홈피 유저의 조회된 기록 중 접속 날짜가 현재 날짜랑 같을 경우
						} else {
							
							// 해당 미니홈피 유저의 일일 조회수 1 증가
							miniUser.setToday(miniUser.getToday() + 1);
							// 증가된 일일 조회수로 해당 미니홈피 유저 정보 갱신
							main_dao.updateTodayCount(miniUser);
							
						}
						
					// 로그인한 유저의 조회된 기록 중 접속 날짜가 현재 날짜와 같을 경우
					} else {
						
						// 조회수를 증가시키지 않고 통과
						
					}
					
				// 로그인한 유저의 조회된 기록이 없을 경우
				} else {
					
					// 로그인한 유저의 해당 미니홈피 접속 기록을 추가
					main_dao.insertViewsToday(todayMap);
					
					// 해당 미니홈피 유저의 조회된 기록 중 접속 날짜가 현재 날짜랑 다를 경우
					if ( !miniUser.getToDate().equals(today.format(date)) ) {
						
						// 해당 미니홈피 유저의 일일 조회수를 누적 조회수에 추가
						miniUser.setTotal(miniUser.getTotal() + miniUser.getToday());
						// 해당 미니홈피 유저의 일일 조회수를 0으로 초기화 후 1 증가
						miniUser.setToday(1);
						// 해당 미니홈피 유저의 접속 날짜를 현재 날짜로 갱신
						miniUser.setToDate(today.format(date));
						// 수정된 값들로 해당 미니홈피 유저의 유저 정보 갱신
						main_dao.updateTotalCount(miniUser);
						
					// 해당 미니홈피 유저의 조회된 기록 중 접속 날짜가 현재 날짜랑 같을 경우
					} else {
						
						// 해당 미니홈피 유저의 일일 조회수 1 증가
						miniUser.setToday(miniUser.getToday() + 1);
						// 증가된 일일 조회수로 해당 미니홈피 유저 정보 갱신
						main_dao.updateTodayCount(miniUser);
						
					}
					
				}
				
			// 세션값과 idx값이 같을 경우 - 내 미니홈피 조회 - 조회수 증가 X
			} else {
				
				// 내 미니홈피 접속 날짜 조회
				SignUpVO svo = signUp_dao.selectOneIdx(session.getAttribute("login"));
				
				// 조회된 접속 날짜와 현재 날짜가 다를 경우
				if ( !svo.getToDate().equals(today.format(date)) ) {
					
					// 내 미니홈피의 일일 조회수를 누적 조회수에 추가
					svo.setTotal(svo.getTotal() + svo.getToday());
					// 내 미니홈피의 일일 조회수를 0으로 초기화
					svo.setToday(0);
					// 내 미니홈피의 접속 날짜를 현재 날짜로 갱신
					svo.setToDate(today.format(date));
					// 수정된 값들로 내 미니홈피 정보 갱신
					main_dao.updateTotalCount(svo);
					
				// 조회된 접속 날짜와 현재 날짜가 같을 경우
				} else {
					
					// 조회수를 증가시키지 않고 통과
					
				}
				
			}
			
		}
		
		// 조회수 구역 끝 //
		
		// 그 다음 idx에 해당하는 방명록의 모든 방문글을 조회
		List<GuestBookVO> list = guestbook_dao.selectList(idx);
		// 조회된 모든 방문글을 리스트 형태로 바인딩
		model.addAttribute("list", list);
		
		// 그 다음 idx에 해당하는 유저 정보를 조회
		SignUpVO svo = signUp_dao.selectOneIdx(idx);
		// 조회된 유저 정보를 바인딩
		model.addAttribute("signVo", svo);
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
		
		// 방명록 페이지로 이동
		return Common.GBP_PATH + "guestbook_list.jsp";
	}
	
	// 방명록 방문글 작성 페이지로 이동
	@RequestMapping("/guestbook_insert_form.do")
	public String guestbook_insert_form(Model model) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 방명록에 작성자를 저장하기 위한 세션값에 해당하는 유저 정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		
		// 방문글 작성자 정보 지정
		GuestBookVO vo = new GuestBookVO();
		
		if ( sessionUser.getPlatform().equals("cyworld") ) {
			// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld - 폐기
			// vo.setGuestbookContentName(sessionUser.getUserID() + "@" + sessionUser.getPlatform());
			
			// 플랫폼이 cyworld일 경우 - ( + 이름 + / + ID + ) = ( 관리자 / qwer ) - 변경
			vo.setGuestbookContentName("( " + sessionUser.getName() + " / " + sessionUser.getUserID() + " )");
		} else {
			/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸 뒤 플랫폼명 추가 - 폐기
			 * 네이버 - qwer@ + naver = qwer@naver
			 * 카카오 - qwer@ + kakao = qwer@kakao
			 */
			// vo.setGuestbookContentName(sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
			
			/* 플랫폼이 소셜일 경우 ID가 없으므로 이메일로 대체 - 이름 + 이메일 @부분부터 뒤쪽을 다 잘라낸다 - 변경
			 * 네이버 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
			 * 카카오 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
			 */
			vo.setGuestbookContentName("( " + sessionUser.getName() + " / " + sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") ) + " )");
		}
		// 미리 지정한 작성자 이를을 바인딩
		model.addAttribute("guestbookContentName", vo.getGuestbookContentName());
		// 세션값으로 미니미 지정
		model.addAttribute("minimi", sessionUser.getMinimi());
		// 작성 페이지로 이동
		return Common.GBP_PATH + "guestbook_insert_form.jsp";
	}
	
	// 방명록 새 방문글 작성
	@RequestMapping("/guestbook_insert.do")
	public String guestbook_insert(Integer idx, GuestBookVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 방문글에 작성자의 idx를 저장하기 위해 Integer로 형변환
		Integer sessionIdx = (Integer)session.getAttribute("login");
		
		// 해당 idx의 방명록에 작성된 방문글 개수 조회
		int countNum = guestbook_dao.selectCountNum(idx);
		
		// 방명록에 글이 한 개도 없을경우
		if ( countNum == 0 ) {
			// 방명록에 시작 번호 1 지정
			vo.setGuestbookContentRef(1);
			// 방명록의 idx 지정
			vo.setGuestIdx(idx);
			// 방문글에 좋아요 시작 개수 0 지정
			vo.setGuestbookLikeNum(0);
			// 방문글에 작성자 idx 지정
			vo.setGuestbookSession(sessionIdx);
			// 작성한 방문글을 저장
			guestbook_dao.insert(vo);
			// idx를 들고 방명록 페이지 URL로 이동
			return "redirect:guestbook.do?idx=" + idx;
		// 작성된 방명록이 있을경우
		} else {
			// 가장 최근에 작성한 방문글의 번호에 1 더하기
			vo.setGuestbookContentRef(countNum + 1);
			// 방명록의 idx 지정
			vo.setGuestIdx(idx);
			// 방문글에 좋아요 시작 개수 0 지정
			vo.setGuestbookLikeNum(0);
			// 방문글에 작성자 idx 지정
			vo.setGuestbookSession(sessionIdx);
			// 작성한 방문글을 저장
			guestbook_dao.insert(vo);
			// idx를 들고 방명록 페이지 URL로 이동
			return "redirect:guestbook.do?idx=" + idx;
		}
	}
	
	// 방명록 방문글 삭제
	@RequestMapping("/guestbook_delete.do")
	@ResponseBody // Ajax로 요청된 메서드는 결과를 콜백 메서드로 돌아가기 위해 반드시 필요한 어노테이션
	public String guestbook_delete(GuestBookVO vo, GuestBookLikeVO lvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 삭제할 방명록의 방문글에 idx와 ref를 방문글 삭제 후에도 계속 사용하기 위해 VO에서 따로 분리해서 변수에 각각 저장한다
		int idx = vo.getGuestIdx(); // 삭제할 방명록의 idx
		int ref = vo.getGuestbookContentRef(); // 삭제할 방명록의 방문글 번호
		
		// 그리고 idx와 ref를 사용하기 편하게 Map으로 만들어 놓는다
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("1", idx); // 삭제할 방명록의 idx 저장
		map.put("2", ref); // 삭제할 방명록의 방문글 번호 저장
		
		// DB에 저장된 방문글 중 가져온 정보에 해당하는 방문글 삭제
		int res = guestbook_dao.delete(vo);
		
		// 삭제 실패할 경우
		String result = "no";
		
		if (res == 1) {
			// 삭제 성공할 경우
			result = "yes";
			
			// 방문글 삭제 후 방문글 번호 재정렬
			// 방문글 삭제 후 삭제한 방문글 번호보다 큰 번호의 방문글들 조회
			List<GuestBookVO> list = guestbook_dao.selectListDelete(map);
			// forEach문
			for ( GuestBookVO uref : list ) {
				// 조회된 방문글 번호들을 1씩 감소
				uref.setGuestbookContentRef(uref.getGuestbookContentRef() - 1);
				// 1씩 감소된 번호들을 다시 갱신
				guestbook_dao.updateRefMinus(uref);
			}
			
			// 방문글 삭제 시 삭제된 방문글에 해당하는 좋아요만 모두 삭제
			guestbook_dao.deleteLikeAll(map);
		}
		// 콜백 메소드에 전달
		return result;
	}
	
	// 방명록 방문글 수정 페이지로 이동
	@RequestMapping("/guestbook_modify_form.do")
	public String guestbook_modify_form(GuestBookVO vo, Model model) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 방명록에 작성자를 저장하기 위한 세션값에 해당하는 유저 정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		
		
		// 해당 idx의 방명록에 수정할 방문글을 조회
		GuestBookVO updateVo = guestbook_dao.selectOne(vo);
		if ( updateVo != null ) {
			// 조회된 방문글을 바인딩
			model.addAttribute("vo", updateVo);
			// 세션값으로 미니미 지정
			model.addAttribute("minimi", sessionUser.getMinimi());
		}
		// 수정 페이지로 이동
		return Common.GBP_PATH + "guestbook_modify_form.jsp";
	}
    
	// 방문글 수정하기
	@RequestMapping("/guestbook_modify.do")
	@ResponseBody
	public String guestbook_modify(GuestBookVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 수정된 방문글로 갱신
		int res = guestbook_dao.update(vo);
		// 갱신 실패할 경우 - JSON형태
		String result = "{'result':'no'}";
		if (res != 0) {
			// 갱신 성공할 경우 - JSON형태
			result = "{'result':'yes'}";
		}
		// 콜백 메소드에 전달
		return result;
	}
	
	/////////////// 방명록 좋아요 구역 ///////////////
	@RequestMapping("/guestbook_like.do")
	@ResponseBody
	public String geustbook_like(GuestBookVO vo, GuestBookLikeVO lvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 세션값을 사용하기 위해 Integer타입으로 형변환
		Integer sessionIdx = (Integer)session.getAttribute("login");
		// 좋아요를 누른 로그인한 유저의 세션값 지정
		lvo.setGuestbookLikeSession(sessionIdx);
		// 방명록의 idx 지정
		lvo.setGuestbookLikeIdx(vo.getGuestIdx());
		// 좋아요를 누른 방문글의 번호
		lvo.setGuestbookLikeRef(vo.getGuestbookContentRef());
		// 먼저 DB에 로그인한 유저가 해당 idx의 방명록에 남긴 방문글에 좋아요를 눌렀는지 조회
		GuestBookLikeVO likeVo = guestbook_dao.selectOneLike(lvo);
		// 이미 좋아요를 눌렀을 경우
		if ( likeVo != null ) {
			// 이미 눌린 좋아요를 다시 누를 경우 취소되므로 좋아요 내역을 삭제
			guestbook_dao.deleteLike(lvo);
			// 좋아요 개수 조회
			int likeCount = guestbook_dao.selectLikeCountNum(lvo);
			// 조회된 좋아요 개수를 지정
			vo.setGuestbookLikeNum(likeCount);
			// 조회된 좋아요 개수로 갱신
			guestbook_dao.updateLikeNum(vo);
			// 콜백 메소드에 가져갈 유저 정보
			// GuestBookVO gvo = guestbook_dao.selectOne(vo);
			// 콜백 메소드에 전달
			return "no";
		// 좋아요를 안 눌렀을 경우
		} else {
			// 좋아요를 누를 경우 좋아요 내역을 추가
			guestbook_dao.insertLike(lvo);
			// 좋아요 개수 조회
			int likeCount = guestbook_dao.selectLikeCountNum(lvo);
			// 조회된 좋아요 개수를 지정
			vo.setGuestbookLikeNum(likeCount);
			// 조회된 좋아요 개수로 갱신
			guestbook_dao.updateLikeNum(vo);
			// 콜백 메소드에 가져갈 유저 정보
			// GuestBookVO gvo = guestbook_dao.selectOne(vo);
			// 콜백 메소드에 전달
			return "yes";
		}
	}
}