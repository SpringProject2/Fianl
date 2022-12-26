package vo;

import org.springframework.web.multipart.MultipartFile;

public class GalleryVO {
	/////////////// 게시물 영역 ///////////////
	private int galleryIdx, galleryContentRef, galleryLikeNum; // 해당 유저의 게시판 idx, 게시물 번호, 게시물 좋아요 수, 좋아요 테이블
	private String galleryContent, galleryRegdate, galleryFileName, galleryFileExtension, galleryTitle; // 게시물 내용, 게시물 작성 날짜, 파일 이름, 파일 확장자, 파일 제목
	// 파일을 받기위한 클래스
	private MultipartFile galleryFile; // 물리적 파일
	// 댓글 구역
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	// getter/setter
	public MultipartFile getGalleryFile() {
		return galleryFile;
	}
	
	public void setGalleryFile(MultipartFile galleryFile) {
		this.galleryFile = galleryFile;
	}
	
	public int getGalleryLikeNum() {
		return galleryLikeNum;
	}

	public void setGalleryLikeNum(int galleryLikeNum) {
		this.galleryLikeNum = galleryLikeNum;
	}
	
	public int getGalleryIdx() {
		return galleryIdx;
	}

	public void setGalleryIdx(int galleryIdx) {
		this.galleryIdx = galleryIdx;
	}

	public void setGalleryFileName(String galleryFileName) {
		this.galleryFileName = galleryFileName;
	}
	
	public String getGalleryFileExtension() {
		return galleryFileExtension;
	}

	public void setGalleryFileExtension(String galleryFileExtension) {
		this.galleryFileExtension = galleryFileExtension;
	}
	
	public String getGalleryFileName() {
		return galleryFileName;
	}

	public int getGalleryContentRef() {
		return galleryContentRef;
	}

	public void setGalleryContentRef(int galleryContentRef) {
		this.galleryContentRef = galleryContentRef;
	}

	public String getGalleryContent() {
		return galleryContent;
	}

	public void setGalleryContent(String galleryContent) {
		this.galleryContent = galleryContent;
	}

	public String getGalleryRegdate() {
		return galleryRegdate;
	}

	public void setGalleryRegdate(String galleryRegdate) {
		this.galleryRegdate = galleryRegdate;
	}
	
	public String getGalleryTitle() {
		return galleryTitle;
	}

	public void setGalleryTitle(String galleryTitle) {
		this.galleryTitle = galleryTitle;
	}
}