<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>다이어리 새로 작성</title>
<link rel="stylesheet" href="/cyworld/resources/css/diary_insert.css">
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	<div class="container ">
		<section class="section">
			<div class="dashed-line">
				<div class="gray-background">
					<div class="main">
						<form method="post" enctype="multipart/form-data">
							<input name="diaryIdx" type="hidden" value="${ param.idx }">
							<table>
								<caption>새 글 쓰기</caption>
								
								<tr>
									<th>내용</th>
									<td><textarea rows="5" cols="50" name="diaryContent"></textarea></td>
								</tr>
								
								<tr>
									<td>
										<input id="btn-cover" class="write" type="button" value="글쓰기" onclick="send(this.form);">
										<input id="btn-cover" class="cancel" type="button" value="취소" onclick="location.href='diary.do?idx=${param.idx}'">
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
		</section>
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>	
	<script>
		// 다이어리 글 작성
		function send(f) {
			let diaryContent = f.diaryContent.value;
			
			//유효성 체크
			
			if ( diaryContent == '' ) {
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