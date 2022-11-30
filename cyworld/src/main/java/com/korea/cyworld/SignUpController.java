package com.korea.cyworld;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import dao.GalleryDAO;
import dao.GuestBookDAO;
import dao.MainDAO;
import dao.SignUpDAO;
import mail.MailKey;
import util.Common;
import vo.GalleryVO;
import vo.GuestBookLikeVO;
import vo.GuestBookVO;
import vo.GalleryCommentVO;
import vo.GalleryLikeVO;
import vo.MainVO;
import vo.SignUpVO;

@Controller
public class SignUpController {
	// @Autowired
	@Autowired
	ServletContext app;// 현재 프로젝트의 기본정보들을 저장하고 있는 클래스

	@Autowired
	HttpServletRequest request;

	// SignUpDAO
	SignUpDAO signUp_dao; // 로그인 및 회원가입 DAO
	MainDAO main_dao; // 메인페이지 DAO
	GalleryDAO gallery_dao; // 사진첩 DAO
	GuestBookDAO guestbook_dao; // 방명록 DAO
	
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 기본 및 로그인
	@RequestMapping(value= {"/", "login.do"})
	public String basic() {
		// 이제 기본페이지는 항상 세션값을 확인해서 로그인페이지와 메인페이지로 나눈다.
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return Common.S_PATH + "login.jsp";
		}
		// 세션값이 있다면 이제 로그인은 건너뛰고 세션값에 해당하는 메인페이지로 바로 이동
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	// 로그아웃
	@RequestMapping("/logout.do")
	public String logout() {
		// 로그아웃을 하면 세션을 비운다
		HttpSession session = request.getSession();
		session.removeAttribute("login");
		// 세션을 비우면 다시 로그인페이지로 이동한다.
		return "redirect:login.do";
	}
	
	// 네이버 API 콜백용
	@RequestMapping("/login_naver_callback.do")
	public String naver_join() {
		return Common.S_PATH + "login_naver_callback.jsp";
	}
	
	// 각 플랫폼별 가입자와 비가입자 구별
	@RequestMapping("/login_authentication.do")
	public String login_authentication(SignUpVO vo, Model model) {
		// 플랫폼이 cyworld일때
		if ( vo.getPlatform().equals("cyworld") ) {
			// 가져온 정보중에 ID가 없을경우 - 회원가입
			if ( vo.getUserID() == null ) {
				model.addAttribute("vo", vo);
				// cyworld 회원가입페이지
				return Common.S_PATH + "cyworld_join.jsp";
			}
			// 가져온 정보중에 ID가 있을경우 - 로그인
			// 로그인한 ID로 회원정보 조회
			SignUpVO loginVo = signUp_dao.selectOneIdCheck(vo.getUserID());
			HttpSession session = request.getSession();
			if ( session.getAttribute("login") == null ) {
				// 로그인 세션 부여
				session.setAttribute("login", loginVo.getIdx());
			}
			// 세션값에 해당하는 메인페이지로 이동
			return "redirect:main.do?idx=" + session.getAttribute("login");
		}
		
		// 플랫폼이 소셜일때 (카카오 및 네이버)
		// 플랫폼별 가입자 조회 - 플랫폼 + 이메일
		SignUpVO joinVo = signUp_dao.selectOnePlatformEmail(vo);
		// 조회된 값이 없을때 - 회원가입
		if ( joinVo == null ) {
			// 플랫폼이 카카오일때
			if ( vo.getPlatform().equals("kakao") ) {
				model.addAttribute("vo", vo);
				// 카카오 회원가입페이지
				return Common.S_PATH + "kakao_join.jsp";
			}
			// 플랫폼이 네이버 일때
			if ( vo.getPlatform().equals("naver") ) {
				model.addAttribute("vo", vo);
				// 네이버 회원가입페이지
				return Common.S_PATH + "naver_join.jsp";
			}
		}
		// 조회된 값이 있을때 - 로그인
		// 소셜로그인은 이곳에서 세션을 발급받는다
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 로그인 세션 부여
			session.setAttribute("login", joinVo.getIdx());
		}
		// 세션값에 해당하는 메인페이지로 이동
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	// ID 중복체크
	@RequestMapping("/double_check.do")
	@ResponseBody
	public String doubleCheck(String userID) {
		// 넘어온 ID값으로 이미 가입한 유저가 있는지 조회
		SignUpVO vo = signUp_dao.selectOneIdCheck(userID);
		
		// JSON
		// ID 중복일때
		String result = "{'result':'no'}";
		if ( vo != null ) {
			// ID 중복이 아닐때
			result = "{'result':'yes'}";
		}
		// 콜백메소드에 결과값 전송
		return result;
	}
	
	// 이메일 인증
	@RequestMapping("/emailCheck.do")
	@ResponseBody
	public String emailCheck(SignUpVO vo) {
		// 미리 만들어둔 랜덤키 생성 메소드를 mail패키지의 MailKey.java에서 가져와 사용한다.
		String mail_key = new MailKey().getKey(10, false); // 랜덤키 길이 설정
		// Mail Server 설정
		String charSet = "UTF-8"; // 사용할 언어셋
		String hostSMTP = "smtp.naver.com"; // 사용할 SMTP
		String hostSMTPid = "sksh0000@naver.com"; // 사용할 SMTP에 해당하는 ID
		String hostSMTPpwd = "qpwoeiruty10%"; // 사용할 ID에 해당하는 PWD
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 보내는 사람 E-Mail, 제목, 내용 
		String fromEmail = "sksh0000@naver.com"; // 보내는 사람 email
		String fromName = "관리자"; // 보내는 사람 이름
		String subject = "이메일 인증번호 발송"; // 제목
		
		// 받는 사람 E-Mail 주소
		String mail = vo.getEmail(); // 받는 사람 email
		
		try {
			HtmlEmail email = new HtmlEmail(); // Email 생성
			email.setDebug(true);
			email.setCharset(charSet); // 언어셋 사용
			email.setSSL(true);
			email.setHostName(hostSMTP); // SMTP 사용
			email.setSmtpPort(587);	// SMTP 포트 번호 입력

			email.setAuthentication(hostSMTPid, hostSMTPpwd); // 메일 ID, PWD
			email.setTLS(true);
			email.addTo(mail); // 받는 사람
			email.setFrom(fromEmail, fromName, charSet); // 보내는 사람
			email.setSubject(subject); // 제목
			email.setHtmlMsg("<p>"+ mail_key + "</p>"); // 본문 내용
			email.send(); // 메일 보내기
			// 메일 보내기가 성공하면 메일로 보낸 랜덤키를 콜백메소드에도 전달
			return mail_key;
		} catch (Exception e) {
			System.out.println(e);
			// 메일 보내기가 실패하면 "false"를 콜백메소드에 전달
			return "false";
		}
	}
	
