<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/guestbook.css">
<link rel="stylesheet" href="/cyworld/resources/css/animatemin.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	<div class="container">
		<section class="left-section">
			<div class="left-dashed-line">
				<div class="left-gray-background">
					<p class="todayBanner"><span>Today</span> <span class="todayHere">${ signVo.today }</span><span>&nbsp;｜ Total</span> ${ signVo.total }</p>
					<aside class="left-aside">
						<!-- 좌측, 우측 페이지 이어주는 스프링 디자인 -->
						<div class="item item1"></div>
						<div class="item item1"></div>
						<div class="item item2"></div>
						<div class="item item2"></div>
						<!-- 투데이 -->
						<div class="todayIcon">
							<span class="todayIconText">Today is..</span><img class="box animate__animated animate__headShake animate__infinite" src="resources/images/emoticon1.png" alt="">
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
								<option value="diary.do?idx=${ ilchonList.ilchonIdx }">ㆍ ${ ilchonList.ilchonName }</option>
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
					<!-- bgm - 페이지 오른쪽 상단에 재생 플레이어와 노래 제목 표시 -->
					<img class="musicLogo" src="resources/images/noneMain15.gif" alt="">
					<a class="mp3_title" href="#" ><div class="circle-container">
						<div class="circle circle1"> ♫ 오르트 구름 - 윤하 </div>
					</div></a>
					<audio class="mp3" controls>
						<source src="/cyworld/resources/sound/main.mp3" type="audio/mp3">
					</audio>
					
					<aside class="right-aside" id="scrollBar">
						<div class="fake"></div>
						<div id="main_box">
							<h1>방명록 </h1>
							<input class="textWrite" id="btn_cover" type="button" value="방명록 작성" onclick="location.href='guestbook_insert_form.do?idx=${signVo.idx}'">
						</div>
						
						<c:forEach var="vo" items="${ list }">
							<div class="guestbook_box">
								<form>
									<!-- 방명록 정보 구역 -->
									<div class="contentIntroduce">
										<input name="guestIdx" type="hidden" value="${ vo.guestIdx }">
										<input name="guestbookSession" type="hidden" value="${ vo.guestbookSession }">
										<label for="">게시글 번호:&nbsp; </label><input class="contentRef" type="text" name="guestbookContentRef" value="${ vo.guestbookContentRef }">
										<div class="type_guestbookContentName">${ vo.guestbookContentName }</div>
										<div class="type_guestbookRegdate">작성일자: (${ vo.guestbookRegdate })</div> 
										<!-- 좋아요 구역 -->
										<div class="likeHeart">${ vo.guestbookLikeNum }</div>
										<input id="heart" type="button"  onclick="like(this.form)">
										<!-- 로그인한 유저가 작성자 이거나 방명록 주인일 경우에만 보인다 -->
										<c:if test="${ sessionIdx eq vo.guestbookSession || sessionIdx eq vo.guestIdx }">
											<input class="textBtn" id="btn-cover" type="button" value="수정" onclick="modify(this.form);">
											<input class="textBtn" id="btn-cover" type="button" value="삭제" onclick="del(this.form);">
										</c:if> 
									</div>
									<!-- 방명록 내용 구역 -->
									<div class="GuestContent" >
										<!-- 방명록 작성자의 미니미 -->
										<img class="GuestMinimi"src="/cyworld/resources/minimi/${ vo.guestbookMinimi }"/>
										<!-- 비밀글 구역 -->
										<div class="GuestText">
											<input name="guestbookSecretCheck" type="hidden" value="${ vo.guestbookSecretCheck }">
											<!-- 방명록을 비밀글로 했을 경우 -->
											<c:if test="${ vo.guestbookSecretCheck eq 1 }">
												<div class="flip-box">
													<div class="flip">
														<div class="frontdelComment"><img src="resources/images/cry.png" alt="">&nbsp;&nbsp; 비밀글 입니다.</div>
														<div class="backdelComment" id="backColor">
															<!-- 로그인한 유저가 작성자 이거나 방명록 주인일 경우에만 보인다 -->
															<c:if test="${ sessionIdx eq vo.guestbookSession || sessionIdx eq vo.guestIdx }">
																<pre class="type_guestbookContent"> <textarea class="guestbookContent" id= "scrollBar1" readonly>${ vo.guestbookContent }</textarea></pre>
															</c:if>
															<input name="guestbookSecretCheck" type="hidden" value="${ vo.guestbookSecretCheck }">
														</div>
													</div>
												</div>
											</c:if>
											<!-- 방명록을 공개글로 했을 경우 -->
											<c:if test="${ vo.guestbookSecretCheck eq 0 }">
												<pre class="type_guestbookContent"> <textarea class="guestbookContent" id= "scrollBar1" readonly>${ vo.guestbookContent }</textarea></pre>
											</c:if>
										</div>
									</div>
								</form>
							</div>
						</c:forEach>
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
	
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function del(f) {
			if ( !confirm('정말 삭제하시겠습니까?') ) {
				return;
			}
			
			var url = "guestbook_delete.do";
			var param = "guestIdx=" + f.guestIdx.value +
						"&guestbookContentRef=" + f.guestbookContentRef.value;
			sendRequest(url, param, resultFn, "GET");
		}
		// 방명록 삭제 콜백메소드
		function resultFn() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				var data = xhr.responseText;
				
				if ( data == 'no' ) {
					alert("삭제 실패");
					return;
				}
				
				alert("삭제성공");
				location.href="guestbook.do?idx=${param.idx}";
			}
		}
		
		//게시글 수정
		function modify(f) {
			f.action = 'guestbook_modify_form.do';
			f.method = "GET";
			f.submit();
		}
	</script>
	
	<!-- 좋아요 기능 -->
	<script>
		// 좋아요 구하기
		function like(f) {
			let guestIdx = f.guestIdx.value;
			let guestbookContentRef = f.guestbookContentRef.value;
			
			let url = "guestbook_like.do";
			let param = "guestIdx=" + guestIdx +
						"&guestbookContentRef=" + guestbookContentRef;
			sendRequest(url, param, resultLike, "GET");
		}
		// 좋아요 콜백메소드
		function resultLike() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				let data = xhr.responseText;
				
				// 콜백으로 받아서 페이지를 갱신하지 않고 바로 바꿀수 있는 방법 구상중
				// 현제는 별다른 방법이 없어서 갱신하는 방법 이용중
				location.href = "guestbook.do?idx=${param.idx}"
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
</body>
</html>