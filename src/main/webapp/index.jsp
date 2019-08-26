<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta charset="UTF-8">
<title>shop</title>
   <link href="https://fonts.googleapis.com/css?family=Gothic+A1|Lacquer|Noto+Sans+KR&display=swap" rel="stylesheet">
   <link href="https://fonts.googleapis.com/css?family=Black+Han+Sans|Maven+Pro|Play&display=swap" rel="stylesheet">
   <link rel="styleSheet" type="text/css" href="${root}/resources/css/index/index_content.css" />
   <link rel="styleSheet" type="text/css" href="${root}/resources/css/index/index_footer.css" />

   <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
   <script type="text/javascript" src="${root}/resources/javascript/index/index_js.js"></script>
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
   <script type="text/javascript" src="${root}/resources/javascript/xhr/xhr.js"></script>
<script type="text/javascript">
   var slideIndex = 0;
   showSlides();

   function showSlides() {
      var i;
      var slides = document.getElementsByClassName("mySlides");
      var dots = document.getElementsByClassName("dot");
      for (i = 0; i < slides.length; i++) {
         slides[i].style.display = "none";
      }
      slideIndex++;
      if (slideIndex > slides.length) {
         slideIndex = 1
      }
      for (i = 0; i < dots.length; i++) {
         dots[i].className = dots[i].className.replace(" active", "");
      }
      $(slides[slideIndex - 1]).css({
         display : "block"
      });
      setTimeout(showSlides, 2000); // Change image every 2 seconds
   }
   
   function toServer(root, book_name) {
      var url = root + "/recommend/recommendProcxy.do?bookName=" + book_name;

      //alert(url);
      sendRequest("GET", url, fromServer, null);
   }

   function fromServer() {
      //alert(xhr.readyState+","+xhr.status);
      if (xhr.readyState == 4 && xhr.status == 200) {
         //alert(xhr.responseXML);
         console.log(xhr.responseXML)
         processXML();
      }
   }

   function processXML() {
      var xmlDoc = xhr.responseXML;


      var discription = xmlDoc.getElementsByTagName("description");
      if (discription.length > 1) {
         //alert(discription[1].childNodes[0].nodeValue);
         document.getElementById("todayBook_des").innerHTML = discription[1].childNodes[0].nodeValue;
      } else {
         document.getElementById("todayBook_des").innerHTML = "미리보기내역이 존재하지 않습니다.";
      }
   }
   
      var bestSellerList;

</script>
</head>
<c:if test="${todayDto.book_id == null}">
<body onload="indexStart('${root}');">
  
</c:if>
<c:if test="${todayDto.book_id != null}">
<body>
</c:if>

   <jsp:include page="./header.jsp"></jsp:include>
   <div id="content">
      <div class="section1">
         <div class="center" id="center">
            <div id="cp_widget_47e7b676-c357-476a-9d90-b765f88ec166">...</div>
            <script type="text/javascript">
               var cpo = [];
               cpo["_object"] = "cp_widget_47e7b676-c357-476a-9d90-b765f88ec166";
               cpo["_fid"] = "AgJAcjuKZDUJ";
               var _cpmp = _cpmp || [];
               _cpmp.push(cpo);
               (function() {
                  var cp = document.createElement("script");
                  cp.type = "text/javascript";
                  cp.async = true;
                  cp.src = "//www.cincopa.com/media-platform/runtime/libasync.js";
                  var c = document.getElementsByTagName("script")[0];
                  c.parentNode.insertBefore(cp, c);
               })();
            </script>
            <noscript>
               <span>New Gallery 2019/8/9</span><span>originaldate</span><span>
                  1/1/0001 6:00:00 AM</span><span>width</span><span> 1097</span><span>height</span><span>
                  485</span><span>originaldate</span><span> 1/1/0001 6:00:00 AM</span><span>width</span><span>
                  1102</span><span>height</span><span> 482</span><span>originaldate</span><span>
                  1/1/0001 6:00:00 AM</span><span>width</span><span> 1099</span><span>height</span><span>
                  484</span><span>originaldate</span><span> 1/1/0001 6:00:00 AM</span><span>width</span><span>
                  1099</span><span>height</span><span> 487</span><span>originaldate</span><span>
                  1/1/0001 6:00:00 AM</span><span>width</span><span> 1099</span><span>height</span><span>
                  484</span>
            </noscript>
         </div>
      </div>

      <div class="section2">
         <div class="center" id="center">
            <div class="tmp1">
            <a href="${root}/book/bookInfo.do?book_id=${todayDto.book_id}">
            <body onload="toServer('${root}','${todayDto.book_name}')">
               <div class="todayBook_form">
                  <div class="todayBook_img"><img src="${todayDto.img_path}" width="100%" height="95%"></div>
                  <div class="todayBook_subject_form">
                     <div class="todayBook_sub">
                        <div class="todayBook_subject">
                           <div class="todayBook_subject1"><fmt:formatDate value="${todayDto.publish_date}"
                                 pattern="yyyy-MM-dd" /></div>
                           <div class="todayBook_subject2">
                              <b>${todayDto.book_name}</b>
                           </div>
                           <div class="todayBook_subject3">
                              인터넷 판매가: <b style="color: red"><fmt:formatNumber value="${todayDto.price}" pattern="#,###" /></b> (${todayDto.publisher} | ${todayDto.author})
                           </div>
                        </div>
                        <div class="todayBook_subject_img"><img style="width:100%; height:100%;" src="${root}/resources/images/index/todayBook.PNG"></div>
                     </div>
                     <div class="todayBook_des" id="todayBook_des"></div>
                  </div>
               </div>
               </body>
               </a>
            </div>
         </div>
      </div>


