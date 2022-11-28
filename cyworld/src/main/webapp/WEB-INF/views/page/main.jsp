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
<link rel="stylesheet" href="/cyworld/resources/css/main.css">
</head>
<body>
	Idx : <input id="idx" name="idx" type="text" value="${ vo.idx }">
	SessionIdx : <input id="sessionIdx" name="sessionIdx" type="text" value="${ sessionUser.idx }">
	<input type="button" value="사진첩" onclick="location.href='gallery.do?idx=${vo.idx}'">
	<input type="button" value="방명록" onclick="location.href='guestbook.do?idx=${vo.idx}'">
	<input type="button" value="프로필" onclick="location.href='profile.do?idx=${vo.idx}'">
	<h2 class="leftName">${ vo.name }님의 미니홈피입니다!</h2>
	
	<!-- 플랫폼에 따른 로그아웃 버튼 생성 -->
	<c:if test="${ sessionUser.platform eq 'cyworld' }">
		<input id="btn_cover" class="cy_logout" type="button" value="로그아웃" onclick="location.href='logout.do'">
	</c:if>
	<c:if test="${ sessionUser.platform eq 'naver' }">
		<input id="btn_cover" class="na_logout" type="button" value="네이버 로그아웃" onclick="naverLogout();">
	</c:if>
	<c:if test="${ sessionUser.platform eq 'kakao' }">
		<input id="btn_cover" class="ka_logout" type="button" value="카카오 로그아웃" onclick="kakaoLogout();">
	</c:if>
	
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
						<div class="left-image"><img class="leftImg" src="resources/images/left_profile.png" alt=""></div>
						<textarea class="left-textarea">어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구</textarea>
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
					<p class="title"><a href="#">Test 싸이월드 Title입니다. 누르면 무슨 기능이였더라?</a></p>
					<!-- a태그 = 새로고침  -->
					<!-- <p class="titleLink"><a href="#">http://www.zenghyun.com</a></p> -->
					<!-- 회원 검색 -->
					<!-- 아이디, 이메일 검색 -->
					<form method="post">
						<select class="searchList" name="searchType">
							<option value="name">이름</option>
							<option value="id">아이디</option>
							<option value="email">이메일</option>
						</select>
						<input  class="searchtext" type="text" name="searchValue"></input>
						<input class="search" type="submit" value="검색" onclick="search(this.form);"></input>
						<input type="hidden" name="idx" value="${vo.idx }">	
					</form>
					<aside class="right-aside">
						<div class="miniRoomBox"><p>Mini Room</p>
							<div class="miniRoom"><img src="resources/images/mainroom.png" alt=""></div>
							<div class=" Crayon box animate__animated animate__bounce animate__infinite"><img src="resources/images/Crayon.png" alt=""></div>
						</div>
						<form method="post">
							<div class="Ilchonpyeong">
							
								<span>일촌평</span> <input type="text" name="ilchonpyeongText" placeholder="일촌과 나누고 싶은 이야기를 남겨보세요"></input>
								<input class="Ic-registration" type="button" value="확인" onclick="registration(this.form);"></input>
								
							</div>
						</form>
						<c:forEach var="vo" items="${ list }">
							<div class="Ilchon" >ㆍ ${ vo.ilchonpyeongText } ${ vo.ilchonSession }</div>
						</c:forEach>
					</aside>
				</div>
			</div>
			<div class="tabs">
				<input type="checkbox" id="tab1" checked ></input>
				<input type="checkbox" id="tab2"></input>
				<input type="checkbox" id="tab3"></input>
				<input type="checkbox" id="tab4"></input>
				<input type="checkbox" id="tab5"></input>
				<div class="tab-btns">
					<label for="tab1" id="btn1">홈</label>
					<label for="tab2" id="btn2">프로필</label>
					<label for="tab3" id="btn3">다이어리</label>
					<label for="tab4" id="btn4">사진첩</label>
					<label for="tab5" id="btn5">방명록</label>
				</div>
			</div>
		</section>
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		// 유저 검색
		function search(f){
			let idx = f.idx.value;
			let searchType = f.searchType.value;
			let searchValue = f.searchValue.value;
			
			url = "main_search.do";
			param = "idx=" + idx +
					"&searchType=" + searchType +
					"&searchValue=" + searchValue;
			alert(searchValue);
			sendRequest(url, param, resultSearch, "POST");
		}
		// 콜백메소드
		function resultSearch() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				alert("성공");
				let data = xhr.responseText;
				
				let searchVal = data.searchList;
				let result = data.code;
				alert(result);
         		
				if ( data == "no" ) {
					alert("검색 실패");
					return;
				}
         
				alert("검색 완료");
				location.href = "search.do?idx=${vo.idx}";
			} 
		}
		
		// 일촌평 작성
		function registration(f){
		
			let ilchonpyeongText = f.ilchonpyeongText.value;
			let idx = document.getElementById("idx").value;
			let sessionIdx = document.getElementById("sessionIdx").value;
			
			// 세션이 0이면 비회원이므로 일촌평을 달지 못하게 만든다.
			if ( sessionIdx <= 0 ) {
				alert("로그인후 작성해주세요");
				return;
			}
			
			if( ilchonpyeongText == ""){
				alert("일촌평을 작성해주세요.");
				return;
			}
			
			// Ajax
			url = "ilchon_write.do";
			param = "ilchonpyeongText=" + ilchonpyeongText +
					"&idx=" + idx;
			sendRequest(url, param, resultWrite, "POST");
		}
		// 콜백메소드
		function resultWrite() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				// "{'result':'no'}"
				let data = xhr.responseText;
				
				if ( data == "no" ) {
					alert("작성 실패");
					return;
				}
				
				alert("작성 완료");
				location.href = "main.do?idx=${vo.idx}";
			}
		}
	</script>
	
	<!-- 네이버 로그인 API -->
	<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
	<!-- 네이버 로그아웃 -->
	<script>
		let naverLogoutPopUp; // 팝업창 만들기
		function naverOpenPopUp() { // 팝업 열기 메소드
			// 팝업에 로그아웃 실행 기능 추가 - 네이버 로그아웃이 가능한 주소를 가져다 사용
			naverLogoutPopUp = window.open("https://nid.naver.com/nidlogin.logout", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
		}
		function naverClosePopUp(){ // 팝업 닫기 메소드
			naverLogoutPopUp.close(); // 열린 팝업창을 다시 닫는 기능
		}
		function naverLogout() {
			naverOpenPopUp(); // 팝업 열기
			setTimeout(function() {
				naverClosePopUp(); // 팝업 닫기
				location.href = "logout.do"; // 첫 페이지로 이동
			}, 500); // 팝업 여는거부터 순차적으로 0.5초 간격으로 실행
		}
	</script>
	
	<!-- 카카오 로그인 API -->
	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
	<!-- 카카오 로그아웃 -->
	<script>
		let kakaoLogoutPopUp; // 팝업창 만들기
		function kakaoOpenPopUp() { // 팝업 열기 메소드
			// 팝업에 로그아웃 실행 기능 추가 - 네이버 로그아웃이 가능한 주소를 가져다 사용
			kakaoLogoutPopUp = window.open("https://accounts.kakao.com/logout?continue=https://accounts.kakao.com/weblogin/account", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
		}
		function kakaoClosePopUp(){ // 팝업 닫기 메소드
			kakaoLogoutPopUp.close(); // 열린 팝업창을 다시 닫는 기능
		}
		function kakaoLogout() {
			kakaoOpenPopUp(); // 팝업 열기
			setTimeout(function() {
				kakaoClosePopUp(); // 팝업 닫기
				location.href = "logout.do"; // 첫 페이지로 이동
			}, 500); // 팝업 여는거부터 순차적으로 0.5초 간격으로 실행
		}
	</script>
	
	<!-- 카카오 동의항목 해제 -->
	<script>
		//// 카카오 로그인 API 검증
		//window.Kakao.init("299930f187d00dde5908962ec35a19c9");
		////카카오로그아웃
		//function kakaoLinkLogout() {
			//if (Kakao.Auth.getAccessToken()) { // AccessToken을 가지고 있는지 확인
				//// 유저정보 받아오기
				//Kakao.API.request({
				//// url을 통해 현제 로그인한 사용자를 unlink한다.
					//url: '/v1/user/logout',
					//// 위에 코드가 성공하면 실행
					//success: function (response) {
						//// 로그아웃이 성공하면 이동할 페이지
						//location.href = "logout.do";
					//},
					//fail: function (error) {
						//console.log(error)
					//},
				//});
				//// AccessToken을 "undefined"로 변경
				//Kakao.Auth.setAccessToken(undefined)
			//}
		//}
	</script>
	
</body>
</html>