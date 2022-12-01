<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사진첩 새글쓰기</title>
</head>
<body>
	<!-- 파일전송시 
	enctype="multipart/form-data"
	method="post"
	속성이 필수적으로 추가되어 있어야 한다 -->
	<form method="post" enctype="multipart/form-data">
		<input name="idx" type="text" value="${ param.idx }">
		<table border="1" align="center">
			<caption>새 글 쓰기</caption>
			
			<tr>
				<th>제목</th>
				<td><input name="galleryTitle"></td>
			</tr>
			
			<tr>
				<th>내용</th>
				<td><textarea rows="5" cols="50" name="galleryContent"></textarea></td>
			</tr>
			
			<tr>
				<th>파일첨부</th>
				<td><input type="file" name="galleryFile"></td>
			</tr>
			
			<tr>
				<td colspan="2" align="center">
					<input type="button" value="글쓰기" onclick="insert(this.form);">
					<input type="button" value="취소" onclick="location.href='gallery.do?idx=${ param.idx }'">
				</td>
			</tr>
		</table>
	</form>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<!-- Ajax활용을 위한 js파일 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function insert(f){
			//encodeURIComponent : 특수문자가 포함되어 있는 경우에 내용을 그대로 서버로 전달하기 위해
			//존재하는 메서드
			
			let galleryContent = f.galleryContent.value;
			let galleryTitle = f.galleryTitle.value;
			
			//유효성 체크
			if ( galleryTitle =='' ) {
				alert("제목은 입력은 필수입니다");
				return;
			}
			if( galleryContent == '' ){
				alert("내용 입력은 필수입니다");
				return;
			}
			
			f.action = "gallery_insert.do";
			f.method = "post";
			f.submit();
		}
	</script>
</body>
</html>