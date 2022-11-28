<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/animate.css">
<link rel="stylesheet" href="/cyworld/resources/css/nonMain.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	  <div class="container">
                <section class="left-section">
                        <div class="left-dashed-line">
                            <div class="left-gray-background">
                                 <aside class="left-aside">
                                    <div class="manAni">
                                        <img src="resources/images/noneJoinMan.png" alt="">
                                    </div>
                                   
                                </aside>
                            </div>
                        </div>
                        <div class="leftMinimiPosition">
                            <img class="noneMain1" src="resources/images/noneMain1.gif" alt="">
                            <pre>
                                Copyright 2022. 2조 All rights reserved.
                                Developer: 이정현, 박성철, 황유진, 장유진, 장현중 
                                Reference: Cyworld 
                            </pre>
                            <img class="noneMain6" src="resources/images/noneMain6.gif" alt="">
                            <img class="noneMain12" src="resources/images/noneMain12.gif" alt="">
                            <img class="noneMain14" src="resources/images/noneMain14.gif" alt="">
                            <img class="noneMain16" src="resources/images/noneMain16.gif" alt="">
                            <img class="noneMain18" src="resources/images/noneMain18.gif" alt="">
                            <img class="noneMain17" src="resources/images/noneMain17.gif" alt="">
                        </div>
                </section>
               
                <section class="right-section">
                        <div class="right-dashed-line">
                            <div class="right-gray-background">
                                <form method="post">
                                    <select id="btn-cover" class="searchList" name="searchType">
                                    <option value="name">이름</option>
                                    <option value="id">아이디</option>
                                    <option value="email">이메일</option>
                                    </select>
                                    <input  class="searchText" type="text" name="searchValue"></input>
                                    <input  id="btn-cover" class="search" type="submit" value="검색" onclick="search(this.form);"></input>
                                    <input type="hidden" name="idx" value="${vo.idx }">
                                    
                                    </form>
                                <aside class="right-aside">
                               <pre>
                               <h2>Cyworld의 회원으로 이용해보세요!</h2>
                            <span class="firstText"><span id="hoverText">다이어리</span>, <span id="hoverText">사진첩</span>, <span id="hoverText">방명록</span>, <span id="hoverText">일촌평 작성</span> 등의 서비스 이용이 가능합니다!</span>
                            <span class="secondText">계정이 있다면? <input id="btn-cover" type="button" onclick="goLogin()" value="로그인"></span>
                            <span class="thirdText">cyworld 이용이 처음이라면?<input id="btn-cover" type="button" onclick="memberJoin()" value="회원가입"></span>
                               </pre>
                               <div class="womanAni"><img src="resources/images/noneJoinWoman.png" alt=""></div>
                                </aside>
                            </div>
                            </div>
                        </div>
                       <div class="rightMinimiPosition">
                        <img  class="noneMain9" src="resources/images/noneMain9.gif" alt="">
                        <img  class="noneMain10" src="resources/images/noneMain10.gif" alt="">
                        <img  class="noneMain5" src="resources/images/noneMain5.gif" alt="">
                        <img  class="noneMain4" src="resources/images/noneMain4.gif" alt="">
                        <img  class="noneMain2" src="resources/images/noneMain2.gif" alt="">
                        <img  class="noneMain11" src="resources/images/noneMain11.gif" alt="">
                        <img  class="noneMain7" src="resources/images/noneMain7.gif" alt="">
                        <img  class="noneMain8" src="resources/images/noneMain8.gif" alt="">
                       
                       </div> 
                       
                </section>
    </div>
    <!-- 좌측, 우측 페이지 이어주는 막대 디자인 -->
    <div class="item item1"></div>
    <div class="item item1"></div>
    <div class="item item2"></div>
    <div class="item item2"></div>
    <!-- script -->
    <script>
        function search(){
        }
    </script>
    <script>
        function goLogin(){
        	location.href = "logout.do"
        }
    </script>
    <script>
        function memberJoin(){
        	location.href = "login_authentication.do?platform=cyworld";
        }
    </script>
</body>
</html>