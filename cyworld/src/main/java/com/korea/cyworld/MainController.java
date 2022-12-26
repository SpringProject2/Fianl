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
import vo.IlchonVO;
import vo.MainVO;
import vo.SignUpVO;
import vo.ViewsVO;

@Controller
public class MainController {
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
	// 메인 페이지로 이동
	@RequestMapping("/main.do")
	public String main(int idx, Model model) {
		// 메인 페이지에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 그 다음 idx로 회원 비회원 구분
		// 회원은 idx가 1부터 시작하기에, -1이면 비회원
		if ( idx == -1 ) {
			// 비회원 페이지로 이동
			return Common.P_PATH + "nmain.jsp";
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
				
				// 그 다음 idx에 해당하는 미니홈피 유저 정보를 조회
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
						
					// 로그인한 유저의 조회된 기록중 접속 날짜가 현재 날짜와 같을 경우
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
		
		// 그 다음 idx로 유저 정보 조회
		SignUpVO idxVo = signUp_dao.selectOneIdx(idx);
		//회원 정보가 있다면 바인딩
		model.addAttribute("signVo", idxVo);
		
		// 그 다음 idx에 해당하는 일촌평 조회
		List<MainVO> list = main_dao.selectList(idx);
		// 조회된 일촌평을 리스트 형태로 바인딩
		model.addAttribute("list", list);
		
		// 세션값이 비회원이 아닐 경우
		if ( (int)session.getAttribute("login") > 0 ) {
			// 그 다음 로그인한 유저의 유저정보 조회
			SignUpVO sessionVo = signUp_dao.selectOneIdx(session.getAttribute("login"));
			// 조회된 유저정보 바인딩
			model.addAttribute("sessionUser", sessionVo);
			
			// 그 다음 일촌관계를 알아보기 위해 IlchonVO를 생성
			IlchonVO ilchonVo = new IlchonVO();
			
			// 맞일촌 상태를 알리는 ilchonUp을 2로 지정
			ilchonVo.setIlchonUp(2);
			// 일촌 idx에 idx를 지정
			ilchonVo.setIlchonSession(sessionVo.getIdx());
			// 그 다음 idx에 해당하는 일촌 조회
			List<IlchonVO> ilchonList = main_dao.selectIlchonList(ilchonVo);
			// 조회된 맞일촌을 리스트 형태로 바인딩
			model.addAttribute("ilchonList", ilchonList);
			
			// 일촌 idx에 해당 미니홈피의 유저 idx를 지정
			ilchonVo.setIlchonIdx(idx);
			// 일촌 session에 로그인한 유저 idx를 지정
			ilchonVo.setIlchonSession(sessionVo.getIdx());
			// 타 유저 미니홈피에 놀러갔을 때 해당 미니홈피 유저와의 일촌 관계를 알기 위해 조회
			IlchonVO ilchon = main_dao.selectIlchonUp(ilchonVo);
			// 조회된 일촌 관계 바인딩
			model.addAttribute("ilchon", ilchon);
		}
		
		// 메인 페이지로 이동
		return Common.P_PATH + "main.jsp";
	}
	
	// 비회원 로그인
	@RequestMapping("/nmain.do")
	public String nmanin(Integer idx, Model model) {
		// 비회원 세션값 지정
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 로그인 세션으로 비회원용 idx 지정
			session.setAttribute("login", idx);
		}
		
		// 비회원 세션값 들고 메인페이지 이동
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	/////////////// 검색 구역 ///////////////
	
	// 검색 팝업 이동
	@RequestMapping("/main_search_popup.do")
	public String main_search_popup() {
		// 검색 페이지로 이동
		return Common.P_PATH + "searchPopUp.jsp";
	}
	
	// 이름 및 ID 및 Email로 유저 검색
	@RequestMapping("/main_search.do")
	public String main_search(String searchType, String searchValue, Model model) {
		// 이름으로 검색할 경우
		if ( searchType.equals("name") ) {
			// 검색한 이름으로 조회
			List<SignUpVO> list = main_dao.selectSearchName(searchValue);
			// 이름으로 조회한 유저 리스트를 바인딩
			model.addAttribute("list", list);
			// 추가로 검색 구분을 하기 위해 검색 타입도 바인딩
			model.addAttribute("searchType", searchType);
			// 검색 페이지로 이동
			return Common.P_PATH + "searchPopUp.jsp";
		// ID로 검색할 경우
		} else {
			/* 검색한 ID로 조회
			 * cyworld 가입자는 ID로 조회
			 * 소셜 가입자는 ID가 없기에 대신 email로 조회
			 * main.xml 참고
			 */
			List<SignUpVO> list = main_dao.selectSearchId(searchValue);
			// ID로 조회한 유저 리스트를 바인딩 - cyworld
			model.addAttribute("list", list);
			// 추가로 검색 구분을 하기 위해 검색 타입도 바인딩
			model.addAttribute("searchType", searchType);
			// 검색 페이지로 이동
			return Common.P_PATH + "searchPopUp.jsp";
		}
	}
	
	// 일촌평 작성 
	@RequestMapping("/ilchon_write.do")
	@ResponseBody
	public String insert( MainVO vo, SignUpVO svo ) {
		// 세션값 존재 여부 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 다시 로그인
			return "redirect:login.do";
		}
		
		// 일촌평에 작성자를 저장하기 위한 세션값에 해당하는 유저 정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		
		// 해당 idx의 메인에 작성된 일촌평 개수 조회
		int countNum = main_dao.selectCountNum(svo.getIdx());
		
		// 작성된 일촌평이 한 개도 없을 경우
		if ( countNum == 0 ) {
			// 일촌평에 시작 번호 1을 지정
			vo.setNum(1);
			// 메인 페이지의 idx 지정
			vo.setIlchonpyeongIdx(svo.getIdx());
			// 일촌평에 작성자 정보 지정
			if ( sessionUser.getPlatform().equals("cyworld") ) {
				// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld - 폐기
				// vo.setIlchonSession(sessionUser.getUserID() + "@" + sessionUser.getPlatform());
				
				// 플랫폼이 cyworld일 경우 - ( + 이름 + / + ID + ) = ( 관리자 / qwer ) - 변경
				vo.setIlchonSession("( " + sessionUser.getName() + " / " + sessionUser.getUserID() + " )");
			} else {
				/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸뒤 플랫폼명 추가 - 폐기
				 * 네이버 - qwer@ + naver = qwer@naver
				 * 카카오 - qwer@ + kakao = qwer@kakao
				 */
				// vo.setIlchonSession(sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
				
				/* 플랫폼이 소셜일 경우 ID가 없으므로 이메일로 대체 - 이름 + 이메일 @부분부터 뒤쪽을 다 잘라낸다 - 변경
				 * 네이버 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
				 * 카카오 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
				 */
				vo.setIlchonSession("( " + sessionUser.getName() + " / " + sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") ) + " )");
			}
			// 작성한 일촌평을 DB에 저장
			int res = main_dao.ilchonWrite(vo);
			// 저장 실패할 경우
			String result = "no";
			if ( res == 1 ) {
				// 저장 성공할 경우
				result = "yes";
			}
			// 콜백 메소드에 전달
			return result;
		}
		
		// 가장 최근에 작성한 일촌평의 번호에 1 더하기
		vo.setNum(countNum + 1);
		// 메인 페이지의 idx 지정
		vo.setIlchonpyeongIdx(svo.getIdx());
		// 일촌평에 작성자 정보 지정
		if ( sessionUser.getPlatform().equals("cyworld") ) {
			// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld - 폐기
			// vo.setIlchonSession(sessionUser.getUserID() + "@" + sessionUser.getPlatform());
			
			// 플랫폼이 cyworld일 경우 - ( + 이름 + / + ID + ) = ( 관리자 / qwer ) - 변경
			vo.setIlchonSession("( " + sessionUser.getName() + " / " + sessionUser.getUserID() + " )");
		} else {
			/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸 뒤 플랫폼명 추가 - 폐기
			 * 네이버 - qwer@ + naver = qwer@naver
			 * 카카오 - qwer@ + kakao = qwer@kakao
			 */
			// vo.setIlchonSession(sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
			
			/* 플랫폼이 소셜일 경우 ID가 없으므로 이메일로 대체 - 이름 + 이메일 @부분부터 뒤쪽을 다 잘라낸다 - 변경
			 * 네이버 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
			 * 카카오 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
			 */
			vo.setIlchonSession("( " + sessionUser.getName() + " / " + sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") ) + " )");
		}
		// 작성한 일촌평을 DB에 저장
		int res = main_dao.ilchonWrite(vo);
		// 저장 실패할 경우
		String result = "no";
		if ( res == 1 ) {
			// 저장 성공할 경우
			result = "yes";
		}
		// 콜백 메소드에 전달
		return result;
	}
	
