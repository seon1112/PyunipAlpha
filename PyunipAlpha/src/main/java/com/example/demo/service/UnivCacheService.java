package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UnivInfoDto;
import com.example.demo.repository.UnivRepository;

import java.util.HashMap;
import java.util.List;

@Service
public class UnivCacheService {

    private static final Logger logger = LoggerFactory.getLogger(UnivCacheService.class);
    private final UnivRepository univRept;

    public UnivCacheService(UnivRepository univRept) {
        this.univRept = univRept;
    }

    @Cacheable(value = "univCache")
    public List<UnivInfoDto> getUnivList(HashMap<String, Object> map) {
        logger.info("Fetching data from database...");
        return univRept.findUnivNameNum(map);
    }
    
    @CacheEvict(value = "univCache", allEntries = true)
    public void clearUnivCache() {
        // 캐시를 무효화하는 메서드
    }
    
    @Cacheable(value = "calendarEventCache")
    public List<UnivInfoDto> findApyDtmList(){
    	logger.info("Fetching data from database...");
    	return univRept.findApyDtm();
    }
    
    @CacheEvict(value="calendarEventCache", allEntries = true)
    public void clearCalendarEventCache() {}
}
