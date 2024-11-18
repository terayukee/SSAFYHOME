package com.ssafy.vue.map.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(title = "SidoGugunCodeDto : 시도, 구군정보", description = "시도, 구군의 이름을 나타낸다.")
public class SidoGugunCodeDto {

	@Schema(description = "시도코드")
	private String sidoCode;
	@Schema(description = "시도이름")
	private String sidoName;
	@Schema(description = "구군코드")
	private String gugunCode;
	@Schema(description = "구군이름")
	private String gugunName;
	
}
