package com.boogie.order.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.boogie.aop.BookAspect;
import com.boogie.email.Email;
import com.boogie.email.EmailSender;
import com.boogie.member.dao.MemberDao;
import com.boogie.member.dto.MemberDto;
import com.boogie.order.dao.OrderDao;
import com.boogie.order.dto.OrderDto;

/**
 * @author : 이민호
 * 2019. 8. 1.
 * @description: 이민호 장바구니 정보 getCartInfo
 */

/**
 * @author : 이민호
 * 2019. 8. 2.
 * @description: getOrderForm 추가 하였음 장바구니page에서 -> 주문서입력 page
 * 				 insertOrderInfo 주문정보를 DB에 insert
 */
@Component
public class OrderServiceImp implements OrderService {
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MemberDao memberDao;	
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private Email email;
	
	@Override
	public void getCartInfo(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		HttpSession session = request.getSession();
		String member_id = (String) session.getAttribute("id");
		BookAspect.logger.info(BookAspect.logMsg + "member_id:" + member_id);
		
		if (member_id == null) {
			System.out.println("장바구니에 존재하는 Item이 없습니다.");
		}
		
		List<OrderDto> cartList = new ArrayList<OrderDto>();
		cartList = orderDao.getCartInfo(member_id);
		BookAspect.logger.info(BookAspect.logMsg+cartList.size());
		
		mav.addObject("member_id",member_id);
		mav.addObject("cartList", cartList);
		mav.setViewName("order/cart");
	}

	//주문서작성 page 정보 get & write
	@Override
	public void getOrderForm(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		HttpSession  session = request.getSession();
		String member_id = (String) session.getAttribute("id");
		String total = request.getParameter("total");
		String quantity = request.getParameter("quantity");
		if(member_id != null) {
			BookAspect.logger.info(BookAspect.logMsg + "total: " + total + "member_id" + member_id);	
			
			MemberDto memberDto = new MemberDto();
			memberDto =  orderDao.getOrderForm(member_id);
			BookAspect.logger.info(BookAspect.logMsg + memberDto.toString());
			
			if(member_id == null || member_id =="") {
				mav.addObject("member_id", null);
			}else {
				mav.addObject("member_id", member_id);
			}
			mav.addObject("memberDto",memberDto);
			
		}
		mav.addObject("quantity",quantity);
		mav.addObject("total", total);
		mav.setViewName("order/orderForm");
	}
	
	//주문정보페이지 정보 write
	@Override
	public void writeOrderInfo(ModelAndView mav) {
		OrderDto orderDto = new OrderDto();
		List<OrderDto> bookList = new ArrayList<OrderDto>();
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpSession  session = request.getSession();
		
		String member_id = (String) session.getAttribute("id");
		String total = request.getParameter("total");
		BookAspect.logger.info(BookAspect.logMsg + "주문정보 member_id : "+ member_id + "total:" + total);
		
		
		if(total == "") {
			int orderNumber = orderDao.selectMyOrderNum(member_id);
			BookAspect.logger.info(BookAspect.logMsg +"orderNumber: " +orderNumber);
			
			orderDto = orderDao.getOrderCheckForm(orderNumber,member_id);
			orderDto.setBook_id(getBookId(orderNumber));
			orderDto.setBook_name(getBookName(orderDto.getBook_id()));
			
			mav.addObject("orderDto", orderDto);
		}else {
			//기존 주문정보를 cart에서 가져옴 list로
			List<OrderDto> cartList = new ArrayList<OrderDto>();
			cartList = orderDao.getCartInfo(member_id);
			BookAspect.logger.info(BookAspect.logMsg+cartList.size());
			
			//주문정보 DB(book_order)에 insert
			int checkBookOrder = orderDao.insertOrderInfo(member_id, total);
			BookAspect.logger.info(BookAspect.logMsg +"checkBookOrder: " +checkBookOrder);
			
			int orderNumber = orderDao.selectMyOrderNum(member_id);
			BookAspect.logger.info(BookAspect.logMsg +"orderNumber: " +orderNumber);
			
			//주문정보 DB(order_detail)에 insert
			
			for(int i=0; i<cartList.size(); i++) {
				orderDto = (OrderDto) cartList.get(i);
				int checkOrderDetail = orderDao.insertOrderDetail(orderNumber,orderDto.getBook_id(),orderDto.getQuantity(),orderDto.getPrice());
				BookAspect.logger.info(BookAspect.logMsg +"checkOrderDetail: " +checkOrderDetail);
			}
				
			//주문정보를 가지고 주문확인 page에 뿌리기 orderDto
			OrderDto oDto = new OrderDto();
			oDto = orderDao.getPaymentInfo(member_id);
			BookAspect.logger.info(BookAspect.logMsg +"oDto.toString()" +oDto.toString());
			
			//해당 Order_id 로 book_id 들을 가져옴
			List<Integer> bookIdArray = new ArrayList<Integer>();
			bookIdArray = orderDao.getBookIdArray(orderNumber);
			BookAspect.logger.info(BookAspect.logMsg +"bookIdArray.size : " +bookIdArray.size());
			
			//book_id 로 book_name & quantity -> bookList에 저장
			for(int i=0; i<bookIdArray.size(); i++) {
				int book_id = (int)bookIdArray.get(i);
				System.out.println("book_id값 : " + book_id);
				orderDto = new OrderDto();
				orderDto = orderDao.getPayInfoByBookId(book_id);
				bookList.add(orderDto);
			}
			BookAspect.logger.info(BookAspect.logMsg + "bookList.size() : " +bookList.size());

			orderInfoSendEmail(member_id, oDto, bookList);
			
			mav.addObject("orderDto", oDto);
			mav.addObject("bookList", bookList);
			mav.setViewName("order/paymentComplete");
			
			//주문이 완료되면 DB에서 장바구상품을 삭제시킨다.
			int check = orderDao.deleteFromCart(member_id);
			BookAspect.logger.info(BookAspect.logMsg + "check : " +check);
		}
		
	}

