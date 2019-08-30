package com.boogie.search.dto;

import java.util.Date;

public class SearchDto {
	
	private String type01;	//대분류
	private String type02;	//중분류
	private String type03;	//소분류
	
	private String img_path;	//이미지경로
	private String book_name;	//책이름
	private String author;		//작가
	private String publisher;	//출판사
	
	private Date publish_date;	//출판일
	
	private int price;			//책가격
	
	private String book_id;		//책고유값
	private String story;		//책간단한 내용
	
	private int sales_volume;	//판매량
	
	private Date enrolled_date;	//책 등록일

	public String getType01() {
		return type01;
	}

	public void setType01(String type01) {
		this.type01 = type01;
	}

	public String getType02() {
		return type02;
	}

	public void setType02(String type02) {
		this.type02 = type02;
	}

	public String getType03() {
		return type03;
	}

	public void setType03(String type03) {
		this.type03 = type03;
	}

	public String getImg_path() {
		return img_path;
	}

	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}

	public String getBook_name() {
		return book_name;
	}

	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(Date publish_date) {
		this.publish_date = publish_date;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getBook_id() {
		return book_id;
	}

	public void setBook_id(String book_id) {
		this.book_id = book_id;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public int getSales_volume() {
		return sales_volume;
	}

	public void setSales_volume(int sales_volume) {
		this.sales_volume = sales_volume;
	}

	public Date getEnrolled_date() {
		return enrolled_date;
	}

	public void setEnrolled_date(Date enrolled_date) {
		this.enrolled_date = enrolled_date;
	}

	@Override
	public String toString() {
		return "SearchDto [type01=" + type01 + ", type02=" + type02 + ", type03=" + type03 + ", img_path=" + img_path
				+ ", book_name=" + book_name + ", author=" + author + ", publisher=" + publisher + ", publish_date="
				+ publish_date + ", price=" + price + ", book_id=" + book_id + ", story=" + story + ", sales_volume="
				+ sales_volume + ", enrolled_date=" + enrolled_date + "]";
	}
	
	
}
