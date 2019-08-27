<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta charset="UTF-8">
<title>shop</title>
   <link href="https://fonts.googleapis.com/css?family=Black+Han+Sans|Maven+Pro|Play&display=swap" rel="stylesheet">
   <link href="https://fonts.googleapis.com/css?family=Gothic+A1|Lacquer|Noto+Sans+KR&display=swap" rel="stylesheet">
   
   <script type="text/javascript" src="${root}/resources/javascript/book/bookInfo.js"></script>
   <script type="text/javascript" src="${root}/resources/javascript/review/reviewWrite.js"></script>
   <script type="text/javascript" src="${root}/resources/javascript/review/reviewDelete.js"></script>
   <script type="text/javascript" src="${root}/resources/javascript/review/reviewSelect.js"></script>
   <script type="text/javascript" src="${root}/resources/javascript/xhr/xhr.js"></script>
      
   <link rel="styleSheet" type="text/css" href="${root}/resources/css/book/bookInfo.css" />
</head>
<script type="text/javascript">

   var sell_price;
   var amount;
   
   function init () {
      sell_price = document.form.sell_price.value;
      amount = document.form.amount.value;
      document.form.sum.value = sell_price;
      change();
   }
   
   function add () {
      hm = document.form.amount;
      sum = document.form.sum;
      hm.value ++ ;
   
      sum.value = parseInt(hm.value) * sell_price;
   }
   
   function del () {
      hm = document.form.amount;
      sum = document.form.sum;
         if (hm.value > 1) {
            hm.value -- ;
            sum.value = parseInt(hm.value) * sell_price;
         }
   }
   
   function change () {
      hm = document.form.amount;
      sum = document.form.sum;
   
         if (hm.value < 0) {
            hm.value = 0;
         }
      sum.value = parseInt(hm.value) * sell_price;
   }  

