package com.korea.cyworld;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dao.MainDAO;
import dao.SignUpDAO;
import mail.MailKey;
import util.Common;
import vo.MainVO;
import vo.SignUpVO;

@Controller
public class SignUpController {
	// SignUpDAO
	SignUpDAO signUp_dao;
	MainDAO main_dao;
	// SI방식
	public void setSignUp_dao(SignUpDAO signUp_dao) {
		this.signUp_dao = signUp_dao;
	}
	public void setMain_dao(MainDAO main_dao) {
		this.main_dao = main_dao;
	}
	
	@RequestMapping(value= {"/", "login.do", "/logout.do"})
	public String login() {
		return Common.S_PATH + "login.jsp";
	}
	
	@RequestMapping("/login_naver_callback.do")
	public String naver_join() {
		return Common.S_PATH + "login_naver_callback.jsp";
	}
	
	// 각 플랫폼별로 회원가입 페이지로 이동
	@RequestMapping("/login_authentication.do")
	public String login_authentication(SignUpVO vo, Model model) {
		// 플랫폼이 cyworld일때
		if ( vo.getPlatform().equals("cyworld") ) {
			model.addAttribute("vo", vo);
			return Common.S_PATH + "cyworld_join.jsp";
		}
		
		// 로그인이 소셜 로그인일때 (카카오 및 네이버)
		// 플랫폼별 가입자 조회 - vo.getPlatform + vo.getEmail
		SignUpVO joinVo = signUp_dao.selectOnePlatformEmail(vo);
		System.out.println(joinVo);
		
		// 조회된 값이 없을때
		if ( joinVo == null ) {
			// 플랫폼이 카카오일때
			if ( vo.getPlatform().equals("kakao") ) {
				model.addAttribute("vo", vo);
				return Common.S_PATH + "kakao_join.jsp";
			}
			// 플랫폼이 네이버 일때
			if ( vo.getPlatform().equals("naver") ) {
				model.addAttribute("vo", vo);
				return Common.S_PATH + "naver_join.jsp";
			}
		}
		// 조회된 값이 있을때
		return "redirect:main.do?idx=" + joinVo.getIdx(); // 환영 페이지로 포워딩
	}
	
	// ID 중복체크
	@RequestMapping("/double_check.do")
	@ResponseBody
	public String doubleCheck(String userID) {
		// ID 중복값 조회
		SignUpVO vo = signUp_dao.selectOneDoubleCheck(userID);
		
		// JSON형태로 콜백메소드에 전달할 결과값 생성
		String result = "{'result':'no'}"; // ID 중복일때
		if ( vo != null ) { // ID 중복이 아닐때
			result = "{'result':'yes'}";
		}
		// 콜백메소드에 결과값 전송
		return result;
	}
	
