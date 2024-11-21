package com.ssafy.home.search.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.search.model.HouseDto;
import com.ssafy.home.search.model.RegionDto;
import com.ssafy.home.search.model.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {
 private final SearchService searchService;

 public SearchController(SearchService searchService) {
     this.searchService = searchService;
 }

 @GetMapping("/region")
 public ResponseEntity<?> searchRegion(@RequestParam String keyword) {
     List<RegionDto> regions = searchService.searchRegion(keyword);
     return ResponseEntity.ok(regions);
 }

 @GetMapping("/house")
 public ResponseEntity<?> searchHouse(@RequestParam String keyword) {
     List<HouseDto> houses = searchService.searchHouse(keyword);
     return ResponseEntity.ok(houses);
 }

 @GetMapping("/all")
 public ResponseEntity<?> searchAll(@RequestParam String keyword) {
     Map<String, Object> result = new HashMap<>();
     result.put("regions", searchService.searchRegion(keyword));
     result.put("houses", searchService.searchHouse(keyword));
     return ResponseEntity.ok(result);
 }
}
