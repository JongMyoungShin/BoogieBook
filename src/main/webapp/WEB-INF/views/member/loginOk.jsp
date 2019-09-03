<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
	<c:set var="root" value="${pageContext.request.contextPath}"/>
	
	<c:if test="${check == 1 }">
		<c:set var="id" value="${id}" scope="session"/> <%--application --%>
		<c:set var="name" value="${name}" scope="session"/> <%--application --%>
		<c:set var="snsNum" value="${snsNum}" scope="session"/> <%--application --%>
		
		<script type="text/javascript">
			alert("로그인 성공");
			var snsNum = ${snsNum};
			
			if(snsNum == 3){
		    setTimeout(function() {
		    opener.location.href ="${root}/index.jsp"; //부모창 리프레쉬
		    self.close(); //현재창 닫기
		    }, 500); // 2초후 실행 1000당 1초
		}else{
			location.href="${root}/index.jsp";
		}
		</script>
	</c:if>
	
	
	<c:if test="${check == 0}">
		<script type="text/javascript">
			alert("입력하신 정보가 존재하지 않습니다.");
			location.href="${root}/member/login.do";
		</script>
	</c:if>
</body>
</html>