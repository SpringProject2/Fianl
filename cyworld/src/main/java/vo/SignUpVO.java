package vo;

import org.springframework.web.multipart.MultipartFile;

public class SignUpVO {
	private int idx, dotoryNum;
	private String name, userID, info, infoR, identityNum, gender, email, phoneNumber, address, addressDetail, platform, minimi;
	// 메인화면에 작성되는 요소들
	private String mainTitle, mainPhoto, mainText;
	// 파일을 받기위한 클래스
	private MultipartFile mainPhotoFile; // 물리적 파일
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// getter / setter
	public int getIdx() { // IDX
		return idx;
	}
	public void setIdx(int idx) { // IDX
		this.idx = idx;
	}
	public String getName() { // 이름
		return name;
	}
	public void setName(String name) { // 이름
		this.name = name;
	}
	public String getUserID() { // ID
		return userID;
	}
	public void setUserID(String userID) { // ID
		this.userID = userID;
	}
	public String getInfo() { // PW
		return info;
	}
	public void setInfo(String info) { // PW
		this.info = info;
	}
	public String getInfoR() { // PW확인
		return infoR;
	}
	public void setInfoR(String infoR) { // PW확인
		this.infoR = infoR;
	}
	public String getIdentityNum() { // 주민번호
		return identityNum;
	}
	public void setIdentityNum(String identityNum) { // 주민번호
		this.identityNum = identityNum;
	}
	public String getGender() { // 성별
		return gender;
	}
	public void setGender(String gender) { // 성별
		this.gender = gender;
	}
	public String getEmail() { // 이메일
		return email;
	}
	public void setEmail(String email) { // 이메일
		this.email = email;
	}
	public String getPhoneNumber() { // 휴대폰번호
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) { // 휴대폰번호
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() { // 주소
		return address;
	}
	public void setAddress(String address) { // 주소
		this.address = address;
	}
	public String getAddressDetail() { // 상세주소
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) { // 상세주소
		this.addressDetail = addressDetail;
	}
	public String getPlatform() { // 플랫폼
		return platform;
	}
	public void setPlatform(String platform) { // 플랫폼
		this.platform = platform;
	}
	public String getMinimi() { // 미니미
		return minimi;
	}
	public void setMinimi(String minimi) { // 미니미
		this.minimi = minimi;
	}
	public int getDotoryNum() { // 도토리 갯수
		return dotoryNum;
	}
	public void setDotoryNum(int dotoryNum) { // 도토리 갯수
		this.dotoryNum = dotoryNum;
	}
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	public String getMainPhoto() {
		return mainPhoto;
	}
	public void setMainPhoto(String mainPhoto) {
		this.mainPhoto = mainPhoto;
	}
	public String getMainText() {
		return mainText;
	}
	public void setMainText(String mainText) {
		this.mainText = mainText;
	}
	public MultipartFile getMainPhotoFile() {
		return mainPhotoFile;
	}
	public void setMainPhotoFile(MultipartFile mainPhotoFile) {
		this.mainPhotoFile = mainPhotoFile;
	}
}
