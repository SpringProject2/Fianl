package com.korea.cyworld;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import dao.DiaryDAO;
import dao.GalleryDAO;
import dao.GuestBookDAO;
import dao.MainDAO;
import dao.SignUpDAO;
import mail.MailKey;
import util.Common;
import vo.SignUpVO;

@Controller
public class SignUpController {
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
	// 기본 및 로그인
	@RequestMapping(value= {"/", "login.do"})
	public String basic() {
		// 이제 기본 페이지는 항상 세션값을 확인해서 로그인 페이지와 메인 페이지로 나눈다
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return Common.S_PATH + "login.jsp";
		}
		// 세션값이 있다면 이제 로그인은 건너뛰고 세션값에 해당하는 메인 페이지로 바로 이동
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	// 로그아웃
	@RequestMapping("/logout.do")
	public String logout() {
		// 로그아웃을 하면 세션을 비운다
		HttpSession session = request.getSession();
		session.removeAttribute("login");
		// 세션을 비우면 다시 로그인 페이지로 이동한다
		return "redirect:login.do";
	}
	
	// 네이버 API 콜백용
	@RequestMapping("/login_naver_callback.do")
	public String naver_join() {
		return Common.S_PATH + "login_naver_callback.jsp";
	}
	
	// 각 플랫폼 별 가입자와 비가입자 구별
	@RequestMapping("/login_authentication.do")
	public String login_authentication(SignUpVO vo, Model model) {
		// 플랫폼이 cyworld일때
		if ( vo.getPlatform().equals("cyworld") ) {
			// 가져온 정보중에 ID가 없을경우 - 회원가입
			if ( vo.getUserID() == null ) {
				model.addAttribute("vo", vo);
				// cyworld 회원가입 페이지
				return Common.S_PATH + "cyworld_join.jsp";
			}
			// 가져온 정보 중에 ID가 있을경우 - 로그인
			// 로그인한 ID로 회원 정보 조회
			SignUpVO loginVo = signUp_dao.selectOneIdCheck(vo.getUserID());
			
			HttpSession session = request.getSession();
			// 세션값이 있을 경우
			if ( session.getAttribute("login") != null ) {
				if ( (int)session.getAttribute("login") < 0 ) {
					// 세션값이 비회원으로 로그인으로 돌아올 경우 세션 제거
					session.removeAttribute("login");
				}
			}
			// 세션값이 없을 경우
			if ( session.getAttribute("login") == null ) {
				// 로그인 세션 부여
				session.setAttribute("login", loginVo.getIdx());
			}
			
			// 세션값에 해당하는 메인 페이지로 이동
			return "redirect:main.do?idx=" + session.getAttribute("login");
		}
		
		// 플랫폼이 소셜일 때 (카카오 및 네이버)
		// 플랫폼 별 가입자 조회 - 플랫폼 + 이메일
		SignUpVO joinVo = signUp_dao.selectOnePlatformEmail(vo);
		// 조회된 값이 없을 때 - 회원가입
		if ( joinVo == null ) {
			// 플랫폼이 카카오일 때
			if ( vo.getPlatform().equals("kakao") ) {
				model.addAttribute("vo", vo);
				// 카카오 회원가입 페이지
				return Common.S_PATH + "kakao_join.jsp";
			}
			// 플랫폼이 네이버일 때
			if ( vo.getPlatform().equals("naver") ) {
				model.addAttribute("vo", vo);
				// 네이버 회원가입 페이지
				return Common.S_PATH + "naver_join.jsp";
			}
		}
		// 조회된 값이 있을 때 - 로그인
		// 소셜 로그인은 이곳에서 세션을 발급받는다
		HttpSession session = request.getSession();
		// 세션값이 있을 경우
		if ( session.getAttribute("login") != null ) {
			if ( (int)session.getAttribute("login") < 0 ) {
				// 세션값이 비회원으로 로그인으로 돌아올 경우 세션 제거
				session.removeAttribute("login");
			}
		}
		// 세션값이 없을 경우
		if ( session.getAttribute("login") == null ) {
			// 로그인 세션 지정
			session.setAttribute("login", joinVo.getIdx());
			
			// 접속 날짜를 기록하기 위해 Date객체 사용
			Date date = new Date();
			// Date객체를 그냥 사용하면 뒤에 시간까지 모두 기록되기에 날짜만 따로 뺴는 작업을 한다
			SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
			// 위에서 구한 현재 날짜를 로그인한 유저의 접속 날짜에 입력
			joinVo.setToDate(today.format(date));
			// 로그인한 유저의 접속 날짜를 갱신
			signUp_dao.updateTodayDate(joinVo);
		}
		// 세션값에 해당하는 메인 페이지로 이동
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	// ID 중복 체크
	@RequestMapping("/double_check.do")
	@ResponseBody
	public String doubleCheck(String userID) {
		// 넘어온 ID값으로 이미 가입한 유저가 있는지 조회
		SignUpVO vo = signUp_dao.selectOneIdCheck(userID);
		
		// JSON
		// ID 중복일때
		String result = "{'result':'no'}";
		if ( vo != null ) {
			// ID 중복이 아닐 때
			result = "{'result':'yes'}";
		}
		// 콜백 메소드에 결과값 전송
		return result;
	}
	
	// 이메일 인증
	@RequestMapping("/emailCheck.do")
	@ResponseBody
	public String emailCheck(SignUpVO vo) {
		// 미리 만들어 둔 랜덤키 생성 메소드를 mail패키지의 MailKey.java에서 가져와 사용한다
		String mail_key = new MailKey().getKey(10, false); // 랜덤키 길이 설정
		// Mail Server 설정
		String charSet = "UTF-8"; // 사용할 언어셋
		String hostSMTP = "smtp.naver.com"; // 사용할 SMTP
		String hostSMTPid = ""; // 사용할 SMTP에 해당하는 ID
		String hostSMTPpwd = ""; // 사용할 ID에 해당하는 PWD
		
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 보내는 사람 E-Mail, 제목, 내용 
		String fromEmail = ""; // 보내는 사람 email
		String fromName = "관리자"; // 보내는 사람 이름
		String subject = "[Cyworld] 이메일 인증번호 발송 안내입니다."; // 제목
		
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
			email.setHtmlMsg(
					"<p>" + "[메일 인증 안내입니다.]" + "</p>" +
					"<p>" + "Cyworld를 사용해 주셔서 감사드립니다." + "</p>" +
					"<p>" + "아래 인증 코드를 '인증번호'란에 입력해 주세요." + "</p>" +
					"<p>" + mail_key + "</p>"); // 본문 내용
			email.send(); // 메일 보내기
			// 메일 보내기가 성공하면 메일로 보낸 랜덤키를 콜백 메소드에도 전달
			return mail_key;
		} catch (Exception e) {
			System.out.println(e);
			// 메일 보내기가 실패하면 "false"를 콜백 메소드에 전달
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
			// 조회된 ID가 없을 경우 "no"를 콜백 메소드에 전달
			return "no";
		}
		// 조회된 ID가 있을 경우 조회된 ID를 콜백 메소드에 전달
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
			// 조회된 비밀번호가 없을경우 "no"를 콜백 메소드에 전달
			return "no";
		}
		// 조회된 비밀번호가 있을 경우 해당 비밀번호를 가져오는 게 아닌 임시 비밀번호로 랜덤키를 발급한다
		// 미리 만들어둔 랜덤키 생성 메소드를 mail패키지의 MailKey.java에서 가져와 사용한다
		String mail_key = new MailKey().getKey(10, false); // 랜덤키 길이 설정
		// Mail Server 설정
		String charSet = "UTF-8"; // 사용할 언어셋
		String hostSMTP = "smtp.naver.com"; // 사용할 SMTP
		String hostSMTPid = ""; // 사용할 SMTP에 해당하는 ID
		String hostSMTPpwd = ""; // 사용할 ID에 해당하는 PWD
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 보내는 사람 E-Mail, 제목, 내용 
		String fromEmail = ""; // 보내는 사람 email
		String fromName = "관리자"; // 보내는 사람 이름
		String subject = "[Cyworld] 임시 비밀번호 발급 안내입니다."; // 제목
		
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
			email.setHtmlMsg(
					"<p>" + "[임시 비밀번호 안내입니다.]" + "</p>" +
					"<p>" + "Cyworld를 이용해 주셔서 감사합니다." + "</p>" +
					"<p>" + "아래 임시 비밀번호를 이용해 로그인 후 프로필 변경에서 비밀번호를 재설정해 주세요." + "</p>" +
					"<p>" + mail_key + "</p>"); // 본문 내용
			email.send(); // 메일 보내기
			
