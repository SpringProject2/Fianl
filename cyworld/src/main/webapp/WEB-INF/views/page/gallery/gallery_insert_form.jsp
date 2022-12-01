<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사진첩 새글쓰기</title>
<link rel="stylesheet" href="/cyworld/resources/css/gallery_insert.css">
<link rel="stylesheet" href="/cyworld/resources/css/reset.css">
 <link href="https://fonts.googleapis.com/css2?family=Jua&display=swap" rel="stylesheet">
</head>
<body>
	<!-- 파일전송시 
	enctype="multipart/form-data"
	method="post"
	속성이 필수적으로 추가되어 있어야 한다 -->
	<div class="container ">
        <section class="section">
                <div class="dashed-line">
                    <div class="gray-background">
                        <div class="main">
                            <form method="post" enctype="multipart/form-data">
                                <input id="myIdx" name="idx" type="hidden" value="${ param.idx }">
                                
                                <table>
                                    <caption>새 글 쓰기</caption>
                                    
                                    <tr>
                                        <th>제목</th>
                                        <td><input id="galleryTitle" name="galleryTitle"></td>
                                    </tr>
                                    
                                    <tr>
                                        <th>내용</th>
                                        <td><textarea rows="5" cols="50" name="galleryContent" onkeyup="check_length(this);" placeholder="최대 작성글자는 50자 이내입니다."></textarea></td>
                                    </tr>
                                    
                                    <tr>
                                        <th>파일첨부</th>
                                        <td><input type="file" name="galleryFile"></td>
                                    </tr>
                                    
                                    <tr>
                                        <td colspan="2">
                                            <input id="btn-cover" class="write" type="button" value="글쓰기" onclick="insert(this.form);" >
                                            <input id="btn-cover" class="cancel" type="button" value="취소" onclick="location.href='gallery.do?idx=${param.idx}'">
                                        </td>
                                    </tr>
                                </table>
                            </form>

                        </div>
                    </div>
                </div>
        </section>
</div>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ -->
	<!-- Ajax활용을 위한 js파일 로드 -->
	<script src="/cyworld/resources/js/httpRequest.js"></script>
	<script>
		function insert(f){
			//encodeURIComponent : 특수문자가 포함되어 있는 경우에 내용을 그대로 서버로 전달하기 위해
			//존재하는 메서드
			
			let galleryContent = f.galleryContent.value;
			let galleryTitle = f.galleryTitle.value;
			
			//유효성 체크
			if ( galleryTitle =='' ) {
				alert("제목은 입력은 필수입니다");
				return;
			}
			if( galleryContent == '' ){
				alert("내용 입력은 필수입니다");
				return;
			}
			
			f.action = "gallery_insert.do";
			f.method = "post";
			f.submit();
		}
	</script>
	<!-- textarea 글자 수 제한 -->
	<script>
	//입력 글자 수 제한
    function check_length(area){
    	var text = area.value;
    	var test_length = text.length;
    	
    	//최대 글자수 
    	var max_length = 50;
    	
    	if(test_length>max_length){
    		alert(max_length+"자 이상 작성할 수 없습니다.")
    		text = text.substr(0, max_length);
    		/* substr() : 문자열에서 특정 부분만 골라낼 때 사용하는 메서드. 
    		??.substr(start, length) 
    		 즉, 여기서는 0부터 50글자까지만 가져와서 text에 저장 
    		*/
    		area.value = text;
    		/* text를 다시 area.value로 반환 */
    		area.focus();
    		/* 다시 area의 위치로 반환 */
    	}
    }	
	</script>
</body>
</html>