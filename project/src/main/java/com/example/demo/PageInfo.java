package com.example.demo;

// 한 페이지 규격을 담는 객체
public class PageInfo {
	private int cPage;
	private int perPageArticles;
	
	public PageInfo() {
		cPage = 1;
		perPageArticles = 10;
	}
	
	public int getPrevPageArticles() {
		return (cPage - 1) * perPageArticles;
	}
	
	public int getCPage() {
		return cPage;
	}
	
	public void setCPage(int cPage) {
		if ( cPage < 1 ) {
			cPage = 1;
		}
		
		this.cPage = cPage;
	}
	
	public int getPerPageArticles() {
		return perPageArticles;
	}
	
	public void setPerPageArticles(int perPageArticles) {
		this.perPageArticles = perPageArticles;
	}
}
