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
						<!-- 좌측 프로필 사진 및 프로필 소개글 수정 -->
						<form method="POST" enctype="multipart/form-data">
							<!-- 해당 미니홈피 유저의 idx -->
							<input name="idx" type="hidden" value="${ signVo.idx }">
							<!-- 기존 프로필 사진 -->
							<div class="left-image"><img class="leftImg" src="/cyworld/resources/mainphoto/${ signVo.mainPhoto }" alt=""></div>
													<input name="mainPhoto" type="hidden" value="${ signVo.mainPhoto }">
							<!-- 프로필 사진 수정 -->
							<input id="btn-cover" class="selectFile"name="mainPhotoFile" type="file">
							<!-- 기존 프로필 소개글 및 수정 -->
							<textarea name="mainText" class="left-textarea scrollBar"  onkeyup="check_length(this);" placeholder="최대 작성글자는 30자 이내입니다.">${ signVo.mainText }</textarea>
							<input id="btn-cover" class="saveLeftMain" type="button" value="좌측 프로필 수정" onclick="modify_main(this.form)">
						</form>
						<!-- 히스토리 -->
						<div class="history"><img src="resources/images/arrow.png" alt=""><h3>History</h3></div>
						<!-- 파도타기 -->
						<select class="myFriend" onchange="if(this.value) location.href=(this.value);">
							<option value="">::: 파도타기 :::</option>
							<c:forEach var="ilchonList" items="${ ilchonList }">
								<!-- 프로필에서 파도타기는 해당 미니홈피 유저의 프로필로 이동하는것이 아닌 메인으로 이동한다 -->
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
					<!-- 기존 메인 타이틀 및 수정 -->
					<p class="title"><input  id="mainTitle" type="text" value="${ signVo.mainTitle }" onkeyup="check_length(this);" placeholder="최대 작성글자는 30자 이내입니다."></p>
					<aside id="right-aside" class="scrollBar">
						<div class="miniRoomBox"><p>Mini Room</p>
							<div class="miniRoom"><img class="miniRoomImg" src="resources/images/MainroomGif.gif" alt="">
								<div class="hover">
									<div class="show">
										<img src="resources/images/sorryForShow.gif" alt="">
										<p class="sorryText">아직 개발중에 있습니다. <span>※개발진 일동</span></p>
									</div>
								</div>
								<div class="Crayon"><img src="resources/images/Crayon.png" alt=""></div>
							</div>
							<div class="zzang1"><img src="resources/images/zzang.gif" alt=""></div>
							<div class="zzang2"><img src="resources/images/zzang2.gif" alt=""></div>
							<div class="zzang3"><img src="resources/images/zzang3.gif" alt=""></div>
							<div class="Crayonz"><img class="friends" src="resources/images/Crayonz.gif" alt=""></div>
							<div class="CrayonDog"><img src="resources/images/CrayonDog.gif" alt=""></div>	                            
						</div>
						<!-- 미니미 수정 -->
						<form>
							<input class="check_btn" id="btn-cover" type="button" value="미니미수정" onclick="toggle();"></input>
							<div id="minimi_correction" class="scrollBar">
								<div class="minimi-list">
									<div class="minimi-area">
										<input id="btn-cover" class="minimi-select"type="submit" value="수정">
										<input class="minimi-choice" type="radio">
										<img src="resources/images/Crayon2 .png" alt="">
									</div>
								</div>
							</div>
						</form>
						
						<div class="hover1">
							<div class="show1">
								<img src="resources/images/sorryForShow.gif" alt="">
								<p class="sorryText1">아직 개발중에 있습니다. <span>※개발진 일동</span></p>
							</div>
						</div>
						<!-- 개인정보 수정 -->
						<form>
							<div class="modify-user-profile">
								<h2>::개인정보 수정::</h2>
								<p id="my-minimi">My minimi</p>
								<!-- 미니미 수정 -->
								<input class="minimi-main" type="button" src="/cyworld/resources/minimi/${ signVo.minimi }" onclick="minimiPopUp();">
								<!-- 해당 미니홈피 유저의 idx -->
								<input name="idx" type="hidden" value="${ signVo.idx }">
								<!-- 해당 미니홈피 유저의 플랫폼 -->
								<input name="platform" type="hidden" value="${ signVo.platform }">
								<!-- 싸이월드 가입자한테만 보이는 추가 항목들 -->
								<c:if test="${ signVo.platform eq 'cyworld' }">
									<p>ID : <input type="text" value="${ signVo.userID }" readonly></p>
									<p>현재 PW : <input name="info" type="text" value="${ signVo.info }" readonly></p>
									<p>새로운 PW : <input id="pw" name="info" type="password" oninput="pwCheck();"></p>
									<div class="pwText" id="pT1"></div>
									<p>PW 확인 : <input id="pw2" type="password" oninput="pw2Check();"></p>
									<div class="pwText pT2"></div>
								</c:if>
								<!-- 소셜 가입자한테만 보이는 추가 항목들 -->
								<c:if test="${ signVo.platform ne 'cyworld' }">
									<p>ID : <input type="text" value="소셜 로그인 이용중" readonly></p>
									<p>PW : <input name="info" type="text" value="소셜 로그인 이용중" readonly></p>
								</c:if>
								<!-- 공통으로 보이는 항목들 -->
								<p>이름 : <input type="text" value="${ signVo.name }" readonly></p>
								<p>주민번호 : <input type="text" value="${ signVo.identityNum }" readonly></p>
								<!-- 남자일 경우 -->
								<c:if test="${ signVo.gender eq 'M' || signVo.gender eq 'male' }">
									<p>성별:&nbsp; <input class="myRadio" type="radio" name="gender" value="${ signVo.gender }" checked readonly>&nbsp;남</p>
								</c:if>
								<!-- 여자일 경우 -->
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
			<!-- 오른쪽 탭 -->
			<div class="tabs">
				<form>
					<div class="tab-btns">
						<!-- 해당 미니홈피 유저의 idx -->
						<input id="idx" name="idx" type="hidden" value="${ signVo.idx }" readonly>
						<!-- 로그인한 유저의 idx -->
						<input id="sessionIdx" name="sessionIdx" type="hidden" value="${ sessionIdx }" readonly>
						<!-- 홈 탭 -->
						<label for="tab1" id="btn1">홈</label>
						<input id="tab1" type="button" value="홈" style="display: none;" onclick="location.href='main.do?idx=${ signVo.idx }'">
						<!-- 프로필 탭 -->
						<label for="tab2" id="btn2">프로필</label>
						<input id="tab2" type="button" value="프로필" style="display: none;" onclick="profile(this.form);">
						<!-- 다이어리 탭 -->
						<label for="tab3" id="btn3">다이어리</label>
						<input id="tab3" type="button" value="다이어리" style="display: none;" onclick="location.href='diary.do?idx=${ signVo.idx }'">
						<!-- 사진첩 탭 -->
						<label for="tab4" id="btn4">사진첩</label>
						<input id="tab4" type="button" value="사진첩" style="display: none;" onclick="location.href='gallery.do?idx=${ signVo.idx }'">
						<!-- 방명록 탭 -->
						<label for="tab5" id="btn5">방명록</label>
						<input id="tab5" type="button" value="방명록" style="display: none;" onclick="location.href='guestbook.do?idx=${ signVo.idx }'">
					</div>
				</form>
			</div>
		</section>
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
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
	</script>
    <!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		// 메인 타이틀 및 비밀번호 수정
		function modifyUserData(f) {
			// idx
			let idx = f.idx.value;
			// 플랫폼
			let platform = f.platform.value;
			// 메인 타이틀
			let mainTitle = document.getElementById("mainTitle").value;
			
			// 유효성 검사
			
			// 메인 타이틀이 공백일시
			if ( mainTitle =='' ) {
				alert("메인 타이틀을 작성해주세요");
				return;
			}
			
			// 소셜 가입자는 가입할때 따로 작성한 아이디 비밀번호가 없기에 메인타이틀만 수정 가능하다.
			if ( platform != 'cyworld' ) {
				url = "profile_modify_userdata.do"
				param = "idx=" + idx +
						"&mainTitle=" + mainTitle +
						"&platform=" + platform;
				sendRequest(url, param, resultModify, "POST");
			// cyworld 가입자는 가입할때 따로 작성한 아이디와 비밀번호가 있기에 비밀번호도 수정 가능하다.
			} else {
				// 비밀번호 패턴 체크
				let info = f.info;
				let pattern1 = /[0-9]/; // 숫자 입력
				let pattern2 = /[a-zA-Z]/; // 영어 소문자, 대문자 입력
				let pattern3 = /[~!@#$%^&*()_+]/; // 특수기호 입력
				let infoR = document.getElementById("pw2").value;
				
				// 유효성 검사
				
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
							"&mainTitle=" + mainTitle +
							"&platform=" + platform;
					sendRequest(url, param, resultModify, "POST");
				// 비밀번호를 변경하지 않고 그대로 가져갈시
				} else {
					
					url = "profile_modify_userdata.do"
					param = "idx=" + idx +
							"&info=" + info[0].value +
							"&mainTitle=" + mainTitle +
							"&platform=" + platform;
					sendRequest(url, param, resultModify, "POST");
				}
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
	</script>
	
	<!-- textarea 글자 수 제한 -->
	<script>
		//입력 글자 수 제한
		function check_length(area) {
			let text = area.value;
			let test_length = text.length;
			
			// 최대 글자수 
			let max_length = 30;
			
			if(test_length>max_length){
				alert(max_length+"자 이상 작성할 수 없습니다.")
				text = text.substr(0, max_length);
				/* substr() : 문자열에서 특정 부분만 골라낼 때 사용하는 메소드
				??.substr(start, length)
				즉, 여기서는 0부터 30글자까지만 가져와서 text에 저장
				*/
				area.value = text;
				/* text를 다시 area.value로 반환 */
				area.focus();
				/* 다시 area의 위치로 반환 */
			}
		}	
	</script>
	
	<!-- 미니미 토글 메뉴 -->
	<script>
		// toggle
		function toggle() {
			const minimi_correction = document.getElementById('minimi_correction');
			
			if(minimi_correction.style.display !== 'none'){
				minimi_correction.style.display ='none';
			}
			else {
				minimi_correction.style.display ='block';
			}
		}
	</script>
	
	<!-- MiniRoom내의 짱구 이동시키는 기능 -->
	<script>
		const draggable = ($target) => {
			let isPress = false, // 마우스를 눌렀을 때
			prevPosX = 0, // 이전에 위치한 X값
			prevPosY = 0; // 이전에 위치한 Y값
			
			// 드래그 구현에 필요한 이벤트
			$target.onmousedown = start; // 이벤트가 적용된 요소 위에서 마우스 왼쪽 버튼 누를 때 발생
			$target.onmouseup = end; // 마우스 버튼을 누르고 있다가 뗄 때 발생
			
			// 요소의 상위 요소
			window.onmousemove = move;
			
			// mousedown
			/* 요소 위에서 마우스 왼쪽 버튼을 클릭할 시 발생되는 mousedown 이벤트로 함수 호출, 요소가 위치한 좌표를 얻어서 변수에 저장해주고,
			마우스 버튼 누름 여부 저장
			*/
			function start(e) {
				prevPosX = e.clientX;
				prevPosY = e.clientY;
				
				isPress = true;
			}
			
			// mousemove
			/*
			요소 위에서 마우스를 움직이면 이벤트가 발생되는 mousemove 이벤트는 "드래그"가 아닌 "움직임"을 감지
			그러므로 드래그의 충족 조건인 왼쪽 마우스 버튼 클릭 여부를 체크함
			마우스 클릭 여부가 체크되었다면, 이제 마우스를 움직인 만큼 요소를 이동시켜야 함. 요소를 이동시키는 방법은 이전에 위치했던 좌표에서 현재 마우스를 움직인 좌표를 뺌으로 차이 값을 구해주고, top과 left 속성으로 이동시켜줌
			*/
			function move(e) {
				if (!isPress) {
					return;
				}
				
				// 이전 좌표와 현재 좌표 차이값
				const posX = prevPosX - e.clientX; 
				const posY = prevPosY - e.clientY; 
				
				// 현재 좌표가 이전 좌표로 바뀜
				prevPosX = e.clientX; 
				prevPosY = e.clientY;
				
				// left, top으로 이동 
				$target.style.left = ($target.offsetLeft - posX) + "px";
				$target.style.top = ($target.offsetTop - posY) + "px";
			}
			
			// mouseup
			/* 마우스를 때면 move 함수의 코드가 실행되지 않도록 isPress 변수 값을
			false로 바꿔서 마우스 버튼을 땠다는 것을 알 수 있게 해줌
			*/
			function end() {
				isPress = false;
			}
		}
		
		window.onload = () => {
			const $target = document.querySelector(".Crayon");
			
			draggable($target);
		}
		
		$(function () {
			const useStorage = $(".Crayon");
			
			useStorage.draggable({cancel: '.notDrag',scroll: false},{ 
				stop: function () {
					const left = this.offsetLeft;
					const top = this.offsetTop;
					
					sessionStorage.setItem("left", left);
					sessionStorage.setItem("top", top);
				}
			});
			
			update(useStorage);
			
		});
		
		function update(useStorage) {
			const left = sessionStorage.getItem("left");
			const top = sessionStorage.getItem("top");
			useStorage.css({ left: left + "px", top: top + "px" });
			
			useStorage[0].offsetTop = top;
			useStorage[0].offsetLeft = left;
		}
	</script>
</body>
</html>