/**
 * 
 */

function deleteFromCart(root,member_id){
	var deleteList = new Array();
	
	if(member_id == null || member_id == ""){
		var date = new Date();
        date.setDate(date.getDate() - 1);
        
		$(".checkBoxes").each(function(){
			if(this.checked == true){
				deleteList.push(this.name);
			}
		});
		if(deleteList.length > 0){
			for(var i=0; i<deleteList.length; i++){
				var delCookie = "";
				delCookie += "book_id"+deleteList[i]+"=; Expires=";
				delCookie += date.toUTCString();
				document.cookie = delCookie;
			}
			location.reload();
		}
		
		
	}else{
		
		$(".checkBoxes").each(function(){
			if(this.checked == true){
				deleteList.push(this.name);
			}
		});
		//alert(deleteList);	
		$.ajax({
			url : root+"/order/cartDelete.do",
			type : "POST",
			data : {"deleteList" : deleteList},
			dataType: "json",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success : function(data){
				//alert(data);
				location.reload();		
			},
			error : function(){
				//alert("delete 실패");
			}
		});
	}
}

var cnt = 1;

function urlCheck(root){
	
}

function changeQuantity(book_id, obj,price){
	var newQuantity = Number(obj.value);
	var price = Number(price);
	document.getElementById(book_id).innerHTML = newQuantity * price;
	
	var prices = new Array(); 
	$(".prices").each(function(){
		prices.push($(this).text());
	});
	
	//alert(prices);
	
	var total = 0;
	for(var i=0; i<prices.length; i++){
		total = total + Number(prices[i]);
	}
	//alert(total);	
	$("input[name=total]").val(total);
	document.getElementById("totalPrice").innerHTML = total+"원";
	
}


function changeQuantityCheck(book_id,obj){ //obj == this
	var price = $("#"+book_id).text();
	price = Number(price);
	
	if(obj.checked==false){
		var total = $("input[name=total]").val();
		total = Number(total);
		total = total - price;
		$("input[name=total]").val(total);
		document.getElementById("totalPrice").innerHTML = total+"원";
	}
	else if(obj.checked==true){
		var total = $("input[name=total]").val();
		total = Number(total);
		total = total + price;
		$("input[name=total]").val(total);
		document.getElementById("totalPrice").innerHTML = total+"원";
	}
	
		
}
