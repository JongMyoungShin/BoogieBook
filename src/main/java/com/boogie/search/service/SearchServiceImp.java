package com.boogie.search.service;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.boogie.aop.BookAspect;
import com.boogie.search.dao.SearchDao;
import com.boogie.search.dto.SearchDto;

@Component
public class SearchServiceImp implements SearchService {

	@Autowired
	private SearchDao searchDao;

	/* 상세검색시 이동할 페이지 경로 설정 */
	@Override
	public void detailSearch(ModelAndView mav) {
		mav.setViewName("search/detail_search");//이동할 페이지 설정
	}

	/* 검색결과처리
	 * 
	 * 
	 *  */
	@Override
	public void searchResult(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");

		String keyword = request.getParameter("keyword"); //검색창에 입력한 값을 받아온다.

		BookAspect.logger.info(BookAspect.logMsg + "keyword: " + keyword);//입력값 확인
		String pageNumber = request.getParameter("pageNumber");//현재 페이지의 위치를 받아온다.

		int boardSize = 5; //화면에 표시할 갯수

		if (pageNumber == null || pageNumber == "")
			pageNumber = "1";//페이지 번호가 없으면 1로 설정한다.

		int currentPage = Integer.parseInt(pageNumber);//현재 위치의 페이지를 설정한다.
		int startRow = (currentPage - 1) * boardSize + 1;// 1-1=0*10=0+1=1//시작
		int endRow = currentPage * boardSize;// 1*10=10//끝

		int count = 0;
		System.out.println("pagenumger" + pageNumber + "current" + currentPage + "start" + startRow + "end" + endRow);
		List<SearchDto> searchResult = null;
		List<SearchDto> searchPageResult = null;

		searchResult = searchDao.keywordSearch(keyword);//키워드에 맞는 값들의 검색결과를 받아온다.
		count = searchResult.size();//리스트의 크기를 저장한다. 
		
		//키워드 값에 따른 결과를 시작페이지부터 마지막페이지까지 계산해서 보여주기 위해
		searchPageResult = searchDao.pageList(keyword, startRow, endRow);
		
		
		if (keyword == "") {//입력된 키워드가 없을시 처리조건
			count = 0;//조회할 내용이 없으므로 갯수는 0
			searchResult = null; // 검색결과는 없음
		}

		mav.addObject("searchPageResult", searchPageResult);//페이지별 검색결과리스트 객체
		mav.addObject("searchResult", searchResult);//전체 검색결과 갯수를 알기위해서
		mav.addObject("currentPage", currentPage);//현제 페이지
		mav.addObject("boardSize", boardSize);//현재 페이지에 표시될 크기
		mav.addObject("count", count);//검색결과의 전체리스트
		mav.addObject("keyword", keyword);//검색어

		mav.setViewName("search/searchOk");//검색결과창으로 이동하는 경로
	}

	/* 다중검색처리 */
	@Override
	public void multiOk(ModelAndView mav) throws ParseException {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String type = request.getParameter("type");
		String book_name = request.getParameter("book_name");
		String author = request.getParameter("author");
		String publisher = request.getParameter("publisher");
		String pageNumber = request.getParameter("pageNumber");

		String year01 = request.getParameter("year01");
		String month01 = request.getParameter("month01");
		String day1 = year01 + "-" + month01 + "-" + "01";//시작 날짜 설정

		String year02 = request.getParameter("year02");
		String month02 = request.getParameter("month02");
		String day2 = year02 + "-" + month02 + "-" + "01";//마지막 날짜 설정
		BookAspect.logger.info(BookAspect.logMsg + "day1: " + day1 + "\t day2" + day2);

		String price01 = request.getParameter("price01");//시작 가격설정
		String price02 = request.getParameter("price02");//최종 범위 가격설정

		if (price01 == null || price01 == "") {//시작 가격설정이 없을시
			price01 = "0";
		}
		if (price02 == null || price02 == "") {//마지막 가격설정
			price02 = "999999";
		}
		int startprice = Integer.parseInt(price01);
		int endprice = Integer.parseInt(price02);

		int boardSize = 5;

		if (pageNumber == null || pageNumber == "")
			pageNumber = "1";

		int currentPage = Integer.parseInt(pageNumber);
		int startRow = (currentPage - 1) * boardSize + 1;// 1-1=0*10=0+1=1
		int endRow = currentPage * boardSize;// 1*10=10

		int count = 0;//조회결과 수

		count = searchDao.searchCount(type, book_name, author, publisher, day1, day2, startprice, endprice);
		BookAspect.logger.info(BookAspect.logMsg + "count: " + count);
		List<SearchDto> searchPageResult = null;//조회 결과를 저장하기위한 리스트 설정
		searchPageResult = searchDao.multiPageList(type, book_name, author, publisher, startRow, endRow, day1, day2,
				startprice, endprice);//조회하기 위한 조건들을 파라메타로 전송한다.

		mav.addObject("searchPageResult", searchPageResult);
		mav.addObject("count", count);
		mav.addObject("currentPage", currentPage);

		mav.addObject("boardSize", boardSize);
		mav.addObject("type", type);
		mav.addObject("book_name", book_name);
		mav.addObject("author", author);
		mav.addObject("publisher", publisher);

		mav.addObject("year01", year01);
		mav.addObject("month01", month01);
		mav.addObject("year02", year02);
		mav.addObject("month02", month02);
		mav.addObject("price02", price02);
		mav.addObject("price01", price01);

		mav.setViewName("search/multiOk");
	}

