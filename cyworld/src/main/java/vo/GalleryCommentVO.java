package vo;

public class GalleryCommentVO {
	   private int galleryCommentRef, galleryCommentIdx, galleryNum, galleryCommentDeleteCheck, galleryCommentSession;
	   private String galleryCommentRegdate, galleryCommentContent, galleryCommentName;

	   /////getter/setter/////

	   public int getGalleryCommentDeleteCheck() {
	      return galleryCommentDeleteCheck;
	   }

	   public void setGalleryCommentDeleteCheck(int galleryCommentDeleteCheck) {
	      this.galleryCommentDeleteCheck = galleryCommentDeleteCheck;
	   }

	   public int getGalleryCommentSession() {
	      return galleryCommentSession;
	   }

	   public void setGalleryCommentSession(int galleryCommentSession) {
	      this.galleryCommentSession = galleryCommentSession;
	   }

	   public String getGalleryCommentName() {
		      return galleryCommentName;
		   }
	   
	   public void setGalleryCommentName(String galleryCommnetName) {
	      this.galleryCommentName = galleryCommnetName;
	   }

	   public int getGalleryCommentIdx() {
	      return galleryCommentIdx;
	   }
	   
	   public void setGalleryCommentIdx(int galleryCommentIdx) {
	      this.galleryCommentIdx = galleryCommentIdx;
	   }

	   public int getGalleryCommentRef() {
	      return galleryCommentRef;
	   }

	   public void setGalleryCommentRef(int galleryCommentRef) {
	      this.galleryCommentRef = galleryCommentRef;
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

	   public int getGalleryNum() {
	      return galleryNum;
	   }

	   public void setGalleryNum(int galleryNum) {
	      this.galleryNum = galleryNum;
	   }
}