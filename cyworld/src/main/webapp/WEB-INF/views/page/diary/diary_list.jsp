<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>다이어리</title>
</head>
<body>
	<div id="main_box">
		<h1>다이어리 목록</h1>
	</div>
	
	<c:forEach var="vo" items="${ list }">
	
		<div class="diary_box">
		
			<form>
				<div class="type_diarycontent">
					<input name="diaryIdx" type="hidden" value="${ param.idx }">
					<input type="hidden" value="${ sessionIdx }">
					<input name="diaryContentRef" type="text" value="${ vo.diaryContentRef }" readonly>
					<br>
					<label>작성일자 : ${ vo.diaryRegdate }</label>
					<br>
					<pre>${ vo.diaryContent }</pre><br>
					<c:if test="${ sessionIdx eq param.idx }">
						<input type="button" value="수정" onclick="modify(this.form);">
						<input type="button" value="삭제" onclick="del(this.form);">
					</c:if>
				</div>
			</form>
		</div>
	</c:forEach>
	
	<c:if test="${ sessionIdx eq param.idx }">
		<div align="right">
			<input type="button" value="글쓰기" onclick="location.href='diary_insert_form.do?idx=${param.idx}'">
		</div>
	</c:if>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
    <script>
    	// 게시글 삭제
		function del(f){
    		
			if( !confirm('정말 삭제하시겠습니까?') ){
				return;
			}
			
			var url = "diary_delete.do";
			var param = "diaryIdx=" + f.diaryIdx.value + 
						"&diaryContentRef=" + f.diaryContentRef.value;
			
			sendRequest( url, param, resultFn, "GET" );
		}
		
		//콜백메서드를 생성
		function resultFn(){
			if( xhr.readyState == 4 && xhr.status == 200 ){
			
				var data = xhr.responseText;
				
				if( data == 'no' ){
					alert("삭제 실패");
					return;
				}
				
				alert("삭제성공");
				location.href="diary.do?idx=${param.idx}";
			}
		}
		
		// 게시글 수정
		function modify(f){
			f.action = 'diary_modify_form.do';
			f.method = "GET";
			f.submit();
		}
	</script>
	
</body>
</html>