			/* HashMap - java.util.HashMap
			 * 메일 보내기가 성공하면 해당 ID의 비밀번호에 랜덤키를 보내야 하는데,
			 * 그럼 ID도 가져가야 하고 랜덤키도 가져가야 한다
			 * 하지만 ID는 SignUpVO에 들어 있고, 랜덤키는 MailKey에 들어 있기에
			 * 이 두 가지 정보를 하나로 통합해서 가져가고 싶다면
			 * Map을 생성해서 두 가지 정보를 다 담아서 가져가면 된다
			 * Tip: 키 타입으로 String을 사용하는 게 대체로 편리하다
			 * Tip: 나중에 VO를 통째로 가져갈때는 value 타입을 Object로 사용한다
			 * Map구조 파악용
			 * HashMap<String, Object> map = new HashMap<String, Object>();
			 * map.put("a", vo);
			 * map.put("b", mail_key);
			 */
			HashMap<String, String> m_key = new HashMap<String, String>();
			m_key.put("1", vo.getUserID()); // 1번키에 ID를 넣는다
			m_key.put("2", mail_key); // 2번키에 임시 비밀번호를 넣는다
			// 메일 보내기가 성공하면 DB에 저장되어 있는 비밀번호를 메일로 보낸 랜덤키로 갱신
			signUp_dao.updateNewPw(m_key);
			// 비밀번호 갱신이 성공하면 갱신한 비밀번호 랜덤키를 콜백 메소드에도 전달
			return mail_key;
		} catch (Exception e) {
			System.out.println(e);
			// 메일 보내기가 실패하면 "false"를 콜백 메소드에 전달
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
		
		// ID값으로 고객 정보를 가져온다
		SignUpVO loginCheckVo = signUp_dao.selectOneIdCheck(id);
		
		// 존재하지 않는 ID
		if ( loginCheckVo == null ) {
			// 콜백 메소드에 JSON형태로 전달
			return "{'result':'no_id'}";
		}
		
		// ID는 존재
		// 입력한 비밀번호와 DB에 ID에 해당하는 비밀번호가 불일치
		if ( !loginCheckVo.getInfo().equals(pw) ) {
			// 콜백 메소드에 JSON형태로 전달
			return "{'result':'no_info'}";
		}
		
		// 모두 일치 - 로그인 가능
		// 콜백 메소드에 JSON형태로 전달
		return "{'result':'clear'}";
	}
	
	// 회원가입 시 추가적으로 더 필요한 정보를 넣기 위한 장소
	@RequestMapping("/welcome.do")
	@ResponseBody
	public String welcome(SignUpVO vo, Model model) {
		// 가입 전 가입자인지 체크
		SignUpVO svo = signUp_dao.selectJoinCheck(vo);
		// 가입자가 아닐 경우
		String result = "no";
		// 조회한 값이 있을 경우 - 가입자
		if ( svo != null ) {
			// 콜백 메소드에 전달
			return result;
		// 조회한 값이 없을 경우 - 비가입자
		} else {
			// 먼저 접속 날짜에 가입 날짜를 기록하기 위해 Date객체 사용
			Date date = new Date();
			// Date객체를 그냥 사용하면 뒤에 시간까지 모두 기록되기에 날짜만 따로 뺴는 작업을 한다
			SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
			
			// cyworld로 회원가입자가 들어올 때
			if ( vo.getPlatform().equals("cyworld") ) {
				// 추가 정보들을 임의로 지정
				vo.setMinimi("mainMinimi.png"); // 기본 미니미 지정
				vo.setDotoryNum(0); // 기본 도토리 개수 지정
				vo.setMainTitle("안녕하세요~ " + vo.getName() + "님의 미니홈피입니다!"); // 메인 화면 제목
				vo.setMainPhoto("no_photo"); // 메인 화면 사진 지정
				vo.setMainText(vo.getName() + "님의 미니홈피에 오신걸 환영합니다!"); // 메인 화면 소개글
				vo.setIlchon(0); // 일촌 수 지정
				vo.setToday(0); // 일일 조회수
				vo.setTotal(0); // 누적 조회수
				vo.setToDate(today.format(date)); // 접속 날짜 ( 가입 날짜 )
				// 가입 성공 시 유저 정보 저장
				signUp_dao.insertJoinSuccess(vo);
				// 저장 성공할 경우
				result = "yes";
				// 콜백메소드에 전달
				return result;
			// 소셜 회원가입자가 들어올 때
			} else {
				// 추가 정보들을 임의로 지정
				vo.setUserID(""); // 소셜 로그인이라 ID정보 없음
				vo.setInfo(""); // 소셜 로그인이라 PW정보 없음
				vo.setMinimi("mainMinimi.png"); // 기본 미니미 지정
				vo.setDotoryNum(0); // 기본 도토리 개수 지정
				vo.setMainTitle("안녕하세요~ " + vo.getName() + " 님의 미니홈피입니다!"); // 메인 화면 제목
				vo.setMainPhoto("no_photo"); // 메인 화면 사진 지정
				vo.setMainText(vo.getName() + "님의 미니홈피에 오신걸 환영합니다!"); // 메인 화면 소개글
				vo.setIlchon(0); // 일촌 수 지정
				vo.setToday(0); // 일일 조회수
				vo.setTotal(0); // 누적 조회수
				vo.setToDate(today.format(date)); // 접속 날짜 ( 가입 날짜 )
				// 가입 성공 시 유저 정보 저장
				signUp_dao.insertJoinSuccess(vo);
				// 저장 성공할 경우
				result = "yes";
				// 콜백 메소드에 전달
				return result;
			}
		}
	}
}