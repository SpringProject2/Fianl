<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/searchPopUp.css">
 <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
     <div class="container" id="scrollBar">
        
        <form>
                <div class="searchBox">
                    <select class="searchList" name="searchType">
                        <option value="name">이름</option>
                        <option value="id">아이디</option>
                    </select>
                    <input  class="searchtext" type="text" name="searchValue"></input>
                    <input id="btn-cover" class="search" type="button" value="검색" onclick="search(this.form);"></input>
                </div>
            </form>
            <div class="memberBox">
                <div class="memberInfo">
                    
                    <c:if test="${ searchType eq 'name' }">
                        <c:forEach var="name" items="${ list }">
                <div class="wantSearch">
            <figure>
                            <input class="memberMinimi" type="button" value="${ name.name }" onclick="opener.location.href='main.do?idx=${ name.idx }'; searchClick();"><a class="myname" onclick="opener.location.href='main.do?idx=${ name.idx }'; searchClick();">${ name.name } </a>  <span class="info1">Email: ${ name.email }</span>
                            <span class="platform">Platform: ${ name.platform }</span>
            </figure>
                </div>
                        </c:forEach>
            </c:if>
            <c:if test="${ searchType eq 'id' }">
                <c:forEach var="id" items="${ list }">
                <div class="wantSearch">
                <figure>    
                    <c:if test="${ id.platform eq 'cyworld' }">
                        <input class="memberMinimi" type="button" value="${ id.userID }"  onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();"> 
                        <a class="myname" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">${ id.name }</a><span class="info1">ID: ${ id.userID }</span> <span class="platform">Platform: ${ id.platform }</span> 
                    </c:if>
                    <c:if test="${ id.platform ne 'cyworld' }">
                        <input class="memberMinimi" type="button" value="socialUser"  onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();"> 
                        <a class="myname" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">${ id.name }</a><span class="info1">Email: ${ id.email }</span><span class="platform">Platform: ${ id.platform }</span> 
                    </c:if>
                </figure>
                </div>
                </c:forEach>
            </c:if>
        </div>
        </div>
    </div>
   
   <script>
      // 유저 검색
      function search(f) {
         f.action = "main_search.do";
         f.method = "GET";
         f.submit();
      }
      
      function searchClick() {
         return window.close();
      }
   </script>
</body>
</html>