	// ID 찾기 페이지 이동
	@RequestMapping("/findID.do")
	public String findID() {
		return Common.S_PATH + "find_id.jsp";
	}
	// ID 찾기
	@RequestMapping("/findIdCheck.do")
	@ResponseBody
	public String findIdCheck(SignUpVO vo) {
		// 넘어온 이름 + 휴대폰 번호에 해당하는 ID가 있는지 조회
		SignUpVO idVo = signUp_dao.selectOneId(vo);
		if ( idVo == null ) {
			// 조회된 ID가 없을경우 "no"를 콜백메소드에 전달
			return "no";
		}
		// 조회된 ID가 있을경우 조회된 ID를 콜백메소드에 전달
		return idVo.getUserID();
	}
	
	// PW 찾기 페이지 이동
	@RequestMapping("/findPW.do")
	public String findPW() {
		return Common.S_PATH + "find_pw.jsp";
	}
	// PW 찾기
	@RequestMapping("/findPwCheck.do")
	@ResponseBody
	public String findPwCheck(SignUpVO vo) {
		// 넘어온 이름 + ID + email에 해당하는 비밀번호 조회
		SignUpVO pwVo = signUp_dao.selectOnePw(vo);
		if ( pwVo == null ) {
			// 조회된 비밀번호가 없을경우 "no"를 콜백메소드에 전달
			return "no";
		}
		// 조회된 비밀번호가 있을경우 해당 비밀번호를 가져오는게 아닌 임시비밀번호로 랜덤키를 발급한다.
		// 미리 만들어둔 랜덤키 생성 메소드를 mail패키지의 MailKey.java에서 가져와 사용한다.
		String mail_key = new MailKey().getKey(10, false); // 랜덤키 길이 설정
		// Mail Server 설정
		String charSet = "UTF-8"; // 사용할 언어셋
		String hostSMTP = "smtp.naver.com"; // 사용할 SMTP
		String hostSMTPid = "id"; // 사용할 SMTP에 해당하는 ID
		String hostSMTPpwd = "pwd"; // 사용할 ID에 해당하는 PWD
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 보내는 사람 E-Mail, 제목, 내용 
		String fromEmail = "id"; // 보내는 사람 email
		String fromName = "관리자"; // 보내는 사람 이름
		String subject = "이메일 인증번호 발송"; // 제목
		
		// 받는 사람 E-Mail 주소
		String mail = vo.getEmail(); // 받는 사람 email
		
		try {
			HtmlEmail email = new HtmlEmail(); // Email 생성
			email.setDebug(true);
			email.setCharset(charSet); // 언어셋 사용
			email.setSSL(true);
			email.setHostName(hostSMTP); // SMTP 사용
			email.setSmtpPort(587);	// SMTP 포트 번호 입력

			email.setAuthentication(hostSMTPid, hostSMTPpwd); // 메일 ID, PWD
			email.setTLS(true);
			email.addTo(mail); // 받는 사람
			email.setFrom(fromEmail, fromName, charSet); // 보내는 사람
			email.setSubject(subject); // 제목
			email.setHtmlMsg("<p>"+ mail_key + "</p>"); // 본문 내용
			email.send(); // 메일 보내기
			
			/* HashMap - java.util.HashMap
			 * 메일 보내기가 성공하면 해당 ID의 비밀번호에 랜덤키를 보내야하는데,
			 * 그럼 ID도 가져가야하고 랜덤키도 가져가야한다.
			 * 히자만 ID는 SignUpVO에 들어있고, 랜덤키는 MailKey에 들어있기에
			 * 이 두가지 정보를 하나로 통합해서 가져가고싶다면
			 * Map을 생성해서 두가지 정보를 다 담아서 가져가면된다.
			 * Tip : 키 타입으로 String을 사용하는게 대체로 편리히다.
			 * Tip : 나중에 VO를 통째로 가져갈때는 value 타입을 Object로 사용한다.
			 * Map구조 파악용
			 * HashMap<String, Object> map = new HashMap<String, Object>();
			 * map.put("a", vo);
			 * map.put("b", mail_key);
			 */
			HashMap<String, String> m_key = new HashMap<String, String>();
			m_key.put("1", vo.getUserID()); // 1번키에 
			m_key.put("2", mail_key);
			// 메일보내기가 성공하면 DB에 저장되있는 비밀번호를 메일로 보낸 랜덤키로 갱신
			signUp_dao.updateNewPw(m_key);
			// 비밀번호 갱신이 성공하면 갱신한 비밀번호 랜덤키를 콜백메소드에도 전달
			return mail_key;
		} catch (Exception e) {
			System.out.println(e);
			// 메일 보내기가 실패하면 "false"를 콜백메소드에 전달
			return "false";
		}
	}
	
