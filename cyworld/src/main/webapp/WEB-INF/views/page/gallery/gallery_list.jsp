<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사진첩</title>
<link rel="stylesheet" href="/cyworld/resources/css/gallery.css">
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body >
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
								<li><a href="#">----- 일상</a></li>
								<li><a href="#">----- 여행</a></li>
								<li><a href="#">----- 기록</a></li>
							</ul>
						</details>
						<details>
							<summary class="summary">2021</summary>
							<ul>
								<li><a href="#">----- 일상</a></li>
								<li><a href="#">----- 여행</a></li>
								<li><a href="#">----- 기록</a></li>
							</ul>
						</details>
						<div class="hover">
							<div class="show">
								<img src="resources/images/sorryForShow.gif" alt="">
								<p class="sorryText">
									카테고리 기능은 개발중에 있습니다. <br> <span>※개발진 일동</span>
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
						<div class="galleryContainer">
							<div id="writing">
								<h1>사진첩 목록</h1>
								<!-- 로그인한 유저가 사진첩 주인일 경우에만 보인다 -->
								<c:if test="${ sessionIdx eq param.idx }">
									<input id="btn-cover" type="button" value="글쓰기" onclick="location.href='gallery_insert_form.do?idx=${param.idx}'">
								</c:if>
							</div>
							
							<c:forEach var="vo" items="${ galleryList }">
								<div class="gallery_box">
									<form>
										<!-- 게시글 정보 구역 -->
										<input type="hidden" name="gallIdx" value="${ vo.gallIdx }">
										<div class="galleryTitle"><span>게시글 제목:</span> ${ vo.galleryTitle }</div>
										<div class="galleryDate">
											<span class="contentNum"> &nbsp;&nbsp;게시글 번호: </span><input class="cNum" type="text" name="galleryContentRef" value="${ vo.galleryContentRef}">
											<span class="date">작성일자 : ${ vo.galleryRegdate } </span>
										</div>
										<!-- 게시글 사진 및 비디오 구역 -->
										<div class="type_galleryFile">
											<!-- 첨부된 이미지가 있는 경우에만 image 및 video태그를 보여주자! -->
											<c:if test="${ vo.galleryFileName ne 'no_file' }">
												<!-- 확장자가 image일 경우 -->
												<c:if test="${ vo.galleryFileExtension eq 'image' }">
													<div class="ImgPosition"> <img class="myImg" src="/cyworld/resources/upload/${ vo.galleryFileName }" /></div>
												</c:if>
												<!-- 확장자가 video일 경우 -->
												<c:if test="${ vo.galleryFileExtension eq 'video' }">
													<div class="VideoPosition">
														<!-- video태그 autoplay : 자동 재생 / controls loop : 반복 재생 / muted : 음소거 -->
														<video  class="myVideo" autoplay controls loop muted src="/cyworld/resources/upload/${ vo.galleryFileName }" />
													</div>
												</c:if>
											</c:if>
										</div>
										<!-- 게시글 내용 구역 -->
										<div class="type_gallerycontent">
											<pre><textarea class="galleryContent" id="scrollBar1" readonly>${ vo.galleryContent }</textarea></pre> 
											<div class="myButton">
												<!-- 좋아요 구역 -->
												<p class="like">
													<span id="galleryLikeNum">${ vo.galleryLikeNum }</span>
													<input  id="heart" type="button" onclick="like(this.form)" >
												</p>
												<!-- 로그인한 유저가 사진첩 주인일 경우에만 보인다. -->
												<c:if test="${ sessionIdx eq vo.gallIdx }">
													<p class="changeBtn">
														<input id="btn-cover" type="button" value="수정" onclick="modify(this.form);">
														<input id="btn-cover" class="del"  type="button" value="삭제" onclick="del(this.form);">
													</p>
												</c:if>
											</div>
										</div>
									</form>
								</div>
								
								<!-- 댓글 구역 -->
								<div class="commentArea">
									<form method="post">
										<div id="GalleryReply">
											<!-- 사진첩 주인 idx -->
											<input type="hidden" name="galleryCommentIdx" value="${ vo.gallIdx }">
											<!-- 사진첩 댓글 번호 -->
											<input type="hidden" name="galleryCommentRef" value="${ vo.galleryContentRef }">
											<!-- 작성자 이름 -->
											<div class="commentWriter"><label>&nbsp;&nbsp;작성자 : </label><input type="text" name="galleryCommentName" value="${ sessionName }" readonly>
												<!-- 댓글 작성 -->
												<label>&nbsp;&nbsp;댓글 : </label><input type="text" name="galleryCommentContent">
												<!-- 댓글 작성 버튼 -->
												<input id="btn_cover" class="GC-reply" type="button" value="댓글등록" onclick="reply(this.form);">
											</div>
										</div>
									</form>
									<!-- 게시글마다 댓글 보이는 구역 -->
									<c:forEach var="cvo" items="${ commentList }">
										<form>
											<c:if test="${ cvo.galleryCommentIdx eq vo.gallIdx && cvo.galleryCommentRef eq vo.galleryContentRef }">
												<div class="Gallerycomment">
													<!-- 사진첩 주인 idx -->
													<input type="hidden" name="galleryCommentIdx" value="${ cvo.galleryCommentIdx }">
													<!-- 사진첩 댓글 번호 -->
													<input type="hidden" name="galleryCommentRef" value="${ cvo.galleryCommentRef }">
													<!-- 사진첩 게시글 번호 -->
													<input type="hidden" name="galleryNum" value="${ cvo.galleryNum }">
													<!-- 댓글 상단 정보 구역 -->
													<div class="textPosition"> 작성자 : ${ cvo.galleryCommentName } / 작성일자 : ${ cvo.galleryCommentRegdate }</div>
													<!-- 댓글 하단 내용 구역 -->
													<c:if test="${cvo.galleryCommentDeleteCheck eq 0}">
														<div class="textPosition"><pre>댓글 : ${ cvo.galleryCommentContent }</pre></div>
													</c:if>
													<!-- 댓글을 삭제했을 경우 -->
													<c:if test="${cvo.galleryCommentDeleteCheck eq -1}">
														<div class="flip-box">
															<div class="flip">
																<div class="frontdelComment"><img src="resources/images/cry.png" alt="">&nbsp;&nbsp; 댓글을 볼 수 없어요 ㅠ</div>
																<div class="backdelComment" id="backColor">삭제된 댓글입니다.</div>
															</div>
														</div>
													</c:if>
													<!-- 댓글을 삭제하지 않았을 경우 -->
													<c:if test="${ cvo.galleryCommentDeleteCheck eq 0 }">
														<!-- 로그인한 유저가 작성자 이거나 사진첩 주인일 경우에만 보인다 -->
														<c:if test="${ sessionIdx eq cvo.galleryCommentSession || sessionIdx eq cvo.galleryCommentIdx }">
															<input  id="btn_cover"type="button" value="댓글삭제" onclick="gcdel(this.form);">
														</c:if>
													</c:if>
												</div>
											</c:if>
										</form>
									</c:forEach>
								</div>
							</c:forEach>
						</div>
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
		// 게시글 삭제
		function del(f){
			if ( !confirm('정말 삭제하시겠습니까?') ) {
				return;
			}
			
			var url = "gallery_delete.do";
			var param = "gallIdx=" + f.gallIdx.value + 
						"&galleryContentRef=" + f.galleryContentRef.value;
			sendRequest( url, param, resultDel, "GET" );
		}
		// 게시글 삭제 콜백메소드
		function resultDel() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				var data = xhr.responseText;
				
				if ( data == 'no' ) {
					alert("삭제 실패");
					return;
				}
				
				alert("삭제성공");
				location.href = "gallery.do?idx=${param.idx}";
			}
		}
		
		// 게시글 수정
		function modify(f) {
			f.action = 'gallery_modify_form.do';
			f.method = "GET";
			f.submit();
		}
	</script>
	
	<!-- 좋아요 기능 -->
	<script>
		// 게시글 좋아요
		function like(f) {
			let gallIdx = f.gallIdx.value;
			let galleryContentRef = f.galleryContentRef.value;
			
			let url = "gallery_like.do";
			let param = "gallIdx=" + gallIdx +
						"&galleryContentRef=" + galleryContentRef;
			sendRequest(url, param, resultLike, "GET");
		}
		// 게시글 좋아요 콜백메소드
		function resultLike() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				let data = xhr.responseText;
				
				// 콜백으로 받아서 페이지를 갱신하지 않고 바로 바꿀수 있는 방법 구상중
				// 현제는 별다른 방법이 없어서 갱신하는 방법 이용중
				location.href = "gallery.do?idx=${param.idx}";
			}
		}
	</script>
	
	<!-- 댓글 기능 -->
	<script>
		// 댓글 작성
		function reply(f) {
			let galleryCommentIdx = f.galleryCommentIdx.value;
			let galleryCommentRef = f.galleryCommentRef.value;
			let galleryCommentName = f.galleryCommentName.value;
			let galleryCommentContent = f.galleryCommentContent.value;
			
			//유효성 체크
			
			if ( galleryCommentContent == "") {
				alert("댓글을 작성해주세요.");
				return;
			}
			
			url = "comment_insert.do";
			param = "galleryCommentIdx=" + galleryCommentIdx +
					"&galleryCommentRef=" + galleryCommentRef +
					"&galleryCommentContent=" + galleryCommentContent +
					"&galleryCommentName=" + galleryCommentName;
			sendRequest(url, param, resultWrite, "POST");
		}
		// 댓글 작성 콜백메소드
		function resultWrite() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				let data = xhr.responseText;
				
				if ( data == "no" ) {
					alert("작성 실패");
					return;
				}
				
				alert("작성 완료");
				location.href = "gallery.do?idx=${param.idx}";
			}
		}
		
		// 댓글 삭제
		function gcdel(f) {
			if ( !confirm('정말 삭제하시겠습니까?') ) {
				return;
			}
			
			var url = "gcomment_delete.do";
			var param = "galleryCommentRef=" + f.galleryCommentRef.value +
						"&galleryCommentIdx=" + f.galleryCommentIdx.value +
						"&galleryNum=" + f.galleryNum.value;
			sendRequest( url, param, resultgcDel, "GET" );
		}
		// 댓글 삭제 콜백메소드
		function resultgcDel() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				var data = xhr.responseText;
				
				if ( data == 'no' ) {
					alert("삭제 실패");
					return;
				}
				alert("삭제성공");
				location.href = "gallery.do?idx=${param.idx}";
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