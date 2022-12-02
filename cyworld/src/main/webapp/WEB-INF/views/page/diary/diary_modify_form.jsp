<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>다이어리 수정</title>
<link rel="stylesheet" href="/cyworld/resources/css/diary_modify.css">
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
<link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	 <div class="container ">
        <section class="section">
                <div class="dashed-line">
                    <div class="gray-background">
                        <div class="main">
                            <form method="post" enctype="multipart/form-data">
                                <input type="hidden" name="diaryIdx" value="${ vo.diaryIdx }">
                                <input type="hidden" name="diaryContentRef" value="${ vo.diaryContentRef }">
                                
                                <table>
                                
                                    <caption>다이어리 수정</caption>
                                    
                                    <tr>
                                        <th>내용</th>
                                        <td><pre><textarea rows="5" cols="50" name="diaryContent">${vo.diaryContent}</textarea></pre></td>
                                    </tr>		
                                    <tr>
                                        <td colspan="2">
                                            <input class="write" id="btn-cover" type="button" value="수정" onclick="send(this.form);">
                                            <input class="cancel" id="btn-cover" type="button" value="취소" onclick="location.href='diary.do?idx=${param.diaryIdx}'">
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
			//encodeURIComponent : 특수문자가 포함되어 있는 경우에 내용을 그대로 서버로 전달하기 위해
			//존재하는 메서드
			
			var url = "diary_modify.do";
			var param = "diaryContentRef="+f.diaryContentRef.value +
			            "&diaryContent="+ encodeURIComponent(f.diaryContent.value);
			sendRequest( url, param, sendCallback, "POST");
			
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
				location.href="diary.do?idx=${param.diaryIdx}";
				
			}
			
		}
		
		
	</script>
</body>
</html>