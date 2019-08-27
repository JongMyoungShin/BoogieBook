/**
 * 
 */

function getQuestionCode(question_code,root){
	var board_number_arr = new Array();
	var question_arr = new Array();
	var j=0;
	var z=0;
	$.ajax({
		url : root + "/customerCenter/questionCode.do",
		type : "get",
		data : {question_code:question_code},
		dataType : "text",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(data){
			data = data.split("|");
			for(var i=0; i<10; i++){
				if(i%2 == 0){
					if(i!=0){
						j=j+1;
						board_number_arr[i-j] = data[i];
					}else board_number_arr[i] = data[i];
				}else{
					if(i != 1){
						z=z+1;
						question_arr[i-z] = data[i];
					}else question_arr[i] = data[i];
				}
			}
			$("#faqBoard").empty();
			for(var i=0; i<5; i++){
				var li = document.createElement("li");
				var a = document.createElement("a");
				
				a.setAttribute("id", "bn"+board_number_arr[i]);
				a.setAttribute("onclick", "getAnswer("+board_number_arr[i]+");this.onclick=null");		
				a.style.fontSize = "1.2em";
				a.innerHTML =  question_arr[i+1];
				
				li.appendChild(a);
				
				$("#faqBoard").append(li);
			}	
		},
		error : function(){
			alert("data 가져오기 실패");
		}
	});
}

function getAnswer(board_number){
	$("#bn"+board_number).text();
	$.ajax({
		url : "/homepage/customerCenter/getAnswer.do",
		type : "get",
		data : {board_number:board_number},
		dataType : "text",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(data){
			//alert(data);
			var p = document.createElement("p");
			var button = document.createElement("button");
			p.setAttribute("id", "pn"+board_number);
			p.style.fontSize = "0.8em";
			button.innerHTML="△";
			button.setAttribute("id", "button"+board_number);
			button.setAttribute("onclick", "closeFaq("+board_number+")");
			button.style.width = "40px";
			button.style.height = "30px";
			button.style.borderRadius = "30px";
			button.style.backgroundColor ="#000099";
			button.style.color = "#5e6b9e";
			p.innerHTML = "<br/>"+ data;
			
			$("#bn"+board_number).append(button);
			$("#bn"+board_number).append(p);	
		},
		error : function(){
			alert("답변을 가져오는데 실패하였습니다.");
		}
	});
}

function closeFaq(board_number){
	//$("#bn"+board_number).attr("onclick","getAnswer("+board_number+");this.onclick=null");
//	$("p").remove("#pn"+board_number);
//	$("#button"+board_number).remove();
	location.reload();
}
