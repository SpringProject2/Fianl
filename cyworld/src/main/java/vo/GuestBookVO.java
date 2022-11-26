package vo;

public class GuestBookVO {
	
	
	private int guestbookContentRef, guestIdx, guestbookLikeNum;
	private String guestbookContent, guestbookRegdate, guestbookContentName;
	
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
}