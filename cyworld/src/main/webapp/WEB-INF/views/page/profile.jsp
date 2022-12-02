<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link rel="stylesheet" href="/cyworld/resources/css/yourProfile.css">
    <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
 <div class="container">
                <section class="left-section">
                        <div class="left-dashed-line">
                            <div class="left-gray-background">
                                <p class="todayBanner"><span>Today</span> <span class="todayHere">156</span><span>&nbsp;｜ Total</span> 45,405</p>
                                <aside class="left-aside">
                                    <div class="item item1"></div>
                                    <div class="item item1"></div>
                                    <div class="item item2"></div>
                                    <div class="item item2"></div>
                                    <div class="todayIcon">
                                        <span class="todayIconText">Today is..</span><img class="box animate__animated animate__headShake animate__infinite " src="resources/images/emoticon1.png" alt="">
                                    </div>
                                    <form method="POST" enctype="multipart/form-data"> <!-- form태그와 button -->
                                    	<input name="idx" type="hidden" value="${ signVo.idx }">
                                    	<div class="left-image"><img class="leftImg" src="/cyworld/resources/mainphoto/${ signVo.mainPhoto }" alt=""></div>
                                    							<input name="mainPhoto" type="hidden" value="${ signVo.mainPhoto }">
                                    							<input name="mainPhotoFile" type="file"> <!-- 사진 바꾸는곳 -->
                                    	<textarea name="mainText" class="left-textarea">${ signVo.mainText }</textarea>
                                    	<input type="button" value="메인 수정" onclick="modify_main(this.form)">
                                    </form>
                                    <div class="history"><img src="resources/images/arrow.png" alt=""><h3>History</h3></div>
                                    <select class="myFriend">
                                        <option value="">::: 파도타기 :::</option>
                                            <option><a href="#">이정현 ｜  친구</a></option>
                                            <option><a href="#">박성철 ｜  친구</a></option>
                                            <option><a href="#">장유진 ｜  친구</a></option>
                                            <option><a href="#">황유진 ｜  친구</a></option>
                                            <option><a href="#">장현중 ｜  친구</a></option>
                                    </select>
                                </aside>
                            </div>
                        </div>
                </section>
               
                <section class="right-section">
                        <div class="right-dashed-line">
                            <div class="right-gray-background">
								<p class="title"><input style="width: 500px; height: 20px;" id="mainTitle" type="text" value="${ signVo.mainTitle }"></p>
                                <!-- a태그 = 새로고침  -->
                                <!-- <p class="titleLink"><a href="#">http://www.zenghyun.com</a></p> -->
                                <aside id="right-aside"
                                class="scrollBar">
                                    <div class="miniRoomBox"><p>Mini Room</p>
                                   <div class="miniRoom"><img class="miniRoomImg" src="resources/images/mainroom.png" alt="">
                                    <div class="hover">
                                        <div class="show">
                                             <img src="resources/images/sorryForShow.gif" alt="">
                                        <p class="sorryText">
                                            아직 개발중에 있습니다. <span>※개발진 일동</span>
                                        </p>
                                        </div>
                                    </div>
                                    <div class="Crayon"><img src="resources/images/Crayon.png" alt=""></div></div>
                                </div>
                                <!-- 미니미 수정 form -->
                                <form action="">
                                <input class="check_btn" id="btn-cover" type="button" value="미니미수정" onclick="toggle();"></input>
                                
                                
                                <div id="minimi_correction"
                                class="scrollBar">
                                  <div class="minimi-list"
                                  >
                                    <div class="minimi-area">
                                        <input id="btn-cover" class="minimi-select"type="submit" value="수정">
                                        <input class="minimi-choice" type="radio">
                                        <img src="resources/images/Crayon2 .png" alt=""></div>
                                    </div>
                                  </div>
                                </form>

                                <!-- 개인정보 수정 form -->
                                <form action="">
                                <div class="modify-user-profile">
                                    <h2>::개인정보 수정::</h2>
                                    <p id="my-minimi">My minimi</p>
                                  <input class="minimi-main" type="button" src="/cyworld/resources/minimi/${ signVo.minimi }" onclick="minimiPopUp();">
									<input name="idx" type="hidden" value="${ signVo.idx }">
									<!-- 싸이월드 가입자만 보이는 추가 항목들 -->
									<c:if test="${ signVo.platform eq 'cyworld' }">
                                    	<p>ID : <input type="text" value="${ signVo.userID }" readonly></p>
                                    	<p>현재 PW : <input name="info" type="text" value="${ signVo.info }" readonly></p>
                                    	<p>새로운 PW : <input id="pw" name="info" type="password" oninput="pwCheck();"></p>
                                    	<div class="pwText" id="pT1"></div>
                                    	<p>PW 확인 : <input id="pw2" type="password" oninput="pw2Check();"></p>
                                    	<div class="pwText pT2"></div>
                                    </c:if>
                                    <!-- 소셜가입자가 보이는 항목들 -->
                                    <p>이름 : <input type="text" value="${ signVo.name }" readonly></p>
                                    <p>주민번호 : <input type="text" value="${ signVo.identityNum }" readonly></p>
                                    <c:if test="${ signVo.gender eq 'M' || signVo.gender eq 'male' }">
                                    	<p>성별:&nbsp; <input class="myRadio" type="radio" name="gender" value="${ signVo.gender }" checked readonly>&nbsp;남</p>
                                    </c:if>
                                    <c:if test="${ signVo.gender eq 'W' || signVo.gender eq 'female' }">
                                    	<p>성별:&nbsp; <input  class="myRadio" type="radio" name="gender" value="${ signVo.gender }" checked readonly>&nbsp;여</p>
                                    </c:if>
                                    <p>이메일 : <input type="text" value="${ signVo.email }" readonly></p>
                                    <p>전화번호 : <input type="tel" value="${ signVo.phoneNumber }" readonly></p>
                                    <input class="final-button" id="btn-cover" type="button" value="수정" onclick="modifyUserData(this.form);">
                                </div>
                            </form> 

                                </aside>
                            </div>
                            </div>

                        <div class="tabs">
							<form>
								<div class="tab-btns">
									<input id="idx" name="idx" type="hidden" value="${ signVo.idx }" readonly>
									<input id="sessionIdx" name="sessionIdx" type="hidden" value="${ sessionIdx }" readonly>
									<label for="tab1" id="btn1">홈</label>
									<input id="tab1" type="button" value="홈" style="display: none;" onclick="location.href='main.do?idx=${ signVo.idx }'">
									<label for="tab2" id="btn2">프로필</label>
									<input id="tab2" type="button" value="프로필" style="display: none;" onclick="profile(this.form);">
									<label for="tab3" id="btn3">다이어리</label>
									<input id="tab3" type="button" value="다이어리" style="display: none;" onclick="location.href='diary.do?idx=${ signVo.idx }'">
									<label for="tab4" id="btn4">사진첩</label>
									<input id="tab4" type="button" value="사진첩" style="display: none;" onclick="location.href='gallery.do?idx=${ signVo.idx }'">
									<label for="tab5" id="btn5">방명록</label>
									<input id="tab5" type="button" value="방명록" style="display: none;" onclick="location.href='guestbook.do?idx=${ signVo.idx }'">
								</div>
							</form>
                        </div>
                </section>
    </div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<script>
		function profile(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			if ( sessionIdx != idx ) {
				alert("프로필은 본인만 들어갈 수 있습니다");
				return;
			}
			f.action = "profile.do";
			f.method = "POST";
			f.submit();
		}
	</script>
    <!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		// 메인 타이틀 및 비밀번호 수정
		function modifyUserData(f) {
			// idx
			let idx = f.idx.value;
			// 메인 타이틀
			let mainTitle = document.getElementById("mainTitle").value;
			// 비밀번호 패턴 체크
			let info = f.info;
			let pattern1 = /[0-9]/; // 숫자 입력
			let pattern2 = /[a-zA-Z]/; // 영어 소문자, 대문자 입력
			let pattern3 = /[~!@#$%^&*()_+]/; // 특수기호 입력
			let infoR = document.getElementById("pw2").value;
			
			// 유효성 검사
			
			// 메인 타이틀이 공백일시
			if ( mainTitle =='' ) {
				alert("메인 타이틀을 작성해주세요");
				return;
			}
			
			// 새로운 비밀번호에 하나라도 입력시
			if ( info[1].value != '' ) {
				// 비밀번호 패턴 체크
				if ( !pattern1.test(info[1].value) || !pattern2.test(info[1].value) || !pattern3.test(info[1].value) || info[1].value.length < 8 ) {
					alert("비밀번호는 영문 + 숫자 + 특수기호 8자리 이상으로 입력하세요");
					return;
				}
				// 비밀번호와 비밀번호 확인 일치 체크
				if ( info[1].value != infoR ) {
					alert("비밀번호 확인이 비밀번호와 일치하지 않습니다");
					return;
				}
				
				url = "profile_modify_userdata.do"
				param = "idx=" + idx +
						"&info=" + info[1].value +
						"&mainTitle=" + mainTitle;
				sendRequest(url, param, resultModify, "GET");
				
			// 비밀번호를 변경하지 않고 그대로 가져갈시
			} else {
				
				url = "profile_modify_userdata.do"
				param = "idx=" + idx +
						"&info=" + info[0].value +
						"&mainTitle=" + mainTitle;
				sendRequest(url, param, resultModify, "GET");
			}
		}
		// 콜백메소드
		function resultModify() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				// "{'result':'no'}"
				let data = xhr.responseText;
				
				if ( data == "no" ) {
					alert("작성 실패");
					return;
				}
				
				alert("수정 완료");
				location.href = "profile.do?idx=${signVo.idx}";
			}
		}
	</script>
	
	<script>
		// 메인 사진 소개글 수정
		function modify_main(f) {
			let mainText = f.mainText.value;
			
			// 유효성 검사
			
			// 메인 소개글이 공백일시
			if ( mainText =='' ) {
				alert("메인 소개글을 작성하세요");
				return;
			}
			
			f.action = "profile_modify_main.do";
			f.submit();
		}
	</script>
	
	<script>
        //window.open (미니미 수정창)
        function minimiPopUp() {
            let popUrl = "profile_minimi_popup.do?idx=${signVo.idx}";
            let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
    	window.open(popUrl, "minimi", popOption);
        }
	</script>
	
	<script>
		// 비밀번호가 패턴에 맞는지 확인용
		function pwCheck() {
			let pwText = document.getElementsByClassName("pwText"); // 비밀번호 아래에 글이 작성될 <div>
			let pw = document.getElementById("pw").value; // 비밀번호 값
			
			let pattern1 = /[0-9]/; // 숫자 입력
			let pattern2 = /[a-zA-Z]/; // 영어 소문자, 대문자 입력
			let pattern3 = /[~!@#$%^&*()_+]/; // 특수기호 입력
			
			// 비밀번호가 패턴에 하나라도 맞지 않을때
			//		숫자 입력 안할시	 or	   영어 입력 안할시	    or   특수기호 입력 안할시	   or  8자리 보다 작을시
			if ( !pattern1.test(pw) || !pattern2.test(pw) || !pattern3.test(pw) || pw.length < 8 ) {
				// 비밀번호 입력창에 입력하자마자 바로 아래에 글 작성
				pwText[0].innerHTML = "영문 + 숫자 + 특수기호 8자리 이상으로 구성하여야 합니다";
			} else {
				// 비밀번호 입력창에 입력하자마자 바로 아래에 글 작성
				pwText[0].innerHTML = "";
			}
		}
		
		// 비밀번호와 비밀번호 확인이 일지한지 확인용
		function pw2Check() {
			let pwText = document.getElementsByClassName("pwText"); // 비밀번호 아래에 글이 작성될 <div>
			let pw = document.getElementById("pw").value; // 비밀번호 값
			let pw2 = document.getElementById("pw2").value; // 비밀번호 확인 값
			
			// 비밀번호와 비밀번호 확인이 서로 맞지 않을때
			if ( pw != pw2 ) {
				// 비밀번호 확인창에 입력하자마자 바로 아래에 글 작성
				pwText[1].innerHTML = "비밀번호가 일치하지 않습니다";
			} else {
				// 비밀번호 확인창에 입력하자마자 바로 아래에 글 작성
				pwText[1].innerHTML = "";
			}
		}
	</script>
	
	<script>
		// toggle
		function toggle(){
			const minimi_correction = document.getElementById('minimi_correction');
			
			if(minimi_correction.style.display !== 'none'){
				minimi_correction.style.display ='none';
			}
			else {
				minimi_correction.style.display ='block';
			}
		}
	</script>
</body>
</html>