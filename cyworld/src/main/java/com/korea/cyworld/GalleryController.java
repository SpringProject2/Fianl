package com.korea.cyworld;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import dao.DiaryDAO;
import dao.GalleryDAO;
import dao.GuestBookDAO;
import dao.MainDAO;
import dao.SignUpDAO;
import util.Common;
import vo.GalleryVO;
import vo.GalleryCommentVO;
import vo.GalleryLikeVO;
import vo.SignUpVO;

@Controller
public class GalleryController {
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
	// 사진첩 조회
	@RequestMapping("/gallery.do")
	public String string(Integer idx, Model model) {
		// 사진첩에 들어오면 가장 먼저 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 그 다음 idx에 해당하는 사진첩의 모든 게시물 조회
		List<GalleryVO> galleryList = gallery_dao.selectList(idx);
		// 조회된 모든 게시물을 리스트 형태로 바인딩
		model.addAttribute("galleryList", galleryList);
		
		// 그 다음 idx에 해당하는 사진첩의 모든 댓글 조회
		List<GalleryCommentVO> commentList = gallery_dao.selectCommentList(idx);
		// 조회된 모든 댓글을 리스트 형태로 바인딩
		model.addAttribute("commentList", commentList);
		
		// 그 다음 idx에 해당하는 유저정보를 조회
		SignUpVO svo = signUp_dao.selectOneIdx(idx);
		// 조회된 유저 정보를 바인딩
		model.addAttribute("signVo", svo);
		// 추가로 세션값도 바인딩
		model.addAttribute("sessionIdx", session.getAttribute("login"));
		
		// 그 다음 사진첩에 댓글 작성자를 만들기 위해 세션값에 해당하는 유저 정보를 조회
		SignUpVO sessionUser = signUp_dao.selectOneIdx(session.getAttribute("login"));
		
		// 조회한 유저정보에서 댓글 작성자를 만들어 담을 String변수
		String galleryCommentName = "";
		
		if ( sessionUser.getPlatform().equals("cyworld") ) {
			// 플랫폼이 cyworld일 경우 - ID + @ + cyworld = qwer@cyworld
			// galleryCommentName = (sessionUser.getUserID() + "@" + sessionUser.getPlatform());
			
			// 플랫폼이 cyworld일 경우 - ( + 이름 + / + ID + ) = ( 관리자 / qwer ) - 변경
			galleryCommentName = ( "( " + sessionUser.getName() + " / " + sessionUser.getUserID() + " )" );
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
			galleryCommentName = ( "( " + sessionUser.getName() + " / " + sessionUser.getEmail().substring( 0, sessionUser.getEmail().indexOf("@") ) + " )" );
		}
		// 만들어진 댓글 작성자를 바인딩
		model.addAttribute("sessionName", galleryCommentName);
		
		// 사진첩 페이지로 이동
		return Common.GP_PATH + "gallery_list.jsp";
	}
	
	// 사진첩 글 작성 페이지로 이동
	@RequestMapping("/gallery_insert_form.do")
	public String gallery_insert_form() {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 세션값이 있다면 작성 페이지로 이동
		return Common.GP_PATH + "gallery_insert_form.jsp";
	}
	
