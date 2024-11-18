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

	private String saveFolder;
	private String originalFile;
	private String saveFile;
}
