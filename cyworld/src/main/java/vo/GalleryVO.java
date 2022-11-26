package vo;

import org.springframework.web.multipart.MultipartFile;

public class GalleryVO {
	private int galleryCommentStep, galleryCommentRef, galleryFolderDepth, galleryFolderStep, galleryFolderRef;
	private String galleryTopCategory, galleryMiddleCategory, galleryCommentRegdate, galleryCommentContent, galleryCommentName;
	////////// 게시물 영역 //////////
	private int gallIdx, galleryContentRef, galleryLikeNum; // 해당 유저의 게시판 idx, 게시물 번호, 게시물 좋아요 수, 좋아요 테이블
	private String galleryContent, galleryRegdate, galleryFileName, galleryFileExtension; // 게시물 내용, 게시물 작성 날짜, 파일 이름, 파일 확장자
	// 파일을 받기위한 클래스
	private MultipartFile galleryFile; // 물리적 파일
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
	
	public int getGallIdx() {
		return gallIdx;
	}

	public void setGallIdx(int gallIdx) {
		this.gallIdx = gallIdx;
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

	public String getGalleryCommentName() {
		return galleryCommentName;
	}

	public void setGalleryCommentName(String galleryCommentName) {
		this.galleryCommentName = galleryCommentName;
	}

	public String getGalleryFileName() {
		return galleryFileName;
	}

	public int getGalleryCommentStep() {
		return galleryCommentStep;
	}

	public void setGalleryCommentStep(int galleryCommentStep) {
		this.galleryCommentStep = galleryCommentStep;
	}

	public int getGalleryCommentRef() {
		return galleryCommentRef;
	}

	public void setGalleryCommentRef(int galleryCommentRef) {
		this.galleryCommentRef = galleryCommentRef;
	}

	public int getGalleryContentRef() {
		return galleryContentRef;
	}

	public void setGalleryContentRef(int galleryContentRef) {
		this.galleryContentRef = galleryContentRef;
	}

	public int getGalleryFolderDepth() {
		return galleryFolderDepth;
	}

	public void setGalleryFolderDepth(int galleryFolderDepth) {
		this.galleryFolderDepth = galleryFolderDepth;
	}

	public int getGalleryFolderStep() {
		return galleryFolderStep;
	}

	public void setGalleryFolderStep(int galleryFolderStep) {
		this.galleryFolderStep = galleryFolderStep;
	}

	public int getGalleryFolderRef() {
		return galleryFolderRef;
	}

	public void setGalleryFolderRef(int galleryFolderRef) {
		this.galleryFolderRef = galleryFolderRef;
	}
	
	public String getGalleryTopCategory() {
		return galleryTopCategory;
	}

	public void setGalleryTopCategory(String galleryTopCategory) {
		this.galleryTopCategory = galleryTopCategory;
	}

	public String getGalleryMiddleCategory() {
		return galleryMiddleCategory;
	}

	public void setGalleryMiddleCategory(String galleryMiddleCategory) {
		this.galleryMiddleCategory = galleryMiddleCategory;
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

	public String getGalleryCommentRegdate() {
		return galleryCommentRegdate;
	}

	public void setGalleryCommentRegdate(String galleryCommentRegdate) {
		this.galleryCommentRegdate = galleryCommentRegdate;
	}

	public String getGalleryCommentContent() {
		return galleryCommentContent;
	}

	public void setGalleryCommentContent(String galleryCommentContent) {
		this.galleryCommentContent = galleryCommentContent;
	}
}