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
public class BoardListDto {
	
	// 글 목록
	private List<BoardDto> articles;
	// 페이지 번호
	private int currentPage;
	// 전체 페이지 수
	private int totalPageCount;
	
}
