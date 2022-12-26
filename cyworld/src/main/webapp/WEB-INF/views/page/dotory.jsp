<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도토리 충전소</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/dotoryPopUp.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	<div class="container" id="scrollBar">
		<h2 class="title" >도토리 충전소</h2>
		<div class="buyDotory">
			<p class="subTitle" >충전하실 도토리 개수를 선택해 주세요</p>
			<div class="selectDotory">
				<form action="">
					<div class="tabs">
						<input type="hidden" value="${ param.idx }" name="idx">
						<p class="myDotory"><img class="dotory" src="resources/images/dotory.png" alt="">도토리 보유량 : ${param.dotoryNum} 개</p>
						<input type="hidden" name="nowDotory" value="${param.dotoryNum}">
						<!-- 도토리 개수 선택 -->
						<input class="btn" type="checkbox" id="tab1" name="dotoryNum" value=10  onclick="NoMultiChk(this)">
						<input class="btn" type="checkbox" id="tab2" name="dotoryNum" value=30  onclick="NoMultiChk(this)">
						<input class="btn" type="checkbox" id="tab3" name="dotoryNum" value=50  onclick="NoMultiChk(this)">
						<input class="btn" type="checkbox" id="tab4" name="dotoryNum" value=100  onclick="NoMultiChk(this)">
					
						<div class="tab-btns">
							<label for="tab1" id="btn1" class="active"><div class="list"><img src="resources/images/dotory10.png" alt=""><p class="price price1">10개 : 1000원</p></div></label>
							<label for="tab2" id="btn2" ><div class="list" ><img src="resources/images/dotory30.png" alt=""><p class="price price2">30개 : 3000원</p></div></label>
							<label for="tab3" id="btn3" ><div class="list"><img src="resources/images/dotory50.png" alt=""><p class="price price3">50개 : 5000원</p></div></label>            
							<label for="tab4" id="btn4" ><div class="list"><img src="resources/images/dotory100.png" alt=""><p class="price price4">100개 : 10000원</p></div></label>            
						</div>
						
						<div id="buyButton">
							<input id="btn-cover" class="buy" type="submit" value="충전하기" onclick="buy(this.form);">
							<input id="btn-cover" class="close "type="button" value="구매완료" onclick="opener.location.href='main.do?idx=${ param.idx }'; buyclose();">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<!-- Ajax 사용을 위한 js를 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		// 도토리 구매
		function buy(f) {
			let dotoryNum = f.dotoryNum;
			
			// 선택한 도토리 구매
			if (dotoryNum[0].checked == true) {
				f.action = "dotoryBuy.do";
				f.method = "GET";
				f.submit();
			} else if (dotoryNum[1].checked == true) {
				f.action = "dotoryBuy.do";
				f.method = "GET";
				f.submit();
			} else if (dotoryNum[2].checked == true) {
				f.action = "dotoryBuy.do";
				f.method = "GET";
				f.submit();
			} else if (dotoryNum[3].checked == true) {
				f.action = "dotoryBuy.do";
				f.method = "GET";
				f.submit();
			}
				alert("구매완료");
		}
		
		// 팝업창 닫기
		function buyclose() {
			return window.close();
		}
	</script>
	
	<!-- checkbox 중복 체크 방지 -->
	<script>
		function NoMultiChk(chk) {
			let obj = document.getElementsByName("dotoryNum");
			for (let i=0; i < obj.length; i++) {
				if (obj[i] != chk) {
					obj[i].checked = false;
				}
			}
		}
	</script>
	
	<!-- setInterval -->
	<script>
		const list = document.querySelectorAll('.tab-btns>label');
		
		list.forEach (function(label, index) {
			label.addEventListener('onclick',labelHandler);
		});
		
		let currentList;
		
		function labelHandler(event) {
			if (currentList) {
				currentList.classList.remove('active');
			}
			
			let target = event.target;
			target.classList.add('active');
			currentList = target;
		}
		
		let count = 0; 
		
		function labelHandler(event) {
			if (currentList) {
				currentList.classList.remove('active');
			}
			
			let target = list[count];
			target.classList.add('active');
			currentList = target;
			count++;
			
			if (count >= list.length) {
				count = 0;
			}
		}
		
		labelHandler();
		setInterval(labelHandler, 3000);
	</script>
</body>
</html>