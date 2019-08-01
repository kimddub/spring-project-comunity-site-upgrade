package com.example.demo.dto;

import java.util.Map;

import org.springframework.web.util.HtmlUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Reply {
	private long id;
	private String regDate;
	private String body;
	private long articleId;
	private long memberId;
	private long boardId;
	private Map<String, String> extra;
}