	/////////////// 도토리 구매 구역 ///////////////
	
	// 도토리 구매 팝업 이동
	@RequestMapping("/dotory.do")
	public String dotory(Integer idx) {
		// 도토리 구매 페이지로 이동
		return Common.P_PATH + "dotory.jsp";
	}
	
	// 도토리 구매
	@RequestMapping("/dotoryBuy.do")
	public String dotoryBuy(SignUpVO vo, Integer nowDotory) {
		// 현제 가지고 있는 도토리 개수를 파라미터로 가져와서 구매한 도토리 개수를 더해서 setter를 통해 넘겨 준다
		vo.setDotoryNum(vo.getDotoryNum()+nowDotory);
		// 도토리 개수 갱신
		signUp_dao.buyDotory(vo);
		// idx와 갱신된 도토리 개수를 들고 도토리 구매 페이지 URL로 이동
		return "redirect:dotory.do?idx=" + vo.getIdx() + "&dotoryNum=" + vo.getDotoryNum();
	}
	
	/////////////// 일촌 구역 ///////////////
	
	@RequestMapping("/main_ilchon")
	@ResponseBody
	public String main_follow(Integer idx, Integer sessionIdx, IlchonVO ivo) {
		// 세션값 존재 여부 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 다시 로그인
			return "redirect:login.do";
		}
		
