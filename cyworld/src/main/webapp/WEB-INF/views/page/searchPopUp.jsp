<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form>
		<select class="searchList" name="searchType">
			<option value="name">이름</option>
			<option value="id">아이디</option>
		</select>
		<input  class="searchtext" type="text" name="searchValue"></input>
		<input class="search" type="button" value="검색" onclick="search(this.form);"></input>
	</form>
	<c:if test="${ searchType eq 'name' }">
		<c:forEach var="name" items="${ list }">
			<input type="button" value="${ name.name }" style="width:100px; height:100px;" onclick="opener.location.href='main.do?idx=${ name.idx }'; searchClick();">&nbsp;@&nbsp;<span>${ name.platform }</span> <br>
			<a style="font-size:50px;" onclick="opener.location.href='main.do?idx=${ name.idx }'; searchClick();"><img src="/cyworld/resources/minimi/${ name.minimi }" width="200"/>${ name.name }</a>&nbsp;@&nbsp;<span>${ name.platform }</span> <br>
		</c:forEach>
	</c:if>
	<c:if test="${ searchType eq 'id' }">
		<c:forEach var="id" items="${ list }">
			<c:if test="${ id.platform eq 'cyworld' }">
				<input type="button" value="${ id.userID }" style="width:100px; height:100px;" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">&nbsp;@&nbsp;<span>${ id.platform }</span> <br>
				<a style="font-size:50px;" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">${ id.userID }</a>&nbsp;@&nbsp;<span>${ id.platform }</span> <br>
			</c:if>
			<c:if test="${ id.platform ne 'cyworld' }">
				<input type="button" value="${ id.email }" style="width:100px; height:100px;" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">&nbsp;@&nbsp;<span>${ id.platform }</span> <br>
				<a style="font-size:50px;" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">${ id.email }</a>&nbsp;@&nbsp;<span>${ id.platform }</span> <br>
			</c:if>
		</c:forEach>
	</c:if>
	
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