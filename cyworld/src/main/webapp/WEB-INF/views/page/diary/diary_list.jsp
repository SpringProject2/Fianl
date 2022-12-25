<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>다이어리</title>
<link rel="stylesheet" href="/cyworld/resources/css/diary.css">
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
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
						<!-- 카테고리 - 미개발 -->
						<details>
							<summary>2022</summary>
							<ul>
								<li><a href="#">----- 일기</a></li>
								<li><a href="#">----- 기록</a></li>
							</ul>
						</details>
						<details>
							<summary class="summary">2021</summary>
							<ul>
								<li><a href="#">----- 일기</a></li>
								<li><a href="#">----- 기록</a></li>
							</ul>
						</details>
						<div class="hover">
							<div class="show">
								<img src="resources/images/sorryForShow.gif" alt="">
								<p class="sorryText">
									아직 개발중에 있습니다. <span>※개발진 일동</span>
								</p>
							</div>
						</div>
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
							<h1>다이어리</h1>
							<!-- 로그인한 유저가 다이어리 주인일 경우에만 보인다 -->
							<c:if test="${ sessionIdx eq param.idx }">
								<div>
									<input class="writing"  type="button" value="글쓰기" onclick="location.href='diary_insert_form.do?idx=${param.idx}'">
								</div>
							</c:if>
						</div>
						
						<c:forEach var="vo" items="${ list }">
							<div class="diary_box" >
								<form>
									<div class="type_diarycontent">
										<div class="top_section">
											<input name="diaryIdx" type="hidden" value="${ param.idx }">
											<input type="hidden" value="${ sessionIdx }">
											<p>
												게시글 번호: <input id="contentNum" name="diaryContentRef" type="text" value="${ vo.diaryContentRef }" readonly>
												<label class="date">작성일자 : ${ vo.diaryRegdate }</label>
											</p>
										</div>
										<!-- 로그인한 유저가 다이어리 주인일 경우에만 보인다 -->
										<c:if test="${ sessionIdx eq param.idx }">
											<input class="modify" id="btn-cover" type="button" value="수정" onclick="modify(this.form);">
											<input class="delete" id="btn-cover" type="button" value="삭제" onclick="del(this.form);">
										</c:if>
										<pre class="diaryContent"><textarea id="scrollBar1" readonly>${ vo.diaryContent }</textarea></pre>
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
		// 다이어리 글 삭제
		function del(f) {
			if ( !confirm('정말 삭제하시겠습니까?') ) {
				return;
			}
			
			var url = "diary_delete.do";
			var param = "diaryIdx=" + f.diaryIdx.value + 
						"&diaryContentRef=" + f.diaryContentRef.value;
			sendRequest( url, param, resultFn, "GET" );
		}
		// 다이어리 글 삭제 콜백메소드
		function resultFn() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				var data = xhr.responseText;
				
				if ( data == 'no' ) {
					alert("삭제 실패");
					return;
				}
				
				alert("삭제성공");
				location.href="diary.do?idx=${param.idx}";
			}
		}
		
		// 다이어리 글 수정
		function modify(f) {
			f.action = 'diary_modify_form.do';
			f.method = "GET";
			f.submit();
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