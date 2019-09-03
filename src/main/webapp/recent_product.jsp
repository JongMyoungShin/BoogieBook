<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<link rel="styleSheet" type="text/css"
      href="${root}/resources/css/index/recent_product.css" />
      <script type="text/javascript">
      function delcookie(id){
//     	alert("ok"+id);
    	var url="${root}/search/delcookies.do?id="+id;
    	$.ajax({
    		url:url,
    		type:"get",
    		
    		dataType:"text",
    		success:function(data){
//     			alert("성공"); 
    			$("#"+id).remove();
//     			alert(id);
    			var product_cnt=document.getElementById("product_cnt");
    		  	   var leng=$(".imgvie").length;
    		  	   		product_cnt.innerHTML=leng;
    		}
    	});
    		
      }
      
  	$(function (){
  		var path = "${root}/search/getcookies.do";
  		 var leng=0;
  		var n=0;
  		var up=0;
  		
  		
  		$("#down_btn").click(function() {
  			
  			if(n<leng-3){
  	  		$("#bar_content > div:eq("+n+")").hide(0);  	  		
  	  		n++;
  	  		}
  	  	});
  		
  		
  		$("#up_btn").click(function() {
  	  		
  			if(n>=0){
  				n--;
  	  		$("#bar_content > div:eq("+n+")").show();
  	  		
  			}
  	  	});
  		
  	   $.ajax({
  	       url:path,
  	       type:'get',
  	       dataType : 'json',
  	       success:function(data){
  	         //alert(data[0].book_name);
  	         var bar=document.getElementById("bar");
  	         
  	         for(var i=0; i<data.length;i++){
  	         var imgbax=document.createElement("div");
  	         imgbax.setAttribute("class", "imgvie");
  	         var id=data[i].book_id;
  	         id=id.trim();
  	       imgbax.setAttribute("id", id);
  	       	
  	       
  	     	var atag=document.createElement("a");
  	     	atag.setAttribute("href", "${root}/book/bookInfo.do?book_id="+data[i].book_id);
  	     	
  	         var img=document.createElement("img");
  	       	 img.setAttribute("src",data[i].img_path);  	       	
  	       	 img.setAttribute("width","100%");
  	     	 img.setAttribute("height","100%");
  	     	
  	     	var closebtn=document.createElement("button");
  	     	closebtn.setAttribute("class", "close_btn");
  	     	closebtn.setAttribute("onclick", "delcookie("+data[i].book_id+")");
  	     	closebtn.innerHTML="x";
  	     	
  	     	var next_btn = document.getElementById("next_btn");
  	     	
  	       	 imgbax.appendChild(atag);
  	       	 atag.appendChild(img);
  	       bar_content.appendChild(imgbax);
  	       imgbax.appendChild(closebtn);
  	       
  	         }
  	     var product_cnt=document.getElementById("product_cnt");
  	   
  	   		product_cnt.innerHTML=data.length;
  	         leng=data.length;
  	       },
  	       error:function(){
  	               alert("에러입니다");
  	       }
  	   });
  	});
  	
  	
      </script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="bar" class="bar">
		<div class="p">최근 본 상품<span id="product_cnt">0</span>
		</div>
		<button class="next_btn" id="up_btn">▲</button>
		<div id ="bar_content"></div>
		<button class="next_btn" id="down_btn">▼</button>
		<div class="top_btn"><a href="#top"><img src="http://image.bandinlunis.com/images/common/2014/btn_top.png"></a></div>
	</div>
	
</body>
</html>