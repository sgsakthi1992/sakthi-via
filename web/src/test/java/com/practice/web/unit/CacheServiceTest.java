package com.practice.web.unit;

import com.practice.web.service.CacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CacheServiceTest {

    @Mock
    CacheManager cacheManager;

    @InjectMocks
    CacheService cacheService;

    @Test
    void evictAllCachesAtIntervals() {
        //GIVEN
        when(cacheManager.getCacheNames()).thenReturn(Collections.EMPTY_LIST);
        //WHEN
        cacheService.evictAllCachesAtIntervals();
        //THEN
        verify(cacheManager, times(2)).getCacheNames();
    }
}