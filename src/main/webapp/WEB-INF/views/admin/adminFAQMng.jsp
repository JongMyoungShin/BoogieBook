<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
    <link rel="stylesheet" type="text/css" href="${root}/resources/css/admin/admin_header.css"/>
    <link rel="stylesheet" type="text/css" href="${root}/resources/css/admin_content.css"/>
    <link rel="stylesheet" type="text/css" href="${root}/resources/css/admin/admin_content.css"/>
</head>
<body>
    <header>
			<div class="gnb">
				<ul class="center">
					<li class="topHeader_r"><a href="#">로그아웃</a></li>
					<li class="topHeader_r"><a href="#">홈으로</a></li>
				</ul>
			</div>
            <div class="center">
                <div class="middleHeader">
                    <div class="logo"><a href="${root}/index/index.do"><img id="logoImg" src="${root}/resources/images/BoogieBook_Logo.png"/></a></div>
                    <div class="search_form">
                        <div class="search_top"></div>
                        <div class="search"><b>Administrator</b></div>
                    </div>
                </div>
            </div>

			<div class="lnb">
				<ul class="center">
					<li><a href="admin.do">사용자 통계</a></li>
					<li><a href="adminMemMng.do">회원관리</a></li>
					<li><a href="adminBookRegMng.do">도서등록</a></li>
					<li><a href="adminBookMng.do">도서관리</a></li>
					<li><a href="adminFAQRegMng.do">FAQ등록</a></li>
					<li><a href="adminFAQMng.do">FAQ관리</a></li>
					<li><a href="adminOrdMng.do">주문 관리</a></li>
				</ul>
			</div>
		</header>
		    <div id="content">
      <div class="section1">
        <div class="center">
	          <div class="tmp1">
	            <div class="borbtmeff">
	              <h4 style="color:#5e6b9e">FAQ관리</h4>
	            </div>
  				<div>
  				 <b style="color:#5e6b9e">FAQ 분류 선택</b>
	              <form action="${root}/admin/adminFAQStat.do" onsubmit="return adminOrdStat(this)" name="ordStatForm">
		              <select name="questionCode">
		                <option value="1">반품/교환/환불</option>
		                <option value="2">주문/결제</option>
		                <option value="3">회원</option>
		                <option value="4">도서/상품정보</option>
		                <option value="5">배송/수령일안내</option>
		              </select>
		              <button class="btn btn-primary" style="height:70%;" type="submit" value="Submit">검색</button>
	              </form>
  				</div>
  				<div style="width:100%; text-align:center;">
          		<b style="font-size:1.5em; text-align:center;">FAQ분류를 선택해 주세요.</b>
          	</div>
	          </div>
			</div>
		</div>
      </div>	
</body>
</html>