package com.example.demo.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ReplyDao;
import com.example.demo.dto.Reply;
import com.example.demo.util.CUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReplyServiceImpl implements ReplyService {
	@Autowired
	private ReplyDao replyDao;
	
	public List<Reply> getList(Map<String,Object> args) {
		if (args.containsKey("extra__writerName") && (boolean) args.containsKey("extra__writerName") == true)
		{
			args.put("leftJoin__member", true);
		}
		return replyDao.getList(args);
	}
	
	public Reply getOne(long id) {
		return replyDao.getOne(id);
	}
	
	public long add(Map<String, Object> args, HttpServletRequest request) {
		long boardId = 0;
		
		args.put("memberId", request.getAttribute("loginMemberId"));
		args.put("boardId", boardId);
		
		replyDao.add(args);

		return CUtil.getAsLong(args.get("id"));
	}
	
	public void delete(Map<String, Object> args) {
		replyDao.delete(args);
	}
	
	public void modify(Map<String, Object> args) {
		replyDao.update(args);
	}	
}
