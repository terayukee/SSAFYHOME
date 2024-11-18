package com.ssafy.home.board.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

	private int articleNo;
//	private String userId;
//	private String userName;
	private String subject;
	private String content;
	private String createdAt;
	private String updatedAt;
	private String ip;
	private int hit;
	
	private List<FileInfoDto> fileInfos;
}