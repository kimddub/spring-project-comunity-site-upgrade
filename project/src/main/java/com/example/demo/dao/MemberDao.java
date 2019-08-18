package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {

	public Member getMember(Map<String, Object> param);
	
	public List<Member> getMemberList(Map<String, Object> param);

	public void join(Map<String, Object> param);

	public void update(Map<String, Object> args);

	public void withdraw(Map<String, Object> args);

	public void updateAuthStatus(Map<String, Object> param);

	public void updatePermission(Map<String, Object> param);
	
}
