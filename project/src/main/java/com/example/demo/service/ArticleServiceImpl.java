package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.ArticleDao;
import com.example.demo.dao.MemberDao;
import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.Member;
import com.example.demo.util.CUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
	@Autowired
	private ArticleDao articleDao;
	@Value("${custom.uploadDir}")
	private String filePath;
	
	// 외부 접근 불가
	private List<String[]> uploadFiles(List<MultipartFile> files) throws IOException {
		
		List<String[]> rs = new ArrayList<>();
		
		for(MultipartFile file : files) {
			if(!file.getOriginalFilename().equals("")) {
				String[] name = new String[2];
				UUID uid = UUID.randomUUID();
				
				name[0] = uid.toString() + "-";
				name[1] = file.getOriginalFilename();
				
				File target = new File(filePath, name[0] + name[1]);
				
				FileOutputStream fos = new FileOutputStream(target);
				fos.write(file.getBytes());
				fos.close();
				
				rs.add(name);
			}
		}
		
		return rs;
	}
	
	public int getTotalCount(Map<String, Object> args) {
		return articleDao.getTotalCount(args);
	}

	public List<Article> getList(Map<String, Object> args) {
		// 글 리스트에 댓글수 추가
		if (args.containsKey("extra__repliesCount") && (boolean) args.containsKey("extra__repliesCount") == true) {
			args.put("leftJoin__articleReply", true);
			args.put("groupBy__articleId", true);
		}
		
		// 글 리스트에 작성자 이름 추가
		if (args.containsKey("extra__writerName") && (boolean) args.containsKey("extra__writerName") == true) {
			args.put("leftJoin__member", true);
		}

		return articleDao.getList(args);
	}

	public Article getOne(Map<String, Object> args) {
		
		if (args.containsKey("shouldViewIncrease") && (boolean) args.containsKey("shouldViewIncrease") == true) {
			if (!(boolean)args.get("loginMemberHasArticleAuth")) {
				articleDao.increaseView(Integer.parseInt((String)args.get("id")));
			}
		}
		
		// 글 상세에서 작성자 이름 추가
		if (args.containsKey("extra__writerName") && (boolean) args.containsKey("extra__writerName") == true)
		{
			args.put("leftJoin__member", true);
		}
		return articleDao.getOne(args);
	}
	
	public List<ArticleFile> getFiles(Map<String, Object> param) {
		return articleDao.getArticleFiles(param);
	}
	
	public ArticleFile getOneFile(int id) {
		return articleDao.getOneFile(id);
	}

	public long add(Map<String, Object> args) {
		articleDao.add(args);
		// 위 쿼리문 실행 과정을 통해 매개변수에 id 키와 값이 추가되었다.
		return CUtil.getAsLong(args.get("id"));
	}
	
	public void addFiles(List<MultipartFile> files, List<String> inputType, List<String> inputType2, Object articleId) throws IOException {
		
		List<String[]> fileNames = uploadFiles(files);
		
		if(fileNames != null && fileNames.size() > 0) {
			for(int i = 0 ;i < fileNames.size() ; i++) {
				articleDao.addFiles((fileNames.get(i))[0], (fileNames.get(i))[1],
						inputType.get(i), inputType2.get(i), articleId);
			}
		}
	}

	// 게시물 수정시 권한 체크 및 파일 추가 업로드와 삭제
	public Map<String, Object> update(Map<String, Object> param, List<MultipartFile> files, List<Integer> deleteFiles, List<String> inputType, List<String> inputType2, Object memberId) throws IOException {
		
		Article article = articleDao.getOne(param);
		String msg = "";
		String resultCode = "";
		
		if(article == null) {
			msg = "존재하지 않는 게시물 입니다.";
			resultCode ="F-3";
			
			return Maps.of("msg",msg, "resultCode",resultCode);
		}
		
		if(article.getMemberId() != (int) memberId) {
			msg = "권한이 없습니다.";
			resultCode ="F-3";
			
			return Maps.of("msg",msg, "resultCode",resultCode);
		}
		
		List<String[]> fileNames = uploadFiles(files);
		
		if(fileNames != null && fileNames.size() > 0) {
			for(int i = 0 ;i < fileNames.size() ; i++) {
				articleDao.addFiles((fileNames.get(i))[0], (fileNames.get(i))[1],
						inputType.get(i), inputType2.get(i), param.get("id"));
			}
		}
		
		if(deleteFiles != null && deleteFiles.size() > 0) {
			List<ArticleFile> deleteList = articleDao.getSelectedFiles(deleteFiles);
			
			if(deleteList != null) {
				for(ArticleFile file : deleteList) {
					File target = new File(filePath, file.getPrefix() + file.getOriginFileName());
					if(target.exists()) {
						target.delete();
					}
				}
				
				articleDao.deleteSelectedFiles(deleteFiles);
			}
		}		
		
		msg = "업데이트 했습니다.";
		resultCode = "S-3";		
		
		return Maps.of("msg", msg, "resultCode", resultCode);
	}
	
	public Map<String,Object> delete(Map<String, Object> param, Object memberId) {
		Article article = articleDao.getOne(param);
		String msg = "";
		String resultCode = "";
		
		if(article == null) {
			msg = "존재하지 않는 게시물 입니다.";
			resultCode ="F-3";
			
			return Maps.of("msg",msg, "resultCode",resultCode);
		}
		
		if(article.getMemberId() != (int) memberId) {
			msg = "권한이 없습니다.";
			resultCode ="F-3";
			
			return Maps.of("msg",msg, "resultCode",resultCode);
		}
		
		List<ArticleFile> files = articleDao.getArticleFiles(param);		
		
		
		for(ArticleFile file : files) {
			File target = new File(filePath, file.getPrefix() + file.getOriginFileName());
			if(target.exists()) {
				target.delete();
			}
		}
		
		articleDao.delete(param);
		articleDao.deleteReply(param);
		articleDao.deleteArticleFiles(param);
		msg = "삭제했습니다.";
		resultCode = "S-3";
		
		return Maps.of("msg",msg, "resultCode",resultCode);
	}
	
	public void deleteReply(Map<String, Object> args) {
		articleDao.deleteReply(args);
	}
}