<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta charset="UTF-8">
<title>searchOk</title>
<script type="text/javascript" src="${root}/resources/javascript/book/bookInfo.js"></script>
<script type="text/javascript" src="/homepage/resources/javascript/xhr/xhr.js"></script>

<style type="text/css">

        .redColor{ color:Red; }

        .green{ color:Green; }

    </style>
<link rel="styleSheet" type="text/css"
	href="${root}/resources/css/search/search_result.css" />
	<link rel="styleSheet" type="text/css"
	href="${root}/resources/css/index/index_footer.css" />
	<link rel="styleSheet" type="text/css"
	href="${root}/resources/css/index/index_header.css" />
	
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	
	<script type="text/javascript">
			
	function colorChange(name){
		var key = '<c:out value="${keyword}"/>';
		var repl2=name.replace(key,"<strong style='color:red'>"+key+"</strong>");
		document.write(repl2);
// 		var repl=$("#book_name").text().replace(key,"<strong style='color:red'>"+key+"</strong>");    	
//     	$("#book_name").html(repl);
	}
//         $(document).ready(function() {
//         	var key = '<c:out value="${keyword}"/>';

// //         	var name = $("#book_name").text();        	         	
// //         	var first=name.indexOf(key);
// //         	var last=name.lastIndexOf(key);
        	
// //         	name=name.replace(key, "");
        	
// //         	var h1 = document.createElement("h1");
        	
// //         	h1.text=key;         	
// //         	alert(h1.text);
// //         	h1.css("color", "red");
// //         	alert($("#book_name").val);

//         	var repl=$("#book_name").text().replace(key,"<strong style='color:red'>"+key+"</strong>");
        	
//         	$("#book_name").html(repl);
//         });
    </script>   
</head>
<body>
<jsp:include page="../../../header.jsp"></jsp:include>
	
	  <div class="center">
	  <c:if test="${count==0 ||searchResult.size()==0}">
		<p>전체 "${keyword}"검색결과 총 0건</p>
		</c:if>
	<c:if test="${searchResult.size()>0}">
	<div class="result_top">전체 "${keyword}"검색결과 총 ${searchResult.size()}건</div>
	<c:forEach var="searchDto" items="${searchPageResult}">
	
            <div class="section1_r">
                <div class="section2">
                    <div class="interest">
                        
                        <div class="interest_body">
                            <div class="interest_img"><a href="${root}/book/bookInfo.do?book_id=${searchDto.book_id}"><img width="100%" height="100%" src="${searchDto.img_path}"></a></div>
                            <div class="interest_subject_form">
                                <div class="interest_sub">
                                    <div class="interest_subject">
                                        <div class="interest_subject1">${searchDto.publish_date} </div>
                                       
                                        <div class="interest_subject2">
                                        	 <a href="${root}/book/bookInfo.do?book_id=${searchDto.book_id}">
                                        		<b id='book_name' ><script type="text/javascript">colorChange('${searchDto.book_name}');</script></b></a></div>
                                        <div class="interest_subject3">인터넷 판매가: <b style="color: red">${searchDto.price}원</b> (${searchDto.author} | ${searchDto.publisher})</div>
                                    </div>
                                </div>
                                <div class="interest_des">
                                 
                                </div>
                                <div class="interest_btn">
                                <c:set var="book_id" value="${searchDto.book_id}"/>
                                	<form name="form" method="get">
										<input type="hidden" name="amount" id="amount" value="1"> 
									</form>
                                    <button type="submit" class="btn"
									style="border: 1px solid #5e6b9e;"
									onclick="javascript:moveToCart2('${root}','${book_id}','1')">장바구니담기</button>
								<button type="submit" class="btn"
									style="border: 1px solid #5e6b9e;"
									onclick="javascript:moveToOrderForm2('${root}','${book_id}','1')">바로구매</button>                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        	</div>
        	
				
        </c:forEach>
        </c:if>
        </div>
        
        <div align="center">
		<c:if test="${count>0}">
			<c:set var="pageBlock" value="${3}"/>
			<fmt:parseNumber var="pageCount" value="${count/boardSize+(count%boardSize==0 ? 0:1)}" integerOnly="true"/>
			
			 <fmt:parseNumber var="result" value="${(currentPage-1)/pageBlock}" integerOnly="true"/>
			 <c:set var="startPage" value="${result*pageBlock+1}"/>
			 <c:set var="endPage" value="${startPage+pageBlock-1}"/>
			 
			 <c:if test="${endPage > pageCount}">
			 	<c:set var="endPage" value="${pageCount}" />
			 </c:if>
			 
			 <c:if test="${startPage > pageBlock}">
			 	<a href="${root}/search/searchOk.do?pageNumber=${startPage-pageBlock}&keyword=${keyword}">[이전]</a>
			 </c:if>
			 
			 <c:forEach var="i" begin="${startPage}" end="${endPage}">
			 	<a href="${root}/search/searchOk.do?pageNumber=${i}&keyword=${keyword}">[${i}]</a>
			 </c:forEach>
			 
			 <c:if test="${endPage < pageCount}">
			 	<a href="${root}/search/searchOk.do?pageNumber=${startPage+pageBlock}&keyword=${keyword}">[다음]</a>
			 </c:if>
		</c:if>
	</div>
        
        <jsp:include page="../../../footer.jsp"></jsp:include>
</body>

</html>