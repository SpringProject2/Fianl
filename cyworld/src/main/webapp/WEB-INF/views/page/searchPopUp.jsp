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
				<!-- 검색 유형 -->
				<select class="searchList" name="searchType">
					<option value="name">이름</option>
					<option value="id">아이디</option>
				</select>
				<!-- 검색어 작성 -->
				<input  class="searchtext" type="text" name="searchValue"></input>
				<!-- 검색 버튼 -->
				<input id="btn-cover" class="search" type="button" value="검색" onclick="search(this.form);"></input>
			</div>
		</form>
		
		<div class="memberBox">
			<div class="memberInfo">
				<!-- 이름 검색 결과 -->
				<c:if test="${ searchType eq 'name' }">
					<c:forEach var="name" items="${ list }">
						<div class="wantSearch">
							<figure>
								<!-- 검색된 유저의 미니미 -->
								<input class="memberMinimi" type="image" src="/cyworld/resources/minimi/${ name.minimi }" value="${ name.name }" onclick="opener.location.href='main.do?idx=${ name.idx }'; searchClick();">
								<!-- 검색된 유저의 이메일 -->
								<a class="myname" onclick="opener.location.href='main.do?idx=${ name.idx }'; searchClick();">${ name.name } </a>  <span class="info1">Email: ${ name.email }</span>
								<!-- 검색된 유저의 플랫폼 -->
								<span class="platform">Platform: ${ name.platform }</span>
							</figure>
						</div>
					</c:forEach>
				</c:if>
				<!-- 아이디 검색 결과 -->
				<c:if test="${ searchType eq 'id' }">
					<c:forEach var="id" items="${ list }">
						<div class="wantSearch">
							<figure>
								<!-- 검색된 유저의 플랫폼이 싸이월드일 경우 -->
								<c:if test="${ id.platform eq 'cyworld' }">
									<!-- 검색된 유저의 미니미 -->
									<input class="memberMinimi" type="image" src="/cyworld/resources/minimi/${ id.minimi }" value="${ id.userID }"  onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">
									<!-- 검색된 유저의 아이디 - 싸이월드 가입자는 아이디가 존재하기에 아이디로 작성 -->
									<a class="myname" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">${ id.name }</a><span class="info1">ID: ${ id.userID }</span>
									<!-- 검색된 유저의 플랫폼 -->
									<span class="platform">Platform: ${ id.platform }</span>
								</c:if>
								<!-- 검색된 유저의 플랫폼이 소셜일 경우 -->
								<c:if test="${ id.platform ne 'cyworld' }">
									<!-- 검색된 유저의 미니미 -->
									<input class="memberMinimi" type="image" src="/cyworld/resources/minimi/${ id.minimi }" value="socialUser"  onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">
									<!-- 검색된 유저의 이메일 - 소셜 가입자는 아이디가 존재하지 않기에 이메일로 대체하여 작성 -->
									<a class="myname" onclick="opener.location.href='main.do?idx=${ id.idx }'; searchClick();">${ id.name }</a><span class="info1">Email: ${ id.email }</span>
									<!-- 검색된 유저의 플랫폼 -->
									<span class="platform">Platform: ${ id.platform }</span>
								</c:if>
							</figure>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</div>
	</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<script>
		// 유저 검색
		function search(f) {
			f.action = "main_search.do";
			f.method = "GET";
			f.submit();
		}
		
		// 팝업창 닫기
		function searchClick() {
			return window.close();
		}
	</script>
</body>
</html>   