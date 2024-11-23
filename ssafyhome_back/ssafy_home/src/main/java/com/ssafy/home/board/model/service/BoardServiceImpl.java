package com.ssafy.home.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.BoardListDto;
import com.ssafy.home.board.model.FileInfoDto;
import com.ssafy.home.board.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService{
	
	private final BoardMapper boardMapper;

	public BoardServiceImpl(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}

	@Transactional
	@Override
	public void writeArticle(BoardDto boardDto) throws Exception {
		
		boardMapper.writeArticle(boardDto);
	    List<FileInfoDto> fileInfos = boardDto.getFileInfos();
	    if (fileInfos != null && !fileInfos.isEmpty()) {
	        for (FileInfoDto fileInfo : fileInfos) {
	            fileInfo.setBoardNo(boardDto.getBoardNo());
	            boardMapper.registerFile(fileInfo);  // 첨부파일 등록
	        }
	    }
	}
	
	@Override
	public BoardListDto getListArticle(Map<String, String> map) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
		int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));
		int start = currentPage * sizePerPage - sizePerPage;
		param.put("start", start);
		param.put("listsize", sizePerPage);

		String key = map.get("key");
		param.put("key", key == null ? "" : key);
		if ("user_id".equals(key))
			param.put("key", key == null ? "" : "b.user_id");
		List<BoardDto> list = boardMapper.listArticle(param);
		
		if ("user_id".equals(key))
			param.put("key", key == null ? "" : "user_id");
		int totalArticleCount = boardMapper.getTotalArticleCount(param);
		int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

		BoardListDto boardListDto = new BoardListDto();
		boardListDto.setArticles(list);
		boardListDto.setCurrentPage(currentPage);
		boardListDto.setTotalPageCount(totalPageCount);

		return boardListDto;
	}


	@Override
	public void updateHit(int articleNo) throws Exception {
		boardMapper.updateHit(articleNo);		
	}

	@Override
	public void modifyArticle(BoardDto boardDto) throws Exception {
		boardMapper.modifyArticle(boardDto);
		
	}

	@Override
	public void deleteArticle(int articleNo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoardDto getArticle(int articleNo) throws Exception {
		return boardMapper.getArticle(articleNo);
	}

}
