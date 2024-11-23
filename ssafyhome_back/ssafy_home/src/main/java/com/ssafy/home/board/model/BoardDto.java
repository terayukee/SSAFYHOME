package com.ssafy.home.board.model;
	
import java.util.List;
	
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;
import lombok.ToString;
	
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDto {
	
	private int BoardNo;
	private int 	userNo;
	private String userNickname;
	private String subject;
	private String content;
	private int hit;
	private String registerTime;
	private List<FileInfoDto> fileInfos;
	
		
}