		// 일촌을 맺기위한 준비과정 //
		
		// 일촌 idx에 해당 미니홈피의 유저 idx를 지정
		ivo.setIlchonIdx(idx);
		// 일촌 session에 로그인한 유저 idx를 지정
		ivo.setIlchonSession(sessionIdx);
		
		// Map에 VO를 통째로 담을 경우 사용방법 - main.xml 참고
		HashMap<String, Object> ilchonMap = new HashMap<String, Object>();
		// 각 키에 위에서 setter로 값을 넣어준 IlchonVO를 통쨰로 넣어 준다
		ilchonMap.put("1", ivo);
		ilchonMap.put("2", ivo);
		ilchonMap.put("3", ivo);
		ilchonMap.put("4", ivo);
		
		// 준비 끝 //
		
		// 일촌 맺기 시작 //
		
		/* 먼저 로그인한 유저가 해당 미니홈피 유저에게 일촌 신청했는지 확인
		 * 반대로 해당 미니홈피 유저가 로그인한 유저에게 일촌 신청했는지 확인
		 * count함수를 사용하여 값을 숫자로 받는다
		 * 0: 로그인한 유저 및 해당 미니홈피 유저 둘 다 일촌 신청 안 한 상태
		 * 1: 로그인한 유저 및 해당 미니홈피 유저 둘 중 하나는 일촌 신청한 상태
		 * 2: 로그인한 유저 및 해당 미니홈피 유저 둘 다 일촌 신청한 상태
		 */
		int followNum = main_dao.selectIlchonSearch(ilchonMap);
		
		// 콜백 메소드에 일촌 신청 결과를 전달해 줄 String 변수
		String result = "";
		
