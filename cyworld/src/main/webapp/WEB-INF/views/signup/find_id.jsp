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
		<label>휴대전화 : </label><input id="phoneNumber" name="phoneNumber" type="text" placeholder="휴대폰 번호를 입력해주세요" maxlength="13"> <br>
		<input type="button" value="아이디 찾기" onclick="findID(this.form)">
	</form>
	
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/bbs/resources/js/httpRequest.js"></script>
	<script>
		// ID 중복 확인
		function findID(f) {
			let name = f.name.value;
			let phoneNumber = f.phoneNumber.value;
			
			// ID 중복 확인을 위한 URL, ID 입력값
			let url = "findIdCheck.do";
			let param = "name=" + name + "&phoneNumber=" + phoneNumber;
			sendRequest(url, param, resultFn, "POST");
		}
		// 콜백메소드
		function resultFn() {
			if ( xhr.readyState == 4 && xhr.status == 200 ) {
				let data = xhr.responseText;
				
				if ( data == "no" ) {
					alert("아이디를 찾지 못하였습니다");
					return;
				}
				
				alert("아이디는 '" + data + "' 입니다");
			}
		}
	</script>
	<script>
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
	</script>
</body>
</html>