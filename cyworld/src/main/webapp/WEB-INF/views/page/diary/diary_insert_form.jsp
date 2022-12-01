<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>다이어리 새로 작성</title>
</head>
<body>
	<form method="post" enctype="multipart/form-data">
		<input name="diaryIdx" type="hidden" value="${ param.idx }">
		<table border="1" align="center">
			<caption>새 글 쓰기</caption>
			<tr>
				<th>내용</th>
				<td><textarea rows="5" cols="50" name="diaryContent"></textarea></td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="글쓰기" onclick="send(this.form);">
					<input type="button" value="취소" onclick="location.href='diary.do?idx=${param.idx}'">
				</td>
			</tr>
		</table>
	</form>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>	
	<script>
		function send(f){
			let diaryContent = f.diaryContent.value;
			
			//유효성 체크
			
			if( diaryContent == '' ){
				alert("내용 입력은 필수입니다.");
				return;
			}
			
			f.action = "diary_insert.do";
			f.method = "GET";
			f.submit();
		}
	</script>
</body>
</html>