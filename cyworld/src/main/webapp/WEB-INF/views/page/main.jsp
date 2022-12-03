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
 <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	<c:if test="${ signVo.idx ne sessionUser.idx }">
		 <input id="btn_cover" class="returnMyHome" type="button" value="내 미니홈피로 가기" onclick="location.href='main.do?idx=${sessionUser.idx}'">
	</c:if>
	<!--  플랫폼에 따른 로그아웃 버튼 생성  -->
	<c:if test="${ sessionUser.platform eq 'cyworld' }">
		<input id="btn_cover" class="cy_logout" type="button" value="로그아웃" onclick="location.href='logout.do'">
	</c:if>
	<c:if test="${ sessionUser.platform eq 'naver' }">
		<input id="btn_cover" class="na_logout" type="button" value="네이버 로그아웃" onclick="naverLogout();">
	</c:if>
	<c:if test="${ sessionUser.platform eq 'kakao' }">
		<input id="btn_cover" class="ka_logout" type="button" value="카카오 로그아웃" onclick="kakaoLogout();">
	</c:if>
	<c:if test="${ sessionUser eq null }">
		<input id="btn_cover" class="ka_logout" type="button" value="로그인" onclick="location.href='logout.do'">
	</c:if>
	
	<div class="container">
		<section class="left-section">
			<div class="left-dashed-line">
				<div class="left-gray-background">
				<c:if test="${ signVo.idx eq sessionUser.idx }">
					<div id="ilchonNum">나의 일촌: ${ signVo.ilchon }</div>
				</c:if>
				<c:if test="${ signVo.idx ne sessionUser.idx }">
					<div id="ilchonNum">${signVo.name}님의 일촌: ${ signVo.ilchon }</div>
				</c:if>
					<c:if test="${ signVo.idx ne sessionUser.idx }">
						<input id="ilchonUp" type="hidden" value="${ ilchon.ilchonUp }">
						<c:if test="${ ilchon eq null }">
							<input id="btn_cover" class="wantIlchon" type="button" value="일촌 신청" onclick="ilchon();">
						</c:if>
						<c:if test="${ ilchon.ilchonUp eq 1 }">
							<input id="btn_cover" class="wantIlchon" type="button" value="일촌 신청중" onclick="ilchon();">
						</c:if>
						<c:if test="${ ilchon.ilchonUp eq 2 }">
							<input id="btn_cover" class="wantIlchon" type="button" value="일촌 해제" onclick="ilchon();">
						</c:if>
						<div class="IlchonAssist"> <p> <span>※  일촌 신청</span> <img class="IlchonAssistImg" src="resources/images/noneMain14.gif" alt="">  <br> 일촌 신청을 하고, <br> 상대방도 나에게 일촌 신청을 하면 일촌이 돼요!</p></div>
					</c:if>
					<p class="todayBanner"><span>Today</span> <span class="todayHere">156</span><span>&nbsp;｜ Total</span> 45,405</p>
					<aside class="left-aside">
						<div class="item item1"></div>
						<div class="item item1"></div>
						<div class="item item2"></div>
						<div class="item item2"></div>
						<div class="todayIcon">
							<span class="todayIconText">Today is..</span><img class="box animate__animated animate__headShake animate__infinite " src="resources/images/emoticon1.png" alt="">
						</div>
						<div class="left-image"><img class="leftImg" src="/cyworld/resources/mainphoto/${ signVo.mainPhoto }" alt=""></div>
						<textarea class="left-textarea" id="scrollBar" readonly>${ signVo.mainText }</textarea>
						<div class="history"><img src="resources/images/arrow.png" alt=""><h3>History</h3></div>
						<select class="myFriend" onchange="if(this.value) location.href=(this.value);">
							<option value="">::: 파도타기 :::</option>
							<c:forEach var="ilchonList" items="${ ilchonList }">
								<option value="main.do?idx=${ ilchonList.ilchonIdx }">ㆍ ${ ilchonList.ilchonName }</option>
							</c:forEach>
						</select>
					</aside>
				</div>
			</div>
		</section>
		
		<section class="right-section">
			<div class="right-dashed-line">
				<div class="right-gray-background">
					<p class="title"><a href="main.do?idx=${ signVo.idx }">${ signVo.mainTitle }</a></p>
					<!-- a태그 = 새로고침  -->
					<!-- <p class="titleLink"><a href="#">http://www.zenghyun.com</a></p> -->
					<!-- 회원 검색 -->
					<!-- 아이디, 이메일 검색 -->
					             <!-- 도토리구매!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! -->
                  
					<form name="sf" method="GET">
						<input id="btn_cover" class="search" type="button" value="회원 검색" onclick="searchPopUp();"></input>
						<input type="hidden" name="idx" value="${ sighVo.idx }">	
					</form>
					<!-- main 페이지에 재생 플레이어와 노래 제목 표시  -->
  				 	<img class="musicLogo" src="resources/images/noneMain15.gif" alt="">
  				 	<a class="mp3_title" href="#" ><div class="circle-container">
        <div class="circle circle1"> ♫ 오르트 구름 - 윤하</div>
    </div></a>
   					<audio class="mp3" controls>
 				  <source src="/cyworld/resources/sound/main.mp3" type="audio/mp3">
 					  </audio>
 					  
					<aside class="right-aside" id="scrollBar">
						<div class="dotory">도토리 보유량 : ${signVo.dotoryNum}개 <input id="btn_cover" class="dotoryBtn" type="button" value="도토리구매" onclick="DotoryPopUp()"></div> 
						 <div class="miniRoomBox"><p>Mini Room</p>
                     <div class="miniRoom"><img src="resources/images/MainroomGif.gif" alt=""></div>
                     <div class=" Crayon box animate__animated animate__bounce animate__infinite"><img src="resources/images/Crayon.png" alt=""></div>
                     <div class="zzang1"><img src="resources/images/zzang.gif" alt=""></div>
                     <div class="zzang2"><img src="resources/images/zzang2.gif" alt=""></div>
                     <div class="zzang3"><img src="resources/images/zzang3.gif" alt=""></div>
                     <div class="Crayonz"><img class="friends" src="resources/images/Crayonz.gif" alt=""></div>
                     <div class="CrayonDog"><img src="resources/images/CrayonDog.gif" alt=""></div>
                     </div>
						<form method="post">
							<div class="Ilchonpyeong">
							
								<span>일촌평</span> <input type="text" name="ilchonpyeongText"  onkeyup="check_length(this);" placeholder="일촌과 나누고 싶은 이야기를 남겨보세요 (최대 45글자)"></input>
								<input id="btn_cover" class="Ic-registration" type="button" value="확인" onclick="registration(this.form);"></input>
								
							</div>
						</form>
						<c:forEach var="vo" items="${ list }">
							<div class="Ilchon" >ㆍ ${ vo.ilchonpyeongText } ${ vo.ilchonSession }</div>
						</c:forEach>
					</aside>
				</div>
			</div>
			<div class="tabs">
				<form>
					<div class="tab-btns">
						<input id="idx" name="idx" type="hidden" value="${ signVo.idx }" readonly>
						<input id="sessionIdx" name="sessionIdx" type="hidden" value="${ sessionUser.idx }" readonly>
						<label for="tab1" id="btn1">홈</label>
						<input id="tab1" type="button" value="홈" style="display: none;" onclick="location.href='main.do?idx=${ signVo.idx }'">
						<label for="tab2" id="btn2">프로필</label>
						<input id="tab2" type="button" value="프로필" style="display: none;" onclick="profile(this.form);">
						<label for="tab3" id="btn3">다이어리</label>
						<input id="tab3" type="button" value="다이어리" style="display: none;" onclick="diary(this.form);">
						<label for="tab4" id="btn4">사진첩</label>
						<input id="tab4" type="button" value="사진첩" style="display: none;" onclick="gallery(this.form);">
						<label for="tab5" id="btn5">방명록</label>
						<input id="tab5" type="button" value="방명록" style="display: none;" onclick="guestbook(this.form);">
					</div>
				</form>
			</div>
		</section>
	</div>
	<!-- 눈내리는 css -->
	<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>
