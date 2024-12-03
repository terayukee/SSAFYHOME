package com.ssafy.home.search.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

	
	@Override
    public List<HouseDto> searchHouse(String keyword) {
        String formattedKeyword = "+" + keyword + "*";

        // 병렬로 테이블 조회
        CompletableFuture<List<HouseDto>> houseInfosFuture =
            CompletableFuture.supplyAsync(() -> searchMapper.searchHouseInfos(formattedKeyword));
        CompletableFuture<List<HouseDto>> villaInfosFuture =
            CompletableFuture.supplyAsync(() -> searchMapper.searchVillaInfos(formattedKeyword));
        CompletableFuture<List<HouseDto>> officetelInfosFuture =
            CompletableFuture.supplyAsync(() -> searchMapper.searchOfficetelInfos(formattedKeyword));

        // 결과를 결합
        try {
            List<HouseDto> houseInfos = houseInfosFuture.get();
            List<HouseDto> villaInfos = villaInfosFuture.get();
            List<HouseDto> officetelInfos = officetelInfosFuture.get();

            List<HouseDto> result = new ArrayList<>();
            result.addAll(houseInfos);
            result.addAll(villaInfos);
            result.addAll(officetelInfos);

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching house data in parallel", e);
        }
    }
	
	// Full-Text Index를 활용하기 위한 키워드 변경 
	public String formattingKeyword(String keyword) {
		return "+" + keyword + "*";
	}
}