</script>
<body onload="init();">
   <jsp:include page="../../../header.jsp"></jsp:include>
   <div class="center">
      <div class="section1_l">
         <ul class="book_list">
         	<c:set var="pageName" value="${root}/koreanBook/koreanBookMain.do"/>
         <c:set var="pageName" value="${root}/koreanBook/koreanBookMain.do"/>
         	<c:if test="${page == 'koreanBook'}">
         		<c:set var="pageName" value="${root}/koreanBook/koreanBookMain.do"/>
         	</c:if>
         	<c:if test="${page == 'newBook'}">
         		<c:set var="pageName" value="${root}/newBook/newBookMain.do"/>
         	</c:if>
         	<c:if test="${page == 'bestSeller'}">
         		<c:set var="pageName" value="${root}/bestSeller/bestSellerMain.do"/>
         	</c:if>
            <li><a href="${pageName}">종합</a></li>
            <li><a href="${pageName}?bookType=소설">소설</a></li>
            <li><a href="${pageName}?bookType=시/에세이">시/에세이</a></li>
            <li><a href="${pageName}?bookType=소설">자기계발</a></li>
            <li><a href="${pageName}?bookType=가정/육아">가정/육아</a></li>
            <li><a href="${pageName}?bookType=건강">건강</a></li>
            <li><a href="${pageName}?bookType=인문">인문</a></li>
            <li><a href="${pageName}?bookType=역사/문화">역사/문화</a></li>
            <li><a href="${pageName}?bookType=취업/수험서">취업/수험서</a></li>
            <li><a href="${pageName}?bookType=취미/실용/스포츠">취미/실용/스포츠</a></li>
            <li><a href="${pageName}?bookType=예술/대중문화">예술/대중문화</a></li>
            <li><a href="${pageName}?bookType=여행">여행</a></li>
            <li><a href="${pageName}?bookType=잡지">잡지</a></li>
            <li><a href="${pageName}?bookType=만화">만화</a></li>
            <li><a href="${pageName}?bookType=컴퓨터/IT">컴퓨터/IT</a></li>            
         </ul>
      </div>
      <div class="section1_r">
         <div class="section2">
            <div class="interest">
               <div class="interest_top">
                  <div class="interest_top_l" style="font-family: 'Do Hyeon', sans-serif;">${bookInfoDto.type01}&nbsp;&nbsp;&nbsp;>&nbsp;&nbsp;${bookInfoDto.type02}&nbsp;&nbsp;>&nbsp;&nbsp;${bookInfoDto.type03}</div>
                  <div class="interest_top_r">
                  </div>
               </div>
               <div class="interest_body">
                  <div class="interest_img">
                     <img src="${bookInfoDto.img_path}" width="100%" height="100%">
                  </div>
                  <div class="interest_subject_form">
                     <div class="interest_sub">
                        <div class="interest_subject">
                           <div class="interest_subject1">
                              [<fmt:formatDate value="${bookInfoDto.publish_date}"
                                 pattern="yyyy-MM-dd" />]
                           </div>
                           <div class="interest_subject2">
                              <b>${bookInfoDto.book_name}</b>
                           </div>
                           <div class="interest_subject3">
                              인터넷 판매가:&nbsp;<b style="color: red; font-size: 1.5em;"><fmt:formatNumber value="${bookInfoDto.price}" pattern="#,###" />원</b>
                              <br/>출판사:&nbsp;<b style="font-size:1.2em;">${bookInfoDto.publisher}</b>
                              <br/>저자:&nbsp;<b style="font-size:1.2em;">${bookInfoDto.author}</b>
                           </div>
                           <div class="book_volume">
                           <c:if test="${ReviewDto.member_id != null}">
                              <form name="form" method="get">
                                 수량 : <input type=hidden name="sell_price" value="${bookInfoDto.price}">
                                 <input type="text" name="amount" id="amount" value="1" size="3" onchange="change();">
                                 <input type="button" value="－" onclick="del();"><input type="button" value="＋" onclick="add();"><br>
                           	     
                                 금액 : <input type="text" class="sell_price" name="sum" size="11" readonly>원
                              </form>
                             </c:if>
                          <c:if test="${ReviewDto.member_id == null || ReviewDto.member_id == ''}">
                              <form name="form" method="get">
     
                              </form>
                           </c:if>   
                           </div>
                           <div class="delivery">배송비:&nbsp;<b style="font-size:1.2em;">무료</b></div>
                        </div>
                     </div>
                     <div class="interest_btn">
                     <c:set var="book_id" value="${bookInfoDto.book_id}"/>
                        <button type="submit" class="btn"
                           style="border: 1px solid #5e6b9e;"
                           onclick="javascript:moveToCart('${root}','${book_id}')">장바구니담기</button>
                        <button type="submit" class="btn"
                           style="border: 1px solid #5e6b9e;"
                           onclick="javascript:moveToOrderForm('${root}','${book_id}')">바로구매</button>
                     </div>
                  </div>
               </div>
            </div>
         </div>

         <div class="section4">
            <div class="review_rate_form">
               <div class="review_rate_l">
                  <div class="review_rate_l_top"><b style="font-size: 1.2em;">평점</b>&nbsp;참여 ${reviewList_size}명</div>
                  <div class="review_rate_l_middle"></div>
                  <div class="review_rate_l_bottom"><b style="font-size: 1.5em;"><fmt:formatNumber value="${rate_average}" pattern="#.#" /></b></div>
               </div>
               <div class="review_rate_r">
                  <div class="review_rate_r_top"><b style="font-size: 1.2em;">&nbsp;&nbsp;평가하기</b></div>
                  <div class="review_rate_r_middle">
                     <div class="radio_form">
                        <input type="radio" name="rate_group" id="rate_group" value="1"><img style="height: 21px;" src="${root}/resources/images/mark/1.PNG"/>
                        <input type="radio" name="rate_group" id="rate_group" value="2"><img src="${root}/resources/images/mark/2.PNG"/>
                        <input type="radio" name="rate_group" id="rate_group" value="3"><img style="height: 23px; widows: 77px;" src="${root}/resources/images/mark/3.PNG"/>
                        <input type="radio" name="rate_group" id="rate_group" value="4"><img src="${root}/resources/images/mark/4.PNG"/>
                        <input type="radio" name="rate_group" id="rate_group" value="5"><img style="height: 23px; widows: 77px;" src="${root}/resources/images/mark/5.PNG"/>
                     </div>
                  </div>
                  <div class="review_rate_r_bottom">
                     <textarea name="review_content" id="review_content"></textarea>
                     <button type="button" onclick="writeToServer('${root}','${id}','${book_id}')">등록</button>
                  </div>
               </div>
            </div>
            </div>

         
            <div class="review_rate_bottom">
               <div class="listAllDiv" id="listAllDiv">
                  <c:forEach var="ReviewDto" items="${reviewList}">
                     <div class="review_list_form">
                        <div class="review_list_top">
                           <div class="reviw_list_id" style="font-size: 1.1em;">${ReviewDto.member_id}</div>
                           <div class="reviw_list_date" style="color:#B1B1B1;font-size: 0.9em;"><fmt:formatDate value="${ReviewDto.review_date}" pattern="yyyy-MM-dd HH:mm:ss" /></div>
                           <div class="reviw_list_rate"><img src="${root}/resources/images/mark/${ReviewDto.rate}.PNG">${ReviewDto.rate}</div>
                        </div>
                        <div class="review_list_bottom">
                           <div class="review_list_content">${ReviewDto.content}</div>
                        </div>
                     </div>
                  </c:forEach>
               </div>
            </div>
         </div>
      </div>
   <jsp:include page="../../../footer.jsp"></jsp:include>
</body>
</html>