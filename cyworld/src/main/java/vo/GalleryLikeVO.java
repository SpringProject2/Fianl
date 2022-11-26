package vo;

public class GalleryLikeVO {
	//////////좋아요 영역 //////////
	// GalleryLike테이블
	private int galleryLikeIdx, galleryLikeRef, galleryLikeSession; // 좋아요를 받을 해당 유저의 게시판 idx, 게시물 번호, 로그인한 사용자
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public int getGalleryLikeIdx() {
		return galleryLikeIdx;
	}

	public void setGalleryLikeIdx(int galleryLikeIdx) {
		this.galleryLikeIdx = galleryLikeIdx;
	}

	public int getGalleryLikeRef() {
		return galleryLikeRef;
	}

	public void setGalleryLikeRef(int galleryLikeRef) {
		this.galleryLikeRef = galleryLikeRef;
	}

	public int getGalleryLikeSession() {
		return galleryLikeSession;
	}

	public void setGalleryLikeSession(int galleryLikeSession) {
		this.galleryLikeSession = galleryLikeSession;
	}
}