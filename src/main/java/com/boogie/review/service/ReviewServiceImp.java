package com.boogie.review.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.boogie.aop.BookAspect;
import com.boogie.review.dao.ReviewDao;

@Component
public class ReviewServiceImp implements ReviewService {
	
	@Autowired
	private ReviewDao reviewDao;
	
	@Override
	public void reviewWrite(ModelAndView mav){
		
		Map<String, Object> map = mav.getModelMap();
		HashMap<String, Object> hMap = new HashMap<String, Object>();
		
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		HttpServletResponse response = (HttpServletResponse) map.get("response");
		
		String id = request.getParameter("id");
		int book_id = Integer.parseInt(request.getParameter("book_id"));
		String content = request.getParameter("review_content");
		int rate = Integer.parseInt(request.getParameter("rate"));

		hMap.put("id",id);
		hMap.put("book_id",book_id);
		hMap.put("review_content",content);
		hMap.put("rate",rate);
		
		int check = reviewDao.reviewWrite(hMap);
		
		BookAspect.logger.info(BookAspect.logMsg + "check: " + check);
		
		if(check>0)
		{	
			try {
			response.setContentType("application/text;charset=UTF-8");
			String str = id+","+book_id+","+content+","+rate;
			PrintWriter out = response.getWriter();
			out.print(str);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
