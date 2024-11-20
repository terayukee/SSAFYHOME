package com.ssafy.home.board.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.home.board.model.BoardDto;
import com.ssafy.home.board.model.BoardListDto;
import com.ssafy.home.board.model.FileInfoDto;
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
    public ResponseEntity<?> writeArticle(MultipartHttpServletRequest mRequest) throws Exception {
		
		// JSON 데이터 추출
		String boardJson = mRequest.getParameter("article");
		ObjectMapper objectMapper = new ObjectMapper();
		BoardDto boardDto = objectMapper.readValue(boardJson, BoardDto.class);
		
		// 파일 추출
		List<MultipartFile> files = mRequest.getFiles("files");
	    if (files != null && !files.isEmpty()) {
	        String uploadDir = "C:/upload"; // 업로드 경로
	        String today = new SimpleDateFormat("yyMMdd").format(new Date());
	        String saveFolder = uploadDir + File.separator + today;
	        File folder = new File(saveFolder);
	        if (!folder.exists()) folder.mkdirs();

	        List<FileInfoDto> fileInfos = new ArrayList<>();
	        for (MultipartFile mfile : files) {
	            String originalFileName = mfile.getOriginalFilename();
	            if (originalFileName != null && !originalFileName.isEmpty()) {
	            	// 파일 이름과 경로 생성
	                String saveFileName = UUID.randomUUID().toString() +
	                                      originalFileName.substring(originalFileName.lastIndexOf('.'));
	                String filePath = saveFolder + File.separator + saveFileName;

	                // 파일 저장
	                mfile.transferTo(new File(folder, saveFileName));

	                // 파일 정보 저장
	                FileInfoDto fileInfoDto = new FileInfoDto();
	                fileInfoDto.setFileName(originalFileName);
	                fileInfoDto.setFileSize((int) mfile.getSize());
	                fileInfoDto.setFileType(mfile.getContentType());
	                fileInfoDto.setFilePath(filePath);
	                
	                fileInfos.add(fileInfoDto);
	                
	                System.out.println(fileInfoDto);
	            }
	        }
	        boardDto.setFileInfos(fileInfos);
	    }
	    System.out.println("보드 " +boardDto);
	    boardService.writeArticle(boardDto);
	    
	    return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
	
	@GetMapping
	public ResponseEntity<?> listArticle(@RequestParam Map<String, String> map) throws Exception {
		BoardListDto boardListDto = boardService.getListArticle(map);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		System.out.println(boardListDto.getArticles().size());
		return ResponseEntity.ok().headers(header).body(boardListDto);
	}
	
	@GetMapping("/{articleno}")
	public ResponseEntity<BoardDto> getArticle(@PathVariable("articleno") int articleNo) throws Exception{
		boardService.updateHit(articleNo);
		System.out.println(articleNo);
		BoardDto boardDto = boardService.getArticle(articleNo);
		System.out.println(boardDto);
		return new ResponseEntity<BoardDto>(boardDto, HttpStatus.OK);
	}
	
	
	
	
}