	/* 자동완성기능 */
	@Override
	public List<SearchDto> autocomplete(ModelAndView mav) {
		List<SearchDto> list = searchDao.listAll2(); //book테이블에 있는 전체 내용을 list로 가져옴
		return list;
	}

	/* 다권검색 */
	@Override
	public void severalSearch(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		String content = request.getParameter("content");	//textarea에 있는 내용을 받아온다.
		BookAspect.logger.info(BookAspect.logMsg + "content: " + content);
		content = content.trim();//앞뒤에 있는 공백문자를 제거하기위해서 trim함수 이용
		String[] keyword = content.split("\n");//split함수를 이용 엔터키로 구분하여 각각의 키워드별로 배열에 저장한다.

		for (int i = 0; i < keyword.length; i++) {
			keyword[i] = keyword[i].trim();//키워드별로 공백을 제거한다.
		}

		BookAspect.logger.info(BookAspect.logMsg + "keywords: " + keyword.length);
		HashMap<Integer, List<SearchDto>> listMap = new HashMap<Integer, List<SearchDto>>();

		//map을 활용하지 않으면 for문 사용하기 어려움
		//키워드 갯수만큼만 조회하고 전송되어야하므로 hashMap을 이용하였음
		for (int i = 0; i < keyword.length; i++) {//입력된 키워드만큼 조회하기위해서
			listMap.put(i, searchDao.keywordSearch(keyword[i]));//0에 저장된 키워드의 검색결과를 hashmap에 저장한다.
			mav.addObject("searchResult" + i, listMap.get(i));//저장된 값을 뷰에 전송한다.
			mav.addObject("count" + i, listMap.get(i).size());//각각의 크기만큼 전송한다.
			mav.addObject("keyword" + i, keyword[i]);//각각 저장된 키워드를 전송한다.
		}
		mav.setViewName("search/severalSearchOk");
	}

	/* 저장된 쿠키를 받아 처리하는 부분 */
	public List<SearchDto> getCookies(ModelAndView mav) {
		Map<String, Object> map = mav.getModelMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		ArrayList<SearchDto> list = new ArrayList<SearchDto>();

		Cookie[] getCookie = request.getCookies(); //전체 쿠키를 받아오는 부분
		SearchDto dto = new SearchDto();//조회된 결과를 dto로 저장하기위하여 설정
		if (getCookie != null) {//저장된 쿠키가 null이 아닐때의 처리
			for (int i = 0; i < getCookie.length; i++) {//받아온 쿠키만큼 반복수행
				String name = getCookie[i].getName(); // 쿠키 이름 가져오기
				String check = ""; //쿠키이름의 앞에 두자리를 저장하기위한 변수선언
				check = name.substring(0, 2);//쿠키이름의 앞에 2자리를 뽑아오기 위한 함수
				if (check != "" || check!=null) {
					if (check.equals("id")) {//앞 두자리가 id로 되어있을시 처리조건
						String book_id = getCookie[i].getValue(); // 쿠키에 저장된 book_id값 가져오기
						System.out.println(book_id);
						dto = searchDao.getOneBook(book_id);//id값을 통해 조회된 결과를 dto로 저장
						System.out.println(dto.toString());
						list.add(dto);//각각의 리스트로 저장
					}
				}
			}
		}
		return list;
	}
}
