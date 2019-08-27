package com.boogie.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.boogie.admin.service.AdminService;
import com.boogie.aop.BookAspect;

import com.boogie.bookInfo.dto.BookInfoDto;
import com.boogie.bookInfo.service.BookInfoService;
import com.boogie.customerCenter.dto.FaqBoardDto;
import com.boogie.customerCenter.dto.StoreMapDto;
import com.boogie.customerCenter.service.CustomerCenterService;
import com.boogie.email.Email;
import com.boogie.email.EmailSender;
import com.boogie.index.service.IndexService;
import com.boogie.member.dto.MemberDto;
import com.boogie.member.service.MemberService;
import com.boogie.order.dto.OrderDto;
import com.boogie.order.service.OrderService;
import com.boogie.recommend.service.RecommendService;
import com.boogie.review.service.ReviewService;
import com.boogie.search.dto.SearchDto;
import com.boogie.search.service.SearchService;

/**
 * @author : 이민호 2019. 8. 5.
 * @description: writeOrderInfo 함수 추가 - 주문정보 page에 주문했던 정보를 뿌려줌.
 */

@Controller
public class BookController {

	@Autowired
	private RecommendService recommendService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private BookInfoService bookInfoService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private Email email;
	@Autowired
	private CustomerCenterService customerCenterService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private AdminService adminService;