	// 이메일 인증
	@RequestMapping("/emailCheck.do")
	@ResponseBody
	public String emailCheck(SignUpVO vo) {
		String mail_key = new MailKey().getKey(10, false); //랜덤키 길이 설정
		// Mail Server 설정
		String charSet = "UTF-8";
		String hostSMTP = "smtp.naver.com";		
		String hostSMTPid = "sksh0000@naver.com"; // 본인의 아이디 입력		
		String hostSMTPpwd = "a1029384756"; // 비밀번호 입력
		
		// 보내는 사람 EMail, 제목, 내용 
		String fromEmail = "sksh0000@naver.com"; // 보내는 사람 eamil
		String fromName = "관리자";  // 보내는 사람 이름
		String subject = "이메일 인증번호 발송"; // 제목
		
		// 받는 사람 E-Mail 주소
		String mail = vo.getEmail();  // 받는 사람 email
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		try {
			HtmlEmail email = new HtmlEmail();
			email.setDebug(true);
			email.setCharset(charSet);
			email.setSSL(true);
			email.setHostName(hostSMTP);
			email.setSmtpPort(587);	// SMTP 포트 번호 입력

			email.setAuthentication(hostSMTPid, hostSMTPpwd);
			email.setTLS(true);
			email.addTo(mail);
			email.setFrom(fromEmail, fromName, charSet);
			email.setSubject(subject);
			email.setHtmlMsg("<p>"+ mail_key + "</p>"); // 본문 내용
			email.send();
			return mail_key;
		} catch (Exception e) {
			System.out.println(e);
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
		SignUpVO idVo = signUp_dao.selectOneId(vo);
		if ( idVo == null ) {
			return "no";
		}
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
		SignUpVO pwVo = signUp_dao.selectOnePw(vo);
		System.out.println(pwVo);
		if ( pwVo == null ) {
			return "no";
		}
		String mail_key = new MailKey().getKey(10, false); //랜덤키 길이 설정
		// Mail Server 설정
		String charSet = "UTF-8";
		String hostSMTP = "smtp.naver.com";		
		String hostSMTPid = "sksh0000@naver.com"; // 본인의 아이디 입력		
		String hostSMTPpwd = "a1029384756"; // 비밀번호 입력
		
		// 보내는 사람 EMail, 제목, 내용 
		String fromEmail = "sksh0000@naver.com"; // 보내는 사람 eamil
		String fromName = "관리자";  // 보내는 사람 이름
		String subject = "이메일 인증번호 발송"; // 제목
		
		// 받는 사람 E-Mail 주소
		String mail = vo.getEmail();  // 받는 사람 email
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		try {
			HtmlEmail email = new HtmlEmail();
			email.setDebug(true);
			email.setCharset(charSet);
			email.setSSL(true);
			email.setHostName(hostSMTP);
			email.setSmtpPort(587);	// SMTP 포트 번호 입력

			email.setAuthentication(hostSMTPid, hostSMTPpwd);
			email.setTLS(true);
			email.addTo(mail);
			email.setFrom(fromEmail, fromName, charSet);
			email.setSubject(subject);
			email.setHtmlMsg("<p>"+ mail_key + "</p>"); // 본문 내용
			email.send();
			HashMap<String, String> m_key = new HashMap<String, String>();
			m_key.put("1", vo.getUserID());
			m_key.put("2", mail_key);
			
			// Map구조 파악용
			//HashMap<String, Object> map = new HashMap<String, Object>();
			//map.put("a", vo);
			//map.put("b", mail_key);
			
			signUp_dao.updateNewPw(m_key);
			return mail_key;
		} catch (Exception e) {
			System.out.println(e);
			return "false";
		}
	}
	
	// 로그인 체크
	@RequestMapping("/login_check.do")
	@ResponseBody
	public String loginCheck(SignUpVO vo) {
		
		String id = vo.getUserID(); // ID 입력값
		String pw = vo.getInfo(); // 비밀번호 입력값
		
		// ID값으로 고객 정보를 가져온다.
		SignUpVO loginCheckVo = signUp_dao.selectOneDoubleCheck(id);
		
		// 존재하지 않는 ID
		if ( loginCheckVo == null ) {
			return "{'result':'no_id'}";
		}
		
		// ID는 존재
		// 입력한 비밀번호와 DB에 ID에 해당하는 비밀번호가 불일치
		if ( !loginCheckVo.getInfo().equals(pw) ) {
			return "{'result':'no_info'}";
		}
		
		// 모두 일치
		// 로그인 가능
		return "{'result':'clear'}";
	}
	
	// 회원가입 및 로그인시 메인페이지로 가기전 한번 거치는 장소
	@RequestMapping("/welcome.do")
	public String welcome(SignUpVO vo, Model model) {
		// ID로 가입자인지 조회
		SignUpVO loginVo = signUp_dao.selectOneDoubleCheck(vo.getUserID());
		// cyworld로 회원가입자가 들어올때
		if ( loginVo == null ) {
			if ( vo.getPlatform().equals("cyworld") ) {
				// 아직 없는 정보값들 임의로 지정
				vo.setMinimi("");
				vo.setDotoryNum(0);
				// 가입 성공시 고객 정보 저장
				signUp_dao.insertJoinSuccess(vo);
				SignUpVO joinVo = signUp_dao.selectOneDoubleCheck(vo.getUserID());
				return "redirect:main.do?idx=" + joinVo.getIdx();
			}
			// 소셜 회원가입자가 들어올때
			// 아직 없는 정보값들 임의로 지정
			vo.setUserID("");
			vo.setInfo("");
			vo.setInfoR("");
			vo.setMinimi("");
			vo.setDotoryNum(0);
			// 가입 성공시 고객 정보 저장
			signUp_dao.insertJoinSuccess(vo);
			SignUpVO joinVo = signUp_dao.selectOneDoubleCheck(vo.getUserID());
			return "redirect:main.do?idx=" + joinVo.getIdx();
		}
		// 가입자일때 (로그인)
		return "redirect:main.do?idx=" + loginVo.getIdx();
	}
	
	// 메인페이지로 이동
	@RequestMapping("/main.do")
	public String main(Integer idx, Model model) {
		if ( idx < 0 ) {
			return "redirect:nmain.do?idx=" + -1;
		}
		SignUpVO idxVo = signUp_dao.selectOneIdx(idx);
		model.addAttribute("vo", idxVo);
		// 로그인한 사용자의 idx에 해당하는 일촌평만 긁어온다.
		List<MainVO> list = main_dao.selectList(idx);
		model.addAttribute("list", list);
		return Common.P_PATH + "main.jsp";
	}
	
	// 비회원 로그인 메인페이지
	@RequestMapping("/nmain.do")
	public String nmanin(Integer idx, Model model) {
		model.addAttribute(idx);
		return Common.P_PATH + "nmain.jsp";
	}
	
	//일촌평  쓰기 
	@RequestMapping("/insert.do")
	@ResponseBody
	public String insert( MainVO vo, SignUpVO vo1 ) { 
		// servlet-context에서 @Autowired 설정 해서 위에 파라미터 안에  HttpServletRequest request 안써도 됨!
		// 일촌평 갯수 구하기
		int countNum = main_dao.selectCountNum();
		// 일촌평이 한개도 없을경우
		if ( countNum == 0 ) {
			vo.setNum(1);
			vo.setIlIdx(vo1.getIdx());
			String result = "no";
			int res = main_dao.insert(vo);
			if ( res == 1 ) {
				result = "yes";
			}
			return result;
		}
		// 가장 최근에 작성한 일촌평 찾기
		int maxNum = main_dao.selectMaxNum();
//		DB에 새 글을 추가하기 위해 DAO에게 vo를 전달
		vo.setNum(maxNum + 1);
		vo.setIlIdx(vo1.getIdx());
		String result = "no";
		int res = main_dao.insert(vo);
		if ( res == 1 ) {
			result = "yes";
		}
		return result;
//		redirect: view로 이동하는 것이 아닌 Controller의 url 매핑을 호출하기 위한 키워드 
		
	}
}
