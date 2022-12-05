package vo;

public class ViewsVO {
	private int ViewsIdx, ViewsSession;
	private String TodayDate;
	
	// getter / setter
	
	public int getViewsIdx() {
		return ViewsIdx;
	}
	public void setViewsIdx(int viewsIdx) {
		ViewsIdx = viewsIdx;
	}
	public int getViewsSession() {
		return ViewsSession;
	}
	public void setViewsSession(int viewsSession) {
		ViewsSession = viewsSession;
	}
	public String getTodayDate() {
		return TodayDate;
	}
	public void setTodayDate(String todayDate) {
		TodayDate = todayDate;
	}
}