	// 관리자페이지-도서등록 에서 입력한 날짜를 받아올때 필요
	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(value = "/recommend/recommendMain.do", method = RequestMethod.GET)
	public ModelAndView recommendMain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		recommendService.recommendMain(mav);
		return mav;
	}

	@RequestMapping(value = "/recommend/recommendProcxy.do", method = RequestMethod.GET)
	public String recommendProcxy(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		String clientId = "v8R5FbX43_upxHbbKBRy";
		String clientSecret = "GAYVywiXFj";

		String bookName = request.getParameter("bookName");

		try {
			String text = URLEncoder.encode(bookName, "UTF-8");

			String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text; // xml 결과
			URL url = new URL(apiURL);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

			int responseCode = con.getResponseCode();

			BufferedReader br;

			if (responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else { // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}

			String inputLine;
			StringBuffer bufferResponse = new StringBuffer();

			while ((inputLine = br.readLine()) != null) {
				bufferResponse.append(inputLine);
			}
			br.close();

			// System.out.println(bufferResponse.toString());
			response.setContentType("application/xml;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(bufferResponse.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/member/register.do", method = RequestMethod.GET)
	public ModelAndView memberRegister(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("member/register");
	}

	@RequestMapping(value = "/member/registerOk.do", method = RequestMethod.GET)
	public ModelAndView memberRegisterOk(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		memberService.memberRegisterOk(mav);
		return mav;
	}

	@RequestMapping(value = "/member/idCheck.do", method = RequestMethod.GET)
	public @ResponseBody String idCheck(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		memberService.idDuplChk(mav);

		Map<String, Object> map = mav.getModelMap();
		String check = (String) map.get("check");

		return check;
	}

	@RequestMapping(value = "/member/content.do", method = RequestMethod.GET)
	public ModelAndView readContent1(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("check", request.getParameter("check"));
		mav.setViewName("member/content");
		return mav;
	}
	

	@RequestMapping(value = "/member/login.do", method = RequestMethod.GET)
	public ModelAndView memberLogin(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("member/login");
	}

	@RequestMapping(value = "/member/loginOk.do", method = RequestMethod.POST)
	public ModelAndView memberLoginOk(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.loginOk(mav);

		return mav;
	}

	@RequestMapping(value = "/member/KaKaologinOk.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaologinOk(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.memberKaKaologinOk(mav);

		return mav;
	}

	@RequestMapping(value = "/member/logout.do", method = RequestMethod.GET)
	public ModelAndView memberLogout(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("member/logout");
	}

	@RequestMapping(value = "/member/forgetId.do", method = RequestMethod.GET)
	public ModelAndView memberforgetId(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("member/forgetId");
	}

	@RequestMapping(value = "/member/forgetPassword.do", method = RequestMethod.GET)
	public ModelAndView memberforgetPassword(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("member/forgetPassword");
	}

	@RequestMapping(value = "/member/findId.do", method = RequestMethod.GET)
	public ModelAndView memberFindId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		memberService.findId(mav);

		Map<String, Object> map = mav.getModelMap();
		String id = (String) map.get("id");
		String name = (String) map.get("name");
		String userEmail = request.getParameter("email");
		int check = 0;
		if (id != null) {
			email.setContent(name + "님의 아이디는 " + id + " 입니다.");
			email.setReceiver(userEmail);
			email.setSubject(name + "님 아이디 찾기 메일입니다.");
			System.out.println(email.toString());
			emailSender.SendEmail(email);

			check = 1;
		} else {
			check = 0;
		}

		mav = new ModelAndView("member/findId");
		mav.addObject("check", check);
		return mav;
	}

	@RequestMapping(value = "/member/findPassword.do", method = RequestMethod.GET)
	public ModelAndView memberFindPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		memberService.findPassword(mav);

		Map<String, Object> map = mav.getModelMap();
		String temporaryPw = (String) map.get("temporaryPw");
		int check = (Integer) map.get("check");
		String id = request.getParameter("member_id");
		String userEmail = request.getParameter("email");
		String content = id + "님의 임시비밀번호는 " + temporaryPw + " 입니다." + "<br/>아래 링크를 이용해 새로운 비밀번호를 설정하세요.<br/>"
				+ "<a href='http://localhost:8181/homepage/member/memberEdit.do?id=" + id + "'>" + "새로운 비밀번호 설정"
				+ "</a>";

		if (check > 0) {
			email.setContent(content);
			email.setReceiver(userEmail);
			email.setSubject(id + "님 비밀번호 찾기 메일입니다.");

			System.out.println(email.toString());
			emailSender.SendEmail(email);

		}
		mav = new ModelAndView("member/findPassword");
		mav.addObject("check", check);
		return mav;
	}

	@RequestMapping(value = "/member/makePassword.do", method = RequestMethod.GET)
	public ModelAndView memberMakePassword(HttpServletRequest request, HttpServletResponse response) {

		return new ModelAndView("member/makePassword");
	}

	@RequestMapping(value = "/member/memberEdit.do", method = RequestMethod.GET)
	public ModelAndView memberEdit(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.memberEdit(mav);

		return mav;
	}

	@RequestMapping(value = "/member/memberEditOk.do", method = RequestMethod.GET)
	public ModelAndView memberEditOk(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.memberEditOk(mav);

		return mav;
	}

	@RequestMapping(value = "/member/KaKaoRegister.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaoRegister(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		String id = request.getParameter("id");
		mav.addObject("id", id);
		mav.setViewName("member/KaKaoRegister");

		return mav;
	}

	@RequestMapping(value = "/member/KaKaoRegisterOk.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaoRegisterOk(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.memberKaKaoRegisterOk(mav);
		return mav;
	}

	@RequestMapping(value = "/member/KaKaoEdit.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaoEdit(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.memberKaKaoEdit(mav);
		return mav;
	}

	@RequestMapping(value = "/member/KaKaoEditOk.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaoEditOk(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		memberService.memberKaKaoEditOk(mav);
		return mav;
	}

	@RequestMapping(value = "/member/withdrawal.do", method = RequestMethod.GET)
	public ModelAndView memberWithdrawal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("id", request.getParameter("id"));
		mav.setViewName("member/memberWithdrawal");

		return mav;
	}

	@RequestMapping(value = "/member/memberWithdrawalOk.do", method = RequestMethod.GET)
	public ModelAndView memberWithdrawalOk(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		memberService.memberWithdrawalOk(mav);
		return mav;
	}

	@RequestMapping(value = "/member/KaKaoWithdrawal.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaoWithdrawal(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("id", request.getParameter("id"));
		mav.setViewName("member/KaKaoWithdrawal");
		return mav;
	}

	@RequestMapping(value = "/member/KaKaoWithdrawalOk.do", method = RequestMethod.GET)
	public ModelAndView memberKaKaoWithdrawalOk(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		memberService.memberKaKaoWithdrawalOk(mav);
		return mav;
	}

	@RequestMapping(value = "/bestSeller/bestSellerMain.do", method = RequestMethod.GET)
	public ModelAndView bestSellerMain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		bookInfoService.bestSellerMain(mav);
		return mav;

	
	}	
	
	@RequestMapping(value = "/bestSeller/indexBestSeller.do", method = RequestMethod.GET)
	public @ResponseBody List<BookInfoDto> indexBestSeller(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();

		return bookInfoService.indexBestSeller(mav);
	}
	
	@RequestMapping(value = "/newBook/newBookMain.do", method = RequestMethod.GET)
	public ModelAndView newBookMain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		bookInfoService.newBookMain(mav);
		return mav;
	}	
	
	@RequestMapping(value = "/koreanBook/koreanBookMain.do", method = RequestMethod.GET)
	public ModelAndView koreanBookMain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		bookInfoService.koreanBookMain(mav);
		return mav;
	}
	
	@RequestMapping(value = "/member/nonMemberCheck.do", method = RequestMethod.GET)
	public ModelAndView nonMemberCheck(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		memberService.nonMemberOrderDetailSearch(mav);
		mav.setViewName("member/nonMemberOk");
		return mav;
	}
	
	
	@RequestMapping(value = "/member/nonMember.do", method = RequestMethod.GET)
	public ModelAndView nonMember(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		memberService.nonMemberOrderDetailSearch(mav);
		orderService.nonMemberOrderDetailSearch(mav);
		
		return mav;
	}
	

	@RequestMapping(value = "/member/searchOrder.do", method = RequestMethod.GET)
	public ModelAndView searchOrder(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		memberService.searchOrder(mav);
		orderService.nonMemberOrderDetailSearch(mav);
	
		return mav;
	}	
	
	/*다중검색
	 *  설명 : 기간, 가격대, 책이름, 저자, 출판사의 여러가지 조건에 맞는 검색결과를 처리하기위한 기능
	 */
	@RequestMapping(value = "/search/detailSearch.do", method = RequestMethod.GET)
	public ModelAndView detailSearchMain(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		searchService.detailSearch(mav);

		return mav;
	}

	//검색결과로 이동하기위한 설정
	@RequestMapping(value = "/search/searchOk.do", method = RequestMethod.GET)
	public ModelAndView detailSearchResult(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		searchService.searchResult(mav);

		return mav;
	}

	/* 다중검색 
	 * 설명 : 다중검색을 통한 결과창으로 이동하기위한 컨트롤러
	 * */
	@RequestMapping(value = "/search/multiOk.do", method = RequestMethod.GET)
	public ModelAndView multiResult(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		searchService.multiOk(mav);

		return mav;
	}


	@RequestMapping(value = "/order/cart.do", method = RequestMethod.GET)
	   public ModelAndView cartWrite(HttpServletRequest request, HttpServletResponse response) {
	      HttpSession  session = request.getSession();
	      String member_id = (String) session.getAttribute("id");
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      mav.addObject("response",response);
	      
	      Cookie[] getCookie = request.getCookies();
	      String book_id  = request.getParameter("book_id");
	      System.out.println(book_id );
	      
	      if(member_id != null) {
	    	  System.out.println("going to addToCart");
	    	  orderService.addToCart(mav);
	    	  System.out.println("going to getCartInfo");
	    	  orderService.getCartInfo(mav);
	      }else if(member_id == null && getCookie != null) {   //비회원
	    	  System.out.println("gooing to NonMemberAddCart");
	         orderService.NonMemberAddCart(mav);
	      }
	      return mav;
	   }

	@RequestMapping(value="/order/orderForm.do", method=RequestMethod.GET)
	   public ModelAndView orderFormWrite(HttpServletRequest request, HttpServletResponse response) {
	      HttpSession  session = request.getSession();
	      String member_id = (String) session.getAttribute("id");
	      
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      mav.addObject("response",response);
	      
	      if(member_id != null && member_id.length() > 3) {
	    	  if(member_id.substring(0,4)=="NaM") {
		    	  member_id = null;
		      }
	      }

	      String book_id  = request.getParameter("book_id");
	      
	      if(book_id != null && member_id != null) {   //회원아이디가잇고 바로구매 버튼클릭시
	         orderService.addOrder(mav);
	      }else if(member_id == null || member_id == ""){
	         orderService.NonMemberDirectOrder(mav);
	      }
	      orderService.getOrderForm(mav); //회원아이디 있고 장바구니에서 넘어올때
	      
	      mav.setViewName("order/orderForm");
	      
	      return mav;
	   }

	@RequestMapping(value = "/order/payProgress.do", method = RequestMethod.GET)
	public ModelAndView payProgress(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("order/payProgress");
	}

	@RequestMapping(value = "/order/paymentComplete.do", method = RequestMethod.POST)
	public ModelAndView writeOrderInfo(HttpServletRequest request, HttpServletResponse response) {
		HttpSession  session = request.getSession();
		String member_id = (String) session.getAttribute("id");
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
	    mav.addObject("response",response);
	    
		if(member_id == null || member_id.substring(0,4) =="NaM") {
			orderService.NonMemberOrderInfo(mav);
		}else {
			orderService.writeOrderInfo(mav);
		}
		

		return mav;
	}

	
	@RequestMapping(value="/order/cartDelete.do", method=RequestMethod.POST)
	public void cartDeleteButton(HttpServletRequest request, HttpServletResponse response) {
		HttpSession  session = request.getSession();
		String member_id = (String) session.getAttribute("id");
		int check = 0;
		String[] deleteList = null;
		
		Enumeration em = request.getParameterNames();
		while(em.hasMoreElements()){
		    String parameterName = (String)em.nextElement();
		    String parameterValue = request.getParameter(parameterName);
		    deleteList = request.getParameterValues(parameterName);
		    if(deleteList != null){
		         for(int i=0; i< deleteList.length; i++){
		             System.out.println("array_" + parameterName + "=" + deleteList[i]);	             
		         }
		         BookAspect.logger.info(BookAspect.logMsg + deleteList.length);
		    } else {
		         System.out.println("매개변수로 아무것도 넘어오지 않았습니다.");
		}
		    
		
		check = orderService.cartDeleteButton(deleteList, member_id);
		
		if(check != 0) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(check);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("cart delete failed.");
			}
		}
	}
	@RequestMapping(value="/order/cartCount.do",method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public void cartCount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession  session = request.getSession();
		String member_id = (String) session.getAttribute("id");
		
		int count = orderService.cartCount(member_id);
		if(count >= 0) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("cartCount에 문제생김");
		}
	}
	
	@RequestMapping(value="/book/bookInfo.do", method=RequestMethod.GET)
	public ModelAndView writeBookInfo(HttpServletRequest request, HttpServletResponse response) {
		// 쿠키생성
		/* 책 상세 페이지로 이동하면 쿠키가 생성된다. */
				String book_id = request.getParameter("book_id");//bookid를 받는다.
				Cookie[] getCookie = request.getCookies();//local에 있는 모든 쿠키를 받아와 배열에 저장한다.
				int cookiecnt = getCookie.length;//받아온 쿠키의 갯수를 저장한다.

				Cookie setCookie = new Cookie("id" + book_id, book_id);//쿠키의 이름과 value설정
				setCookie.setMaxAge(60 * 60);//쿠키의 유지시간설정
				setCookie.setPath("/");//쿠키의 저장위치 설정
				response.addCookie(setCookie);//쿠키를 저장하기 위해 설정
				
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("response", response); // 쿠키 설정으로인해 추가
		bookInfoService.BookInfoMain(mav);
		
		return mav;
	}

	@RequestMapping(value = "/customerCenter/storeMap.do", method = RequestMethod.GET)
	public ModelAndView storeMap(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		mav.setViewName("customerCenter/storeMap");
		return mav;
	}

	@RequestMapping(value = "/customerCenter/customerService.do", method = RequestMethod.GET)
	public ModelAndView customerService(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		mav.setViewName("customerCenter/customerService");
		return mav;
	}

	@RequestMapping(value = "/customerCenter/testPage.do", method = RequestMethod.GET)
	public ModelAndView customerTesting(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		mav.setViewName("customerCenter/testPage");
		return mav;
	}

	@RequestMapping(value = "/customerCenter/storeMapChange.do", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public void storeMapChange(HttpServletRequest request, HttpServletResponse response) {

		System.out.println(request.getParameter("location_code"));
		int location_code = Integer.parseInt(request.getParameter("location_code"));
		StoreMapDto storeMapDto = customerCenterService.getLatAndLongt(location_code);
		if (storeMapDto != null) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(storeMapDto.getLat() + "/");
				response.getWriter().print(storeMapDto.getLongt() + "/");
				response.getWriter().print(storeMapDto.getStore_addr() + "/");
				response.getWriter().print(storeMapDto.getStore_name() + "/");
				response.getWriter().print(storeMapDto.getStore_phone());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("storeMap에서 아무것도 가져오지 못함.");
		}
	}

	@RequestMapping(value = "/customerCenter/questionCode.do", method = RequestMethod.GET, produces = "application/text; charset=utf8")
	public void getFaq(HttpServletRequest request, HttpServletResponse response) {
		int question_code = Integer.parseInt(request.getParameter("question_code"));
		System.out.println(question_code);
		List<FaqBoardDto> faqList = customerCenterService.getFaq(question_code);
		if (faqList.size() != 0) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				FaqBoardDto fb = null;
				for (int i = 0; i < faqList.size(); i++) {
					fb = new FaqBoardDto();
					fb = faqList.get(i);
					response.getWriter().print(fb.getBoard_number() + "|");
					response.getWriter().print(fb.getQuestion() + "|");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("faqBoardDto에서 아무것도 가져오지 못함.");
		}
	}

	
	@RequestMapping(value="/customerCenter/getAnswer.do",method=RequestMethod.GET, produces = "application/text; charset=utf8")
	public void getAnswer(HttpServletRequest request, HttpServletResponse response) {
		int board_number = Integer.parseInt(request.getParameter("board_number"));
		System.out.println(board_number);
		String answer = customerCenterService.getAnswer(board_number);
		if (answer != null) {
			try {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(answer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("answer을 가져오지 못함.");
		}
	}

	
	/* 검색어 자동완성기능
	 * 설명 : 검색창에 책이름을 작성하면 검색창밑에 관련단어를 보여주고 클릭하여 완성할 수 있는 기능
	 * 구동방식 : 
	 * 		1. 데이터베이스에 저장된 모든 책이름 조회
	 * 		2. id로 조회된 dto를 받아온다.
	 * 		3. 책이름을 String배열에 저장
	 * 		4. 책이름만 저장된 배열을 뷰에 전송
	 */	
	@ResponseBody
	@RequestMapping(value = "/search/autocomplet.do", method = RequestMethod.GET)
	public String[] autocomplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		List<SearchDto> book_dto = searchService.autocomplete(mav);//List로 책이름을 받아온다.
		String[] BookNames = new String[book_dto.size()];//List의 갯수만큼 String 배열 생성
		for (int i = 0; i < book_dto.size(); i++) {
			BookNames[i] = book_dto.get(i).getBook_name();// 책이름만 뽑아와서 배열에 저장한다.
		}

		return BookNames;//DB의 모든 책이름을 배열에 저장한다.
	}

	@RequestMapping(value = "/review/reviewWrite.do", method = RequestMethod.GET)
	public void reviewWrite(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("response", response);

		reviewService.reviewWrite(mav);
	}

	/* 다권검색기능
	 * 설명 : 여러개의 책이름을 \n으로 구분하여 받아와 한번에 검색할수 있는기능
	 *  */
	@RequestMapping(value = "/search/Several.do", method = RequestMethod.POST)
	public ModelAndView severalSearch(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		searchService.severalSearch(mav);
		return mav;
	}

	@RequestMapping(value = "/index/index.do", method = RequestMethod.GET)
	public ModelAndView IndexStart(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		System.out.println("controller");

		indexService.indexGetInfo(mav);

		return mav;
	}

	@RequestMapping(value = "/admin/admin.do", method = RequestMethod.GET)
	public ModelAndView admin(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);

		adminService.adminMain(mav);

		return mav;
	}


	 @RequestMapping(value="/admin/adminMemMng.do", method=RequestMethod.GET)
	   public ModelAndView adminMemMng(HttpServletRequest request, HttpServletResponse response, MemberDto memberDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      mav.addObject("MemberDto", memberDto);
		 
	      adminService.adminMemMng(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminMemSearchId.do", method=RequestMethod.GET)
	   public ModelAndView adminMemSearchId(HttpServletRequest request, HttpServletResponse response, MemberDto memberDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      mav.addObject("MemberDto", memberDto);
		 
	      adminService.adminMemSearchId(mav);
	      
	      return mav;
	   }
	  
	 @RequestMapping(value="/admin/adminMemMngEdit.do", method=RequestMethod.GET)
	   public ModelAndView adminMemMngEdit(HttpServletRequest request, HttpServletResponse response, MemberDto memberDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      mav.addObject("memberDto", memberDto);
	      
	      adminService.adminMemMngEdit(mav);
	      
	      return mav;

	}
	 	 
	 @RequestMapping(value="/admin/adminBookMng.do", method=RequestMethod.GET)
	   public ModelAndView adminBookMng(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);
	      
	      adminService.adminBookMng(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminBookMngInsert.do", method=RequestMethod.GET)
	   public ModelAndView adminBookMngInsert(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);
	      
	      adminService.adminBookMngInsert(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminBookEditMng.do", method=RequestMethod.GET)
	   public ModelAndView adminBookEditMng(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);

	      adminService.adminBookEditMng(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminBookMngUpdate.do", method=RequestMethod.GET)
	   public ModelAndView adminBookMngUpdate(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);
	      
	      adminService.adminBookMngUpdate(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminBookDelMng.do", method=RequestMethod.GET)
	   public ModelAndView adminBookDelMng(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);

	      adminService.adminBookDelMng(mav);
	      
	      return mav;
	   } 
	 //FAQ관리
	 @RequestMapping(value="/admin/adminFAQRegMng.do", method=RequestMethod.GET)
	   public ModelAndView adminFAQRegMng(HttpServletRequest request, HttpServletResponse response){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      
	      adminService.adminFAQRegMng(mav);
	      
	      return mav;
	   }
	 //FAQ등록
	 @RequestMapping(value="/admin/adminFAQMng.do", method=RequestMethod.GET)
	   public ModelAndView adminFAQMng(HttpServletRequest request, HttpServletResponse response){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request", request);
	      
	      adminService.adminFAQMng(mav);
	      
	      return mav;
	   }
	 
	 //FAQ 질문유형 변경
	 @RequestMapping(value="/admin/adminFAQStat.do", method=RequestMethod.GET)
	   public ModelAndView adminFAQStat(HttpServletRequest request, HttpServletResponse response, FaqBoardDto faqBoardDto){
		  ModelAndView mav = new ModelAndView();
		  mav.addObject("request", request);
		  mav.addObject("faqBoardDto", faqBoardDto);
		  
		  adminService.adminFAQStat(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminFAQEditMng.do", method=RequestMethod.GET)
	   public ModelAndView adminFAQEditMng(HttpServletRequest request, HttpServletResponse response, FaqBoardDto faqBoardDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("faqBoardDto",faqBoardDto);

	      adminService.adminFAQEditMng(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminFAQUpdate.do", method=RequestMethod.GET)
	   public ModelAndView adminFAQUpdate(HttpServletRequest request, HttpServletResponse response, FaqBoardDto faqBoardDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("faqBoardDto",faqBoardDto);

	      adminService.adminFAQUpdate(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminFAQDelMng.do", method=RequestMethod.GET)
	   public ModelAndView adminFAQDelMng(HttpServletRequest request, HttpServletResponse response, FaqBoardDto faqBoardDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("faqBoardDto",faqBoardDto);

	      adminService.adminFAQDelMng(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminOrdSearchMng.do", method=RequestMethod.GET)
	   public ModelAndView adminOrdSearchMng(HttpServletRequest request, HttpServletResponse response, OrderDto orderDto){
		  ModelAndView mav = new ModelAndView();
		  mav.addObject("request", request);
		  mav.addObject("orderDto", orderDto);
		  
		  adminService.adminOrdSearchMng(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminOrdMng.do", method=RequestMethod.GET)
	   public ModelAndView adminOrdMng(HttpServletRequest request, HttpServletResponse response, OrderDto orderDto){
		  ModelAndView mav = new ModelAndView();
		  mav.addObject("request", request);
		  mav.addObject("orderDto", orderDto);
		  
		  adminService.adminOrdMng(mav);
	      
	      return mav;
	   }
	 
	 
	 @RequestMapping(value="/admin/adminOrdStat.do", method=RequestMethod.GET)
	   public ModelAndView adminOrdStat(HttpServletRequest request, HttpServletResponse response, OrderDto orderDto){
		  ModelAndView mav = new ModelAndView();
		  mav.addObject("request", request);
		  mav.addObject("orderDto", orderDto);
		  
		  adminService.adminOrdStat(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminChangeOrdStat.do", method=RequestMethod.GET)
	   public ModelAndView adminChangeOrdStat(HttpServletRequest request, HttpServletResponse response, OrderDto orderDto){
		  ModelAndView mav = new ModelAndView();
		  mav.addObject("request", request);
		  mav.addObject("orderDto", orderDto);
		  
		  adminService.adminChangeOrdStat(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminDelOrder.do", method=RequestMethod.GET)
	   public ModelAndView adminDelOrder(HttpServletRequest request, HttpServletResponse response, OrderDto orderDto){
		  ModelAndView mav = new ModelAndView();
		  mav.addObject("request", request);
		  mav.addObject("orderDto", orderDto);
		  
		  adminService.adminDelOrd(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminBookRegMng.do", method=RequestMethod.GET)
	   public ModelAndView adminBookRegMng(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);
	      
	      adminService.adminBookMngInsert(mav);
	      
	      return mav;
	   }
	 
	 @RequestMapping(value="/admin/adminBookSearchMng.do", method=RequestMethod.GET)
	   public ModelAndView adminBookSearchMng(HttpServletRequest request, HttpServletResponse response, BookInfoDto bookInfoDto){
		 System.out.println("search controller");
	      ModelAndView mav = new ModelAndView();
	      mav.addObject("request",request);
	      mav.addObject("bookInfoDto",bookInfoDto);
	      
	      adminService.adminBookSearchMng(mav);
	      
	      return mav;
	   }


	// 아직 안만듬 ajax쓸예정이고 안된다면 지워야함.
	@RequestMapping(value = "/admin/adminMemMngEditOk.do", method = RequestMethod.GET)
	public ModelAndView adminMemMngEditOk(HttpServletRequest request, HttpServletResponse response,
			MemberDto memberDto) {
		System.out.println("controller");
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("memberDto", memberDto);

		adminService.adminMemMngEditOk(mav);

		return mav;
	}

	@RequestMapping(value = "/admin/adminMemMngDel.do", method = RequestMethod.GET)
	public ModelAndView adminMemMngDel(HttpServletRequest request, HttpServletResponse response, MemberDto memberDto) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		mav.addObject("memberDto", memberDto);

		adminService.adminMemMngDel(mav);

		return mav;
	}





	/* 저장된 쿠키 조회기능
	 * 설명 : local에 저장되어 있는 쿠키를 조회하여 최근본상품에 뿌려주기 위한 기능 
	 * */
	@ResponseBody
	@RequestMapping(value = "/search/getcookies.do", method = RequestMethod.GET)
	public List<SearchDto> getCookiees(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("request", request);
		
		List<SearchDto> list=searchService.getCookies(mav);
		
		
	return list;	
	}
	
	/* 쿠키삭제기능
	 * 설명 : 최근본상품에 대해 저장된 쿠키를 삭제하는 기능
	 * 
	 * 구동방식 : 
	 * 1. book_id를 받아온다.
	 * 2. 같은 이름의 쿠키를 만들면고 만든이름의 쿠키의 유효시간을 0으로 설정한다.
	 * 3. response객체에 설정된 쿠키를 추가하면 쿠키를 삭제할수 있다.
	 *  */
	@RequestMapping(value = "/search/delcookies.do", method = RequestMethod.GET)
	public void delCookiees(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		String id=request.getParameter("id");
		id="id"+id;//저장되어 있는 쿠키명을 맞춰주기 위해서 id값 설정
		Cookie[] cookies = request.getCookies();
		
		for(int i=0; i< cookies.length; i++){
			System.out.println(cookies[i].getName());//받아온 쿠키의 이름 조회
			if(cookies[i].getName().equals(id)) {//저장된 쿠키의 이름과 요청페이지의 id값과 같은 쿠키 필터링
			Cookie cookie=new Cookie(cookies[i].getName(),"");//같은이름으로 쿠키 생성
			cookie.setPath("/");//쿠키 저장위치 설정
			cookie.setMaxAge(0); // 유효시간을 0으로 설정
			response.addCookie(cookie); // 응답 헤더에 추가

			}
		}
		
	}
}
