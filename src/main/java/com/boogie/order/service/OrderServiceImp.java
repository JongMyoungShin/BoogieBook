package com.boogie.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.boogie.aop.BookAspect;
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
	
	@Override
	public void getCartInfo(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String member_id = request.getParameter("member_id");
		BookAspect.logger.info(BookAspect.logMsg + "member_id:" + member_id);
		
		if (member_id == null) {
			System.out.println("장바구니에 존재하는 Item이 없습니다.");
			member_id = "minho";
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
			
		String total = request.getParameter("total");
		String member_id = request.getParameter("member_id");
		BookAspect.logger.info(BookAspect.logMsg + "total: " + total + "member_id" + member_id);	
		
		MemberDto memberDto = new MemberDto();
		memberDto =  orderDao.getOrderForm(member_id);
		BookAspect.logger.info(BookAspect.logMsg + memberDto.toString());
		
		mav.addObject("member_id", member_id);
		mav.addObject("memberDto",memberDto);
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
		
		String member_id = request.getParameter("member_id");
		String total = request.getParameter("total");
		BookAspect.logger.info(BookAspect.logMsg + "주문정보 member_id : "+ member_id + "total:" + total);
		
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
		
		//주문이 완료되면 DB에서 장바구상품을 삭제시킨다.
		int check = orderDao.deleteFromCart(member_id);
		BookAspect.logger.info(BookAspect.logMsg + "check : " +check);
		
		mav.addObject("orderDto", oDto);
		mav.addObject("bookList", bookList);
		mav.setViewName("order/paymentComplete");
	}

}
