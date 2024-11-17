package com.ssafy.home.board.model.service;

import com.ssafy.home.board.model.BoardDto;

public interface BoardService {
	void writeArticle(BoardDto boardDto) throws Exception;
	
}
