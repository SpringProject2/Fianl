package vo;

public class GuestBookVO {
	private int guestbookContentRef, guestIdx, guestbookSession;
	private String guestbookContent, guestbookRegdate, guestbookContentName;
	// 좋아요
	private int guestbookLikeNum;
	// 비밀글
	private int guestbookSecretCheck;
	// 미니미
	private String guestbookMinimi;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public int getGuestbookContentRef() {
		return guestbookContentRef;
	}
	public void setGuestbookContentRef(int guestbookContentRef) {
		this.guestbookContentRef = guestbookContentRef;
	}
	public int getGuestIdx() {
		return guestIdx;
	}
	public void setGuestIdx(int guestIdx) {
		this.guestIdx = guestIdx;
	}
	public String getGuestbookContent() {
		return guestbookContent;
	}
	public void setGuestbookContent(String guestbookContent) {
		this.guestbookContent = guestbookContent;
	}
	public String getGuestbookRegdate() {
		return guestbookRegdate;
	}
	public void setGuestbookRegdate(String guestbookRegdate) {
		this.guestbookRegdate = guestbookRegdate;
	}
	public String getGuestbookContentName() {
		return guestbookContentName;
	}
	public void setGuestbookContentName(String guestbookContentName) {
		this.guestbookContentName = guestbookContentName;
	}
	public int getGuestbookLikeNum() {
		return guestbookLikeNum;
	}
	public void setGuestbookLikeNum(int guestbookLikeNum) {
		this.guestbookLikeNum = guestbookLikeNum;
	}
	public int getGuestbookSecretCheck() {
		return guestbookSecretCheck;
	}
	public void setGuestbookSecretCheck(int guestbookSecretCheck) {
		this.guestbookSecretCheck = guestbookSecretCheck;
	}
	public int getGuestbookSession() {
		return guestbookSession;
	}
	public void setGuestbookSession(int guestbookSession) {
		this.guestbookSession = guestbookSession;
	}
	public String getGuestbookMinimi() {
		return guestbookMinimi;
	}
	public void setGuestbookMinimi(String guestbookMinimi) {
		this.guestbookMinimi = guestbookMinimi;
	}
}