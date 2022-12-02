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
                        <p class="todayBanner"><span>Today</span> <span class="todayHere">156</span><span>&nbsp;｜ Total</span> 45,405</p>
                        <aside class="left-aside">
                            <div class="item item1"></div>
                            <div class="item item1"></div>
                            <div class="item item2"></div>
                            <div class="item item2"></div>
                            <div class="todayIcon">
                                <span class="todayIconText">Today is..</span><img class="box animate__animated animate__headShake animate__infinite" src="resources/images/emoticon1.png" alt="">
                            </div>
                            <div class="left-image"><img class="leftImg" src="/cyworld/resources/mainphoto/${ signVo.mainPhoto }" alt=""></div>
						<textarea class="left-textarea" readonly>${ signVo.mainText }</textarea>
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
                        <p class="title"><a href="main.do?idx=${ signVo.idx }">${ signVo.mainTitle }</a></p>
                  <!-- main 페이지에 재생 플레이어와 노래 제목 표시  -->
  				 	<img class="musicLogo" src="resources/images/noneMain15.gif" alt="">
  				 	<a class="mp3_title" href="#" >
  				 	<div class="circle-container">
        <div class="circle circle1"> 오르트 구름 - 윤하 </div>
    </div></a>
   					<audio class="mp3" controls>
 				  <source src="/cyworld/resources/sound/main.mp3" type="audio/mp3">
 					  </audio>
 				
                        <aside class="right-aside" id="scrollBar">
                        <div class="fake"></div>
                            <div id="main_box">
                                <h1>방명록 </h1>
                                    <input class="textWrite"
                                    id="btn_cover" type="button" value="방명록 작성" onclick="location.href='guestbook_insert_form.do?idx=${signVo.idx}'">
                            </div>
                            
                             <c:forEach var="vo" items="${ list }">
                             
                                 <div class="guestbook_box">
                                     <form>
                                        <!-- 방명록 소개란 -->
                                        <div class="contentIntroduce">
                                             <label for="">게시글 번호:&nbsp; </label><input class="contentRef" type="text" name="guestbookContentRef" value="${ vo.guestbookContentRef }">
                                            <div class="type_guestbookContentName">${ vo.guestbookContentName }</div>
                                            <div class="type_guestbookRegdate"> (${ vo.guestbookRegdate })</div> 
                                             <!-- 좋아요 -->
                                        <div class="likeHeart">${ vo.guestbookLikeNum }</div>
                                        <input id="heart" type="button"  onclick="like(this.form)">
                                        
                                        <input name="guestbookSession" type="hidden" value="${ vo.guestbookSession }">
                                        <input type="hidden" name="guestIdx" value="${ vo.guestIdx }"> 
                                            <!-- 수정삭제 -->
                                            <c:if test="${ sessionIdx eq vo.guestbookSession || sessionIdx eq vo.guestIdx }">
                                                <input id="btn-cover" type="button" value="수정" onclick="modify(this.form);">
                                                <input  id="btn-cover" type="button" value="삭제" onclick="del(this.form);">
                                            </c:if> 
                                        </div>
                                        
                                        
                                        
                                        <div class="GuestContent">
                                            <img class="GuestMinimi"src="/cyworld/resources/minimi/${ vo.guestbookMinimi }" />
                                            
                                            <div class="GuestText">
                                                <!-- 로그인한 사람이 작성자 이거나 방명록 주인일 경우에만 보인다. -->
                                                 <!-- 비밀글 -->
                                                 <c:if test="${ vo.guestbookSecretCheck eq 1 }">
                                                 <div class="flip-box">
															    <div class="flip">
																	<div class="frontdelComment"><img src="resources/images/cry.png" alt="">&nbsp;&nbsp; 비밀글 입니다.</div>
															        <div class="backdelComment" id="backColor">
															   
                                                     <c:if test="${ sessionIdx eq vo.guestbookSession || sessionIdx eq vo.guestIdx }">
                                                         <pre class="type_guestbookContent"> ${ vo.guestbookContent }</pre>
                                                     </c:if>
                                                    
                                                     <input name="guestbookSecretCheck" type="hidden" value="${ vo.guestbookSecretCheck }">
                                                  </div>
												</div>
												</div>
                                                 </c:if>
                                                 
                                                 <!-- 공개글 -->
                                                 <c:if test="${ vo.guestbookSecretCheck eq 0 }">
                                                    <pre class="type_guestbookContent"> ${ vo.guestbookContent }</pre>
                                                </c:if>
                                                <input name="guestbookSecretCheck" type="hidden" value="${ vo.guestbookSecretCheck }">
                                            </div>
                                        </div>
                                    
                                    </form>
                                    
                                </div>
                                
                            </c:forEach>
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
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function del(f){
		
			if( !confirm('정말 삭제하시겠습니까?') ){
				return;
			}
		
			var url = "guestbook_delete.do";
			var param = "guestIdx=" + f.guestIdx.value +
						"&guestbookContentRef=" + f.guestbookContentRef.value;
			sendRequest(url, param, resultFn, "GET");
		}
	
		//콜백메서드를 생성
		function resultFn(){
			if( xhr.readyState == 4 && xhr.status == 200 ){
				//xhr.responseText : 컨트롤러에서 return으로 보내준 결과값
				var data = xhr.responseText;
				if( data == 'no' ){
					alert("삭제 실패");
					return;
				}
				alert("삭제성공");
				location.href="guestbook.do?idx=${param.idx}";
			}
		}
		
  		//게시글 수정
		function modify(f){
		
			f.action = 'guestbook_modify_form.do';
			f.method = "GET";
			f.submit();
		
		}
		
		// 좋아요 구하기
		function like(f) {
			let guestIdx = f.guestIdx.value;
			let guestbookContentRef = f.guestbookContentRef.value;
			
			let url = "guestbook_like.do";
			let param = "guestIdx=" + guestIdx +
						"&guestbookContentRef=" + guestbookContentRef;
			sendRequest(url, param, resultLike, "GET");
		}
		// 좋아요 콜백메소드 생성
		function resultLike() {
			if( xhr.readyState == 4 && xhr.status == 200 ){
				//xhr.responseText : 컨트롤러에서 return으로 보내준 결과값
				let data = xhr.responseText;
				alert(data[1]);
				//location.href = "guestbook.do?idx=${param.idx}"
			}
		}
	</script>
</body>
</html>