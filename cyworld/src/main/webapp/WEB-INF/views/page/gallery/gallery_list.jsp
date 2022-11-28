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
				<div>${ vo.galleryTitle }</div>
		
				<div class="type_galleryFile">
					<!-- 첨부된 이미지가 있는 경우에만 img 및 viedo태그를 보여주자! -->
					<c:if test="${ vo.galleryFileName ne 'no_file' }">
						<!-- 확장자가 img일때 -->
						<c:if test="${ vo.galleryFileExtension eq 'image' }">
							<img src="/cyworld/resources/upload/${ vo.galleryFileName }" width="200"/>
						</c:if>
						<!-- 확장자가 videl일때 -->
						<c:if test="${ vo.galleryFileExtension eq 'video' }">
							<video autoplay controls loop muted src="/cyworld/resources/upload/${ vo.galleryFileName }" width="200"/>
							<!-- video태그 autoplay : 자동 재생 / controls loop : 반복 재생 / muted : 음소거 -->
						</c:if>
					</c:if>
				</div>
			
				<div class="type_gallerycontent">
					<pre>${ vo.galleryContent }</pre> <br>
					작성일자 : ${ vo.galleryRegdate }<br>
					<span id="galleryLikeNum">${ vo.galleryLikeNum }</span>
					<input type="button" value="좋아요" onclick="like(this.form)">
				</div>
			
				<input type="text" name="gallIdx" value="${ vo.gallIdx }">
				<input type="text" name="galleryContentRef" value="${ vo.galleryContentRef }">
				<input type="button" value="수정" onclick="modify(this.form);">
				<input type="button" value="삭제" onclick="del(this.form);">
			</form>
			
		</div>
		
	</c:forEach>
	
	<div align="right">
		<input type="button" value="글쓰기" onclick="location.href='gallery_insert_form.do?idx=${param.idx}'">
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	
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
				let galleryLikeNum = document.getElementById("galleryLikeNum");
				// 콜백으로 받아서 페이지를 갱신하지 않고 바로 바꿀수 있는 방법 구상중
				// 현제는 별다른 방법이 없어서 갱신하는 방법 이용중
				location.href = "gallery.do?idx=${param.idx}";
			}
		}
	</script>
</body>
</html>