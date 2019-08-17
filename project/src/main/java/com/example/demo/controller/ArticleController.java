package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.PageInfo;
import com.example.demo.PageMaker;
import com.example.demo.dto.Article;
import com.example.demo.dto.ArticleFile;
import com.example.demo.dto.Board;
import com.example.demo.dto.Member;
import com.example.demo.dto.Reply;
import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import com.example.demo.service.ReplyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ReplyService replyService;
	@Value("${custom.uploadDir}")
	private String filePath;

	@RequestMapping("article/list")
	public String showList(
			@RequestParam(value="boardId", defaultValue="1") int boardId,
			@RequestParam Map<String, Object> param,
			PageInfo pInfo, Model model	) {
		
		if(boardId != 1 && boardId != 2) {
			boardId = 0;
		}
		
		Board board = articleService.getBoard(boardId);
		model.addAttribute("board", board);
		
		param.put("boardId", boardId);
		
		param.put("extra__repliesCount", true);
		param.put("extra__writerName", true);
		
		param.put("prevPageArticles", pInfo.getPrevPageArticles());
		param.put("PerPageArticles", pInfo.getPerPageArticles());
		
		List<Article> list = articleService.getList(param);

		// PageInfo : 게시물 리스트의 한 페이지 규격을 담은 객체
		// PageInfo는 파라미터로 계속 사용되므로 페이지별 리스트화로는 사용하지 말자!
		// PageMaker : 게시물 리스트를 페이지화 해주는 객체
		
		// 아래의 과정을 통해 
		// 현재 페이지, 한 페이지 당 게시물 수, 총 게시물 수를 지정 후
		// 출력되는 리스트에서 게시물 페이지화 꾸며냄
		PageMaker maker = new PageMaker(pInfo,articleService.getTotalCount(param));
		
		model.addAttribute("list", list);
		model.addAttribute("maker", maker);
		model.addAttribute("cPage", pInfo.getCPage());
		
		return "article/list";
	}

	@RequestMapping("/article/detail")
	public String showDetail(@RequestParam Map<String,Object> param, Model model, HttpSession session) {
		int articleMemberId = (int)articleService.getOne(param).getMemberId();
		
		int loginMemberId = (int) session.getAttribute("loginMemberId");
		if ( loginMemberId == articleMemberId ) {
			param.put("loginMemberHasArticleAuth",true);
		} else {
			param.put("loginMemberHasArticleAuth",false);
		}
		
		param.put("shouldViewIncrease", true);
		param.put("extra__writerName", true);

		
		Article article = articleService.getOne(param);
		List<ArticleFile> files = articleService.getFiles(param);
		
		model.addAttribute("article", article);
		model.addAttribute("files", files);
		
		return "article/detail";
	}
	
	// 실제 파일의 경로에 있는 이미지를 이미지태그 소스 경로에서 열기
	@RequestMapping("/article/showImg")
	@ResponseBody
	public ResponseEntity<Resource> showImg(int id, HttpServletResponse response) throws IOException{
		
		ArticleFile articleFile = articleService.getOneFile(id);
		HttpHeaders header = new HttpHeaders();
		File target = new File(filePath, articleFile.getPrefix()+articleFile.getOriginFileName());		
		Resource rs = null;
		String mimeType = null;	
		
		
		if(target.exists()) {
			rs = new UrlResource(target.toURI());
			mimeType = Files.probeContentType(Paths.get(rs.getFilename()));
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}
			
			header.setContentType(MediaType.parseMediaType(mimeType));
			
			
		}
		return new ResponseEntity<Resource>(rs, header, HttpStatus.OK);
	}
	
	@RequestMapping("/article/downloadFile")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(int id, Model model) throws IOException{
		ArticleFile articleFile = articleService.getOneFile(id);
		
		File target = new File(filePath, articleFile.getPrefix()+articleFile.getOriginFileName());		
		Resource rs = null;
		String mimeType = null;	
		
		
		if(target.exists()) {
			rs = new UrlResource(target.toURI());
			mimeType = Files.probeContentType(Paths.get(rs.getFilename()));
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}		
			
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(mimeType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rs.getFilename() +"\"")
					.body(rs);
		}
		return null;
	}

	// ajax에 반응
	@RequestMapping("/article/getReplies")
	@ResponseBody
	public List getReplies(@RequestParam Map<String, Object> param) {
		param.put("extra__writerName", true);
		List<Reply> replies = replyService.getList(param);

		return replies;
	}

	@RequestMapping("/article/add")
	public String showAdd(@RequestParam Map<String, Object> param, HttpServletRequest request, Model model) {
		int boardId = Integer.parseInt((String)param.get("boardId"));
		
		if( boardId == 2 && (int)request.getAttribute("loginMemberPermissionLevel") != 0 ) {
			model.addAttribute("resultCode", "F-1");
			model.addAttribute("alertMsg", "관리자만 작성할 수 있는 게시판입니다.");
			model.addAttribute("historyBack", true);
			
			return "common/redirect";
		}
			
		Board board = articleService.getBoard(boardId);
		model.addAttribute("board", board);
		
		return "/article/add";
	}

	// 파라미터 정리
	// param : 일반적인 맵
	// files : 첨부파일 사진 순서 리스트
	// type : 첨부파일 타입1 순서 리스트 (body 고정)
	// typ2 : 첨부파일 타입2 파일자료형 순서 리스트
	// 여러개의 첨부파일이 각 항목에 따라 순서대로 들어오므로
	// 맵이 아닌 각 변수의 리스트 구조를 이용
	@RequestMapping("/article/doAdd")
	public String doAdd(@RequestParam Map<String, Object> param, 
			@RequestParam(value="files") List<MultipartFile> files,
			@RequestParam(value="type", required=false) List<String> inputType,
			@RequestParam(value="type2", required=false) List<String> inputType2,
			HttpSession session, Model model) {

		String redirectUrl = (String) param.get("redirectUrl");
		String msg = "";
		
		if (Integer.parseInt((String)param.get("boardId")) < 1) {
			redirectUrl = "/home/main";
			msg = "잘못된 접근입니다.";
			model.addAttribute("redirectUrl", redirectUrl);
			model.addAttribute("alertMsg", msg);
			
			return "common/redirect";
		} 
		
		try{
			int loginedId = (int)session.getAttribute("loginMemberId");
			param.put("memberId", loginedId);			
			
			// 게시물과 파일을 각각의 DB에 추가 처리
			articleService.add(param);	
			articleService.addFiles(files,inputType, inputType2, param.get("id"));
			
			
			if(redirectUrl == null ||redirectUrl.length() == 0) {
				redirectUrl = "/article/detail?id="+param.get("id") + "&boardId=" + param.get("boardId");				
			}
			model.addAttribute("redirectUrl", redirectUrl);
			msg = "글이 작성되었습니다.";
			
		}catch(Exception e) {
			e.printStackTrace();
			msg = "글을 작성할 수 없습니다.";
			model.addAttribute("historyBack",true);
		}
		model.addAttribute("alertMsg", msg);
		
		return "common/redirect";
	}

	@RequestMapping("/article/modify")
	public String showModify(@RequestParam Map<String,Object> param, Model model, long id, HttpSession session) {
		
		int loginMemberId = (int) session.getAttribute("loginMemberId");
		Article article = articleService.getOne(param);	
		
		// 게시물 수정 뷰 접근 권한
		if(loginMemberId != article.getMemberId()) {
			model.addAttribute("alertMsg", "권한이 없습니다.");			
			model.addAttribute("historyBack", true);
			
			return "common/redirect";
		}
		
		// 게시물 수정시 게시글 불러옴	
		model.addAttribute("article", article);
		
		// 게시물 수정시 파일이 있다면 불러옴
		List<ArticleFile> files = articleService.getFiles(param);
		
		
		if(files != null && files.size() > 0) {
			model.addAttribute("files", files);
		}
		
		return "/article/modify";
		
		
	}

	@RequestMapping("/article/doModify")
	public String doModify(@RequestParam Map<String,Object> param,
			@RequestParam(value="files") List<MultipartFile> files,
			@RequestParam(value="deleteFiles", required=false) List<Integer> deleteFiles,
			@RequestParam(value="type", required=false) List<String> inputType,
			@RequestParam(value="type2", required=false) List<String> inputType2,
			Model model, HttpSession session) throws IOException {
		
		int loginMemberId = (int) session.getAttribute("loginMemberId");
		Article article = articleService.getOne(param);	
		
		// 게시물 수정 접근 권한
		if(loginMemberId != article.getMemberId()) {
			model.addAttribute("alertMsg", "권한이 없습니다.");			
			model.addAttribute("historyBack", true);
			
			return "common/redirect";
		}
			
		Map<String, Object> rs = articleService.update(param, files, deleteFiles, inputType, inputType2, session.getAttribute("loginMemberId"));
		
		String resultCode = (String) rs.get("resultCode");
		model.addAttribute("alertMsg", rs.get("msg"));
		
		if(resultCode.startsWith("S-")) {
			
			String redirectUrl = "/article/detail?id="+param.get("id") + "&boardId=" + param.get("boardId");
			model.addAttribute("redirectUrl", redirectUrl);
			
		}else {
			model.addAttribute("historyBack", true);
		}
		
		return "common/redirect";	
	}

	@RequestMapping("/article/doDelete")
	public String doDelete(@RequestParam Map<String, Object> param, HttpSession session, Model model) {

		int loginMemberId = (int) session.getAttribute("loginMemberId");
		Article article = articleService.getOne(param);	
		
		// 게시물 삭제 접근 권한
		if(loginMemberId != article.getMemberId()) {
			model.addAttribute("alertMsg", "권한이 없습니다.");			
			model.addAttribute("historyBack", true);
			
			return "common/redirect";
		}
		
		Map<String,Object> rs = articleService.delete(param, loginMemberId);		
		String msg = (String) rs.get("msg");		
		String resultCode = (String) rs.get("resultCode");		
		String redirectUrl = "";		
		
		model.addAttribute("alertMsg", msg);
		
		if(resultCode.startsWith("S-")) {			
			redirectUrl = "/article/list?boardId="+param.get("boardId");			
			model.addAttribute("redirectUrl", redirectUrl);		
			
		}else {			
			model.addAttribute("historyBack", true);			
		}
		return "common/redirect";
	}

	// ajax에 반응
	@RequestMapping("/article/doAddReply")
	@ResponseBody
	public Map doAddReply(@RequestParam Map<String, Object> param, HttpServletRequest request) {

		long id = replyService.add(param, request);
		Reply reply = replyService.getOne(id);

		Map<String, Object> rs = new HashMap<>();

		rs.put("resultCode", "S-1");
		rs.put("msg", id + "번 댓글이 작성되었습니다.");
		rs.put("reply", reply);
		return rs;
	}

	// ajax에 반응
	@RequestMapping("/article/doDeleteReply")
	@ResponseBody
	public Map doDeleteReply(@RequestParam Map<String, Object> param) {

		replyService.delete(param);

		Map<String, Object> rs = new HashMap<>();

		rs.put("resultCode", "S-1");
		rs.put("msg", param.get("id") + "번 댓글이 삭제되었습니다.");
		return rs;
	}

	// ajax에 반응
	@RequestMapping("/article/doModifyReply")
	@ResponseBody
	public Map doModifyReply(@RequestParam Map<String, Object> param) {

		replyService.modify(param);

		Map<String, Object> rs = new HashMap<>();

		rs.put("resultCode", "S-1");
		rs.put("msg", param.get("id") + "번 댓글이 수정되었습니다.");
		return rs;
	}
}