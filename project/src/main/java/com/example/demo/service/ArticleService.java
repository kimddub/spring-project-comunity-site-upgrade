package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.Board;

public interface ArticleService {
	public List<Article> getList(Map<String, Object> args);

	public Article getOne(Map<String, Object> args);

	public long add(Map<String, Object> args);

	public void addFiles(List<MultipartFile> files, List<String> inputType, List<String> inputType2, Object articleId) throws IOException;
	
	public ArticleFile getOneFile(int id);
	
	public  Map<String, Object> update(Map<String, Object> param, List<MultipartFile> files, List<Integer> deleteFiles, List<String> inputType, List<String> inputType2, Object memberId)  throws IOException;
	
	public Map<String,Object> delete(Map<String, Object> param, Object memberId);
	
	public void deleteReply(Map<String, Object> args);

	public int getTotalCount(Map<String, Object> args);

	public List<ArticleFile> getFiles(Map<String, Object> param);

	public List<Article> getArticleByCondition(Map<String, Object> param);

	public Board getBoard(int boardId);


}