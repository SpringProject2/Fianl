<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link rel="stylesheet" href="/cyworld/resources/css/yourProfile.css">
    <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
 <div class="container">
                <section class="left-section">
                        <div class="left-dashed-line">
                            <div class="left-gray-background">
                                <p class="todayBanner"><span>Today</span> <span class="todayHere">156</span><span>&nbsp;｜ Total</span> 45,405</p>
                                <aside class="left-aside">
                                    <div class="item item1"></div>
                                    <div class="item item1"></div>
                                    <div class="item item2"></div>
                                    <div class="item item2"></div>
                                    <div class="todayIcon">
                                        <span class="todayIconText">Today is..</span><img class="box animate__animated animate__headShake animate__infinite " src="resources/images/emoticon1.png" alt="">
                                    </div>
                                    <div class="left-image"><img class="leftImg" src="resources/images/left_profile.png" alt=""></div>
                                    <textarea class="left-textarea">어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구어쩌구저쩌구</textarea>
                                    <div class="history"><img src="resources/images/arrow.png" alt=""><h3>History</h3></div>
                                    <select class="myFriend">
                                        <option value="">::: 파도타기 :::</option>
                                            <option><a href="#">이정현 ｜  친구</a></option>
                                            <option><a href="#">박성철 ｜  친구</a></option>
                                            <option><a href="#">장유진 ｜  친구</a></option>
                                            <option><a href="#">황유진 ｜  친구</a></option>
                                            <option><a href="#">장현중 ｜  친구</a></option>
                                    </select>
                                </aside>
                            </div>
                        </div>
                </section>
               
                <section class="right-section">
                        <div class="right-dashed-line">
                            <div class="right-gray-background">
                                <p class="title"><a href="#">Test 싸이월드 Title입니다. 누르면 무슨 기능이였더라?</a></p>
                                <!-- a태그 = 새로고침  -->
                                <!-- <p class="titleLink"><a href="#">http://www.zenghyun.com</a></p> -->
                                <aside id="right-aside"
                                class="scrollBar">
                                    <div class="miniRoomBox"><p>Mini Room</p>
                                   <div class="miniRoom"><img class="miniRoomImg" src="resources/images/mainroom.png" alt="">
                                    <div class="hover">
                                        <div class="show">
                                             <img src="resources/images/sorryForShow.gif" alt="">
                                        <p class="sorryText">
                                            아직 개발중에 있습니다. <span>※개발진 일동</span>
                                        </p>
                                        </div>
                                    </div>
                                    <div class="Crayon"><img src="resources/images/Crayon.png" alt=""></div></div>
                                </div>
                                <!-- 미니미 수정 form -->
                                <form action="">
                                <input class="check_btn" id="btn-cover" type="button" value="미니미수정" onclick=toggle()></input>
                                
                                
                                <div id="minimi_correction"
                                class="scrollBar">
                                  <div class="minimi-list"
                                  >
                                    <div class="minimi-area">
                                        <input id="btn-cover" class="minimi-select"type="submit" value="수정">
                                        <input class="minimi-choice" type="radio">
                                        <img src="resources/images/Crayon2 .png" alt=""></div>
                                    </div>
                                  </div>
                                </form>

                                <!-- 개인정보 수정 form -->
                                <form action="">
                                <div class="modify-user-profile">
                                    <h2>::개인정보 수정::</h2>
                                    <p id="my-minimi">My minimi</p>
                                  <input class="minimi-main" type="button" onclick= popUp()>

                                    <p>ID : <input type="text" value="${ vo.userID }"></p>
                                    <p>PW : <input type="text" value="${ vo.info }"></p>
                                    <p>PW 확인 : <input type="text"></p>
                                    <p>이름 : <input type="text" value="${ vo.name }"></p>
                                    <p>주민번호 : <input type="text" value="${ vo.identityNum }"></p>
                                    <p>성별:&nbsp; <input class="myRadio" type="radio" name="gender" value="${ vo.gender }">&nbsp;남 <input  class="myRadio" type="radio" name="gender" value="${ vo.gender }">&nbsp;여 </p>
                                    <p>이메일 : <input type="text" value="${ vo.email }"></p>
                                    <p>전화번호 : <input type="tel" value="${ vo.phoneNumber }"></p>
                                    <input class="final-button" id="btn-cover" type="button" value="수정">
                                </div>
                            </form> 

                                </aside>
                            </div>
                            </div>

                        <div class="tabs">
                                <input type="checkbox" id="tab1" ></input>
                                <input type="checkbox" id="tab2"checked ></input>
                                <input type="checkbox" id="tab3"></input>
                                <input type="checkbox" id="tab4"></input>
                                <input type="checkbox" id="tab5"></input>
                                <div class="tab-btns">
                                    <label for="tab1" id="btn1">홈</label>
                                    <label for="tab2" id="btn2">프로필</label>
                                    <label for="tab3" id="btn3">다이어리</label>
                                    <label for="tab4" id="btn4">사진첩</label>
                                    <label for="tab5" id="btn5">방명록</label>
                                </div>
                        </div>
                </section>
    </div>
    <script>
      
        //window.open (미니미 수정창)
        function popUp() {
            let popUrl = "profile_minimi_popup.do";
            let popOption = "top=100, left=800, width=600, height=800, status=no, menubar=no, toolbar=no, resizable=no";
    window.open(popUrl, "minimi", popOption);
        }
    </script>

    <script>
        // toggle
        function toggle(){
            const minimi_correction = document.getElementById('minimi_correction');

            if(minimi_correction.style.display !== 'none'){
                minimi_correction.style.display ='none';
            }
            else {
                minimi_correction.style.display ='block';
            }
        }
    </script>
</body>
</html>