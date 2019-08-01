package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.dto.Reply;

public interface ReplyService {

	public List<Reply> getList(Map<String,Object> args);

	public Reply getOne(long id);
	
	public long add(Map<String, Object> param, HttpServletRequest request);

	public void delete(Map<String, Object> param);

	public void modify(Map<String, Object> param);


}
