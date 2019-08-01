package com.example.demo;

public class PageMaker {
	private PageInfo pInfo;
	private int startPage;
	private int endPage;
	private int totalArticles;
	private int pageBlock;
	private boolean next;
	private boolean prev;
	
	public PageMaker(PageInfo pInfo, int totalArticles) {
		// 페이지 버튼 표시될 개수
		pageBlock = 5;
		this.pInfo = pInfo;
		this.totalArticles = totalArticles;
		calcData();
	}
	
	private void calcData() {
		// ceil(1p~5p/5) = 1block => s:1 ,e:5
		// ceil(6p~10p/5)  = 2block => s:6 ,e:10
		// ceil(11p~15p/5) = 3block => s:10 ,e:15
		
		endPage = (int)Math.ceil(pInfo.getCPage() / (double)pageBlock) * pageBlock;
		startPage = endPage - pageBlock + 1;
		
		int limitPage = (int)Math.ceil(totalArticles / (double)pInfo.getPerPageArticles());
		if(endPage > limitPage) {
			endPage = limitPage;
		}
		
		prev = startPage == 1 ? false : true;
		next = endPage == limitPage ? false : true;
		
	}
	
	public PageInfo getPInfo() {
		return pInfo;
	}
	public void setPInfo(PageInfo pInfo) {
		this.pInfo = pInfo;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getTotalArticles() {
		return totalArticles;
	}
	public void setTotalArticles(int totalArticles) {
		this.totalArticles = totalArticles;
		calcData();
	}
	public int getPageBlock() {
		return pageBlock;
	}
	public void setPageBlock(int pageBlock) {
		this.pageBlock = pageBlock;
	}
	public boolean isNext() {
		return next;
	}
	public void setNext(boolean next) {
		this.next = next;
	}
	public boolean isPrev() {
		return prev;
	}
	public void setPrev(boolean prev) {
		this.prev = prev;
	}
}
