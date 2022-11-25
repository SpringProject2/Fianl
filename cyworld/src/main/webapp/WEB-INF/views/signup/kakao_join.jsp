<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link rel="stylesheet" href="/cyworld/resources/css/kakaoJoin.css">
</head>
<body>
<div class="container ">
        <section class="section">
                <div class="dashed-line">
                    <div class="gray-background">
                        <div class="main">
                            <img class="logo-main box animate__animated animate__rubberBand animate__" src="resources/images/logo_cyworld.png" alt="">
                            <form>
                                <input name="platform" type="hidden" value="${ vo.platform }">
                                <input name="gender" type="hidden" value="${ vo.gender }">
                                <input name="email" type="hidden" value="${ vo.email }">
                                <p class="name">이름<br>  <input name="name" type="text"> </p>
                                <p class="rNum">주민번호<br> <input name="identityNum" id="identityNum" type="text" placeholder="주민번호 13자리를 입력해주세요" maxlength="14"> </p>
                                <p class="phone">휴대전화<br><input id="phoneNumber" name="phoneNumber" type="text" placeholder="휴대폰 번호를 입력해주세요" maxlength="13"> </p>
                                <p class="address">
                                       주소<br><input id="addressText" class="address_kakao" name="address" type="text">
                                    <input id="btn-cover" class="address_kakao" type="button" value="주소찾기">
                                </p> 
                                <p class="rAddress">상세 주소<br> <input class="address_kakao" name="addressDetail" type="text"> </p>
                                <input class="join" id="btn-cover" type="button" value="가입" onclick="join(this.form)">
                            </form>
                    </div>
                </div>
                </div>
        </section>
</div>
	
	<!-- 다음 주소 api -->
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script>
		window.onload = function(){
			document.getElementsByClassName("address_kakao")[0].addEventListener("click", function(){ //주소 입력칸을 클릭하면
				new daum.Postcode({
					oncomplete: function(data) { //선택시 입력값 세팅
						document.getElementsByClassName("address_kakao")[0].value = data.address; // 주소 넣기
						document.getElementsByClassName("address_kakao")[2].focus(); //상세입력 포커싱
						}
				}).open();
			});
			document.getElementsByClassName("address_kakao")[1].addEventListener("click", function(){ //주소 버튼을 클릭
				new daum.Postcode({
					oncomplete: function(data) { //선택시 입력값 세팅
						document.getElementsByClassName("address_kakao")[0].value = data.address; // 주소 넣기
						document.getElementsByClassName("address_kakao")[2].focus(); //상세입력 포커싱
						}
				}).open();
			});
		}
		// 휴대폰용 자동 하이픈
		function phoneAutoHyphen(str){
			str = str.replace(/[^0-9]/g, ''); // 입력값에 숫자만 적용
			let tmp = '';
			if ( str.length < 4 ) { // 입력값이 4자리보다 작을시
				return str;
			} else if ( str.length < 7 ) { // 입력값이 7자리보다 작을시
				tmp += str.substr(0, 3);
				tmp += '-';
				tmp += str.substr(3);
				return tmp;
			} else if ( str.length < 11 ) { // 입력값이 11자리보다 작을시
				tmp += str.substr(0, 3); // 000
				tmp += '-'; // 000-
				tmp += str.substr(3, 3); // 000-000
				tmp += '-'; // 000-000-
				tmp += str.substr(6); // 000-000-0000
				return tmp;
			} else { // 입력값이 11자리일시
				tmp += str.substr(0, 3); // 000
				tmp += '-'; // 000-
				tmp += str.substr(3, 4); // 000-0000
				tmp += '-'; // 000-0000-
				tmp += str.substr(7); // 000-0000-0000
				return tmp;
			}
			return str;
		}
		// 휴대폰 입력값 가져오기
		const phoneNumber = document.getElementById("phoneNumber");
		phoneNumber.onkeyup = function(event) { // 값을 입력시 발동
			event = event || window.event;
			let val = this.value.trim(); // 입력값 가져오기
			this.value = phoneAutoHyphen(val); // 입력값에 자동 하이픈 메소드 적용
		}
		
		// 주민번호용 자동 하이픈
		function identityAutoHyphen(str){
			str = str.replace(/[^0-9]/g, ''); // 입력값에 숫자만 적용
			let tmp = '';
			if ( str.length < 7 ) { // 입력값이 7자리보다 작을시
				return str;
			} else { // 입력값이 13자리일시
				tmp += str.substr(0, 6); // 000000
				tmp += '-'; // 000000-
				tmp += str.substr(6); // 000000-0000000
				return tmp;
			}
			return str;
		}
		// 주민번호 입력값 가져오기
		const identityNum = document.getElementById("identityNum");
		identityNum.onkeyup = function(event) { // 값을 입력시 발동
			event = event || window.event;
			let val = this.value.trim(); // 입력값 가져오기
			this.value = identityAutoHyphen(val); // 입력값에 자동 하이픈 메소드 적용
		}
		
		// 비밀번호가 패턴에 맞는지 확인용
		function pwCheck() {
			let pwText = document.getElementsByClassName("pwText"); // 비밀번호 아래에 글이 작성될 <div>
			let pw = document.getElementById("pw").value; // 비밀번호 값
			
			let pattern1 = /[0-9]/; // 숫자 입력
			let pattern2 = /[a-zA-Z]/; // 영어 소문자, 대문자 입력
			let pattern3 = /[~!@#$%^&*()_+]/; // 특수기호 입력
			
			// 비밀번호가 패턴에 하나라도 맞지 않을때
			//		숫자 입력 안할시	 or	   영어 입력 안할시	    or   특수기호 입력 안할시	   or  8자리 보다 작을시
			if ( !pattern1.test(pw) || !pattern2.test(pw) || !pattern3.test(pw) || pw.length < 8 ) {
				// 비밀번호 입력창에 입력하자마자 바로 아래에 글 작성
				pwText[0].innerHTML = "영문 + 숫자 + 특수기호 8자리 이상으로 구성하여야 합니다";
			} else {
				// 비밀번호 입력창에 입력하자마자 바로 아래에 글 작성
				pwText[0].innerHTML = "";
			}
		}
		
		// 비밀번호와 비밀번호 확인이 일지한지 확인용
		function pw2Check() {
			let pwText = document.getElementsByClassName("pwText"); // 비밀번호 아래에 글이 작성될 <div>
			let pw = document.getElementById("pw").value; // 비밀번호 값
			let pw2 = document.getElementById("pw2").value; // 비밀번호 확인 값
			
			// 비밀번호와 비밀번호 확인이 서로 맞지 않을때
			if ( pw != pw2 ) {
				// 비밀번호 확인창에 입력하자마자 바로 아래에 글 작성
				pwText[1].innerHTML = "비밀번호가 일치하지 않습니다";
			} else {
				// 비밀번호 확인창에 입력하자마자 바로 아래에 글 작성
				pwText[1].innerHTML = "";
			}
		}
		
		// DB저장하러 이동
		function join(f) {
			f.action = "welcome.do";
			f.method = "POST";
			f.submit();
		}
	</script>
</body>
</html>