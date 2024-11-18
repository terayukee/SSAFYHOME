package com.ssafy.home.board.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {
	
	private final BoardService boardService;
	
	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@PostMapping
	public ResponseEntity<?> writeArticle(BoardDto boardDto) throws Exception {
		boardService.writeArticle(boardDto);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	
	
}
