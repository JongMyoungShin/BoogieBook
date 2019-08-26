<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="root" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <link type="text/css" rel="stylesheet" href="${root}/resources/css/order/cart.css"/>
    <script type="text/javascript" src="${root}/resources/javascript/order/cart.js"></script>
    <script type="text/javascript">
    	function reload(root){
    		
    		$("#bar").hide(0);
    		
    		var url = location.href;
    		var parameter = (url.slice(url.indexOf('?') + 1, url.length)).split('&');
    		if(parameter.length > 1){
    			location.href=root+"/order/cart.do";
    		}
    	}
    </script> 
</head>

<body onload="reload('${root}')">
	<c:set var="cartList" value="${cartList}"/>
	<jsp:include page="../../../header.jsp"></jsp:include>
  	<div style="height: 100px; width: 800px;"></div>
  	<form action="${root}/order/orderForm.do" method="get">
    <div class="container mb-4">
        <div class="row">
            <div class="col-12">
                <div class="table-responsive">
                
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th scope="col"> </th>
                                <th scope="col">도서</th>
                                <th scope="col">재고</th>
                                <th scope="col" class="text-center">수량</th>
                                <th scope="col" class="text-right">가격</th>
                                <th scope="col" class="text-right"><button type="button" class="btn btn-danger" onclick="deleteFromCart('${root}')">선택삭제</button></th>
                            </tr>
                        </thead>
                      
                        <tbody>
                        	<c:if test="${cartList.size()==0}">
                        		<tr>
                        			<td></td>
	                                <td></td>
	                                <td></td>
	                                <td></td>
	                                <td></td>
	                                <td></td>
                            	</tr>
                           	</c:if>
                         
   							<c:if test="${cartList.size()>0}">		
   								<c:forEach var="orderDto" items="${cartList}">
   								<c:set var="quantity" value="${orderDto.quantity}"/>
   								<c:set var="book_id" value="${orderDto.book_id}"/>
   								<c:set var="eachPrice" value="${orderDto.quantity * orderDto.price}" />
   									<tr>
	   									<td><img src="${orderDto.img_path}" style="height:60px; width: 40px;"/></td>
		                                <td style="style:margin-top:20px;">${orderDto.book_name}</td>
		                                <td>${orderDto.stock}</td>
		                                <td><input class="form-control" type="text" name="quantity" value="${quantity}" oninput="changeQuantity('${book_id}',this,'${orderDto.price}')"/></td>
		                                <td class="prices" id="${book_id}">${eachPrice}</td>
		                                <td class="text-right"><input type="checkbox" class="checkBoxes" name="${book_id}" oninput="changeQuantityCheck('${book_id}',this)" checked/></td>
		                            </tr>
		                            <c:set var="total" value="${total=total+orderDto.price}"/>
		                            <input type="hidden" name="member_id" value="${member_id}"/> 
   								</c:forEach>	
  								<input type="hidden" name="total" value="${total}"/>
   							</c:if>                     	     
							<tr>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td><strong>Total</strong></td>
                                <td class="text-right"><strong id="totalPrice">${total}원</strong></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col mb-2">
                <div class="row">
                    <div class="col-sm-12  col-md-6">
                        <button type="button" class="btn btn-secondary btn-lg btn-block" onclick="location.href='${root}/index.jsp'">취소</button>
                    </div>
                    <div class="col-sm-12 col-md-6 text-right">
       					<c:if test="${cartList.size()>0}">
                        	<input type="submit" class="btn btn-primary btn-lg btn-block" value="구매"></button>
                        </c:if>
                        <c:if test="${cartList.size()==0}">
                        	<input type="submit" class="btn btn-primary btn-lg btn-block" value="구매" disabled></button>
                        </c:if>
                    </div>
                </div>
            </div>  
        </div>
    </div>
    </form>
    <div style="height: 100px; width: 800px;"></div>
	<jsp:include page="../../../footer.jsp"></jsp:include>
</body>
</html>