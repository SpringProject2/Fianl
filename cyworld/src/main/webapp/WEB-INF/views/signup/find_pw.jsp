<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link rel="stylesheet" href="/cyworld/resources/css/findpw.css">
</head>
<body>
<div class="container ">
        <section class="section">
                <div class="dashed-line">
                    <div class="gray-background">
                        <div class="main">
                            <img class="logo-main box animate__animated animate__rubberBand animate__" src="resources/images/logo_cyworld.png" alt="">
                            <form>
                                <p class="myName">이름<br> <input name="name" type="text"> </p>
                                <p class="myId">ID<br> <input id="userID" name="userID" type="text"></p>
                                <p class="myEmail">이메일<br> <input name="email" type="text"></p>
                                <input id="btn-cover" type="button" value="비밀번호 찾기" onclick="findPW(this.form)">
                            </form>

                        </div>
                    </div>
                </div>
        </section>
</div>
	
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
				
				alert("메일로 임시비밀번호 발급이 완료되었습니다");
				location.href = "login.do";
			}
		}
	</script>
</body>
</html>