	// 사진첩 새 글 작성
	@RequestMapping("/gallery_insert.do")
	public String insert(Integer idx, GalleryVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 클라이언트의 파일 업로드를 위해 상대 경로를 통한 절대 경로를 생성
		String webPath = "/resources/upload/"; // 상대 경로
		String savePath = app.getRealPath(webPath); // 절대 경로
		
		// 업로드를 위해 파라미터로 넘어온 파일의 정보
		MultipartFile galleryFile = vo.getGalleryFile();
		
		// 업로드된 파일이 없을 경우 파일 이름 지정
		String galleryFileName = "no_file";
		
		// 업로드된 파일이 있을 경우
		if ( !galleryFile.isEmpty() ) {
			// 업로드된 파일의 원본 이름을 지정
			galleryFileName = galleryFile.getOriginalFilename();
			// 파일을 저장할 경로를 지정
			File saveFile = new File(savePath, galleryFileName);
			// 저장할 경로가 없을 경우
			if(!saveFile.exists()) {
				// 경로를 생성
				saveFile.mkdirs();
			// 저장할 경로가 있을 경우
			}else {
				// 파일명 중복 방지를 위해 파일명 앞에 시간 추가
				long time = System.currentTimeMillis();
				galleryFileName = String.format("%d_%s", time, galleryFileName);
				saveFile = new File(savePath, galleryFileName);
			}
			// 업로드된 파일을 실제로 저장 - try~catch 필요
			try {
				// 업로드된 파일을 실제로 저장해 주는 메소드
				galleryFile.transferTo(saveFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* 확장자 구하기
		 * 파일 원본 이름에서 마지막 .이 들어간 위치에서 한 칸 더한 위치부터 끝까지 잘라내기
		 * ex) 19292930388시바견.img --> .img --> img
		 */
		String extension = galleryFileName.substring( galleryFileName.lastIndexOf( "." ) + 1 );
		// 파일 종류가 비디오일 경우
		if ( extension.equals("mp4") ) {
			// 확장자에 비디오 지정
			vo.setGalleryFileExtension("video");
		// 파일 종류가 이미지일 경우
		} else if ( extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") ) {
			// 확장자에 이미지 지정
			vo.setGalleryFileExtension("image");
		// 업로드된 파일이 없는 경우
		} else if ( galleryFileName.equals("no_file") ) {
			// 확장자에 파일 없음 지정
			vo.setGalleryFileExtension("no_file");
		// 업로드된 파일의 종류가 사용할 수 없는 경우
		} else {
			// 확장자에 파일 없음 지정
			vo.setGalleryFileExtension("no_file");
		}
		
		// 해당 idx의 사진첩에 작성된 글 개수 조회
		int countNum = gallery_dao.selectCountNum(idx);
		// 작성된 게시글이 한 개도 없을 경우
		if ( countNum == 0 ) {
			// 게시글에 시작 번호 1 지정
			vo.setGalleryContentRef(1);
			// 사진첩의 idx 지정
			vo.setGallIdx(idx);
			// 파일 이름 지정
			vo.setGalleryFileName(galleryFileName);
			// 게시글에 좋아요 시작 개수 0 지정
			vo.setGalleryLikeNum(0);
			// 작성한 게시글을 저장
			gallery_dao.insert(vo);
			// idx를 들고 사진첩 페이지 URL로 이동
			return "redirect:gallery.do?idx=" + idx;
		// 작성된 글이 있을 경우
		} else {
			// 가장 최근에 작성한 게시글의 번호에 1 더하기
			vo.setGalleryContentRef(countNum + 1);
			// 사진첩의 idx 지정
			vo.setGallIdx(idx);
			// 파일 이름 지정
			vo.setGalleryFileName(galleryFileName);
			// 게시글에 좋아요 시작 개수 0 지정
			vo.setGalleryLikeNum(0);
			// 작성한 게시글을 저장
			gallery_dao.insert(vo);
			// idx를 들고 사진첩 페이지 URL로 이동
			return "redirect:gallery.do?idx=" + idx;
		}
	}
	
	// 사진첩 글 삭제
	@RequestMapping("/gallery_delete.do")
	@ResponseBody // Ajax로 요청된 메서드는 결과를 콜백 메서드로 돌려주기 위해 반드시 @ResponseBody가 필요!!
	public String delete(GalleryVO vo, GalleryLikeVO lvo, GalleryCommentVO cvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 삭제할 사진첩의 게시글에 idx와 ref를 게시글 삭제 후에도 계속 사용하기 위해 VO에서 따로 분리해서 변수에 각각 저장한다
		int idx = vo.getGallIdx(); // 삭제할 사진첩의 idx
		int ref = vo.getGalleryContentRef(); // 삭제할 사진첩의 게시글에 ref
		
		// 그리고 idx와 ref를 사용하기 편하게 Map으로 만들어 놓는다
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("1", idx); // 삭제할 사진첩의 idx 저장
		map.put("2", ref); // 삭제할 사진첩의 게시글 번호 저장
		
		// DB에 저장된 게시글 중 가져온 정보에 해당하는 게시글 삭제
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
			
			// 게시글 삭제시 삭제된 게시글에 해당하는 댓글만 모두 삭제
			gallery_dao.deleteCommentAll(map);
			
			// 게시글 삭제시 삭제된 게시글에 해당하는 좋아요만 모두 삭제
			gallery_dao.deleteLikeAll(map);
		}
		// 콜백 메소드에 전달
		return result;
	}
	
	// 사진첩 게시글 수정 페이지로 이동
	@RequestMapping("/gallery_modify_form.do")
	public String modify_form(Integer idx, GalleryVO vo, Model model) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 해당 idx의 사진첩에 수정할 게시글을 조회
		GalleryVO refVo = gallery_dao.selectOneRef(vo);
		if ( vo != null ) {
			// 조회된 게시글을 바인딩
			model.addAttribute("vo", refVo);
		}
		// 수정 페이지로 이동
		return Common.GP_PATH + "gallery_modify_form.jsp";
	}
	