<div class="snowflake"></div>

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
		function diary(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			f.action = "diary.do";
			f.method = "GET";
			f.submit();
		}
		function gallery(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			f.action = "gallery.do";
			f.method = "GET";
			f.submit();
		}
		function guestbook(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			f.action = "guestbook.do";
			f.method = "GET";
			f.submit();
		}
	</script>
	<!-- 음악 재생  -->
	<script type="text/javascript">
		//Audio 사용을 위한 객체 생성
		var audio = new Audio();
		//오디오가 참조하는 노래 주소 지정
		audio.src = "/cyworld/resources/sound/main.mp3";
		myAudio.loop = true; //노래가 끝나도 loop가 가능하게 설정
		audio.play();
		audio.volume = 3;
	</script> 
	
	<script>
		//window.open (검색 결과창)
		function searchPopUp() {
			let popUrl = "main_search_popup.do";
			let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
			window.open(popUrl, "search", popOption);
			document.sf.action = "main_search_popup.do";
		    document.sf.target="search";s
		    document.sf.submit();
		}
	</script>
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
				location.href = "search.do?idx=${signVo.idx}";
			} 
		}
		
		// 일촌평 작성
		function registration(f){
			let ilchonpyeongText = f.ilchonpyeongText.value;
			let idx = document.getElementById("idx").value;
			let sessionIdx = document.getElementById("sessionIdx").value;
			let ilchonUp = document.getElementById("ilchonUp").value;
			
			// 세션이 0이면 비회원이므로 일촌평을 달지 못하게 만든다.
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			if ( ilchonUp != 2 ) {
				alert("일촌평은 서로 일촌 상태여야 작성 가능합니다");
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
				location.href = "main.do?idx=${signVo.idx}";
			}
		}
		
		function ilchon() {
			let idx = document.getElementById("idx").value;
			let sessionIdx = document.getElementById("sessionIdx").value;
			
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			url = "main_ilchon.do";
			param = "idx=" + idx +
					"&sessionIdx=" + sessionIdx;
			sendRequest(url, param, resultIlchon, "GET");
		}
		// 콜백메소드
		function resultIlchon() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				
				let data = xhr.responseText;
				let ilchonNum = document.getElementById("ilchonNum").value;
				
				if ( data == "no" ) {
					alert("일촌 취소");
					location.href = "main.do?idx=${signVo.idx}";
					return;
				}
				
				alert("일촌 신청 완료");
				location.href = "main.do?idx=${signVo.idx}";
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
	   <script>
   //도토리 구매창 팝업
      function DotoryPopUp(){
         let popUrl="dotory.do?idx=${param.idx}&dotoryNum=${signVo.dotoryNum}";
         let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
         let pop = window.open(popUrl, "_blank", popOption);
      }
   </script>
   <!-- textarea 글자 수 제한 -->
	<script>
	//입력 글자 수 제한
    function check_length(area){
    	var text = area.value;
    	var test_length = text.length;
    	
    	//최대 글자수 
    	var max_length = 45;
    	
    	if(test_length>max_length){
    		alert(max_length+"자 이상 작성할 수 없습니다.")
    		text = text.substr(0, max_length);
    		/* substr() : 문자열에서 특정 부분만 골라낼 때 사용하는 메서드. 
    		??.substr(start, length) 
    		 즉, 여기서는 0부터 45글자까지만 가져와서 text에 저장 
    		*/
    		area.value = text;
    		/* text를 다시 area.value로 반환 */
    		area.focus();
    		/* 다시 area의 위치로 반환 */
    	}
    }	
	</script>
</body>
</html>