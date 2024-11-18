package com.ssafy.home.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.FileInfoDto;
import com.ssafy.home.board.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{
	
	private final BoardMapper boardMapper;

	public BoardServiceImpl(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}

	@Override
	public void writeArticle(BoardDto boardDto) throws Exception {
		boardMapper.writeArticle(boardDto);
		List<FileInfoDto> fileInfos = boardDto.getFileInfos();
		// 파일이 있으면
		if (fileInfos != null && !fileInfos.isEmpty()) {
			boardMapper.registerFile(boardDto);
		}
		
	}

}