	// 게시글 수정하기
	@RequestMapping("/gallery_modify.do")
	public String modify(GalleryVO vo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 클라이언트의 파일 업로드를 위해 상대 경로를 통한 절대 경로를 생성
		String webPath = "/resources/upload/"; // 상대 경로
		String savePath = app.getRealPath(webPath); // 절대 경로
		
		// 업로드를 위해 파라미터로 넘어온 파일의 정보
		MultipartFile galleryFile = vo.getGalleryFile();
		
		// 업로드된 파일이 없을 경우 이미 저장되어 있는 파일 이름 지정
		String galleryFileName = vo.getGalleryFileName();
		
		// 업로드된 파일이 있을 경우
		if ( !galleryFile.isEmpty() ) {
			// 이미 저장되어 있는 파일 삭제
			File delFile = new File(savePath, vo.getGalleryFileName());
			delFile.delete();
			// 업로드된 파일의 원본 이름을 지정
			galleryFileName = galleryFile.getOriginalFilename();
			// 파일을 저장할 경로를 지정
			File saveFile = new File(savePath, galleryFileName);
			// 저장할 경로가 없을 경우
			if(!saveFile.exists()) {
				// 경로를 생성
				saveFile.mkdirs();
			// 저장할 경로가 있을 경우
			}else {
				// 파일명 중복 방지를 위해 파일명 앞에 시간 추가
				long time = System.currentTimeMillis();
				galleryFileName = String.format("%d_%s", time, galleryFileName);
				saveFile = new File(savePath, galleryFileName);
			}
			// 업로드된 파일을 실제로 등록 - try~catch 필요
			try {
				// 업로드된 파일을 실제로 등록해 주는 메소드
				galleryFile.transferTo(saveFile);
				// 파일 이름 지정
				vo.setGalleryFileName(galleryFileName);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* 확장자 구하기
		 * 파일 원본 이름에서 마지막 .이 들어간 위치에서 한 칸 더한 위치부터 끝까지 잘라내기
		 * ex) 19292930388시바견.img --> .img --> img
		 */
		String extension = galleryFileName.substring( galleryFileName.lastIndexOf( "." ) + 1 );
		// 파일 종류가 비디오일 경우
		if ( extension.equals("mp4") ) {
			// 확장자에 비디오 지정
			vo.setGalleryFileExtension("video");
		// 파일 종류가 이미지일 경우
		} else if ( extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") ) {
			// 확장자에 이미지 지정
			vo.setGalleryFileExtension("image");
		// 업로드된 파일이 없는 경우
		} else if ( galleryFileName.equals("no_file") ) {
			// 확장자에 파일 없음 지정
			vo.setGalleryFileExtension("no_file");
		// 업로드된 파일의 종류가 사용할 수 없는 경우
		} else {
			// 확장자에 파일 없음 지정
			vo.setGalleryFileExtension("no_file");
		}
		
		// 수정된 파일 및 게시글로 갱신
		gallery_dao.update(vo);
		// idx를 들고 사진첩 페이지 URL로 이동
		return "redirect:gallery.do?idx=" + vo.getGallIdx();
	}
	
	/////////////// 사진첩 댓글 구역 ///////////////
	
	// 댓글 작성
	@RequestMapping("/comment_insert.do")
	@ResponseBody
	public String gallery_reply(GalleryCommentVO cvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
			return "redirect:login.do";
		}
		
		// 세션값을 사용하기 위해 Integer타입으로 형변환
		Integer sessionIdx = (Integer)session.getAttribute("login");
		
		// 해당 idx의 사진첩에 작성된 해당 게시물에 달린 댓글 개수 조회
		int countNum = gallery_dao.selectCommentNum(cvo);
		
		// 사진첩 게시글에 댓글이 한 개도 없을 경우
		if ( countNum == 0 ) {
			// 댓글에 시작 번호 1 지정
			cvo.setGalleryNum(1);
			// 댓글의 삭제 여부 0 지정
			cvo.setGalleryCommentDeleteCheck(0);
			// 댓글의 작성자 idx 지정
			cvo.setGalleryCommentSession(sessionIdx);
			// 작성한 댓글을 저장
			int res = gallery_dao.insertComment(cvo);
			// 저장 실패할 경우
			String result = "no";
			if ( res == 1 ) {
				// 저장 성공할 경우
				result = "yes";
			}
			// 콜백 메소드에 전달
			return result;
		// 작성된 댓글이 있을경우
		} else {
			// 가장 최근에 작성한 댓글의 번호에 1 더하기
			cvo.setGalleryNum(countNum + 1);
			// 댓글의 삭제 여부 0 지정
			cvo.setGalleryCommentDeleteCheck(0);
			// 댓글의 작성자 idx 지정
			cvo.setGalleryCommentSession(sessionIdx);
			// 작성한 댓글을 저장
			int res = gallery_dao.insertComment(cvo);
			// 저장 실패할 경우
			String result = "no";
			if ( res == 1 ) {
				// 저장 성공할 경우
				result = "yes";
			}
			// 콜백 메소드에 전달
			return result;
		}
	}
	
