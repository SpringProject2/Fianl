<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사진첩</title>
<link rel="stylesheet" href="/cyworld/resources/css/gallery.css">
</head>
<body>

	<div id="main_box">
	
		<h1>사진첩 목록</h1>
		
	</div>
	
	
	<c:forEach var="vo" items="${ list }">
	
		<div class="gallery_box">
		
			<form>
		
				<div class="type_galleryFile">
					<!-- 첨부된 이미지가 있는 경우에만 img태그를 보여주자! -->
					<c:if test="${ vo.galleryFileName ne 'no_file' }">
						<c:if test="${ vo.galleryFileExtension eq 'image' }">
							<img src="/cyworld/resources/upload/${ vo.galleryFileName }" width="200"/>
						</c:if>
						<c:if test="${ vo.galleryFileExtension eq 'video' }">
							<video autoplay controls loop muted src="/cyworld/resources/upload/${ vo.galleryFileName }" width="200"/>
							<!-- video태그 autoplay : 자동 재생 / controls loop : 반복 재생 / muted : 음소거 -->
						</c:if>
					</c:if>
				</div>
			
				<div class="type_gallerycontent">
				<pre>${ vo.galleryContent }</pre> <br>
				작성일자 : ${ vo.galleryRegdate }<br>
				</div>
			
				<!--  <div class="type_galleryLike"> 좋아요 : ${ vo.galleryLike }</div>
			
				<div class="type_galleryComment"> 
				댓글 작성자 : ${ vo.galleryCommentName } <br>
				댓글 작성일자 : ${ vo.galleryCommentRegdate } <br>
				댓글 내용 : ${ vo.galleryCommentContent }
				</div>
				-->
			
				<input type="text" name="idx" value="${ idx }">
				<input type="text" name="galleryContentRef" value="${ vo.galleryContentRef }">
				<input type="button" value="수정" onclick="modify(this.form);">
				<input type="button" value="삭제" onclick="del(this.form);">
			</form>
			
		</div>
		
	</c:forEach>
	
	<div align="right">
		<input type="button" value="글쓰기" onclick="location.href='insert_form.do?idx=${idx}'">
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function del(f){
			
			if( !confirm('정말 삭제하시겠습니까?') ){
				return;
			}
			
			//idx를 Ajax를 통해서 서버로 전달
			var url = "delete_gallery.do";
			var param = "idx=" + ${idx} + 
						"&galleryContentRef=" + f.galleryContentRef.value;
			//준비된 두 개의 정보를 콜백메서드로 전달
			sendRequest( url, param, resultDel, "GET" );
		}
		
		//콜백메서드를 생성
		function resultDel(){
			//서버에서 넘어온 데이터를 받아오기 위한 메서드
			if( xhr.readyState == 4 && xhr.status == 200 ){
				
				//xhr.responseText : 컨트롤러에서 return으로 보내준 결과값
				var data = xhr.responseText;
			
				if( data == 'no' ){
					alert("삭제 실패");
					return;
				}
				alert("삭제성공");
				location.href="gallery.do?idx=${idx}";
			}
		}
		
		//게시글 수정
		function modify(f){
			f.action = 'modify_form.do';
			f.method = "get";
			f.submit();
		}
	</script>
	<!-- 파일 수정 가져오기 -->
	<script>
		document.addEventListener("DOMContentLoaded", function(){

	 	// Handler when the DOM is fully loaded

		});

	</script>
</body>
</html>