	// 로그인 체크
	@RequestMapping("/login_check.do")
	@ResponseBody
	public String loginCheck(SignUpVO vo) {
		// 가져온 VO에서 ID와 비밀번호를 변수를 생성해 분리
		String id = vo.getUserID(); // ID 입력값
		String pw = vo.getInfo(); // 비밀번호 입력값
		
		// ID값으로 고객 정보를 가져온다.
		SignUpVO loginCheckVo = signUp_dao.selectOneIdCheck(id);
		
		// 존재하지 않는 ID
		if ( loginCheckVo == null ) {
			return "{'result':'no_id'}";
		}
		
		// ID는 존재
		// 입력한 비밀번호와 DB에 ID에 해당하는 비밀번호가 불일치
		if ( !loginCheckVo.getInfo().equals(pw) ) {
			return "{'result':'no_info'}";
		}
		
		// 모두 일치 - 로그인 가능
		// 로그인 유지를 위한 세션 부여
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// ID값으로 회원정보 조회
			SignUpVO sessionIdx = signUp_dao.selectOneIdCheck(id);
			// 로그인 세션 부여
			session.setAttribute("login", sessionIdx.getIdx());
		}
		return "{'result':'clear'}";
	}
	
	// 회원가입시 추가적으로 더 필요한 정보를 넣기위한 장소
	@RequestMapping("/welcome.do")
	public String welcome(SignUpVO vo, Model model) {
		// cyworld로 회원가입자가 들어올때
		if ( vo.getPlatform().equals("cyworld") ) {
			// 추가 정보들을 임의로 지정
			vo.setMinimi(""); // 기본 미니미 지정
			vo.setDotoryNum(0); // 기본 도토리 갯수 지정
			// 가입 성공시 유저 정보 저장
			signUp_dao.insertJoinSuccess(vo);
			// 회원가입 정보 저장후 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 소셜 회원가입자가 들어올때
		// 추가 정보들을 임의로 지정
		vo.setUserID(""); // 소셜로그인이라 ID정보 없음
		vo.setInfo(""); // 소셜로그인이라 PW정보 없음
		vo.setMinimi("mainMinimi.png"); // 기본 미니미 지정
		vo.setDotoryNum(0); // 기본 도토리 갯수 지정
		// 가입 성공시 유저 정보 저장
		signUp_dao.insertJoinSuccess(vo);
		// 회원가입 정보 저장후 로그인 페이지로 이동
		return "redirect:login.do";
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 메인페이지로 이동
	@RequestMapping("/main.do")
	public String main(Integer idx, Model model) {
		// 메인페이지에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 그 다음 idx로 회원 비회원 구분
		// 회원은 idx가 1부터 시작하기에, -1이면 비회원
		if ( idx == -1 ) {
			// 비회원페이지로 이동
			return Common.P_PATH + "nmain.jsp";
		}
		
		// 그 다음 idx로 유저정보 조회
		SignUpVO idxVo = signUp_dao.selectOneIdx(idx);
		// 회원정보가 없을때
		if ( idxVo == null ) {
			// 에러페이지로 이동
			return Common.P_PATH + "xmain.jsp";
		}
		//회원정보가 있다면 바인딩
		model.addAttribute("sighVo", idxVo);
		
		// 그 다음 idx에 해당하는 일촌평 조회 및 바인딩
		List<MainVO> list = main_dao.selectList(idx);
		model.addAttribute("list", list);
		
		// 추가로 세션값으로 유저정보 조회
		SignUpVO sessionVo = signUp_dao.selectOneIdx(session.getAttribute("login"));
		// 조회된 유저정보 바인딩
		model.addAttribute("sessionUser", sessionVo);
		
		// 메인페이지로 이동
		return Common.P_PATH + "main.jsp";
	}
	
	// 비회원 로그인
	@RequestMapping("/nmain.do")
	public String nmanin(Integer idx, Model model) {
		// 비회원 세션값 지정
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			session.setAttribute("login", idx);
		}
		// 비회원 세션값 들고 메인페이지 이동
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	// 검색 팝업으로 이동
	@RequestMapping("/main_search_popup.do")
	public String main_search_popup(Integer idx) {
		return Common.P_PATH + "searchPopUp.jsp";
	}
	
	// 이름 및 ID 및 Email로 유저 검색 ( 진행중 )
	@RequestMapping("/main_search.do")
	public String main_search(SignUpVO vo, String searchType, String searchValue, Model model) {
		// 이름으로 검색할 경우
		if ( searchType.equals("name") ) {
			// 검색한 이름으로 조회
			List<SignUpVO> list = main_dao.selectSearchName(searchValue);
			// 이름으로 조회한 유저 리스트를 바인딩
			model.addAttribute("list", list);
			// 추가로 검색구분을 하기 위해 검색타입도 바인딩
			model.addAttribute("searchType", searchType);
			// 검색페이지로 이동
			return Common.P_PATH + "searchPopUp.jsp";
			// ID로 검색할 경우
		} else {
			/* 검색한 ID로 조회
			 * cyworld 가입자는 ID로 조회
			 * 소셜 가입자는 ID가 없기에 대신 email로 조회
			 */
			List<SignUpVO> list = main_dao.selectSearchId(searchValue);
			// ID로 조회한 유저 리스트를 바인딩 - cyworld
			model.addAttribute("list", list);
			// 추가로 검색구분을 하기 위해 검색타입도 바인딩
			model.addAttribute("searchType", searchType);
			// 검색페이지로 이동
			return Common.P_PATH + "searchPopUp.jsp";
			// email로 검색할 경우
		}
	}
	
	// 일촌평 작성 
	@RequestMapping("/ilchon_write.do")
	@ResponseBody
	public String insert( MainVO vo, SignUpVO svo ) {
		// 세션값 존재여부 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 다시 로그인
			return "redirect:login.do";
		}
		
		// 일촌평에 작성자를 저장하기 위한 세션값에 해당하는 유저정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		
		// 해당 idx의 메인에 작성된 일촌평 갯수 조회
		int countNum = main_dao.selectCountNum(svo.getIdx());
		
		// 작성된 일촌평이 한개도 없을 경우
		if ( countNum == 0 ) {
			// 일촌평에 시작번호 1을 지정
			vo.setNum(1);
			// 메인페이지의 idx 지정
			vo.setIlIdx(svo.getIdx());
			// 일촌평에 작성자 정보 지정
			if ( sessionUser.getPlatform().equals("cyworld") ) {
				// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
				vo.setIlchonSession(sessionUser.getUserID() + "@" + sessionUser.getPlatform());
			} else {
				/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸뒤 플랫폼명 추가
				 * 네이버 - qwer@ + naver = qwer@naver
				 * 카카오 - qwer@ + kakao = qwer@kakao
				 */
				vo.setIlchonSession(sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
			}
			// 작성한 일촌평을 DB에 저장
			int res = main_dao.ilchonWrite(vo);
			// 저장 실패할 경우
			String result = "no";
			if ( res == 1 ) {
				// 저장 성공할 경우
				result = "yes";
			}
			// 콜백메소드에 전달
			return result;
		}
		
		// 가장 최근에 작성한 일촌평의 번호에 1 더하기
		vo.setNum(countNum + 1);
		// 메인페이지의 idx 지정
		vo.setIlIdx(svo.getIdx());
		// 일촌평에 작성자 정보 지정
		if ( sessionUser.getPlatform().equals("cyworld") ) {
			// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
			vo.setIlchonSession(sessionUser.getUserID() + "@" + sessionUser.getPlatform());
		} else {
			/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸뒤 플랫폼명 추가
			 * 네이버 - qwer@ + naver = qwer@naver
			 * 카카오 - qwer@ + kakao = qwer@kakao
			 */
			vo.setIlchonSession(sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
		}
		// 작성한 일촌평을 DB에 저장
		int res = main_dao.ilchonWrite(vo);
		// 저장 실패할 경우
		String result = "no";
		if ( res == 1 ) {
			// 저장 성공할 경우
			result = "yes";
		}
		// 콜백메소드에 전달
		return result;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 프로필 조회
	@RequestMapping("/profile.do")
	public String profile(Integer idx, Model model) {
		// 프로필에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 그 다음 idx에 해당하는 프로필 조회
		SignUpVO idxVo = signUp_dao.selectOneIdx(idx);
		// 조회된 프로필 정보를 바인딩
		model.addAttribute("vo", idxVo);
		// 추가로 세션값도 바인딩
		model.addAttribute("sessionIdx", session.getAttribute("login"));
		// 프로필페이지로 이동
		return Common.P_PATH + "profile.jsp";
	}
	
	// 미니미 수정 팝업페이지 이동
	@RequestMapping("/profile_minimi_popup.do")
	public String popup(Integer idx, Model model) {
		SignUpVO vo = signUp_dao.selectOneIdx(idx);
		model.addAttribute("minimi", vo.getMinimi());
		return Common.P_PATH + "minimiPopUp.jsp";
	}
	
	// 미니미 변경
	@RequestMapping("/profile_minimi_change.do")
	public String minimiChange(SignUpVO vo) {
		signUp_dao.updateMinimi(vo);
		return "redirect:profile_minimi_popup.do?idx=" + vo.getIdx();
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 사진첩 조회
	@RequestMapping("/gallery.do")
	public String string(Integer idx, Model model) {
		// 사진첩에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 그 다음 사진첩에 작성자를 저장하기 위해 세션값에 해당하는 유저정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		// 조회한 유저정보에서 작성자를 만들기 위해 String변수 준비
		String galleryCommentName = "";
		if ( sessionUser.getPlatform().equals("cyworld") ) {
			// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
			galleryCommentName = (sessionUser.getUserID() + "@" + sessionUser.getPlatform());
       } else {
          /* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸뒤 플랫폼명 추가
           * 네이버 - qwer@ + naver = qwer@naver
           * 카카오 - qwer@ + kakao = qwer@kakao
           */
          galleryCommentName = (sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
       }
       
       // 그 다음 idx에 해당하는 사진첩의 모든 게시물 조회
       List<GalleryVO> galleryList = gallery_dao.selectList(idx);
       // 조회된 모든 게시물을 리스트 형태로 바인딩
       model.addAttribute("galleryList", galleryList);
       // 그 다음 idx에 해당하는 사진첩의 모든 댓글 조회
       List<GalleryCommentVO> commentList = gallery_dao.selectCommentList(idx);
       // 조회된 모든 댓글을 리스트 형태로 바인딩
       model.addAttribute("commentList", commentList);
       // 추가로 세션값도 바인딩
       model.addAttribute("sessionIdx", session.getAttribute("login"));
       // 추가로 댓글 작성을 위해 위에서 세션값으로 조회해서 만든 작성자를 바인딩
       model.addAttribute("sessionName", galleryCommentName);
       // 사진첩페이지로 이동
       return Common.GP_PATH + "gallery_list.jsp";
    }
    
    // 사진첩 글 작성페이지로 이동
    @RequestMapping("/gallery_insert_form.do")
    public String gallery_insert_form() {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       // 세션값이 있다면 작성페이지로 이동
       return Common.GP_PATH + "gallery_insert_form.jsp";
    }
    
    // 사진첩 새 글 작성
    @RequestMapping("/gallery_insert.do")
    public String insert(Integer idx, GalleryVO vo) {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       // 클라이언트의 파일 업로드를 위해 상대경로를 통한 절대경로를 생성
       String webPath = "/resources/upload/"; // 상대경로
       String savePath = app.getRealPath(webPath); // 절대경로
       
       // 업로드를 위해 파라미터로 넘어온 파일의 정보
       MultipartFile galleryFile = vo.getGalleryFile();
       
       // 업로드된 파일이 없을 경우 파일 이름 지정
       String galleryFileName = "no_file";
       
       // 업로드된 파일이 있을 경우
       if ( !galleryFile.isEmpty() ) {
          // 업로드된 파일의 원본 이름을 지정
          galleryFileName = galleryFile.getOriginalFilename();
          // 이미지를 저장할 경로를 지정
          File saveFile = new File(savePath, galleryFileName);
          // 지정할 경로가 없을 경우
          if(!saveFile.exists()) {
             // 경로를 생성
             saveFile.mkdirs();
          // 지정할 경로가 있을 경우
          }else {
             // 파일명 중복 방지를 위해 파일명 앞에 시간 추가
             long time = System.currentTimeMillis();
             galleryFileName = String.format("%d_%s", time, galleryFileName);
             saveFile = new File(savePath, galleryFileName);
          }
          
          // 업로드된 파일을 실제로 등록 - try~catch 필요
          try {
             // 업로드된 파일을 실제로 등록해주는 메소드
             galleryFile.transferTo(saveFile);
          } catch (IllegalStateException e) {
             e.printStackTrace();
          } catch (IOException e) {
             e.printStackTrace();
          }
       }
       
       /* 확장자 구하기
        * 파일 원본 이름에서 마지막 .이 들어간 위치에서 한칸 더한 위치부터 끝까지 잘라내기
        * ex) 19292930388시바견.img --> .img --> img
        */
       String extension = galleryFileName.substring( galleryFileName.lastIndexOf( "." ) + 1 );
       if ( extension.equals("mp4") ) {
          // 확장자가 비디오일 경우
          vo.setGalleryFileExtension("video");
       } else if ( extension.equals("jpg") || extension.equals("png") ) {
          // 확장자가 이미지일 경우
          vo.setGalleryFileExtension("image");
       } else if ( galleryFileName.equals("no_file") ) {
          // 파일이 없을 경우
          vo.setGalleryFileExtension("no_file");
       } else {
          // 파일의 확장자가 사용할 수 없는 경우
          vo.setGalleryFileExtension("no_file");
       }
       
       // 해당 idx의 사진첩에 작성된 글 갯수 조회
       int countNum = gallery_dao.selectCountNum(idx);
       // 작성된 게시글이 한개도 없을 경우
       if ( countNum == 0 ) {
          // 게시글에 시작번호 1을 지정
          vo.setGalleryContentRef(1);
          // 사진첩의 idx 지정
          vo.setGallIdx(idx);
          // 파일 이름 지정
          vo.setGalleryFileName(galleryFileName);
          // 게시글에 좋아요 시작갯수 0개 지정
          vo.setGalleryLikeNum(0);
          // 작성한 게시글을 DB에 저장
          gallery_dao.insert(vo);
          // 저장 성공시 idx를 들고 사진첩 첫페이지로 이동
          return "redirect:gallery.do?idx=" + idx;
       }
       // 작성된 글이 있을 경우
       // 가장 최근에 작성한 게시글의 번호에 1 더하기
       vo.setGalleryContentRef(countNum + 1);
       // 사진첩의 idx 지정
       vo.setGallIdx(idx);
       // 파일 이름 지정
       vo.setGalleryFileName(galleryFileName);
       // 게시글에 좋아요 시작갯수 0개 지정
       vo.setGalleryLikeNum(0);
       // 작성한 게시글을 DB에 저장
       gallery_dao.insert(vo);
       // 저장 성공시 idx를 들고 사진첩 첫페이지로 이동
       return "redirect:gallery.do?idx=" + idx;
    }
    
    // 사진첩 글 삭제
    @RequestMapping("/gallery_delete.do")
    @ResponseBody // Ajax로 요청된 메서드는 결과를 콜백메서드로 돌려주기 위해 반드시 @ResponseBody가 필요!!
    public String delete(GalleryVO vo, GalleryLikeVO lvo, GalleryCommentVO cvo) {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       // 삭제할 사진첩의 게시글에 idx와 ref를 VO에서 따로 분리
       int idx = vo.getGallIdx(); // 삭제할 사진첩의 idx
       int ref = vo.getGalleryContentRef(); // 삭제할 사진첩의 게시글에 ref
       
       // idx와 ref를 사용하기 위해 Map으로 둘다 저장한다.
       HashMap<String, Integer> map = new HashMap<String, Integer>();
       map.put("1", idx); // 삭제할 사진첩의 idx 저장
       map.put("2", ref); // 삭제할 사진첩의 게시글 번호 저장
       
       // DB에 저장된 게시글중 가져온 정보에 해당하는 게시글 삭제
       int res = gallery_dao.delete(vo);
       
       // 삭제 실패할 경우
       String result = "no";
       if (res == 1) {
          // 삭제 성공할 경우
          result = "yes";
          
          // 게시글 삭제 후 게시글 번호 재정렬
          // 게시글 삭제 후 삭제한 게시글 번호보다 큰 번호의 게시글들 조회
          List<GalleryVO> list = gallery_dao.selectListDelete(map);
          // forEach문
          for ( GalleryVO uref : list ) {
             // 조회된 게시글 번호들을 1씩 감소
             uref.setGalleryContentRef(uref.getGalleryContentRef() - 1);
             // 1씩 감소된 번호들을 다시 갱신
             gallery_dao.updateRefMinus(uref);
          }
          
          // 게시글 삭제시 댓글도 삭제
          // GalleryCommentVO에 삭제된 사진첩의 idx 지정
          cvo.setGalleryCommentIdx(idx);
          // GalleryCommentVO에 삭제된 사진첩의 게시글 번호 지정
          cvo.setGalleryCommentRef(ref);
          // DB에 저장된 댓글중 삭제된 게시글에 해당하는 댓글만 삭제
          gallery_dao.deleteCommentAll(cvo);
          
          // 게시글 삭제시 좋아요도 삭제
          // GalleryLikeVO에 삭제된 사진첩의 idx 지정
          lvo.setGalleryLikeIdx(idx);
          // GalleryLikeVO에 삭제된 사진첩의 게시글 번호 지정
          lvo.setGalleryLikeRef(ref);
          // DB에 저장된 좋아요중 삭제된 게시글에 해당하는 좋아요만 삭제
          gallery_dao.deleteLikeAll(lvo);
       }
       // 콜백메소드에 전달
       return result;
    }
    
    // 사진첩 글 수정 페이지로 이동
    @RequestMapping("/gallery_modify_form.do")
    public String modify_form(Integer idx, GalleryVO vo, Model model) {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       // 해당 idx의 사진첩에 수정할 게시글을 조회
       GalleryVO refVo = gallery_dao.selectOneRef(vo);
       if ( vo != null ) {
          // 조회가 성공할 경우 바인딩
          model.addAttribute("vo", refVo);
       }
       // 수정페이지로 이동
       return Common.GP_PATH + "gallery_modify_form.jsp";
    }
    
    // 게시글 수정하기
    @RequestMapping("/gallery_modify.do")
    public String modify(GalleryVO vo) {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       // 클라이언트의 파일 업로드를 위해 상대경로를 통한 절대경로를 생성
       String webPath = "/resources/upload/"; // 상대경로
       String savePath = app.getRealPath(webPath); // 절대경로
       
       // 업로드를 위해 파라미터로 넘어온 파일의 정보
       MultipartFile galleryFile = vo.getGalleryFile();
       
       // 업로드된 파일이 없을 경우 이미 저장되있는 파일 이름 지정
       String galleryFileName = vo.getGalleryFileName();
       // 업로드된 파일이 있을 경우
       if ( !galleryFile.isEmpty() ) {
          // 이미 저장되있는 파일 삭제
          File delFile = new File(savePath, vo.getGalleryFileName());
          delFile.delete();
          // 업로드된 파일의 원본 이름을 지정
          galleryFileName = galleryFile.getOriginalFilename();
          // 이미지를 저장할 경로를 지정
          File saveFile = new File(savePath, galleryFileName);
          // 지정할 경로가 없을 경우
          if(!saveFile.exists()) {
             // 경로를 생성
             saveFile.mkdirs();
          // 지정할 경로가 있을 경우
          }else {
             // 파일명 중복 방지를 위해 파일명 앞에 시간 추가
             long time = System.currentTimeMillis();
             galleryFileName = String.format("%d_%s", time, galleryFileName);
             saveFile = new File(savePath, galleryFileName);
          }
          
          // 업로드된 파일을 실제로 등록 - try~catch 필요
          try {
             // 업로드된 파일을 실제로 등록해주는 메소드
             galleryFile.transferTo(saveFile);
             // 파일 이름 지정
             vo.setGalleryFileName(galleryFileName);
             /* 확장자 구하기
              * 파일 원본 이름에서 마지막 .이 들어간 위치에서 한칸 더한 위치부터 끝까지 잘라내기
              * ex) 19292930388시바견.img --> .img --> img
              */
             String extension = vo.getGalleryFileName().substring( vo.getGalleryFileName().lastIndexOf( "." ) + 1 );
             if ( extension.equals("mp4")  ) {
                // 확장자가 비디오일 경우
                vo.setGalleryFileExtension("video");
             } else if ( extension.equals("jpg") || extension.equals("png") ) {
                // 확장자가 이미지일 경우
                vo.setGalleryFileExtension("image");
             } else {
                // 파일의 확장자가 사용할 수 없는 경우
                vo.setGalleryFileExtension("no_file");
             }
          } catch (IllegalStateException e) {
             e.printStackTrace();
          } catch (IOException e) {
             e.printStackTrace();
          }
       }
       // 수정된 파일의 정보로 DB에 갱신
       gallery_dao.update(vo);
       // 갱신 성공시 idx를 들고 사진첩 첫페이지로 이동
       return "redirect:gallery.do?idx=" + vo.getGallIdx();
    }
    
    /////////////// 사진첩 댓글 구역 ///////////////
    @RequestMapping("/comment_insert.do")
    @ResponseBody
    public String gallery_reply(GalleryCommentVO cvo) {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       Integer sessionIdx = (Integer)session.getAttribute("login");
       
       int countNum = gallery_dao.selectCommentNum(cvo);
       
       if ( countNum == 0 ) {
          // 댓글에 시작번호 1을 지정
          cvo.setGalleryNum(1);
          cvo.setGalleryCommentDeleteCheck(0);
          cvo.setGalleryCommentSession(sessionIdx);
          // 작성한 댓글을 DB에 저장
          int res = gallery_dao.insertComment(cvo);
          // 저장 실패할 경우
          String result = "no";
          if ( res == 1 ) {
             // 저장 성공할 경우
             result = "yes";
          }
          // 콜백메소드에 전달
          return result;
       }
       // 가장 최근에 작성한 댓글의 번호에 1 더하기
       cvo.setGalleryNum(countNum + 1);
       cvo.setGalleryCommentDeleteCheck(0);
       cvo.setGalleryCommentSession(sessionIdx);
       // 작성한 댓글을 DB에 저장
       int res = gallery_dao.insertComment(cvo);
       // 저장 실패할 경우
       String result = "no";
       if ( res == 1 ) {
          // 저장 성공할 경우
          result = "yes";
       }
       // 콜백메소드에 전달
       return result;
    }
    
    //댓글 삭제
    @RequestMapping("/gcomment_delete.do")
    @ResponseBody
    public String gcomment_delete(GalleryCommentVO cvo) {
       cvo.setGalleryCommentDeleteCheck(-1);
       // DB에 저장된 게시글중 가져온 정보에 해당하는 게시글 삭제
       int res = gallery_dao.deleteGComment(cvo);
       String result = "no";
       if (res == 1) {
          // 삭제 성공할 경우
          result = "yes";
       }
       return result;
    }
    
    /////////////// 사진첩 좋아요 구역 ///////////////
    @RequestMapping("/gallery_like.do")
    @ResponseBody
    public String gallery_like(GalleryVO vo, GalleryLikeVO lvo) {
       // 세션값이 있는지 확인
       HttpSession session = request.getSession();
       if ( session.getAttribute("login") == null ) {
          // 세션값이 없다면 로그인페이지로 이동
          return "redirect:login.do";
       }
       
       // 세션값을 사용하기 위해 Integer타입으로 형변환
       Integer sessionIdx = (Integer)session.getAttribute("login");
       // 좋아요를 누른 로그인한 유저의 세션값 지정
       lvo.setGalleryLikeSession(sessionIdx);
       // 사진첩의 idx 지정
       lvo.setGalleryLikeIdx(vo.getGallIdx());
       // 좋아요를 누른 게시글의 번호
       lvo.setGalleryLikeRef(vo.getGalleryContentRef());
       // 먼저 DB에 로그인한 유저가 해당 idx의 사진첩 게시글에 좋아요를 눌렀는지 조회
       GalleryLikeVO likeVo = gallery_dao.selectOneLike(lvo);
       // 이미 좋아요를 눌렀을 경우
       if ( likeVo != null ) {
          // 이미 눌린 좋아요를 다시 누를 경우 취소되므로 좋아요 내역을 삭제
          gallery_dao.deleteLike(lvo);
          // 좋아요 갯수 조회
          int likeCount = gallery_dao.selectLikeCountNum(lvo);
          // 조회된 좋아요 갯수를 지정
          vo.setGalleryLikeNum(likeCount);
          // 조회된 좋아요 갯수로 갱신
          gallery_dao.updateLikeNum(vo);
          // 좋아요 갯수를 콜백메소드에 전달하기 위해 String타입으로 형변환
          String likeNum = Integer.toString(likeCount);
          // 콜백메소드에 전달
          return likeNum;
       }
       // 좋아요를 안눌렀을 경우
       // 좋아요를 누를 경우 좋아요 내역을 추가
       gallery_dao.insertLike(lvo);
       // 좋아요 갯수 조회
       int likeCount = gallery_dao.selectLikeCountNum(lvo);
       // 조회된 좋아요 갯수를 지정
       vo.setGalleryLikeNum(likeCount);
       // 조회된 좋아요 갯수로 갱신
       gallery_dao.updateLikeNum(vo);
       // 좋아요 갯수를 콜백메소드에 전달하기 위해 String타입으로 형변환
       String likeNum = Integer.toString(likeCount);
       // 콜백메소드에 전달
       return likeNum;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 방명록 조회
	@RequestMapping("/guestbook.do")
	public String guestbook_list (Integer idx, Model model) {
		// 방명록에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 그 다음 idx에 해당하는 방명록의 모든 방문글을 조회
		List<GuestBookVO> list = guestbook_dao.selectList(idx);
		// 조회된 모든 방문글을 리스트 형태로 바인딩
		model.addAttribute("list", list); //바인딩
		// 추가로 세션값도 바인딩
		model.addAttribute("sessionIdx", session.getAttribute("login"));
		// 방명록페이지로 이동
		return Common.GBP_PATH + "guestbook_list.jsp";
	}
	
	// 방명록 방문글 작성페이지로 이동
	@RequestMapping("/guestbook_insert_form.do")
	public String guestbook_insert_form(Model model) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 방명록에 작성자를 저장하기 위한 세션값에 해당하는 유저정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		// 방문글 작성자 정보 지정
		GuestBookVO vo = new GuestBookVO();
		if ( sessionUser.getPlatform().equals("cyworld") ) {
			// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
			vo.setGuestbookContentName(sessionUser.getUserID() + "@" + sessionUser.getPlatform());
		} else {
			/* 플랫폼이 소셜일 경우 - 이메일 @부분까지 잘라낸뒤 플랫폼명 추가
			 * 네이버 - qwer@ + naver = qwer@naver
			 * 카카오 - qwer@ + kakao = qwer@kakao
			 */
			vo.setGuestbookContentName(sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") + 1 ) + sessionUser.getPlatform());
		}
		// 미리 지정한 작성자 이를을 바인딩
		model.addAttribute("guestbookContentName", vo.getGuestbookContentName());
		// 세션값으로 미니미 지정
		model.addAttribute("minimi", sessionUser.getMinimi());
		// 작성페이지로 이동
		return Common.GBP_PATH + "guestbook_insert_form.jsp";
	}
	
	// 방명록 새 방문글 작성
	@RequestMapping("/guestbook_insert.do")
	public String guestbook_insert(Integer idx, GuestBookVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 방문글에 작성자의 idx를 저장하기 위해 Integer로 형변환
		Integer sessionIdx = (Integer)session.getAttribute("login");
		
		// 해당 idx의 방명록에 작성된 방문글 갯수 조회
		int countNum = guestbook_dao.selectCountNum(idx);
		// 방명록에 글이 한개도 없을경우
		if ( countNum == 0 ) {
			// 방명록에 시작번호 1 부여
			vo.setGuestbookContentRef(1);
			// 방명록의 idx 지정
			vo.setGuestIdx(idx);
			// 방문글에 좋아요 시작갯수 0개 지정
			vo.setGuestbookLikeNum(0);
			// 방문글에 작성자 idx 지정
			vo.setGuestbookSession(sessionIdx);
			// 작성한 방문글을 DB에 저장
			guestbook_dao.insert(vo);
			// 저장 성공시 idx를 들고 방명록 첫페이지로 이동
			return "redirect:guestbook.do?idx=" + idx;
		}
		// 작성된 방명록이 있을경우
		// 가장 최근에 작성한 방문글의 번호에 1 더하기
		vo.setGuestbookContentRef(countNum + 1);
		// 방명록의 idx 지정
		vo.setGuestIdx(idx);
		// 방문글에 좋아요 시작갯수 0개 지정
		vo.setGuestbookLikeNum(0);
		// 방문글에 작성자 idx 지정
		vo.setGuestbookSession(sessionIdx);
		// 작성한 방문글을 DB에 저장
		guestbook_dao.insert(vo);
		// 저장 성공시 idx를 들고 방명록 첫페이지로 이동
		return "redirect:guestbook.do?idx=" + idx;
	}
	
	// 방명록 삭제
	@RequestMapping("/guestbook_delete.do")
	@ResponseBody // Ajax로 요청된 메서드는 결과를 콜백 메서드로 돌아가기 위해 반드시 필요한 어노테이션
	public String guestbook_delete(GuestBookVO vo, GuestBookLikeVO lvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 삭제할 방명록의 방문글에 idx와 ref를 VO에서 따로 분리
		int idx = vo.getGuestIdx(); // 삭제할 방명록의 idx
		int ref = vo.getGuestbookContentRef(); // 삭제할 방명록의 방문글 번호
		
		// idx와 ref를 사용하기 위해 Map으로 둘다 저정한다.
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("1", idx); // 삭제할 방명록의 idx 저장
		map.put("2", ref); // 삭제할 방명록의 방문글 번호 저장
		
		// DB에 저장된 방문글중 가져온 정보에 해당하는 방문글 삭제
		int res = guestbook_dao.delete(vo);
		
		// 삭제 실패할 경우
		String result = "no";
		if (res == 1) {
			// 삭제 성공할 경우
			result = "yes";
			// GuestBookLikeVO에 삭제된 방명록의 idx 지정
			lvo.setGuestbookLikeIdx(idx);
			// GuestBookLikeVO에 삭제된 방명록의 방문글 번호 지정
			lvo.setGuestbookLikeRef(ref);
			// DB에 저장된 좋아요중 삭제된 방문글에 해당하는 좋아요만 삭제
			guestbook_dao.deleteLikeAll(lvo);
			// 방문글 삭제 후 삭제한 방문글 번호보다 큰 번호의 방문글들 조회
			List<GuestBookVO> list = guestbook_dao.selectListDelete(map);
			// forEach문
			for ( GuestBookVO uref : list ) {
				// 조회된 방문글 번호들을 1씩 감소
				uref.setGuestbookContentRef(uref.getGuestbookContentRef() - 1);
				// 1씩 감소된 번호들을 다시 갱신
				guestbook_dao.updateRefMinus(uref);
			}
		}
		// 콜백메소드에 전달
		return result;
	}
	
	// 글 수정 폼으로 전환
	@RequestMapping("/guestbook_modify_form.do")
	public String guestbook_modify_form(GuestBookVO vo, Model model) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 방명록에 작성자를 저장하기 위한 세션값에 해당하는 유저정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		
		
		// 해당 idx의 방명록에 수정할 방문글을 조회
		GuestBookVO updateVo = guestbook_dao.selectOne(vo);
		if ( updateVo != null ) {
			// 조회가 성공할 경우 바인딩
			model.addAttribute("vo", updateVo);
			// 세션값으로 미니미 지정
			model.addAttribute("minimi", sessionUser.getMinimi());
		}
		// 수정페이지로 이동
		return Common.GBP_PATH + "guestbook_modify_form.jsp";
	}
    
	// 게시글 수정하기
	@RequestMapping("/guestbook_modify.do")
	@ResponseBody
	public String guestbook_modify(GuestBookVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인페이지로 이동
			return "redirect:login.do";
		}
		
		// 수정된 파일의 정보로 DB에 갱신
		int res = guestbook_dao.update(vo);
		// 갱신 실패할 경우 "no"를 전달
		String result = "{'result':'no'}";
		if (res != 0) {
			// 갱신 성공할 경우 "yes"를 전달
			result = "{'result':'yes'}";
		}
		// 콜백메소드에 전달
		return result;
	}
	
	/////////////// 방명록 좋아요 구역 ///////////////
	@RequestMapping("/guestbook_like.do")
	@ResponseBody
	public GuestBookVO geustbook_like(GuestBookVO vo, GuestBookLikeVO lvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
//		if ( session.getAttribute("login") == null ) {
//			// 세션값이 없다면 로그인페이지로 이동
//			return "redirect:login.do";
//		}
		
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
			// 좋아요 갯수 조회
			int likeCount = guestbook_dao.selectLikeCountNum(lvo);
			// 조회된 좋아요 갯수를 지정
			vo.setGuestbookLikeNum(likeCount);
			// 조회된 좋아요 갯수로 갱신
			guestbook_dao.updateLikeNum(vo);
			// 콜백메소드에 가져갈 유저정보
			GuestBookVO gvo = guestbook_dao.selectOne(vo);
			// 콜백메소드에 전달
			return gvo;
		}
		// 좋아요를 안눌렀을 경우
		// 좋아요를 누를 경우 좋아요 내역을 추가
		guestbook_dao.insertLike(lvo);
		// 좋아요 갯수 조회
		int likeCount = guestbook_dao.selectLikeCountNum(lvo);
		// 조회된 좋아요 갯수를 지정
		vo.setGuestbookLikeNum(likeCount);
		// 조회된 좋아요 갯수로 갱신
		guestbook_dao.updateLikeNum(vo);
		// 콜백메소드에 가져갈 유저정보
		GuestBookVO gvo = guestbook_dao.selectOne(vo);
		// 콜백메소드에 전달
		return gvo;
	}
}
