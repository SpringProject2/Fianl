<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form>
		<label>이름 : </label><input name="name" type="text"> <br>
		<label>ID : </label><input name="userID" type="text">
		<label>이메일 : </label><input name="email" type="text">
		<input type="button" value="비밀번호 찾기" onclick="findPW(this.form)">
	</form>
	
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		// ID 중복 확인
		function findPW(f) {
			let name = f.name.value;
			let userID = f.userID.value;
			let email = f.email.value;
			
			// ID 중복 확인을 위한 URL, ID 입력값
			let url = "findPwCheck.do";
			let param = "name=" + name +
						"&userID=" + userID +
						"&email=" + encodeURIComponent(email);
			sendRequest(url, param, resultFn, "POST");
		}
		// 콜백메소드
		function resultFn() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				let data = xhr.responseText;
				
				if ( data == "no" ) {
					alert("해당 ID의 비밀번호를 찾지 못하였습니다");
					return;
				}
				
				alert("메일로 임시비밀번호의 발급이 완료되었습니다");
			}
		}
	</script>
</body>
</html>