	private String getBookName(int book_id) {
		String book_name;
		book_name = orderDao.getBookName(book_id);
		return book_name;
	}

	private int getBookId(int orderNumber) {
		int book_id = 0;
		book_id = orderDao.getBookId(orderNumber);
		return book_id;
	}

	public void orderInfoSendEmail(String member_id, OrderDto oDto, List<OrderDto> bookList) {
		String id = member_id;
		String userEmail = orderDao.getUserEmail(member_id); 
		String content = id + "<!doctypeHTML>" + 
				"    <html>" + 
				"" + 
				"    <head>" + 
				"        <title>Book Info</title>" + 
				"        <meta charset=\"utf-8\">" + 
				"        <link rel=\"stylesheet\" type=\"text/css\" href=\"bookInfo.css\" />" + 
				"        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" + 
				"        <style type=\"text/css\">" + 
				"            " + 
				"        </style>" + 
				"    </head>" + 
				"    <body>" + 
				"        <table class=\"table table-striped\">" + 
				"            <thead>" + 
				"                <tr>" + 
				"                    <th scope=\"col\">" + 
				"                        <h3>"+id+"님의 주문정보</h3>" + 
				"                    </th>" + 
				"                </tr>" + 
				"            </thead>" + 
				"            <tbody>" + 
				"                <tr>" + 
				"                    <td style=\"padding-left:60px;\"><label>주문번호</label> </td>" + 
				"                    <td></td>" + 
				"                    <td>"+ oDto.getOrder_id() +"</td>" + 
				"                    <td></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                </tr>" + 
				"                <tr>" + 
				"                    <td style=\"padding-left:60px;\"><label>주문날짜</label> </td>" + 
				"                    <td></td>" + 
				"                    <td>"+ oDto.getOrder_date()+"</td>" + 
				"                    <td></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                </tr>" + 
				"                <tr>" + 
				"                    <td style=\"padding-left:60px;\"><label>배송지</label> </td>" + 
				"                    <td></td>" + 
				"                    <td>"+oDto.getAddr1() +" " + oDto.getAddr2()+"</td>" + 
				"                    <td></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                </tr>" + 
				"                <tr>" + 
				"                    <td style=\"padding-left:60px;\"><label>주문자</label></td>" + 
				"                    <td></td>" + 
				"                    <td>"+ oDto.getName() +"</td>" + 
				"                    <td></td>" + 
				"                    <td></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                </tr>" + 
				"" + 
				"                <tr>" + 
				"                    <td></td>" ; 
				if(bookList != null) {
					for(int i=0; i<bookList.size(); i++) {
						OrderDto orderDto = new OrderDto();
						orderDto = bookList.get(i);
						content += "<td style=\"padding-left:60px;\"><label>"+ orderDto.getBook_name()+"("+orderDto.getQuantity()+")" +"</label></td>";
					}
				}else {
					content += "<td style=\"padding-left:60px;\"><label>"+ oDto.getBook_name()+"("+oDto.getQuantity()+")" +"</label></td>";
				}
				content += "                        <td>" + 
				"                        </td>" + 
				"                    <td><strong></strong></td>" + 
				"                    <td class=\"text-right\"><strong></strong></td>" + 
				"                </tr>" + 
				"                <tr>" + 
				"                    <td style=\"padding-left:60px;\"><label>주문금액</label></td>" + 
				"                    <td></td>" + 
				"                    <td><strong>"+oDto.getTotal_price()+"</strong>원</td>" + 
				"                    <td></td>" + 
				"                    <td></td>" + 
				"                    <td class=\"text-right\"></td>" + 
				"                </tr>" + 
				"            </tbody>" + 
				"        </table>" + 
				"    </body>" + 
				"" + 
				"    </html>" + 
				"";
		
		if(userEmail != null) {
			email.setContent(content);
			email.setReceiver(userEmail);
			email.setSubject(id + "님의 주문정보 입니다.");

			System.out.println(email.toString());
			try {
				emailSender.SendEmail(email);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}

	@Override
	public void addToCart(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpSession session = request.getSession();
		int check=999;
		
		String member_id = (String) session.getAttribute("id");
		int book_id = Integer.parseInt(request.getParameter("book_id"));
		int amount = Integer.parseInt(request.getParameter("amount"));
		BookAspect.logger.info(BookAspect.logMsg + "book_id: "+book_id + " " +"member_id :" +member_id +" amount:" + amount);
		
		int countSameBook = orderDao.countSameBook(book_id, member_id);
		if(countSameBook==0) {
			check = orderDao.addToCart(book_id, member_id,amount);
		}else {
			check = orderDao.addQuantity(book_id, member_id,amount);
		}		
		BookAspect.logger.info(BookAspect.logMsg + check);		
	}

	@Override
	public void addOrder(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpSession  session = request.getSession();
		
		String member_id = (String) session.getAttribute("id");
		int book_id = Integer.parseInt(request.getParameter("book_id"));
		int amount = Integer.parseInt(request.getParameter("amount"));
		BookAspect.logger.info(BookAspect.logMsg + "book_id: "+book_id + " " +"member_id :" +member_id +" amount:" + amount);
		
		int price = orderDao.getBookPrice(book_id);
		int addOrderCheck = orderDao.addOrder(member_id, book_id, (price*amount));
		int orderNumber = orderDao.selectMyOrderNum(member_id);
		int addOrderDetailCheck = orderDao.insertOrderDetail(orderNumber,book_id,amount,price);
		BookAspect.logger.info(BookAspect.logMsg + addOrderCheck + "," +orderNumber+"," +addOrderDetailCheck);
	}

	@Override
	public int cartDeleteButton(String[] book_id,String member_id) {		
		System.out.println(member_id + member_id);
		
		int check = orderDao.cartDeleteButton(book_id,member_id);
		
		return check;
	}

	@Override
	public int cartCount(String member_id) {
		int count = orderDao.cartCount(member_id);
		
		return count;
	}

	@Override
	public void NonMemberAddCart(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		
		String book_id = request.getParameter("book_id");
		String amount = request.getParameter("amount");
		if(amount == null) {
			amount = "1";
		}
		response.setCharacterEncoding("utf-8");
		Cookie cookie = new Cookie("book_id"+book_id,book_id);
		cookie.setMaxAge(60*60);
		response.addCookie(cookie);
		
		Cookie[] cookies = request.getCookies();
		ArrayList<String> book_identification = new ArrayList<String>();
		
		if(cookies != null) {
			System.out.println("cookies are not null");
			for(int i=0; i<cookies.length; i++) {
				System.out.println(cookies[i].getName() +" : " + cookies[i].getValue());
				if(cookies[i].getName().length() > 6) {
					if(cookies[i].getName().substring(0,7).equals("book_id")) {
						System.out.println("------------book_id not null---------------");
						book_identification.add(cookies[i].getValue());
					}
				}
			}
		}
		System.out.println(book_identification.size() + " ------------------book_identification size------------------");		
		ArrayList<OrderDto> cartList = new ArrayList<OrderDto>();
		OrderDto orderDto = null;
		
		for(int i=0; i<book_identification.size(); i++) {
			if(book_identification.get(i) == null || book_identification.get(i) == "") {
				continue;
			}
			System.out.println("orderservice 392line ----------- book id : " + book_identification.get(i));
			orderDto = new OrderDto();
			orderDto = orderDao.NonMemberAddCart(Integer.parseInt(book_identification.get(i)));
			orderDto.setQuantity(Integer.parseInt(amount));
			
			cartList.add(orderDto);
			BookAspect.logger.info(BookAspect.logMsg+orderDto.toString());
		}

		mav.addObject("cartList", cartList);
		mav.setViewName("order/cart");
	}

	@Override
	public void NonMemberOrderInfo(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		ArrayList<String> bookIdList = new ArrayList<String>();
		List<OrderDto> bookList = new ArrayList<OrderDto>();
		
		String member_id = null;
		if(member_id == null) {
			DateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date now = new Date();
			String nowDate = sdFormat.format(now);
			member_id = "NaM"+nowDate;
			
			Cookie cookie = new Cookie("member_id", member_id);
			cookie.setMaxAge(60*60);
			cookie.setPath("/");
			
			response.addCookie(cookie);
		}
		
		
		Cookie[] cookies = request.getCookies();
		String book_id = null;
		if(cookies != null) {
			System.out.println("cookies are not null");
			for(int i=0; i<cookies.length; i++) {
				System.out.println(cookies[i].getName() +" : " + cookies[i].getValue());
				if(cookies[i].getName().length() > 6) {
					if(cookies[i].getName().substring(0,7).equals("book_id")) {
						System.out.println("------------book_id not null---------------");
						bookIdList.add(cookies[i].getValue());
					}
				}
			}
		}
		 				
		MemberDto memberDto = new MemberDto();
		memberDto.setMember_id(member_id);
		memberDto.setName(request.getParameter("name"));
		memberDto.setZipcode(request.getParameter("zipcode"));
		memberDto.setEmail(request.getParameter("email"));
		memberDto.setAddr1(request.getParameter("addr1"));
		memberDto.setAddr2(request.getParameter("addr2"));
		memberDto.setPhone(request.getParameter("phone"));
		memberDto.setNon_member(0);
		memberDto.setSns_num(3);
		BookAspect.logger.info(BookAspect.logMsg + memberDto.toString());
		
		int checkMem = memberDao.NonMemberAdd(memberDto);
		BookAspect.logger.info(BookAspect.logMsg +  "checkMem:  "+ checkMem);
		
		if(checkMem > 0) {
			int checkOrder = 0;
			int total = 0;
			if(request.getParameter("total") == null || request.getParameter("total") == "") {
				total = orderDao.getBookPrice(Integer.parseInt(request.getParameter("book_id")));
				checkOrder = orderDao.NonMemberAddOrder(total,member_id);
			}else {
				checkOrder = orderDao.NonMemberAddOrder(Integer.parseInt(request.getParameter("total")),member_id);
			}
			
			BookAspect.logger.info(BookAspect.logMsg +  "checkOrder:  "+ checkOrder);
			int price = 0;
			int checkOrderDetail = 0;
			if(checkOrder > 0) {
				
				int order_id = orderDao.selectMyOrderNum(member_id);
				
				for(int i=0; i<cookies.length; i++) {
					if(cookies[i].getName().length() > 6) {
						if(cookies[i].getName().substring(0,7).equals("book_id")) {
							if(cookies[i].getValue() == null || cookies[i].getValue()=="") {
								continue;
							}
							book_id = cookies[i].getValue();
							price = orderDao.getBookPrice(Integer.parseInt(book_id));
							if(request.getParameter("quantity")==null || request.getParameter("quantity")=="") {
								checkOrderDetail = orderDao.NonMemberAddOrderDetail(order_id,book_id,1,price);
							}else {
								checkOrderDetail = orderDao.NonMemberAddOrderDetail(order_id,book_id,Integer.parseInt(request.getParameter("quantity")),price);
							}
							
							BookAspect.logger.info(BookAspect.logMsg +  "checkOrderDetail:  "+ checkOrderDetail);
						}
					}
				}
				
				if(checkOrderDetail>0) {
					//paymentComplete에 뿌릴정보 select
					OrderDto orderDto = new OrderDto();
					orderDto = orderDao.NonMemberGetOrderInfo(order_id);
					orderDto.setAddr1(request.getParameter("addr1"));
					orderDto.setAddr2(request.getParameter("addr2"));
					orderDto.setName(request.getParameter("name"));
					if(request.getParameter("total")==null || request.getParameter("total") == "") {
						orderDto.setTotal_price(orderDao.getBookPrice(Integer.parseInt(request.getParameter("book_id"))));
					}else {
						orderDto.setTotal_price(price);
					}
									
					//book_id 로 book_name & quantity -> bookList에 저장
					for(int i=0; i<bookIdList.size(); i++) {
						if(bookIdList.get(i) == null || bookIdList.get(i) == "") {
							continue;
						}
						int bookId = Integer.parseInt(bookIdList.get(i));
						System.out.println("book_id값 : " + bookId);
						OrderDto dto = new OrderDto();
						dto = orderDao.getPayInfoByBookId(bookId);
						bookList.add(dto);
						BookAspect.logger.info(BookAspect.logMsg +  "=============="+ dto.toString());
					}

					orderInfoSendEmail(member_id, orderDto, bookList);
			
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String transDate = format.format(new Date());
					try {
						Date date = format.parse(transDate);
						orderDto.setOrder_date(date);
					} catch (ParseException e) {	
						e.printStackTrace();
					}
		
					mav.addObject("orderDto", orderDto);
					mav.addObject("bookList", bookList);
					
					Cookie[] cookiesList = request.getCookies();
					if(bookList.size() != 0 && cookies != null) {
						System.out.println("Cookie Delete function");
						OrderDto oDto = new OrderDto();
						for(int i=0; i<bookIdList.size(); i++) {
							System.out.println("cookie 지우는 곳!!!!! " + bookIdList.get(i));
							if(bookIdList.get(i) != null || bookIdList.get(i) != "") {
								for(int j=0; j<cookiesList.length; j++) {
									if(cookiesList[j].getName().substring(7).equals(bookIdList.get(i))) {
										System.out.println(cookiesList[j].getName()+"============detlete book_id cookie================");
										Cookie cookie = new Cookie(cookiesList[j].getName(),"");
										cookie.setMaxAge(0);
										response.addCookie(cookie);
										System.out.println("북아이디지워짐~!~!~!~!~");
									}else if(cookiesList[j].getName().equals("member_id")) {
										member_id = null;
										Cookie cookie = new Cookie(cookiesList[j].getName(),"");
										cookie.setMaxAge(0);
										cookie.setPath("/");
										response.addCookie(cookie);
										System.out.println("member_id delete from cookie--------------");
									}
								}
							}
						}
					}
				}
			}
		}
		mav.setViewName("order/paymentComplete");	
	}

	@Override
	public void NonMemberDirectOrder(ModelAndView mav) {
		 Map<String, Object> map = mav.getModelMap();
	      HttpServletRequest request = (HttpServletRequest) map.get("request");
	      HttpServletResponse response = (HttpServletResponse) map.get("response");
	      HttpSession  session = request.getSession();

	      
	      String member_id = (String) session.getAttribute("id");
	      String book_id = request.getParameter("book_id");
	      String quantity = request.getParameter("amount");
	      
	      if(quantity==null || quantity == "") {
	         quantity = "1";
	      }
	      if(book_id != null) {
	    	   Cookie cookie = new Cookie("book_id"+book_id, book_id);
	 	      cookie.setMaxAge(60*60);
	 	      cookie.setPath("/");
	 	      response.addCookie(cookie);
	      }
   
//	      Cookie[] cookies = request.getCookies();
//	      if(cookies != null && member_id == null) {
//	         for(int i=0; i<cookies.length; i++) {
//	            if(cookies[i].getName().equals("cp_sessionid")) {
//	               member_id = "NaM"+cookies[i].getValue();
//	            }
//	         }
//	      }
	      System.out.println("=======NON MEMBER ID AND BOOKID====================" + member_id +" : "+ book_id);
	      
	      mav.addObject("member_id", member_id);
	      mav.addObject("book_id", book_id);
	      mav.addObject("quantity", quantity);
	}

	@Override
	public void nonMemberOrderDetailSearch(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		MemberDto memberDto = (MemberDto) map.get("memberDto");
		List<OrderDto> orderList = new ArrayList<OrderDto>();;
		
		System.out.println(memberDto.toString());
		
		String member_id = memberDto.getMember_id();
		
		BookAspect.logger.info(BookAspect.logMsg + "member_id : " + member_id);
		orderList = orderDao.nonMemberOrderDetailSearch(member_id);
		
		BookAspect.logger.info(BookAspect.logMsg + orderList.size());
		orderList.get(0).setAddr1(memberDto.getAddr1());
		orderList.get(0).setAddr2(memberDto.getAddr2());
		orderList.get(0).setName(memberDto.getName());
		
		mav.addObject("orderList", orderList);	
		mav.setViewName("member/nonMemberOrderDetailSearch");
		
	}

}