		/* IlchonVO - ilchonUp : 로그인한 유저와 해당 미니홈피 유저와의 일촌 관계
		 * 0: 둘 다 일촌 신청 안 한 상태
		 * 1: 둘 중 하나는 일촌 신청한 상태
		 * 2: 둘 다 일촌 신청한 상태
		 */
		
		// 로그인한 유저 및 해당 미니홈피 유저 둘다 일촌 신청 안 한 경우
		// 이때는 다른 것을 더 조회할 필요없이 바로 INSERT해서 일촌 신청 상태로 만든다
		if ( followNum == 0 ) {
			// 로그인한 유저만 일방적으로 일촌 신청을 하였기에 ilchonUp을 1로 만든다
			ivo.setIlchonUp(1);
			// 해당 미니홈피 유저의 idx로 유저 정보를 조회
			SignUpVO svo = signUp_dao.selectOneIdx(idx);
			// 조회된 유저 정보로 일촌에 등록될 이름을 만든다
			if ( svo.getPlatform().equals("cyworld") ) {
				// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
				// galleryCommentName = (sessionUser.getUserID() + "@" + sessionUser.getPlatform());
				
				// 플랫폼이 cyworld일 경우 - ( + 이름 + / + ID + ) = ( 관리자 / qwer ) - 변경
				ivo.setIlchonName( "( " + svo.getName() + " / " + svo.getUserID() + " )" );
			} else {
				/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸뒤 플랫폼명 추가 - 폐기
				 * 네이버 - qwer@ + naver = qwer@naver
				 * 카카오 - qwer@ + kakao = qwer@kakao
				 */
				// galleryCommentName = (sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
				
				/* 플랫폼이 소셜일 경우 ID가 없으므로 이메일로 대체 - 이름 + 이메일 @부분부터 뒤쪽을 다 잘라낸다 - 변경
				 * 네이버 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
				 * 카카오 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
				 */
				ivo.setIlchonName( "( " + svo.getName() + " / " + svo.getEmail().substring( 0, svo.getEmail().indexOf("@") ) + " )" );
			}
			// 로그인한 유저의 일촌 신청 정보를 저장
			main_dao.insertIlchon(ivo);
			// 일촌 신청될 경우
			result = "yes";
		}
		// 로그인한 유저 및 해당 미니홈피 유저 둘 중 하나는 일촌 신청한 경우
		// 이때는 로그인한 유저가 일촌 신청을 했는지, 해당 미니홈피 유저가 일촌 신청을 했는지 모르기에 조회를 한 번 더 한다
		if ( followNum == 1 ) {
			// 로그인한 유저가 해당 미니홈피 유저에게 일촌 신청을 했는지 조회
			IlchonVO vo = main_dao.selectIlchon(ivo);
			// 로그인한 유저가 해당 미니홈피 유저에게 일촌 신청 X
			// 해당 미니홈피 유저가 로그인한 유저에게 일촌 신청 O
			if ( vo == null ) {
				// 로그인한 유저가 마저 일촌 신청을 하면서 이제 맞일촌 상태가 됐으므로 ilchonUp을 2로 만든다
				ivo.setIlchonUp(2);
				// 해당 미니홈피 유저의 idx로 유저 정보를 조회
				SignUpVO svo = signUp_dao.selectOneIdx(idx);
				// 조회된 유저 정보로 일촌에 등록될 이름을 만든다
				if ( svo.getPlatform().equals("cyworld") ) {
					// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
					// galleryCommentName = (sessionUser.getUserID() + "@" + sessionUser.getPlatform());
					
					// 플랫폼이 cyworld일 경우 - ( + 이름 + / + ID + ) = ( 관리자 / qwer ) - 변경
					ivo.setIlchonName( "( " + svo.getName() + " / " + svo.getUserID() + " )" );
				} else {
					/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸 뒤 플랫폼명 추가 - 폐기
					 * 네이버 - qwer@ + naver = qwer@naver
					 * 카카오 - qwer@ + kakao = qwer@kakao
					 */
					// galleryCommentName = (sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
					
					/* 플랫폼이 소셜일 경우 ID가 없으므로 이메일로 대체 - 이름 + 이메일 @부분부터 뒤쪽을 다 잘라낸다 - 변경
					 * 네이버 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
					 * 카카오 - ( + 관리자 + / + sksh0000 + ) = ( 관리자 / sksh0000 )
					 */
					ivo.setIlchonName( "( " + svo.getName() + " / " + svo.getEmail().substring( 0, svo.getEmail().indexOf("@") ) + " )" );
				}
				// 로그인한 유저의 일촌 신청 정보를 저장
				main_dao.insertIlchon(ivo);
				// 해당 미니홈피 유저의 ilchonUp을 2로 갱신
				main_dao.updateIlchon(ivo);
				// 일촌 신청될 경우
				result = "yes";
			// 해당 미니홈피 유저가 로그인한 유저에게 일촌 신청 X
			// 로그인한 유저가 해당 미니홈피 유저에게 일촌 신청 O
			} else {
				// 이미 일촌 신청된 것을 다시 눌렀기에 저장되어 있던 일촌 신청 정보를 삭제해서 일촌 신청이 해제된 상태로 만든다
				main_dao.deleteIlchon(ivo);
				// 일촌 해제될 경우
				result = "no";
			}
		}
		// 로그인한 유저 및 해당 미니홈피 유저 둘 다 일촌 신청한 경우
		// 이때는 다른 것을 더 조회할 필요없이 바로 DELETE해서 일촌 해제 상태로 만든다
		if ( followNum == 2 ) {
			// 일촌 신청 정보를 삭제
			main_dao.deleteIlchon(ivo);
			// 로그인한 유저가 일촌 해제 상태가 되면서 이제 맞일촌 상태가 아니므로 ilchonUp을 1로 만든다
			ivo.setIlchonUp(1);
			// 해당 미니홈피 유저의 ilchonUp을 1로 갱신
			main_dao.updateIlchon(ivo);
			// 일촌 해제될 경우
			result = "no";
		}
		
