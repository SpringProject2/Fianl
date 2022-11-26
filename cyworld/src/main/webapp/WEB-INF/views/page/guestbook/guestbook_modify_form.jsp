<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
						"&guestbookContentName=" + encodeURIComponent(f.guestbookContentName.value);
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