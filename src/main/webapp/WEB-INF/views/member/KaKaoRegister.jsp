<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<head>
<meta charset="UTF-8">

<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<link href="${root}/resources/css/member/register_css.css" rel="stylesheet">
<script src="${root}/resources/javascript/member/KaKaoRegister_js.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

<title>BoogieBook</title>
</head>
<body>

<jsp:include page="../../../header.jsp"></jsp:include>

<div class="container" style="border: 0px solid blue; margin: 0px auto; height:400px; margin-top:50px; margin-left: 120px;">
	<div class="row">
    <div class="col-md-8" style="width: 1000px; height: 400px; margin: 0px auto;">  
        <h1 class="entry-title" style="border-bottom: 3px solid black; padding-bottom: 10px;"><span>약관동의</span> </h1>
        <hr>
       <div class="form-horizontal">
        <div class="form-group" >
          <div class="col-md-8 col-sm-9" 
          style="border: 1px solid rgb(209, 209, 209); width: 900px; height: 230px; margin-left: 50px; padding: 0px;">
            <div style="width: 900px; height: 50px; border-bottom: 1px solid rgb(209, 209, 209); line-height: 50px; padding-left: 50px;">
            	<input type="checkbox" name="item_all" onclick="selectAll()"/>
            	<span style="font-size: 18px; font-weight: bold;"> 전체동의</span>
            </div>
            
            <div style="width: 730px; height: 120px; line-height: 43px; padding-left: 50px; float:left;">
            	<ul class="agreement_ul">
            		<li>
            			<input type="checkbox" name="item01" onclick="selectAllCheck()"/>
     					<span> “이용 약관”에 동의 (필수)</span>
     				</li>
     				<li>
            			<input type="checkbox" name="item02" onclick="selectAllCheck()"/>
     					<span> 개인정보 “필수“ 항목에 대한 수집과 이용 동의 (필수)</span>
     				</li>
     				<li>
            			<input type="checkbox" name="item03" onclick="selectAllCheck()"/>
     					<span> 개인정보 “선택“ 항목에 대한 수집과 이용 동의</span>
     					<span style="font-weight: bold"> (선택)</span>
     				</li>
     				<li>
            			<input type="checkbox" name="item04" onclick="selectAllCheck()"/>
     					<span> 제3자 정보 제공 동의</span>
     					<span style="font-weight: bold"> (선택)</span>
     				</li>
            	</ul>
            </div>  
            
            <div style="width: 100px; height: 120px; line-height: 43px; float:left;">
            	<ul class="agreement_ul">
            		<li>
     					<input type="button" value="내용보기" style="width: 70px; height: 25px; line-height:25px; text-align: center; font-size: 12px;"
     						onclick="readContent(1)"/>
     				</li>
     				<li>
     					<input type="button" value="내용보기" style="width: 70px; height: 25px; line-height:25px; text-align: center; font-size: 12px;"
     						onclick="readContent(2)"/>
     				</li>
     				<li>
     					<input type="button" value="내용보기" style="width: 70px; height: 25px; line-height:25px; text-align: center; font-size: 12px;"
     						onclick="readContent(3)"/>
     				</li>
     				<li>
						<input type="button" value="내용보기" style="width: 70px; height: 25px; line-height:25px; text-align: center; font-size: 12px;"
							onclick="readContent(4)"/>
     				</li>
            	</ul>
            </div>                
          </div>
       
        </div>
        </div>
        <div style="margin-left: 32px;">
       	 <p>선택 항목의 경우 동의하지 않아도 회원 가입이 가능하나, 구매 등 서비스 이용시 추가적인 정보 입력이 필요합니다.</p>
        </div>
    </div>