		// 일촌 맺기 끝 //
		
		// 맞일촌 구하기 //
		
		// 먼저 맞일촌 상태를 찾기 위해 ilchonUp을 2로 지정
		ivo.setIlchonUp(2);
		// count로 해당 미니홈피 유저와 맞일촌 상태인 유저들의 수를 조회
		int ilchonNum = main_dao.selectIlchonNum(ivo);
		// 조회된 맞일촌 수를 해당 미니홈피 유저 정보 중 맞일촌 수를 나타내는 ilchon에 갱신하기 위해 SignUpVO를 생성한다
		SignUpVO vo = new SignUpVO();
		// 해당 미니홈피 유저의 idx를 지정
		vo.setIdx(idx);
		// 조회된 맞일촌 수를 ilchon에 지정
		vo.setIlchon(ilchonNum);
		// 조회된 맞일촌 수를 해당 미니홈피 유저 정보에 갱신
		main_dao.updateIlchonNum(vo);
		
		// 그 다음 로그인한 유저의 맞일촌 수도 조회하기 위해 ilchonIdx를 로그인한 유저의 idx로 지정
		ivo.setIlchonIdx(sessionIdx);
		// count로 로그인한 유저와 맞일촌 상태인 유저들의 수를 조회
		int ilchonReverseNum = main_dao.selectIlchonNum(ivo);
		// 조회된 맞일촌 수를 로그인한 유저 정보 중 맞일촌 수를 나타내는 ilchon에 갱신하기 위해 SignUpVO에 로그인한 유저 정보를 지정한다
		// 로그인한 유저의 idx를 지정
		vo.setIdx(sessionIdx);
		// 조회된 맞일촌 수를 ilchon에 지정
		vo.setIlchon(ilchonReverseNum);
		// 조회된 맞일촌 수를 로그인한 유저 정보에 갱신
		main_dao.updateIlchonNum(vo);
		
		// 맞일촌 끝 //
		
		// 콜백 메소드에 전달
		return result;
	}
}