	// 댓글 삭제 - 완전 삭제가 아닌 삭제된 것처럼 만들기
	@RequestMapping("/gcomment_delete.do")
	@ResponseBody
	public String gcomment_delete(GalleryCommentVO cvo) {
		// 댓글의 삭제 여부 -1 지정
		cvo.setGalleryCommentDeleteCheck(-1);
		// 삭제할 댓글의 삭제 여부만 갱신
		int res = gallery_dao.updateDeleteComment(cvo);
		// 삭제 실패할 경우
		String result = "no";
		if (res == 1) {
			// 삭제 성공할 경우
			result = "yes";
		}
		// 콜백 메소드에 전달
		return result;
	}
	
	/////////////// 사진첩 좋아요 구역 ///////////////
	
	@RequestMapping("/gallery_like.do")
	@ResponseBody
	public String gallery_like(GalleryVO vo, GalleryLikeVO lvo) {
		// 세션값이 있는지 확인
		HttpSession session = request.getSession();
		if ( session.getAttribute("login") == null ) {
			// 세션값이 없다면 로그인 페이지로 이동
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
			// 좋아요 개수 조회
			int likeCount = gallery_dao.selectLikeCountNum(lvo);
			// 조회된 좋아요 개수를 지정
			vo.setGalleryLikeNum(likeCount);
			// 조회된 좋아요 개수로 갱신
			gallery_dao.updateLikeNum(vo);
			// 좋아요 개수를 콜백 메소드에 전달하기 위해 String타입으로 형변환
			String likeNum = Integer.toString(likeCount);
			// 콜백 메소드에 전달
			return likeNum;
		// 좋아요를 안 눌렀을 경우
		} else {
			// 좋아요를 누를 경우 좋아요 내역을 추가
			gallery_dao.insertLike(lvo);
			// 좋아요 개수 조회
			int likeCount = gallery_dao.selectLikeCountNum(lvo);
			// 조회된 좋아요 개수를 지정
			vo.setGalleryLikeNum(likeCount);
			// 조회된 좋아요 개수로 갱신
			gallery_dao.updateLikeNum(vo);
			// 좋아요 개수를 콜백 메소드에 전달하기 위해 String타입으로 형변환
			String likeNum = Integer.toString(likeCount);
			// 콜백 메소드에 전달
			return likeNum;
		}
	}
}