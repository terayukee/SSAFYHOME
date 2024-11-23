package com.ssafy.home.board.model.service;

import java.util.Map;

import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.BoardListDto;
import com.ssafy.home.board.model.FileInfoDto;

public interface BoardService {
	
	void writeArticle(BoardDto boardDto) throws Exception;
	
	BoardListDto getListArticle(Map<String, String> map) throws Exception;
	
	BoardDto getArticle(int boardNo) throws Exception;
	
	void updateHit(int articleNo) throws Exception;
	
	void modifyArticle(BoardDto boardDto) throws Exception;
	
	void deleteArticle(int boardNo) throws Exception;
	
	void deleteFile(int attachmentId) throws Exception;

	void registerFile(FileInfoDto fileInfoDto) throws Exception;
	
	String getOriginalFileName(String filePath) throws Exception;
}
