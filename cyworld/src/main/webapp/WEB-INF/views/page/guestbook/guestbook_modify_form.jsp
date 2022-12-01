<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>방명록 수정</title>
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link rel="stylesheet" href="/cyworld/resources/css/guestbook_modify.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">

</head>
<body>
	<div class="container ">
        <section class="section">
                <div class="dashed-line">
                    <div class="gray-background">
                        <div class="main">
                            <form method="post" enctype="multipart/form-data">
                                <input type="hidden" name="guestIdx" value="${ vo.guestIdx }">
                                <input type="hidden" name="guestbookContentRef" value="${ vo.guestbookContentRef }">
                                <input name="guestbookSession" type="hidden" value="${ vo.guestbookSession }">
                                
                                <table >
                                
                                    <caption>:::방명록 수정:::</caption>
                                    
                                    <tr>
                                        <th>미니미</th>
                                            <td>
                                                <img 
                                                class="myImg"
                                                src="/cyworld/resources/minimi/${ minimi }" />
                                                <input name="guestbookMinimi" type="hidden" value="${ minimi }">
                                            </td>
                                    </tr>
                                    
                                    <tr>
                                        <th>내용</th>
                                        <td><pre><textarea rows="5" cols="50" name="guestbookContent">${vo.guestbookContent}</textarea></pre></td>
                                    </tr>
                                    
                                    <tr>
                                        <th>작성자</th>
                                        <td><input type="text" name="guestbookContentName" value="${vo.guestbookContentName}" readonly></td>
                                    </tr>
                                    
                                    <tr>
                                        <td colspan="2" 
                                        class="lastTd" >
                                            <input id="btn-cover"type="button" class="write" value="수정" onclick="send(this.form);">
                                            <input id="btn-cover" 
                                            class="cancel" type="button" value="취소" onclick="location.href='guestbook.do?idx=${vo.guestIdx}'">
                                            <select 
                                            class="textChoice"
                                            name="guestbookSecretCheck" onchange="selectbox">
                                                <c:if test="${ vo.guestbookSecretCheck eq 0 }">
                                                    <option value="0" selected>전체 공개</option>
                                                    <option value="1">비밀글</option> 
                                                </c:if>
                                                <c:if test="${ vo.guestbookSecretCheck eq 1 }">
                                                    <option value="0">전체 공개</option>
                                                    <option value="1" selected>비밀글</option>
                                                </c:if>
                                            </select>
                                        </td>
                                    </tr>
                                    
                                </table>
                            </form>

                        </div>
                    </div>
                </div>
        </section>
</div>
	
	<!-- Ajax활용을 위한 js파일 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function send(f){
			
			var url = "guestbook_modify.do";
			var param = "guestIdx=" + f.guestIdx.value +
						"&guestbookContentRef=" + f.guestbookContentRef.value +
						"&guestbookContent=" + encodeURIComponent(f.guestbookContent.value) +
						"&guestbookContentName=" + encodeURIComponent(f.guestbookContentName.value) +
						"&guestbookSecretCheck=" + f.guestbookSecretCheck.value +
						"&guestbookSession=" + f.guestbookSession.value +
						"&guestbookMinimi=" + f.guestbookMinimi.value;
			sendRequest( url, param, sendCallback, "get");
		}
		
		function sendCallback(){
			
			if( xhr.readyState == 4 && xhr.status == 200 ){
				
				//"{'result':'no'}"
				var data = xhr.responseText;
				
				//문자열 구조로 넘어온 data를 실제 JSON타입으로 변경
				var json = (new Function('return'+data))();
				
				if( json.result == 'no' ){
					alert("수정실패");
					return;
				}
				
				alert("수정성공");
				location.href="guestbook.do?idx=${vo.guestIdx}";
				
			}
			
		}
		
		
	</script>
</body>
</html>