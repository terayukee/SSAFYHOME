package com.ssafy.home.search.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.home.search.model.HouseDto;
import com.ssafy.home.search.model.RegionDto;
import com.ssafy.home.search.model.mapper.SearchMapper;

@Service
public class SearchServiceImpl implements SearchService {
	private final SearchMapper searchMapper;

	public SearchServiceImpl(SearchMapper searchMapper) {
		this.searchMapper = searchMapper;
	}

	// 지역검색 Service
	@Override
	public List<RegionDto> searchRegion(String keyword) {
		String formattedKeyword = formattingKeyword(keyword);
		return searchMapper.searchRegion(formattedKeyword);
	}

	// 주택검색 Service
	@Override
	public List<HouseDto> searchHouse(String keyword) {
		// 1. "서울" 검색 시 "서울"이라는 이름이 들어간 아파트, 빌라, 오피스텔 모두 가져오기 
		String formattedKeyword = formattingKeyword(keyword);
		List<HouseDto> houses = searchMapper.searchHouse(formattedKeyword);

		// 2. 각 HouseDto에 시-구-동 정보를 조회해 설정
		for (HouseDto house : houses) {		
			// DB 조회용 파라미터 설정 
			Map<String, String> params = new HashMap<>();
			params.put("sggCd", house.getSggCd());
			params.put("umdCd", house.getUmdCd());

			// DB 조회
			Map<String, String> dongInfo = searchMapper.getDongInfo(params);
			if (dongInfo != null) {
				house.setSidoName(dongInfo.get("sidoName"));
				house.setGugunName(dongInfo.get("gugunName"));
				house.setDongName(dongInfo.get("dongName"));
			}
		}

		return houses;
	}
	
	// Full-Text Index를 활용하기 위한 키워드 변경 
	public String formattingKeyword(String keyword) {
		return "+" + keyword + "*";
	}
}
