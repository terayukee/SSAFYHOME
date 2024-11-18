package com.ssafy.home.board.model.service;

import java.util.Map;

import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.BoardListDto;

public interface BoardService {
	
	void writeArticle(BoardDto boardDto) throws Exception;
	
	BoardListDto getListArticle(Map<String, String> map) throws Exception;
	
	BoardDto getArticle(int articleNo) throws Exception;
	
	void updateHit(int articleNo) throws Exception;
	
	void modifyArticle(BoardDto boardDto) throws Exception;
	
	void deleteArticle(int articleNo) throws Exception;
}
