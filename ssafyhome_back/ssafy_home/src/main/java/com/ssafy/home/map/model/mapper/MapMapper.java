package com.ssafy.home.map.model.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.home.map.model.SidoGugunCodeDto;

public interface MapMapper {

	List<SidoGugunCodeDto> getSido() throws SQLException;
	List<SidoGugunCodeDto> getGugunInSido(String sido) throws SQLException;
	
}
