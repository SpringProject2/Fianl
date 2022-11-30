<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/popUp.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
 <div class="container" id="scrollBar">
        <h2 class="title">미니미 설정창</h2>

        <div class="myMinimiBox">
            <p class="subTitle">-나의 미니미-</p>
            <div class="myMinimi">
                <img src="/cyworld/resources/minimi/${ minimi }" alt="">
            </div>
        </div>

        <div class="myMinimiListBox" id="scrollBar">
            <p class="subTitle">-보유 미니미-</p>
            <div class="myMinimiList">
                <!-- 보유 미니미 => 나의 미니미 변경 -->
                <!-- 예시로 3개만 넣고 액션 미니미 구매시 보유미니미 개수 갱신되어야 함  -->
                <form action="">
                    <div class="tabs">
                    	<input name="idx" type="hidden" value="${ param.idx }">
                        <input id="tab1" class="btn" name="minimi" type="checkbox" value="Spongebob.gif"></input>
                        <input id="tab2" class="btn" name="minimi" type="checkbox" value="stitch.gif"></input>
                        <input id="tab3" class="btn" name="minimi" type="checkbox" value="Crayon3.gif"></input>
                    
                        <div class="tab-btns">
                            <label for="tab1" id="btn1" ><div class="list"><img src="resources/images/Spongebob.gif" alt=""></div></label>
                            <label for="tab2" id="btn2" ><div class="list"><img name="stitch.gif" src="resources/images/stitch.gif" alt=""></div></label>
                            <label for="tab3" id="btn3" ><div class="list"><img name="Crayon3.gif" src="resources/images/Crayon3.gif" alt=""></div></label>
                        </div>
                        <input id="btn-cover" class="change" type="button" value="변경" onclick="changeMinimi(this.form);">
                    </div>
                    
                </form>
              
            </div>
        </div>
       <div class="buyMinimi" id="scrollBar">
           <p class="subTitle" id="lastTitle">※ 구매 가능</p>
           <p class="subTitle" >-액션 미니미-</p>
           <!-- 액션 미니미 구매 
           구매하면 내가 구매한 미니미가 보유 미니미로 가고 
           리스트 갱신
         -->
           <form action="">
            <div class="tabs">
                <input class="btn" type="checkbox" id="tab4"></input>
                <input class="btn" type="checkbox" id="tab5"></input>
                <input class="btn" type="checkbox" id="tab6"></input>
            
                <div class="tab-btns">
                    <label for="tab4" id="btn4" ><div class="list"><img name="cat.gif" src="resources/images/cat.gif" alt=""></div></label>
                    <label for="tab5" id="btn5" ><div class="list"><img name="thePooh.gif" src="resources/images/thePooh.gif" alt=""></div></label>
                    <label for="tab6" id="btn6" ><div class="list"><img name="fat.gif" src="resources/images/fat.gif" alt=""></div></label>
                </div>
                <input id="btn-cover" class="change" type="button" value="구매" onclick="purchaseMinimi(this.form);">
            </div>
            
        </form>
       </div>
    </div>
    
	<script>
		// 미니미 변경
		function changeMinimi(f) {
			let minimi = f.minimi;
			
			if ( minimi[0].checked == true ) {
				f.action = "profile_minimi_change.do";
				f.method = "GET";
				f.submit();
				return;
			} else if ( minimi[1].checked == true ) {
				f.action = "profile_minimi_change.do";
				f.method = "GET";
				f.submit();
				return;
			} else if ( minimi[2].checked == true ) {
				f.action = "profile_minimi_change.do";
				f.method = "GET";
				f.submit();
				return;
			}
		}
		
		function purchaseMinimi() {
			
		}
	</script>
</body>
</html>