package com.ssafy.home.board.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.FileInfoDto;

@Mapper
public interface BoardMapper {

	int writeArticle(BoardDto boardDto) throws SQLException;

	List<BoardDto> listArticle(Map<String, Object> param) throws SQLException;

	int getTotalArticleCount(Map<String, Object> param) throws SQLException;

	BoardDto getArticle(int boardNo) throws SQLException;

	void updateHit(int boardNo) throws SQLException;

	void modifyArticle(BoardDto boardDto) throws SQLException;

	void deleteFile(int boardNo) throws SQLException;

	void deleteArticle(int boardNo) throws SQLException;

	void registerFile(FileInfoDto fileDto) throws SQLException;
	
	List<FileInfoDto> fileInfoList(int articleNo) throws SQLException;
	
	FileInfoDto getFileInfo(int attachmentId) throws SQLException;
	
	Optional<FileInfoDto> getFileInfoByPath(String filePath) throws SQLException;
	
}
