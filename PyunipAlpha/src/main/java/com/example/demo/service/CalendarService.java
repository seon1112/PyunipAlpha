package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UnivInfoDto;
import com.example.demo.util.DateUtils;

@Component
@Service
public class CalendarService {
	@Autowired
	private DateUtils dateService;
	
	public List<Map<String, Object>> getEventList(List<UnivInfoDto> list){
	    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
	    ArrayList<String> color=new ArrayList<>();
	    color.add("rgb(254 116 151 / 30%)");
	    color.add("rgb(2 180 203 / 30%)");
	    color.add("rgb(7 180 127 / 30%)");
	    color.add("rgb(253 208 15 / 30%)");
	    color.add("rgb(1 139 248 / 30%)");
	    
	    int i=0;
	    for(UnivInfoDto dto : list) {
	    	if(dto.getAPY_ST_DTM() == null || dto.getAPY_ED_DTM() == null) {
	    		continue;
	    	}
	    	Map<String, Object> map=new HashMap<>();
	    	map.put("no", dto.getUNIV_NUM());
	    	String dtm=dateService.DateConversion(dto.getAPY_ST_DTM().replace("-", ""))+"~"+dateService.DateConversion(dto.getAPY_ED_DTM().replace("-", ""));
	    	map.put("title", dto.getUNIV_NAME()+" 원서 접수 "+dtm);
	    	map.put("start", dto.getAPY_ST_DTM());
	    	map.put("end", dto.getAPY_ED_DTM());
	    	//map.put("textcolor", "#656565");
	    	map.put("color", color.get(i%5));
	    	i++;
	    	mapList.add(map);
	    }
	    
	    return mapList;
	}
	
}
