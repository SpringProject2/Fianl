<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록</title>
<link rel="stylesheet" href="/cyworld/resources/css/guestbook.css">
</head>
<body>
	<div id="main_box">
		<h1>방명록 목록</h1>
		
		<div align="center">
			<input type="button" value="방명록 작성" onclick="location.href='guestbook_insert_form.do?idx=${param.idx}'">
		</div> 		
	</div>
	
 	<c:forEach var="vo" items="${ list }"> 
	
 		<div class="guestbook_box">
 			<form>
				<div class="type_guestbookContent"> ${ vo.guestbookContent }</div>
				<div class="type_guestbookContentName">${ vo.guestbookContentName }</div>
				<div class="type_guestbookRegdate">작성일: ${ vo.guestbookRegdate }</div>
				<span>${ vo.guestbookLikeNum }</span>
				<input type="button" value="좋아요" onclick="like(this.form)">
				
				<input type="text" name="guestIdx" value="${ vo.guestIdx }">
				<input type="text" name="guestbookContentRef" value="${ vo.guestbookContentRef }">
				<input type="button" value="수정" onclick="modify(this.form);">
				<input type="button" value="삭제" onclick="del(this.form);">
			</form>
			
		</div>
		
	</c:forEach>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->	
	
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
				location.href = "guestbook.do?idx=${param.idx}"
			}
		}
	</script>
</body>
</html>