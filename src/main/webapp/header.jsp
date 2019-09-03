<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<head>
   <meta charset="UTF-8">
   <title>shop</title>
   <style type="text/css">
   .ui-autocomplete {
      max-height: 300px;
      overflow-y: auto; /* prevent horizontal scrollbar */
      overflow-x: hidden;
   }
   /* IE 6 doesn't support max-height
        * we use height instead, but this forces the menu to always be this tall
        */
   html .ui-autocomplete {
      height: 300px;
   }
   .ui-autocomplete-term { font-weight: bold; color: blue; }
   </style>
   <link
      href="https://fonts.googleapis.com/css?family=Black+Han+Sans|Maven+Pro|Play&display=swap"
      rel="stylesheet">
   <link
      href="https://fonts.googleapis.com/css?family=Do+Hyeon&display=swap"
      rel="stylesheet">
   
   <link rel="styleSheet" type="text/css"
      href="${root}/resources/css/index/index_header.css" />
   <link rel="styleSheet" type="text/css"
      href="${root}/resources/css/index/index_content.css" />
   <link rel="styleSheet" type="text/css"
      href="${root}/resources/css/index/index_footer.css" />
   <script src="${root}/resources/javascript/index/header_js.js"></script>
   <link
      href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css"
      rel="stylesheet" id="bootstrap-css">
   
   <script
      src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>

   <link
      href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css"
      rel="stylesheet" type="text/css" />
   
   <!-- jQuery library -->
   <script src="https://code.jquery.com/jquery.js"></script>
   <!-- jQuery ui library -->
   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
   <script type="text/javascript"> 
  
          $(function() {
             var bookList = new Array();
             $.ajax({
                   type : "get",
                   url : "${root}/search/autocomplet.do",
                   dataType : "text",
                   success : function(data) {
                      bookList = data.split(",");
                      for(var i=0; i<bookList.length; i++){
                         bookList[i] = bookList[i].replace(/"/gi, "");                         
                      }
                      $("#term").autocomplete({              
                          source : bookList
                          
                      });
                   }
               });
              
          });
          
          $(function(){
            var count = 0;
            $.ajax({
                 type : "get",
                 url : "${root}/order/cartCount.do",
                 dataType : "text",
                 success : function(data) { 
                       $("#cartCount").text(data);
                    }
                 ,error : function(){
                    alert("failed ");
                 }                
             });
          });
      </script>
</head>
<body>
   <header>
      <div id="result"></div>
      <div class="gnb">
         <ul class="center">
            <li class="topHeader_l"><a href="${root}/customerCenter/storeMap.do">매장안내</a><span></span></li>
            <li class="topHeader_l"><a href="#">회원혜택</a><span>∨</span></li>
            <li></li>
            <li class="topHeader_r"><a href="#"></a></li>
            <li class="topHeader_r"><a href="${root}/order/cart.do"><img width="13px" height="13px" src="${root}/resources/images/keep.jpg"><span id="cartCount">0</span></a></li>
            <li class="topHeader_r"><a href="${root}/customerCenter/customerService.do">고객센터</a></li>
            <li class="topHeader_r"><a href="#">주문배송</a></li>

            <c:if test="${name == null}">
               <li class="topHeader_r"><a href="${root}/member/register.do">회원가입</a></li>
               <li class="topHeader_r"><a href="javascript:OpenLoginFrame()">로그인</a></li>
            </c:if>
            <c:if test="${name !=null}">
               <li class="topHeader_r"><a href="${root}/member/searchOrder.do">주문조회</a></li>
               <c:if test="${snsNum == 3}">
                  <li class="topHeader_r"><a
                     href="${root}/member/memberEdit.do?id=${id}">회원정보수정</a></li>
               </c:if>
               <c:if test="${snsNum == 1}">
                  <li class="topHeader_r"><a
                     href="${root}/member/KaKaoEdit.do?id=${id}">회원정보수정</a></li>
               </c:if>
               <li class="topHeader_r"><a href="${root}/member/logout.do">로그아웃</a></li>
               <li class="topHeader_r"><b>${name}님 환영합니다.</b></li>
            </c:if>
         </ul>
      </div>

      <div class="center">
         <div class="middleHeader">
            <div class="logo">
               <a href="${root}/index.jsp"><img src="${root}/resources/images/BoogieBook_Logo.png"></a>
            </div>
            <div class="search_form">
               
               <div class="search">
                  <form action="${root}/search/searchOk.do" method="get">
                     <div id="custom-search-input">
                        <div class="input-group col-md-12">
                           <input type="text" class="  search-query form-control"
                              name="keyword" id="term" placeholder="검색어를 입력하세요" /> <span
                              class="input-group-btn">
                              <button class="btn btn-danger" type="submit">
                                 <span class=" glyphicon glyphicon-search"></span>
                              </button>
                           </span>
                        </div>
                     </div>
                  </form>
               </div>
            </div>
         </div>
      </div>

      <div class="lnb">
         <div id="center">
            <ul class="center" id="ul_center">
               <li><a href="${root}/koreanBook/koreanBookMain.do">국내도서</a></li>
               <li><a href="${root}/bestSeller/bestSellerMain.do">베스트셀러</a></li>
               <li><a href="${root}/newBook/newBookMain.do">신간도서</a></li>
               <li><a href="${root}/search/detailSearch.do">상세검색</a></li>
               <c:if test="${id != null}">
                  <li><a href="${root}/recommend/recommendMain.do?id=${id}">추천도서</a></li>
               </c:if>
               <c:if test="${id == null}">
                  <li><a href="${root}/recommend/recommendMain.do">추천도서</a></li>
               </c:if>
               <li><a href="${root}/customerCenter/storeMap.do">매장안내</a></li>
               <c:if test="${id=='admin'}">
                  <li><a href="${root}/admin/admin.do">관리자페이지</a></li>
               </c:if>
            </ul>
         </div>
      </div>
   </header>
</body>
<jsp:include page="./recent_product.jsp"></jsp:include>
</html>