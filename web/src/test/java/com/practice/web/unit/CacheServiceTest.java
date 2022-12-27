package com.practice.web.unit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.practice.web.service.CacheService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
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