<script type="text/javascript">
   var path = "/homepage/bestSeller/indexBestSeller.do";
   $.ajax({
       url:path,
       type:'get',
       dataType : 'json',
       success:function(data){
          getBestSeller(data);
       },
       error:function(){
               alert("에러입니다");
       }
   });
   
   function getBestSeller(bestSellerList){
      for(var i=1; i<9; i++){
         document.getElementById("img_bestSeller"+i).innerHTML="<a href='${root}/book/bookInfo.do?book_id=" + bestSellerList[i-1].book_id + "'><img src='" + bestSellerList[i-1].img_path + "'width='100%' height='100%' style='line-height: 0px;'></a>";
         document.getElementById("type_bestSeller"+i).innerHTML="<a href = '${root}/book/bookInfo.do?book_id=" + bestSellerList[i-1].book_id + "'class='th_txt'>" + bestSellerList[i-1].type02 + "</a>";
         document.getElementById("sub_bestSeller"+i).innerHTML="<a href = '${root}/book/bookInfo.do?book_id=" + bestSellerList[i-1].book_id + "'class='su_txt'>" + bestSellerList[i-1].book_name + "</a>";         
      }
      
   }
</script>
      <div class="section3">
         <div class="center" id="center">
            <div class="tmp2">
               <div class="best_header">베스트셀러</div>
               <div class="best_body">
               <div>
                  <ul >
                        <li style="margin-left:95px;"><div class="best_img" id="img_bestSeller1"></div>
                           <div class="th" id="type_bestSeller1">
                           </div>
                           <div class="su" id="sub_bestSeller1">
                           </div></li>
                        <li><div class="best_img" id="img_bestSeller2"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller2"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller2"></a>
                           </div></li>
                        <li><div class="best_img" id="img_bestSeller3"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller3"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller3"></a>
                           </div></li>
                        <li><div class="best_img" id="img_bestSeller4"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller4"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller4"></a>
                           </div></li>
                        <li style="margin-left:95px;"><div class="best_img" id="img_bestSeller5"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller5"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller5"></a>
                           </div></li>
                        <li><div class="best_img" id="img_bestSeller6"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller6"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller6"></a>
                           </div></li>
                        <li><div class="best_img" id="img_bestSeller7"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller7"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller7"></a>
                           </div></li>
                        <li><div class="best_img" id="img_bestSeller8"></div>
                           <div class="th">
                              <a class="th_txt" id="type_bestSeller8"></a>
                           </div>
                           <div class="su">
                              <a class="su_txt" id="sub_bestSeller8"></a>
                           </div></li>
                     </div>
                  </ul>
                  </div>
            </div>
         </div>
      </div>


      <div class="section4">
         <div class="center" id="center">
            <div class="tmp3">
               <div class="spot_form">
                  <div class="spot_l">
                     <div class="spot_inside">
                        <b>지점안내<br /></b><b>바로가기</b>
                     </div>
                  </div>
                  <div class="spot_m"><a href="${root}/customerCenter/storeMap.do"><img id="spot_img" style="width:100%; height:100%;" src="${root}/resources/images/index/index_map.PNG"></a></div>
                  <div class="spot_r">
                     <div class="spot_r_inside">
                        <ul class="spot_list">
                           <li>강남점</li>
                           <li>천호점</li>
                           <li>군자역점</li>
                           <li>발산점</li>
                           <li>목동점</li>
                           <li>상봉점</li>
                           <li>홍대점</li>
                           <li>신촌점</li>
                           <li>코엑스점</li>
                        </ul>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>

   </div>
   <jsp:include page="./footer.jsp"></jsp:include>

</body>
</html>