package com.ssafy.home.board.controller;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {
	
	private final BoardService boardService;

	@Value("${file.upload-dir}")
	private String uploadDir; // application.properties에서 파일 경로를 주입받기
	
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
	        // 업로드 경로 설정
	        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
	        String saveFolder = uploadDir + File.separator + today;
	        
	        File folder = new File(saveFolder);
	        if (!folder.exists()) {
	            folder.mkdirs();  // 디렉토리가 없으면 생성
	        }

	        List<FileInfoDto> fileInfos = new ArrayList<>();
	        for (MultipartFile mfile : files) {
	            String originalFileName = mfile.getOriginalFilename();
	            
	            if (originalFileName != null && !originalFileName.isEmpty()) {
	                // 파일 이름과 경로 생성
	                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
	                String saveFileName = UUID.randomUUID().toString() + fileExtension; // UUID로 파일명 생성
	                String filePath = saveFolder + File.separator + saveFileName;

	                try {
	                    // 파일 저장
	                    mfile.transferTo(new File(filePath));  // 지정된 경로에 파일 저장

	                    // 파일 정보 저장
	                    FileInfoDto fileInfoDto = new FileInfoDto();
	                    fileInfoDto.setFileName(originalFileName);
	                    fileInfoDto.setFileSize((int) mfile.getSize());
	                    fileInfoDto.setFileType(mfile.getContentType());
	                    fileInfoDto.setFilePath(filePath);
	                    fileInfos.add(fileInfoDto);
	                    
	                    log.info("File uploaded: " + fileInfoDto);
	                } catch (Exception e) {
	                    log.error("Error while uploading file: " + originalFileName, e);
	                    throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.");
	                }
	            }
	        }
	        // 게시글에 첨부된 파일 정보 추가
	        boardDto.setFileInfos(fileInfos);
	    }

	    // 게시글 작성 서비스 호출
	    boardService.writeArticle(boardDto);

	    return new ResponseEntity<>(HttpStatus.CREATED);  // HTTP 201 Created 반환
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
	public ResponseEntity<BoardDto> getArticle(@PathVariable("articleno") int articleNo) throws Exception {
	    boardService.updateHit(articleNo); // 조회수 증가
	    BoardDto boardDto = boardService.getArticle(articleNo); // 게시글 및 첨부파일 조회
	    System.out.println(boardDto);
	    return ResponseEntity.ok(boardDto); // JSON으로 반환
	}
	
	
	@GetMapping("/file/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
	    try {
	        // 경로 중복 방지를 위해 filePath를 상대 경로로 처리하도록 수정
	        Path path = Paths.get(uploadDir).resolve(filePath).normalize();  // 경로 결합 및 정상화

	        Resource resource = new FileSystemResource(path);

	        if (!resource.exists()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        // 파일 이름
	        String fileName = resource.getFilename();

	        // 파일 응답 헤더 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
	        headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path));

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(resource);
	    } catch (Exception e) {	
	        log.error("Error during file download: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@PutMapping("/{boardNo}")
	public ResponseEntity<?> modifyArticle(@PathVariable("boardNo") int boardNo, @RequestBody BoardDto boardDto) throws Exception {
	    try {
	        boardDto.setBoardNo(boardNo); // 게시글 번호 설정
	        boardService.modifyArticle(boardDto); // 수정 서비스 호출
	        return new ResponseEntity<>(HttpStatus.OK); // 수정 성공
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 오류 발생 시
	    }
	}

	
	
}
