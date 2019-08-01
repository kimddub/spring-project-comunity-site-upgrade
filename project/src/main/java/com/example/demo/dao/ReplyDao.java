package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Reply;

@Mapper
public interface ReplyDao {

	public List<Reply> getList(Map<String,Object> args);

	public Reply getOne(long id);

	public void add(Map<String, Object> args);

	public void delete(Map<String, Object> args);

	public Object update(Map<String, Object> args);

}
