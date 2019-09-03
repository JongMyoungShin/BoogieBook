package com.boogie.recommend.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.boogie.aop.BookAspect;
import com.boogie.recommend.dao.RecommendDao;
import com.boogie.recommend.dto.RecommendInterestDto;
import com.boogie.recommend.dto.RecommendMarkDto;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * @Author : 나다윤
 * @Date : 2019. 8. 1.
 * @Description :
 */

@Component
public class RecommendServiceImp implements RecommendService {

	@Autowired
	private RecommendDao recommandDao;

	@Override
	public void recommendMain(ModelAndView mav)
	{
		Map<String, Object> map = mav.getModelMap();

		RecommendInterestDto interestDto = new RecommendInterestDto();

		HttpServletRequest request = (HttpServletRequest) map.get("request");

		
		String id = request.getParameter("id");
		

		if(id!=null) {
		String interest = recommandDao.getInterest(id);
		BookAspect.logger.info(BookAspect.logMsg + "interest: " + interest);

		// 관심분야 도서 가져오기 : 사용자 회원 가입 시 입력했던 관심 분야로 도서 디비에서 해당 분야의 도서중 1권을 랜덤 추출
		interestDto = recommandDao.getBookInterest(interest);
		BookAspect.logger.info(BookAspect.logMsg + interestDto.toString());
		}

		// 평점 순으로 추천 : 사용자가 입력한 리뷰로 평점 평균을 내어 상위 4개를 가져옴
		List<String> markBookName = recommandDao.getMarkBookName();		// 평점 평균 낸 상위 4권 책의 book_id를 가져옴
		BookAspect.logger.info(BookAspect.logMsg + "markBookName.size:" + markBookName.size());

		List<Float> markList = recommandDao.getMarkList();		// 상위 평점 평균 4개를 가져옴
		BookAspect.logger.info(BookAspect.logMsg + "markList.size:" + markList.size());

		List<RecommendMarkDto> markBookList = recommandDao.getMarkBookList(markBookName);	// 위에서 가져온 book_id를 가지고 해당 책 정보를 가져옴
		BookAspect.logger.info(BookAspect.logMsg + "markBookList.size:" + markBookList.size());

		
		// 별 모양 아이콘 이미지를 뿌려주기 위해 각 평점에 적합한 이미지 링크를 String 배열에 넣음.
		String[] markBookIcon = new String[4];
		for (int i = 0; i < 4; i++) 
		{
			if (markList.get(i) > 0 && markList.get(i) < 1) {
				markBookIcon[i]="/resources/images/mark/05.PNG";
			} else if (markList.get(i) == 1) {
				markBookIcon[i]="/resources/images/mark/1.PNG";
			} else if (markList.get(i) > 1 && markList.get(i) < 2) {
				markBookIcon[i]="/resources/images/mark/15.PNG";
			} else if (markList.get(i) == 2) {
				markBookIcon[i]="/resources/images/mark/2.PNG";
			} else if (markList.get(i) > 2 && markList.get(i) < 3) {
				markBookIcon[i]="/resources/images/mark/25.PNG";
			} else if (markList.get(i) == 3) {
				markBookIcon[i]="/resources/images/mark/3.PNG";
			} else if (markList.get(i) > 3 && markList.get(i) < 4) {
				markBookIcon[i]="/resources/images/mark/35.PNG";
			} else if (markList.get(i) == 4) {
				markBookIcon[i]="/resources/images/mark/4.PNG";
			} else if (markList.get(i) > 4 && markList.get(i) < 5) {
				markBookIcon[i]="/resources/images/mark/45.PNG";
			} else if (markList.get(i) == 5) {
				markBookIcon[i]="/resources/images/mark/5.PNG";
			}
		}
		
		// 사용자 기반 추천을 위한 변수 선언 - 총 2권을 추천한다.
		List<String[]> reviewForRecommand = new ArrayList<String[]>();
		String[] recommend_imgs = new String[2];
		String[] recommend_imgs_book_id = new String[2];
		
		//사용자 기반 추천 기능
		if(id!=null) // 로그인 했을 경우에만 해당 로직을 돈다.
		{
			
			// 추천에 필요한 데이터를 가져옴 - 사용자 아이디, 책 아이디, 평점
			reviewForRecommand = recommandDao.getReview();
			BookAspect.logger.info(BookAspect.logMsg+"listSize: "+reviewForRecommand.size());

			
			// 협업 필터링 알고리즘에 필요한 데이터 형식으로 파일 생성  
			// (파일을 생성까지 하지 않고 바로 데이터를 넘기는 방식으로 구현하고 싶었으나 방법을 못찾고 파일을 생성해서 넘기는 방법으로 구현)
			File file = new File("data.csv");
		
			// 해당 파일이 이미 존재하면 삭제
			if(file.exists()){
	            if(file.delete()){
	                System.out.println("파일삭제 성공");
	            }else{
	                System.out.println("파일삭제 실패");
	            }
	        }else{
	            System.out.println("파일이 존재하지 않습니다.");
	        }
		
			try
			{
				BufferedWriter fw = new BufferedWriter(new FileWriter("data.csv", true));
				for(int i = 0 ; i < reviewForRecommand.size() ; i++) // DB로부터 가져온 데이터를 csv 파일에 삽입
				{
					fw.write(reviewForRecommand.get(i)[0]+","+reviewForRecommand.get(i)[1]+","+reviewForRecommand.get(i)[2]);
					fw.newLine();
				}
				fw.flush();
				fw.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		
			

			
			try {
	
				File out_file = new File("data.csv");
				if(out_file.exists()){
		            System.out.println("파일이 존재합니다.");
		            //System.out.println(out_file.getAbsolutePath());
		        }else{
		            System.out.println("파일이 존재하지 않습니다.");
		        }
				
				
				// 직접 구현 X 라이브러리로부터 가져와서 사용
				DataModel model = new FileDataModel(out_file);  
				
				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
				UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
				UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
	
				//ID가 member_num인 사람에게 2개의 아이템을 추천
				int member_num = recommandDao.getRMemberNum(id);
				List<RecommendedItem> recommendations = recommender.recommend(member_num, 2);
				
				BookAspect.logger.info(BookAspect.logMsg+"recommendations_size: "+recommendations.size());
				
				
				int i = 0;
				// 뷰 페이지로 넘기기 위해 필요한 데이터를 형식을 맞춰 배열에 저장
				for (RecommendedItem recommendation : recommendations) {
					String result_book_id = String.format("%-20s", recommendation.getItemID());
					//System.out.println(result_member_num);
					recommend_imgs[i]=recommandDao.getImag(result_book_id);
					recommend_imgs_book_id[i]=result_book_id;
					i+=1;
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mav.addObject("recommend_imgs_book_id",recommend_imgs_book_id);
		mav.addObject("recommend_imgs",recommend_imgs);
		mav.addObject("markBookIcon",markBookIcon);
		mav.addObject("markList", markList);
		mav.addObject("markBookList", markBookList);
		mav.addObject("interestDto", interestDto);

		// mav.setViewName("recommend/recommendMain");

	}

}
