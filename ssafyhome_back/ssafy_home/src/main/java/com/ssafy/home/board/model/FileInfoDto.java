package com.ssafy.home.board.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileInfoDto {
	
	private int attachmentId;
    private int boardNo;      // 게시글 번호 (외래 키)
    private String fileName;  // 파일 이름
    private int fileSize;     // 파일 크기
    private String fileType;  // 파일 타입
    private String filePath;  // 파일 저장 경로
    private String createdAt;
    
}
