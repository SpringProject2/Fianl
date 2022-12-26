<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${ signVo.name }님의 미니홈피</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link rel="stylesheet" href="/cyworld/resources/css/main.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	<!-- 타 유저의 미니홈피에서 내 미니홈피로 바로 갈수 있는 버튼 -->
	<c:if test="${ signVo.idx ne sessionUser.idx && sessionUser.idx > 0 }">
		<input id="btn_cover" class="returnMyHome" type="button" value="내 미니홈피로 가기" onclick="location.href='main.do?idx=${sessionUser.idx}'">
	</c:if>
	<!-- 로그인한 유저의 플랫폼에 따른 로그아웃 버튼 생성 -->
	<!-- 플랫폼이 싸이월드일 경우 -->
	<c:if test="${ sessionUser.platform eq 'cyworld' }">
		<input id="btn_cover" class="cy_logout" type="button" value="로그아웃" onclick="location.href='logout.do'">
	</c:if>
	<!-- 플랫폼이 네이버일 경우 -->
	<c:if test="${ sessionUser.platform eq 'naver' }">
		<input id="btn_cover" class="na_logout" type="button" value="네이버 로그아웃" onclick="naverLogout();">
	</c:if>
	<!-- 플랫폼이 카카오일 경우 -->
	<c:if test="${ sessionUser.platform eq 'kakao' }">
		<input id="btn_cover" class="ka_logout" type="button" value="카카오 로그아웃" onclick="kakaoLogout();">
	</c:if>
	<!-- 비회원일 경우 -->
	<c:if test="${ sessionUser eq null }">
		<input id="btn_cover" class="ka_logout" type="button" value="로그인" onclick="location.href='logout.do'">
	</c:if>
	
	<div class="container">
		<section class="left-section">
			<div class="left-dashed-line">
				<div class="left-gray-background">
					<!-- 로그안힌 유저의 미니홈피일 경우 일촌 수 -->
					<c:if test="${ signVo.idx eq sessionUser.idx }">
						<div id="ilchonNum">나의 일촌: ${ signVo.ilchon }</div>
					</c:if>
					<!-- 타 유저의 미니홈피일 경우 일촌 수 -->
					<c:if test="${ signVo.idx ne sessionUser.idx }">
						<div id="ilchonNum">${signVo.name}님의 일촌: ${ signVo.ilchon }</div>
					</c:if>
					<!-- 타 유저의 미니홈피일 경우 일촌 신청 버튼 생성 -->
					<c:if test="${ signVo.idx ne sessionUser.idx }">
						<!-- 일촌 신청 상태를 나타내는 숫자 -->
						<input id="ilchonUp" type="hidden" value="${ ilchon.ilchonUp }">
						<!-- 일촌 신청 하지 않은 경우 -->
						<c:if test="${ ilchon eq null }">
							<input id="btn_cover" class="wantIlchon" type="button" value="일촌 신청" onclick="ilchon();">
						</c:if>
						<!-- 일방적으로 일촌 신청한 경우 -->
						<c:if test="${ ilchon.ilchonUp eq 1 }">
							<input id="btn_cover" class="wantIlchon" type="button" value="일촌 신청중" onclick="ilchon();">
						</c:if>
						<!-- 쌍방으로 일촌 신청한 경우 -->
						<c:if test="${ ilchon.ilchonUp eq 2 }">
							<input id="btn_cover" class="wantIlchon" type="button" value="일촌 해제" onclick="ilchon();">
						</c:if>
						<!-- 일촌 신청 버튼에 대한 설명 알람 -->
						<div class="IlchonAssist"> <p> <span>※  일촌 신청</span> <img class="IlchonAssistImg" src="resources/images/noneMain14.gif" alt="">  <br> 일촌 신청을 하고, <br> 상대방도 나에게 일촌 신청을 하면 일촌이 돼요!</p></div>
					</c:if>
					<!-- 조회수 -->
					<p class="todayBanner"><span>Today</span> <span class="todayHere">${ signVo.today }</span><span>&nbsp;｜ Total</span> ${ signVo.total }</p>
					
					<aside class="left-aside">
						<!-- 좌측, 우측 페이지 이어주는 스프링 디자인 -->
						<div class="item item1"></div>
						<div class="item item1"></div>
						<div class="item item2"></div>
						<div class="item item2"></div>
						<!-- 투데이 -->
						<div class="todayIcon">
							<span class="todayIconText">Today is..</span><img class="box animate__animated animate__headShake animate__infinite " src="resources/images/emoticon1.png" alt="">
						</div>
						<!-- 프로필 사진 -->
						<div class="left-image"><img class="leftImg" src="/cyworld/resources/mainphoto/${ signVo.mainPhoto }" alt=""></div>
						<!-- 프로필 소개글 -->
						<textarea class="left-textarea" id="scrollBar" readonly>${ signVo.mainText }</textarea>
						<!-- 히스토리 -->
						<div class="history"><img src="resources/images/arrow.png" alt=""><h3>History</h3></div>
						<!-- 파도타기 -->
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
					<!-- 메인 타이틀 - 클릭시 새로고침 역할 -->
					<p class="title"><a href="main.do?idx=${ signVo.idx }">${ signVo.mainTitle }</a></p>
					<!-- 검색 -->
					<form name="sf" method="GET">
						<input id="btn_cover" class="search" type="button" value="회원 검색" onclick="searchPopUp();"></input>
					</form>
					<!-- bgm - 페이지 오른쪽 상단에 재생 플레이어와 노래 제목 표시 -->
					<img class="musicLogo" src="resources/images/noneMain15.gif" alt="">
					<a class="mp3_title" href="#" ><div class="circle-container">
						<div class="circle circle1"> ♫ 오르트 구름 - 윤하</div>
					</div></a>
					<audio class="mp3" controls>
						<source src="/cyworld/resources/sound/main.mp3" type="audio/mp3">
					</audio>
					
					<aside class="right-aside" id="scrollBar">
						<!-- 도토리 - 타 유저한테는 안보이게 숨김 -->
						<c:if test="${ signVo.idx eq sessionUser.idx }">
							<div class="dotory">도토리 보유량 : ${signVo.dotoryNum}개 <input id="btn_cover" class="dotoryBtn" type="button" value="도토리구매" onclick="DotoryPopUp()"></div> 
						</c:if>
						<!-- 미니룸 -->
						<div class="miniRoomBox"><p>Mini Room</p>
							<div class="miniRoom"><img src="resources/images/MainroomGif.gif" alt=""></div>
							<div class=" Crayon box animate__animated animate__bounce animate__infinite"><img src="resources/images/Crayon.png" alt=""></div>
							<div class="zzang1"><img src="resources/images/zzang.gif" alt=""></div>
							<div class="zzang2"><img src="resources/images/zzang2.gif" alt=""></div>
							<div class="zzang3"><img src="resources/images/zzang3.gif" alt=""></div>
							<div class="Crayonz"><img class="friends" src="resources/images/Crayonz.gif" alt=""></div>
							<div class="CrayonDog"><img src="resources/images/CrayonDog.gif" alt=""></div>
						</div>
						<!-- 액션 미니미 광고 배너 - 타 유저한테는 안보이게 숨김 -->
						<c:if test="${ signVo.idx eq sessionUser.idx }">
							<div class="right-banner" id="banner">
								<p class="title">액션 미니미 <br>출시!</p>
								<p class="blink blink1">NEW</p><div class="img"><img src="resources/images/cat.gif" alt="" onclick="minimiPopUp();"></div>
								<p class="blink blink2">NEW</p><div class="img"><img src="resources/images/thePooh.gif" alt="" onclick="minimiPopUp();"></div>
								<p class="blink blink3">NEW</p><div class="img"><img src="resources/images/fat.gif" alt="" onclick="minimiPopUp();"></div>
								<input type="button" class="bannerCancel" id="btn_cover" onclick="displayNone(this)" value="끄기">
							</div>
						</c:if>
						
						<form>
							<div class="Ilchonpyeong">
								<!-- 일촌평 작성 -->
								<span>일촌평</span> <input type="text" name="ilchonpyeongText"  onkeyup="check_length(this);" placeholder="일촌과 나누고 싶은 이야기를 남겨보세요 (최대 45글자)"></input>
								<input id="btn_cover" class="Ic-registration" type="button" value="확인" onclick="registration(this.form);"></input>
								
							</div>
						</form>
						<!-- 작성된 일촌평 리스트 -->
						<c:forEach var="vo" items="${ list }">
							<div class="Ilchon" >ㆍ ${ vo.ilchonpyeongText } ${ vo.ilchonSession }</div>
						</c:forEach>
						
					</aside>
				</div>
			</div>
			<!-- 오른쪽 탭 -->
			<div class="tabs">
				<form>
					<div class="tab-btns">
						<!-- 해당 미니홈피 유저의 idx -->
						<input id="idx" name="idx" type="hidden" value="${ signVo.idx }">
						<!-- 로그인한 유저의 idx -->
						<input id="sessionIdx" type="hidden" value="${ sessionUser.idx }">
						<!-- 홈 탭 -->
						<label for="tab1" id="btn1">홈</label>
						<input id="tab1" type="button" value="홈" style="display: none;" onclick="location.href='main.do?idx=${ signVo.idx }'">
						<!-- 프로필 탭 -->
						<label for="tab2" id="btn2">프로필</label>
						<input id="tab2" type="button" value="프로필" style="display: none;" onclick="profile(this.form);">
						<!-- 다이어리 탭 -->
						<label for="tab3" id="btn3">다이어리</label>
						<input id="tab3" type="button" value="다이어리" style="display: none;" onclick="diary(this.form);">
						<!-- 사진첩 탭 -->
						<label for="tab4" id="btn4">사진첩</label>
						<input id="tab4" type="button" value="사진첩" style="display: none;" onclick="gallery(this.form);">
						<!-- 방명록 탭 -->
						<label for="tab5" id="btn5">방명록</label>
						<input id="tab5" type="button" value="방명록" style="display: none;" onclick="guestbook(this.form);">
					</div>
				</form>
			</div>
		</section>
	</div>
	<!-- 눈내리는곳 -->
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
	<!-- bgm 재생  -->
	<script>
		//Audio 사용을 위한 객체 생성
		let audio = new Audio();
		//오디오가 참조하는 노래 주소 지정
		audio.src = "/cyworld/resources/sound/main.mp3";
		myAudio.loop = true; //노래가 끝나도 loop가 가능하게 설정
		audio.play();
		audio.volume = 3;
	</script>
	
	<!-- 각종 팝업창 -->
	<script>
		// 도토리 구매 팝업창
		function DotoryPopUp() {
			let popUrl = "dotory.do?idx=${param.idx}&dotoryNum=${signVo.dotoryNum}";
			let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
			let pop = window.open(popUrl, "_blank", popOption);
		}
		
		// 검색 팝업창
		function searchPopUp() {
			//let popUrl = "main_search_popup.do";
			let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
			window.open("", "search", popOption);
			document.sf.action = "main_search_popup.do";
			document.sf.target="search";
			document.sf.submit();
		}
	</script>
	
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<!-- 일촌 신청 및 일촌평 작성 기능 -->
	<script>
		// 일촌 신청
		function ilchon() {
			let idx = document.getElementById("idx").value;
			let sessionIdx = document.getElementById("sessionIdx").value;
			
			// 비회원일 경우
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			url = "main_ilchon.do";
			param = "idx=" + idx +
					"&sessionIdx=" + sessionIdx;
			sendRequest(url, param, resultIlchon, "GET");
		}
		// 일촌 신청 콜백메소드
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
		
		// 일촌평 작성
		function registration(f) {
			let ilchonpyeongText = f.ilchonpyeongText.value;
			let idx = document.getElementById("idx").value;
			let sessionIdx = document.getElementById("sessionIdx").value;
			
			// 비회원일 경우
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			// 타 유저의 미니홈피일 경우
			if ( idx != sessionIdx ) {
				let ilchonUp = document.getElementById("ilchonUp").value; // 일촌 신청 상태를 나타내는 숫자
				
				// 쌍방으로 일촌 신청하지 않은 경우
				if ( ilchonUp != 2 ) {
					alert("일촌평은 서로 일촌 상태여야 작성 가능합니다");
					return;
				}
			}
			
			// 공백일 경우
			if ( ilchonpyeongText == "") {
				alert("일촌평을 작성해주세요.");
				return;
			}
			
			url = "ilchon_write.do";
			param = "ilchonpyeongText=" + ilchonpyeongText +
					"&idx=" + idx;
			sendRequest(url, param, resultIlchonpyeong, "GET");
		}
		// 일촌평 콜백메소드
		function resultIlchonpyeong() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				let data = xhr.responseText;
				
				if ( data == "no" ) {
					alert("작성 실패");
					return;
				}
			
				alert("작성 완료");
				location.href = "main.do?idx=${signVo.idx}";
			}
		}
	</script>
	
	<!-- 액션 미니미 광고 배너 -->
	<script>
		// 액션 미니미 광고 배너 클릭시 --> 미니미 수정 팝업창
		function minimiPopUp() {
			let popUrl = "profile_minimi_popup.do?idx=${signVo.idx}";
			let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
			window.open(popUrl, "minimi", popOption);
		}
		
		// 액션 미니미 광고 배너 끄기
		function displayNone(f) {
			const rightBanner = document.querySelector('.right-banner');
			rightBanner.style.display = 'none';
		}
	</script>
	
	<!-- 액션 미니미 광고 배너 배경색 -->
	<script>
		// 배너 배경색 랜덤
		const colors = [ '#83bdf490', '#42d3fb98', '#00e5e98b',  '#5bf3c391', '#aafa9494', '#f9f97194', '#ffafc8ac','#b595ff8f','#e4f7d280','#fdd785ac','#f9aa80b7'];
		
		const LENGTH = colors.length;
		
		// setInterval(callback, delay); 지연시간동안 callback을 호출   
		const timer = setInterval(randomColor,3000);
		
		function randomColor() {
			let num1 = Math.floor(Math.random()*LENGTH);
			let num2 = Math.floor(Math.random()*LENGTH);
			let num3 = Math.floor(Math.random()*LENGTH);
			let num4 = Math.floor(Math.random()*LENGTH);
			let num5 = Math.floor(Math.random()*LENGTH);
			// document.body.style.backgroundColor = colors[num];
			document.getElementById('banner').style.background = "linear-gradient(45deg," + colors[num1] + "," + colors[num2] + "," + colors[num3] + "," + colors[num4] + "," + colors[num5] + ")";
		}
		
		//맨 처음부터 배경색 지정
		randomColor();
	</script>
	
	<!-- 오른쪽 탭 기능 -->
	<script>
		// 프로필 탭
		function profile(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			
			// 타 유저 접근 불가
			if ( sessionIdx != idx ) {
				alert("프로필은 본인만 들어갈 수 있습니다");
				return;
			}
			
			f.action = "profile.do";
			f.method = "POST";
			f.submit();
		}
		
		// 다이어리 탭
		function diary(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			
			// 비회원일 경우
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			f.action = "diary.do";
			f.method = "GET";
			f.submit();
		}
		
		// 사진첩 탭
		function gallery(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			
			// 비회원일 경우
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			f.action = "gallery.do";
			f.method = "GET";
			f.submit();
		}
		
		// 방명록 탭
		function guestbook(f) {
			let sessionIdx = document.getElementById("sessionIdx").value;
			let idx = document.getElementById("idx").value;
			
			// 비회원일 경우
			if ( sessionIdx <= 0 ) {
				alert("로그인후 이용 가능합니다");
				return;
			}
			
			f.action = "guestbook.do";
			f.method = "GET";
			f.submit();
		}
	</script>
	
	<!-- textarea 글자 수 제한 -->
	<script>
		// 입력 글자 수 제한
		function check_length(area) {
			let text = area.value;
			let test_length = text.length;
			
			// 최대 글자수 
			let max_length = 45;
			
			if (test_length>max_length) {
				alert(max_length+"자 이상 작성할 수 없습니다.")
				text = text.substr(0, max_length);
				/* substr() : 문자열에서 특정 부분만 골라낼 때 사용하는 메소드
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
	
	<!-- 네이버 로그인 API -->
	<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js" charset="utf-8"></script>
	<!-- 네이버 로그아웃 -->
	<script>
		let naverLogoutPopUp; // 팝업창 만들기
		// 팝업창 열기 메소드
		function naverOpenPopUp() {
			// 팝업창에 로그아웃 실행 기능 추가 - 네이버 로그아웃이 가능한 주소를 가져다 사용
			naverLogoutPopUp = window.open("https://nid.naver.com/nidlogin.logout", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
		}
		// 팝업창 닫기 메소드
		function naverClosePopUp() {
			naverLogoutPopUp.close(); // 열린 팝업창을 다시 닫는 기능
		}
		// 네이버 로그아웃 실행 메소드
		function naverLogout() {
			naverOpenPopUp(); // 팝업창 열기
			setTimeout(function() {
				naverClosePopUp(); // 팝업창 닫기
				location.href = "logout.do"; // 첫 페이지로 이동
			}, 500); // 팝업창 여는거부터 순차적으로 0.5초 간격으로 실행
		}
	</script>
	
	<!-- 카카오 로그인 API -->
	<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
	<!-- 카카오 로그아웃 -->
	<script>
		let kakaoLogoutPopUp; // 팝업창 만들기
		// 팝업창 열기 메소드
		function kakaoOpenPopUp() {
			// 팝업창에 로그아웃 실행 기능 추가 - 카카오 로그아웃이 가능한 주소를 가져다 사용
			kakaoLogoutPopUp = window.open("https://accounts.kakao.com/logout?continue=https://accounts.kakao.com/weblogin/account", "_blank", "toolbar=yes,scrollbars=yes,resizable=yes,width=1,height=1");
		}
		// 팝업창 닫기 메소드
		function kakaoClosePopUp() {
			kakaoLogoutPopUp.close(); // 열린 팝업창을 다시 닫는 기능
		}
		// 카카오 로그아웃 실행 메소드
		function kakaoLogout() {
			kakaoOpenPopUp(); // 팝업창 열기
			setTimeout(function() {
				kakaoClosePopUp(); // 팝업창 닫기
				location.href = "logout.do"; // 첫 페이지로 이동
			}, 500); // 팝업창 여는거부터 순차적으로 0.5초 간격으로 실행
		}
	</script>
	
	<!-- 카카오 동의항목 해제 -->
	<script>
		//// 카카오 로그인 API 검증
		//window.Kakao.init("299930f187d00dde5908962ec35a19c9");
		//// 카카오로그아웃
		//function kakaoLinkLogout() {
			//// AccessToken을 가지고 있는지 확인
			//if (Kakao.Auth.getAccessToken()) {
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