<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록 새로 쓰기</title>

</head>
<body>
	<form method="GET">
		<input name="idx" type="text" value="${ param.idx }">
		<table border="1" align="center">
			<caption>:::방명록 새로 쓰기:::</caption>
			
			<tr>
				<th>미니미</th>
					<td>
						<img src="/cyworld/resources/minimi/${ minimi }" width="100"/>
						<input name="guestbookMinimi" type="hidden" value="${ minimi }">
					</td>
			</tr>
			
			<tr>
				<th>내용</th>
					<td><textarea rows="5" cols="50" name="guestbookContent"></textarea></td>
			</tr>
			
			<tr>
				<th>작성자</th>
					<td><input name="guestbookContentName" value="${ guestbookContentName }" readonly></td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="글쓰기" onclick="send(this.form);">
					<input type="button" value="취소" onclick="location.href='guestbook.do?idx=${param.idx}'">
					<select id="guestscret" name="guestbookSecretCheck" onchange="selectbox">
						<option value="0">전체 공개</option>
						<option value="1">비밀글</option> 
                    </select>
				</td>
			</tr>
		</table>
	</form>
	
	<script>
		function send(f){
			f.action = "guestbook_insert.do";
			f.submit();
		}
	</script>
</body>
</html>