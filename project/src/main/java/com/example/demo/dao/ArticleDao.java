package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.Board;

@Mapper
public interface ArticleDao {
	public List<Article> getList(Map<String, Object> args);

	public Article getOne(Map<String, Object> args);

	public void add(Map<String, Object> args);
	
	public void addFiles(String prefix, String originFileName, String inputType, String inputType2,
			Object articleId);
	
	public void update(Map<String, Object> args);
	
	public void delete(Map<String, Object> args);
	
	public void deleteReply(Map<String, Object> args);

	public int getTotalCount(Map<String, Object> args);

	public List<ArticleFile> getArticleFiles(Map<String, Object> param);

	public ArticleFile getOneFile(int id);

	public List<ArticleFile> getSelectedFiles(List<Integer> deleteFiles);

	public void deleteArticleFiles(Map<String, Object> args);

	public void deleteSelectedFiles(List<Integer> deleteFiles);

	public void increaseView(int id);

	public List<Article> getArticleByCondition(Map<String,Object> param);

	public Board getBoard(int id);

	

}