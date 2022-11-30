<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록 수정</title>
</head>
<body>
	<form method="post" enctype="multipart/form-data">
		<input type="text" name="guestIdx" value="${ vo.guestIdx }">
		<input type="text" name="guestbookContentRef" value="${ vo.guestbookContentRef }">
		<input name="guestbookSession" type="text" value="${ vo.guestbookSession }">
		
		<table border="1" align="center">
		
			<caption>:::방명록 수정:::</caption>
			
			<tr>
				<th>내용</th>
				<td><pre><textarea rows="5" cols="50" name="guestbookContent">${vo.guestbookContent}</textarea></pre></td>
			</tr>
			
			<tr>
				<th>작성자</th>
				<td><input type="text" name="guestbookContentName" value="${vo.guestbookContentName}" readonly></td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="수정" onclick="send(this.form);">
					<input type="button" value="취소" onclick="location.href='guestbook.do?idx=${vo.guestIdx}'">
					<select id="guestscret" name="guestbookSecretCheck" onchange="selectbox">
						<c:if test="${ vo.guestbookSecretCheck eq 0 }">
							<option value="0" selected>전체 공개</option>
							<option value="1">비밀글</option> 
						</c:if>
						<c:if test="${ vo.guestbookSecretCheck eq 1 }">
							<option value="0">전체 공개</option>
							<option value="1" selected>비밀글</option>
						</c:if>
                    </select>
				</td>
			</tr>
			
		</table>
	</form>
	
	<!-- Ajax활용을 위한 js파일 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function send(f){
			
			var url = "guestbook_modify.do";
			var param = "guestIdx=" + f.guestIdx.value +
						"&guestbookContentRef=" + f.guestbookContentRef.value +
						"&guestbookContent=" + encodeURIComponent(f.guestbookContent.value) +
						"&guestbookContentName=" + encodeURIComponent(f.guestbookContentName.value) +
						"&guestbookSecretCheck=" + f.guestbookSecretCheck.value +
						"&guestbookSession=" + f.guestbookSession.value;
			sendRequest( url, param, sendCallback, "POST");
		}
		
		function sendCallback(){
			
			if( xhr.readyState == 4 && xhr.status == 200 ){
				
				//"{'result':'no'}"
				var data = xhr.responseText;
				
				//문자열 구조로 넘어온 data를 실제 JSON타입으로 변경
				var json = (new Function('return'+data))();
				
				if( json.result == 'no' ){
					alert("수정실패");
					return;
				}
				
				alert("수정성공");
				location.href="guestbook.do?idx=${vo.guestIdx}";
				
			}
			
		}
		
		
	</script>
</body>
</html>