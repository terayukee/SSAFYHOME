package com.ssafy.home.map.model.service;

import java.util.List;

import com.ssafy.home.map.model.SidoGugunCodeDto;

public interface MapService {

	List<SidoGugunCodeDto> getSido() throws Exception;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws Exception;
	
}