</div>
</div>
	<div class="container" style="border: 0px solid red; margin: 0px auto; height: 500px; margin-left: 105px; margin-bottom: 250px;">
	<div class="row" style="margin: 0px auto; border: 0px solid red;">
    <div class="col-md-8" style="width: 1000px; height: 700px; margin: 0px auto;">  
        <h1 class="entry-title" style="border-bottom: 3px solid black; padding-bottom: 10px;"><span>회원가입</span> </h1>
        <hr>
        <form class="form-horizontal" action="${root}/member/KaKaoRegisterOk.do" method="get" name="signup" id="signup" enctype="multipart/form-data"
        	onsubmit="return registerForm(this)">      
        <input type="hidden" name="id" value="${id}"/>	
        <div class="form-group" >
          <label class="control-label col-sm-3">이름</label>
          <div class="col-md-8 col-sm-9">
            <input type="text" style="width: 230px" class="form-control" name="name" id="mem_name" placeholder="이름을 입력하세요." value="">
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3">생년월일</label>
          <div class="col-xs-8">
            <div class="form-inline" style="width: 360px">
            
              <div class="form-group">
                <select name="yyyy" class="form-control" style="width: 90px; margin-left: 15px;" >
                  <option value="0">년</option>
                  <option value="1955" >1955 </option><option value="1956" >1956 </option><option value="1957" >1957 </option><option value="1958" >1958 </option><option value="1959" >1959 </option><option value="1960" >1960 </option><option value="1961" >1961 </option><option value="1962" >1962 </option><option value="1963" >1963 </option><option value="1964" >1964 </option><option value="1965" >1965 </option><option value="1966" >1966 </option><option value="1967" >1967 </option><option value="1968" >1968 </option><option value="1969" >1969 </option><option value="1970" >1970 </option><option value="1971" >1971 </option><option value="1972" >1972 </option><option value="1973" >1973 </option><option value="1974" >1974 </option><option value="1975" >1975 </option><option value="1976" >1976 </option><option value="1977" >1977 </option><option value="1978" >1978 </option><option value="1979" >1979 </option><option value="1980" >1980 </option><option value="1981" >1981 </option><option value="1982" >1982 </option><option value="1983" >1983 </option><option value="1984" >1984 </option><option value="1985" >1985 </option><option value="1986" >1986 </option><option value="1987" >1987 </option><option value="1988" >1988 </option><option value="1989" >1989 </option><option value="1990" >1990 </option><option value="1991" >1991 </option><option value="1992" >1992 </option><option value="1993" >1993 </option><option value="1994" >1994 </option><option value="1995" >1995 </option><option value="1996" >1996 </option><option value="1997" >1997 </option><option value="1998" >1998 </option><option value="1999" >1999 </option><option value="2000" >2000 </option><option value="2001" >2001 </option><option value="2002" >2002 </option><option value="2003" >2003 </option><option value="2004" >2004 </option><option value="2005" >2005 </option><option value="2006" >2006 </option>                
              	  <option value="2007" >2007 </option><option value="2008" >2008 </option><option value="2008" >2008 </option><option value="2009" >2009 </option><option value="2010" >2010 </option><option value="2011" >2011 </option><option value="2012" >2012 </option><option value="2013" >2013 </option><option value="2014" >2014 </option><option value="2015" >2015 </option><option value="2016" >2016 </option><option value="2017" >2017 </option>
              	  <option value="2007" >2018 </option><option value="2007" >2019 </option>
              	</select> 	
              </div>
              
              <div class="form-group">
                <select name="mm" class="form-control" style="width: 80px; margin-left: 31px;">
                  <option value="">월</option>
                  <option value="1">1월</option><option value="2">2월</option><option value="3">3월</option><option value="4">4월</option><option value="5">5월</option><option value="6">6월</option><option value="7">7월</option><option value="8">8월</option><option value="9">9월</option><option value="10">10월</option><option value="11">11월</option><option value="12">12월</option>                </select>
              </div>
              
              <div class="form-group">
                <select name="dd" class="form-control" style="width: 80px; margin-left: 30px;">
                  <option value="">일</option>
                  <option value="1" >1 </option><option value="2" >2 </option><option value="3" >3 </option><option value="4" >4 </option><option value="5" >5 </option><option value="6" >6 </option><option value="7" >7 </option><option value="8" >8 </option><option value="9" >9 </option><option value="10" >10 </option><option value="11" >11 </option><option value="12" >12 </option><option value="13" >13 </option><option value="14" >14 </option><option value="15" >15 </option><option value="16" >16 </option><option value="17" >17 </option><option value="18" >18 </option><option value="19" >19 </option><option value="20" >20 </option><option value="21" >21 </option><option value="22" >22 </option><option value="23" >23 </option><option value="24" >24 </option><option value="25" >25 </option><option value="26" >26 </option><option value="27" >27 </option><option value="28" >28 </option><option value="29" >29 </option><option value="30" >30 </option><option value="31" >31 </option>                </select>
              </div>
              
              <input type="hidden" name="birth_date" id="birth_date"value="">	
                          
            </div>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3">성별</label>
          <div class="col-md-8 col-sm-9">
            <label>
            <input name="gender" type="radio" value="Male" checked>
            남 </label>
               
            <label>
            <input name="gender" type="radio" value="Female" >
            여 </label>
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3">휴대폰번호</label>
          <div class="col-md-5 col-sm-8">
          	<div class="input-group">
              <span class="input-group-addon"><i class="glyphicon glyphicon-phone"></i></span>
            <input type="text" style="width: 350px" class="form-control" name="phone" id="contactNum" 
            placeholder="' - ' 제외한 휴대폰 번호를 입력하세요." value="" oninput="checkContactNum()">
            </div>
            <small class="chkContactNumMsg"> </small>  
          </div>
        </div>
        <div class="form-group">
          <label class="control-label col-sm-3">주소 <br></label>
          <div class="col-md-5 col-sm-8" style=" width: 500px; height: 90px; border: 0px solid red;">
          	<div style="width: 550px; height: 40px; border: 0px solid red">
            	<input type="text" class="form-control" name="zipcode" id="zipcode" placeholder="" value="" 
            	style="width: 150px; margin-bottom: 10px; float: left;" readOnly>
            
            	<input type = "button" value = "우편번호 검색" style="float: left; margin-left: 10px;" 
            			onclick="zipcodeRead()"/>
            </div>
            
            <div style="width: 550px; height: 40px; border: 0px solid red; float: left; ">
            	<input type="text" class="form-control" name="addr1" id="addr1" placeholder="" value=""
            	style="width: 250px;  float: left; margin-left: 0px;" readOnly/>
            
            	<input type="text" class="form-control" name="addr2" id="addr2" placeholder="상세 주소 입력" value=""
            	style="width: 250px; float: left; margin-left: 10px;"/>
            </div>    
          </div>
        </div>
        
          
        <div class="form-group">
          <label class="control-label col-sm-3">직업</label>
          <div class="col-md-8 col-sm-9">
            	<select name = "job" class="form-control" style="width: 80px; height: 30px; padding: 0px">
					<option></option>				
					<option value = "초중고 학생">초중고 학생</option>
					<option value = "대학생">대학생</option>
					<option value = "일반회사원">일반회사원</option>
					<option value = "디자이너">디자이너</option>
					<option value = "공무원">공무원</option>
					<option value = "군인">군인</option>
					<option value = "경찰">경찰</option>
					<option value = "소방관">소방관</option>
					<option value = "의사">의사</option>
					<option value = "간호사">간호사</option>
					<option value = "변호사">변호사</option>
					<option value = "기타">기타</option>
				</select>
          </div>
        </div>
        
        <div class="form-group" style="border: 0px solid red">
          <label class="control-label col-sm-3">관심분야</label>
          <div class="col-md-8 col-sm-9" style="border: 0px solid red; width: 600px;">
          
            	<input type = "radio" name = "interestValue" value = "소설" checked/> 
				<span>소설</span>
				<input type = "radio" name = "interestValue" value = "시/에세이"/>
				<span>시/에세이</span>
				<input type = "radio" name = "interestValue" value = "경제/경영"/>
				<span>경제/경영</span>
				<input type = "radio" name = "interestValue" value = "자기계발"/>
				<span>자기계발</span>
				<input type = "radio" name = "interestValue" value = "인문"/>
				<span>인문</span>
				<input type = "radio" name = "interestValue" value = "역사/문화"/>
				<span>역사/문화</span>
				<input type = "radio" name = "interestValue" value = "종교"/>
				<span>종교</span>
				<input type = "radio" name = "interestValue" value = "정치/사회"/>
				<span style = "margin-right: 20px;">정치/사회</span>
				<input type = "radio" name = "interestValue" value = "예술/대중문화"/>
				<span style = "margin-right: 6px;">예술/대중문화</span>
				<input type = "radio" name = "interestValue" value = "과학"/>
				<span style = "margin-right: 6px;">과학</span>
				<input type = "radio" name = "interestValue" value = "기술/공학"/>
				<span style = "margin-right: 6px;">기술/공학</span>
				<input type = "radio" name = "interestValue" value = "컴퓨터/IT"/>
				<span style = "margin-right: 6px;">컴퓨터/IT</span>
				<input type = "radio" name = "interestValue" value = "유아(0~7세)"/>
				<span style = "margin-right: 6px;">유아(0~7세)</span>
				<input type = "radio" name = "interestValue" value = "어린이(초등)"/>
				<span style = "margin-right: 30px;">어린이(초등)</span>
				<input type = "radio" name = "interestValue" value = "어린이전집"/>
				<span style = "margin-right: 4px;">어린이전집</span>
				<input type = "radio" name = "interestValue" value = "어린이영어"/>
				<span style = "margin-right: 4px;">어린이영어</span>
				<input type = "radio" name = "interestValue" value = "청소년"/>
				<span style = "margin-right: 4px;">청소년</span>
				<input type = "radio" name = "interestValue" value = "초등참고서"/>
				<span style = "margin-right: 4px;">초등참고서</span>
				<input type = "radio" name = "interestValue" value = "중/고등참고서"/>
				<span style = "margin-right: 4px;">중/고등참고서</span>
				<input type = "radio" name = "interestValue" value = "대학교재"/>
				<span style = "margin-right: 30px;">대학교재</span>
				<input type = "radio" name = "interestValue" value = "취업/수험서"/>
				<span>취업/수험서</span>
				<input type = "radio" name = "interestValue" value = "외국어"/>
				<span>외국어</span>
				<input type = "radio" name = "interestValue" value = "가정/육아"/>
				<span>가정/육아</span>
				<input type = "radio" name = "interestValue" value = "건강"/>
				<span>건강</span>
				<input type = "radio" name = "interestValue" value = "여행"/>
				<span>여행</span>
				<input type = "radio" name = "interestValue" value = "요리"/>
				<span>요리</span>
				<input type = "radio" name = "interestValue" value = "취미/실용/스포츠"/>
				<span>취미/실용/스포츠</span>
				<input type = "radio" name = "interestValue" value = "잡지"/>
				<span>잡지</span>
				<input type = "radio" name = "interestValue" value = "만화"/>
				<span>만화</span>
				<input type = "radio" name = "interestValue" value = "한국소개도서"/>
				<span>한국소개도서</span>
          </div>
        </div>
        
        <div class="form-group">
          <div class="col-xs-offset-3 col-xs-10" style="padding-left: 200px; padding-top: 30px;">
            <input name="Submit" type="submit" value="가입신청" class="btn btn-primary"
            style="width: 130px; height: 40px; background-color: #5e6b9e;"/>
          </div>
        </div>
      </form>
    </div>
</div>
</div>

<jsp:include page="../../../footer.jsp"></jsp:include>
</body>
</html>