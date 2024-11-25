package com.ssafy.home.comments.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HouseCommentDto {
	private int commentNo;
	private int userNo;
	private String aptSeq;
	private String houseType;
	private String content;
	private Timestamp registerTime;

	// Getters and Setters
}
