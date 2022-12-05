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
                        <p class="todayBanner"><span>Today</span> <span class="todayHere">${ signVo.today }</span><span>&nbsp;｜ Total</span> ${ signVo.total }</p>
                        <aside class="left-aside">
                            <div class="item item1"></div>
                            <div class="item item1"></div>
                            <div class="item item2"></div>
                            <div class="item item2"></div>
                            
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
                        </aside>
                    </div>
                </div>
        </section>
       
        <section class="right-section">
                <div class="right-dashed-line">
                    <div class="right-gray-background">
                        <p class="title"><a href="main.do?idx=${ signVo.idx }">${ signVo.mainTitle }</a></p>
                  <!-- main 페이지에 재생 플레이어와 노래 제목 표시  -->
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
								<!-- 로그인한 사람이 사진첩 주인일 경우에만 보인다. -->
								<c:if test="${ sessionIdx eq param.idx }">
									<input id="btn-cover" type="button" value="글쓰기" onclick="location.href='gallery_insert_form.do?idx=${param.idx}'">
								</c:if>
                            </div>
                            <c:forEach var="vo" items="${ galleryList }">
                                
                                <div class="gallery_box">
                                    
                                   
                                    <form>
                                         <div class="galleryTitle"><span>게시글 제목:</span> ${ vo.galleryTitle }</div>
                                        <div class="galleryDate"> <span class="contentNum"> &nbsp;&nbsp;게시글 번호: </span>
                                            <input class="cNum" type="text" name="galleryContentRef" value="${ vo.galleryContentRef}">
                                            <span class="date">작성일자 : ${ vo.galleryRegdate } </span>
                                        </div>
                                        <input type="hidden" name="gallIdx" value="${ vo.gallIdx }">
                                       
                                        <div class="type_galleryFile">
                                            <!-- 첨부된 이미지가 있는 경우에만 img 및 viedo태그를 보여주자! -->
                                            <c:if test="${ vo.galleryFileName ne 'no_file' }">
                                                <!-- 확장자가 img일때 -->
                                                <c:if test="${ vo.galleryFileExtension eq 'image' }">
                                                <div class="ImgPosition"> <img class="myImg" src="/cyworld/resources/upload/${ vo.galleryFileName }" /></div>
                                                </c:if>
                                                <!-- 확장자가 video일때 -->
                                                <c:if test="${ vo.galleryFileExtension eq 'video' }">
                                                  <div class="VideoPosition"> 
                                                     <video  class="myVideo" autoplay controls loop muted src="/cyworld/resources/upload/${ vo.galleryFileName }" /> 
                                                </div> 
                                                    <!-- video태그 autoplay : 자동 재생 / controls loop : 반복 재생 / muted : 음소거 -->
                                                </c:if>
                                            </c:if>
                                        </div>
                                        
                                    <!-- 좋아요, 수정, 삭제 기능 -->
                                        <div class="type_gallerycontent">
                                            <pre><textarea class="galleryContent" id="scrollBar1" readonly>${ vo.galleryContent }</textarea></pre> 
                                                <div class="myButton">
                                                    <p class="like">
                                                        <span id="galleryLikeNum">${ vo.galleryLikeNum }</span>
                                                      <input  id="heart" type="button" onclick="like(this.form)" >
                                                    </p>
                                                    <!-- 로그인한 사람이 사진첩 주인일 경우에만 보인다. -->
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
                                <div class="commentArea">
                              <!-- 댓글 구역 -->
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
													<input type="hidden" name="galleryCommentIdx" value="${ cvo.galleryCommentIdx }">
													<input type="hidden" name="galleryCommentRef" value="${ cvo.galleryCommentRef }">
													<input type="hidden" name="galleryNum" value="${ cvo.galleryNum }">
													<div class="textPosition"> 작성자 : ${ cvo.galleryCommentName } / 작성일자 : ${ cvo.galleryCommentRegdate }</div>
													<c:if test="${cvo.galleryCommentDeleteCheck eq -1}">
														
														<div class="flip-box">
															    <div class="flip">
																	<div class="frontdelComment"><img src="resources/images/cry.png" alt="">&nbsp;&nbsp; 댓글을 볼 수 없어요 ㅠ</div>
															        <div class="backdelComment" id="backColor">삭제된 댓글입니다.</div>
															    </div>
															</div>
													</c:if>
													<c:if test="${cvo.galleryCommentDeleteCheck eq 0}">
														<div class="textPosition"><pre>댓글 : ${ cvo.galleryCommentContent }</pre></div>
													</c:if>
													<!-- 1. 댓글을 삭제하기 전에만 보인다. -->
													<c:if test="${ cvo.galleryCommentDeleteCheck eq 0 }">
														<!-- 2. 로그인한 사람이 사진첩 주인 이거나 작성자일 경우에만 보인다. -->
														<c:if test="${ sessionIdx eq cvo.galleryCommentIdx || sessionIdx eq cvo.galleryCommentSession }">
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
     
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
      function del(f){
         
         if( !confirm('정말 삭제하시겠습니까?') ){
            return;
         }
         
         //idx를 Ajax를 통해서 서버로 전달
         var url = "gallery_delete.do";
         var param = "gallIdx=" + f.gallIdx.value + 
                  "&galleryContentRef=" + f.galleryContentRef.value;
         //준비된 두 개의 정보를 콜백메서드로 전달
         sendRequest( url, param, resultDel, "GET" );
      }
      // 삭제 콜백메서드를 생성
      function resultDel() {
         //서버에서 넘어온 데이터를 받아오기 위한 메서드
         if( xhr.readyState == 4 && xhr.status == 200 ){
            
            //xhr.responseText : 컨트롤러에서 return으로 보내준 결과값
            var data = xhr.responseText;
         
            if( data == 'no' ){
               alert("삭제 실패");
               return;
            }
            alert("삭제성공");
            location.href = "gallery.do?idx=${param.idx}";
         }
      }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      //게시글 수정
      function modify(f){
         f.action = 'gallery_modify_form.do';
         f.method = "GET";
         f.submit();
      }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      // 좋아요 구하기
      function like(f) {
         let gallIdx = f.gallIdx.value;
         let galleryContentRef = f.galleryContentRef.value;
         
         let url = "gallery_like.do";
         let param = "gallIdx=" + gallIdx +
                  "&galleryContentRef=" + galleryContentRef;
         sendRequest(url, param, resultLike, "GET");
      }
      // 좋아요 콜백메소드 생성
      function resultLike() {
         if( xhr.readyState == 4 && xhr.status == 200 ){
            //xhr.responseText : 컨트롤러에서 return으로 보내준 결과값
            let data = xhr.responseText;
            // 콜백으로 받아서 페이지를 갱신하지 않고 바로 바꿀수 있는 방법 구상중
            // 현제는 별다른 방법이 없어서 갱신하는 방법 이용중
            location.href = "gallery.do?idx=${param.idx}";
         }
      }
   </script>
   
   <script>
