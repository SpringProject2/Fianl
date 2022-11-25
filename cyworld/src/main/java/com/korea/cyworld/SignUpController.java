package com.korea.cyworld;

import java.io.File;
import java.io.IOException;
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
import vo.GuestBookVO;
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
	SignUpDAO signUp_dao;
	MainDAO main_dao;
	GalleryDAO gallery_dao;
	GuestBookDAO guestbook_dao;
	
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
	@RequestMapping(value= {"/", "login.do"})
	public String login() {
		HttpSession session = request.getSession();
		
		if ( session.getAttribute("login") == null ) {
			return Common.S_PATH + "login.jsp";
		}
		return "redirect:main.do?idx=" + session.getAttribute("login");
	}
	
	@RequestMapping("/logout.do")
	public String logout() {
		// 세션을 비운다
		HttpSession session = request.getSession();
		session.removeAttribute("login");
		return "redirect:login.do";
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
		String hostSMTPid = "id"; // 본인의 아이디 입력		
		String hostSMTPpwd = "pw"; // 비밀번호 입력
		
		// 보내는 사람 EMail, 제목, 내용 
		String fromEmail = "id"; // 보내는 사람 eamil
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
		String hostSMTPid = "id"; // 본인의 아이디 입력		
		String hostSMTPpwd = "pw"; // 비밀번호 입력
		
		// 보내는 사람 EMail, 제목, 내용 
		String fromEmail = "id"; // 보내는 사람 eamil
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
		SignUpVO sessionId = signUp_dao.selectOneDoubleCheck(id);
		// 모두 일치
		// 로그인 가능
		// 로그인 유지를 위한 세션 부여
		HttpSession session = request.getSession();
		
		String show = (String)session.getAttribute("login");
		if ( show == null ) {
			// 로그인 세션 부여
			session.setAttribute("login", sessionId.getIdx());
		}
		System.out.println(session.getAttribute("login"));
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 메인페이지로 이동
	@RequestMapping("/main.do")
	public String main(Integer idx, Model model) {
		if ( idx < 0 ) {
			return "redirect:nmain.do?idx=" + 1;
		}
		SignUpVO idxVo = signUp_dao.selectOneIdx(idx);
		model.addAttribute("vo", idxVo);
		// 로그인한 사용자의 idx에 해당하는 일촌평만 긁어온다.
		List<MainVO> list = main_dao.selectList(idx);
		model.addAttribute("list", list);
		HttpSession session = request.getSession();
		
		if ( session.getAttribute("login") == null ) {
			return Common.S_PATH + "login.jsp";
		}
		model.addAttribute("sessionIdx", session.getAttribute("login"));
		return Common.P_PATH + "main.jsp";
	}
	
	// 비회원 로그인 메인페이지
	@RequestMapping("/nmain.do")
	public String nmanin(Integer idx, Model model) {
		model.addAttribute(idx);
		return Common.P_PATH + "nmain.jsp";
	}
	
	// 이름 및 ID 및 Email로 유저 검색
	@RequestMapping("/main_search.do")
	@ResponseBody
	public HashMap<String, Object> main_search(SignUpVO vo, String searchType, String searchValue) {
		if ( searchType.equals("name") ) {
			List<SignUpVO> list = main_dao.selectSearchName(searchValue);
			HashMap<String, Object> searchVal = new HashMap<String, Object>();
			searchVal.put("searchList", list);
			searchVal.put("code", "ok");
			return searchVal;
		} else if ( searchType.equals("userID") ) {
			List<SignUpVO> list = main_dao.selectSearchId(searchValue);
			HashMap<String, Object> searchVal = new HashMap<String, Object>();
			searchVal.put("searchList", list);
			searchVal.put("code", "ok");
			return searchVal;
		} else if ( searchType.equals("email") ) {
			List<SignUpVO> list = main_dao.selectSearchEmail(searchValue);
			HashMap<String, Object> searchVal = new HashMap<String, Object>();
			searchVal.put("searchList", list);
			searchVal.put("code", "ok");
			return searchVal;
		}
		HashMap<String, Object> searchVal = new HashMap<String, Object>();
		searchVal.put("code", "no");
		return searchVal;
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 사진첩 조회
	@RequestMapping("/gallery.do")
	public String list(Integer idx, Model model) {

		List<GalleryVO> list = gallery_dao.selectList(idx);
		model.addAttribute("idx", idx);
		model.addAttribute("list", list);// 바인딩 : jsp까지 정보운반
		return Common.GP_PATH + "gallery_list.jsp";// 포워딩
		
	}

	@RequestMapping("/insert_form.do")
	public String gallery_insert_form() {
		return Common.GP_PATH + "gallery_insert_form.jsp";
	}
	
	// 새 글쓰기
	@RequestMapping("/insert_gallery.do")
	// public String insert(String name, String content, String pwd) {
	public String insert(Integer idx, GalleryVO vo) {
		// 클라이언트의 파일 업로드를 위한 절대경로를 생성
		String webPath = "/resources/upload/";
		String savePath = app.getRealPath(webPath);// 절대경로
		System.out.println("경로 : " + savePath);
		
		// 업로드를 위해 파라미터로 넘어온 사진의 정보
		MultipartFile galleryFile = vo.getGalleryFile();
		String galleryFileName = "no_file";
		//업로드 된 파일이 존재할 때!!
		if(!galleryFile.isEmpty()) {
			//업로드가 된 실제 파일의 이름
			galleryFileName = galleryFile.getOriginalFilename();
			//이미지를 저장할 경로를 지정
			File saveFile = new File(savePath, galleryFileName);
			
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}else {
				//동일파일명 변경
				long time = System.currentTimeMillis();
				galleryFileName = String.format("%d_%s", time, galleryFileName);
				saveFile = new File(savePath, galleryFileName);
			}
			
			try {
				//업로드를 위한 파일을 실제로 등록해주는 메서드
				galleryFile.transferTo(saveFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 해당 idx로 사진첩 조회
		int countNum = gallery_dao.selectCountNum(idx);
		// 사진첩에 글이 한개도 없을경우
		if ( countNum == 0 ) {
			// 사진첩에 번호 부여
			vo.setGalleryContentRef(1);
			// 게시글에 해당 사용자의 idx부여
			vo.setGallIdx(idx);
			// 게시글의 파일 이름 부여
			vo.setGalleryFileName(galleryFileName);
			// 확장자 구하기
			String extension = vo.getGalleryFileName().substring( vo.getGalleryFileName().lastIndexOf( "." ) + 1 );
			System.out.println(vo.getGalleryFileExtension());
			if ( extension.equals("mp4")  ) {
				vo.setGalleryFileExtension("video");
			} else {
				vo.setGalleryFileExtension("image");
			}
			gallery_dao.insert(vo);
			
			return "redirect:gallery.do?idx=" + idx;
		}
		
		// 작성된 글이 있을경우
		// 해당 idx로 가장 최근에 작성한 일촌평 찾기
		int maxNum = gallery_dao.selectMaxNum(idx);
		// 가져온 최근 게시글 번호에 1추가
		vo.setGalleryContentRef(maxNum + 1);
		// 게시글에 해당 사용자의 idx부여
		vo.setGallIdx(idx);
		// 게시글의 파일 이름 부여
		vo.setGalleryFileName(galleryFileName);
		// 확장자 구하기
		String extension = vo.getGalleryFileName().substring( vo.getGalleryFileName().lastIndexOf( "." ) + 1 );
		System.out.println(vo.getGalleryFileExtension());
		if ( extension.equals("mp4")  ) {
			vo.setGalleryFileExtension("video");
		} else {
			vo.setGalleryFileExtension("image");
		}
		gallery_dao.insert(vo);
		
		return "redirect:gallery.do?idx=" + idx;
	}
	
	// 게시글 삭제
	@RequestMapping("/delete_gallery.do")
	@ResponseBody // Ajax로 요청된 메서드는 결과를 콜백메서드로 돌려주기 위해 반드시 @ResponseBody가 필요!!
	public String delete(Integer idx, int galleryContentRef) {
		// 해당 idx 사용자의 게시글 번호
		HashMap<String, Integer> galleryKey = new HashMap<String, Integer>();
		galleryKey.put("1", idx);
		galleryKey.put("2", galleryContentRef);
		
		int res = gallery_dao.delete(galleryKey);
		
		String result = "no";
		if (res == 1) {
			result = "yes";
		}
		
		// yes, no값을 가지고 콜백메서드(resultFn)로 돌아간다
		// 콜백으로 리턴되는 값은 영문으로 보내준다
		return result;
	}
	
	// 글 수정 폼으로 전환
	@RequestMapping("/modify_form.do")
	public String modify_form(Integer idx, int galleryContentRef, Model model) {
		// 해당 idx 사용자의 게시글 번호
		HashMap<String, Integer> galleryKey = new HashMap<String, Integer>();
		galleryKey.put("1", idx);
		galleryKey.put("2", galleryContentRef);
		GalleryVO vo = gallery_dao.selectOne(galleryKey);
		
		if (vo!=null) {
			model.addAttribute("vo", vo);
		}
		
		return Common.GP_PATH + "gallery_modify_form.jsp";
		
	}
	
	// 게시글 수정하기
	@RequestMapping("/modify_gallery.do")
	public String modify(GalleryVO vo) {
		// 절대 경로 생성
		String webPath = "/resources/upload/";
		String savePath = app.getRealPath(webPath);// 절대경로
		System.out.println("경로 : " + savePath);
		
		// 업로드를 위해 파라미터로 넘어온 사진의 정보
		
		MultipartFile galleryFile = vo.getGalleryFile();
		String galleryFileName = vo.getGalleryFileName();
		//업로드 된 파일이 존재할 때!!
		if(!galleryFile.isEmpty()) {
			// 먼저 가지고 있던 파일 삭제
			File delFile = new File(savePath, vo.getGalleryFileName());
			delFile.delete();
			
			//업로드가 된 실제 파일의 이름
			galleryFileName = galleryFile.getOriginalFilename();
			
			//이미지를 저장할 경로를 지정
			File saveFile = new File(savePath, galleryFileName);
			
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}else {
				
				//동일파일명 변경
				long time = System.currentTimeMillis();
				galleryFileName = String.format("%d_%s", time, galleryFileName);
				saveFile = new File(savePath, galleryFileName);
				
			}
			
			try {
				//업로드를 위한 파일을 실제로 등록해주는 메서드
				galleryFile.transferTo(saveFile);
				vo.setGalleryFileName(galleryFileName);
				// 확장자 구하기
				String extension = vo.getGalleryFileName().substring( vo.getGalleryFileName().lastIndexOf( "." ) + 1 );
				System.out.println(vo.getGalleryFileExtension());
				if ( extension.equals("mp4")  ) {
					vo.setGalleryFileExtension("video");
				} else {
					vo.setGalleryFileExtension("image");
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		
		int res = gallery_dao.update(vo);
		return "redirect:gallery.do?idx=" + vo.getGallIdx();
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//방명록 조회
	@RequestMapping("/guestbook.do")
	public String guestbook_list (Integer idx, Model model) {
		
		List<GuestBookVO> list = guestbook_dao.selectList(idx);
		model.addAttribute("list", list); //바인딩
		return Common.GBP_PATH + "guestbook_list.jsp"; //포워딩
	}
	
	// 방명록 작성페이지로 이동
	@RequestMapping("/guestbook_insert_form.do")
	public String guestbook_insert_form() {
		return Common.GBP_PATH + "guestbook_insert_form.jsp";
	}
	
	//새 글 작성
	@RequestMapping("/guestbook_insert.do")
	public String guestbook_insert(Integer idx, GuestBookVO vo) {
		// 해당 idx로 사진첩 조회
		int countNum = guestbook_dao.selectCountNum(idx);
		// 사진첩에 글이 한개도 없을경우
		if ( countNum == 0 ) {
			// 사진첩에 번호 부여
			vo.setGuestbookContentRef(1);
			// 게시글에 해당 사용자의 idx부여
			vo.setGuestIdx(idx);
			guestbook_dao.insert(vo);
			
			return "redirect:guestbook.do?idx=" + idx;
		}
		
		// 작성된 글이 있을경우
		// 해당 idx로 가장 최근에 작성한 일촌평 찾기
		int maxNum = guestbook_dao.selectMaxNum(idx);
		// 가져온 최근 게시글 번호에 1추가
		vo.setGuestbookContentRef(maxNum + 1);
		// 게시글에 해당 사용자의 idx부여
		vo.setGuestIdx(idx);
		guestbook_dao.insert(vo);
		
		return "redirect:guestbook.do?idx=" + idx;
	}
	
	//방명록 삭제
	@RequestMapping("/guestbook_delete.do")
	@ResponseBody // Ajax로 요청된 메서드는 결과를 콜백 메서드로 돌아가기 위해 반드시 필요한 어노테이션
	public String guestbook_delete(GuestBookVO vo) {
		
		int res = guestbook_dao.delete(vo);
		
		String result = "no";
		if (res == 1) {
			result = "yes";
		}
		
		// yes, no값을 가지고 콜백메서드(resultFn)로 돌아간다
		// 콜백으로 리턴되는 값은 영문으로 보내준다
		return result;
	}
	
	// 글 수정 폼으로 전환
	@RequestMapping("/guestbook_modify_form.do")
	public String guestbook_modify_form(GuestBookVO vo, Model model) {
		// modify_form.do?idx=2&pwd=1111&c_pwd=1111
		GuestBookVO updateVo = guestbook_dao.selectOne(vo);
		
		if ( updateVo != null ) {
			model.addAttribute("vo", updateVo);
		}
		
		return Common.GBP_PATH + "guestbook_modify_form.jsp";
	}
    
	// 게시글 수정하기
	@RequestMapping("/guestbook_modify.do")
	@ResponseBody
	public String guestbook_modify(GuestBookVO vo) {
		
		int res = guestbook_dao.update(vo);
		
		String result = "{'result':'no'}";
		if (res != 0) {
			result = "{'result':'yes'}";
		}
		
		return result;
	}
}