///////////////////////////////////////////////////////////////////////////////////////
      function reply(f) {
          let galleryCommentContent = f.galleryCommentContent.value;
          let galleryCommentIdx = f.galleryCommentIdx.value;
          let galleryCommentRef = f.galleryCommentRef.value;
          let galleryCommentName = f.galleryCommentName.value;
         
          //유효성 체크
         if( galleryCommentContent == ""){
            alert("댓글을 작성해주세요.");
            return;
         }
         
          url = "comment_insert.do";
          param = "galleryCommentIdx=" + galleryCommentIdx
                + "&galleryCommentRef=" + galleryCommentRef
                + "&galleryCommentContent=" + galleryCommentContent
                + "&galleryCommentName=" + galleryCommentName;
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
            location.href = "gallery.do?idx=${param.idx}";
         }
   }
      /////댓글삭제/////
      function gcdel(f){
         if( !confirm('정말 삭제하시겠습니까?') ){
         return;
         }
         
         
         
         //idx를 Ajax를 통해서 서버로 전달
         var url = "gcomment_delete.do";
         var param = "galleryCommentRef=" + f.galleryCommentRef.value
                  +"&galleryCommentIdx=" + f.galleryCommentIdx.value
                  +"&galleryNum=" + f.galleryNum.value;
         //준비된 두 개의 정보를 콜백메서드로 전달
         sendRequest( url, param, resultgcDel, "GET" );
      }
         
      function resultgcDel() {
         //서버에서 넘어온 데이터를 받아오기 위한 메서드
         if( xhr.readyState == 4 && xhr.status == 200 ){
            
            //xhr.responseText : 컨트롤러에서 return으로 보내준 결과값
            var data = xhr.responseText;
         
            if( data == 'no' ){
               alert("삭제 실패");
               return;
            }
            alert("삭제성공");
            location.href = "gallery.do?idx=${param.idx}";
         }
   }
   </script>

	<script>
    // 사진첩 목록 배경색 랜덤
    const colors = ['#83BEF4', '#42D3FB', '#00E6E9',  '#5BF3C3', '#AAFA94', '#F9F871', '#EFA2C2', '#FFAFC8','#B495FF','#E4F7D2','#FDD785','#DFF980','#F9AA80'];

    const LENGTH = colors.length;

    // setInterval(callback, delay); 지연시간동안 callback을 호출   
    const timer = setInterval(randomColor,3000);

    function randomColor(){
        let num1 = Math.floor(Math.random()*LENGTH);
        let num2 = Math.floor(Math.random()*LENGTH);
        let num3 = Math.floor(Math.random()*LENGTH);
        let num4 = Math.floor(Math.random()*LENGTH);
        let num5 = Math.floor(Math.random()*LENGTH);
        //document.body.style.backgroundColor = colors[num];
        document.getElementById('writing').style.background  = "linear-gradient(45deg,"+colors[num1]+"," + colors[num2]+"," + colors[num3] + ","+ colors[num4] + "," + colors[num5] + ")";
    }

	randomColor();
	//맨 처음부터 배경색 지정
	</script>



    

</body>
</html>