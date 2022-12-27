package com.practice.web.service;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

  /**
   * Logger Object to log the details.
   */
  private static final Logger LOGGER = LoggerFactory
      .getLogger(CacheService.class);

  /**
   * CacheManager object.
   */
  private final CacheManager cacheManager;

  /**
   * Parameterized constructor to bind the object.
   *
   * @param cacheManager CacheManager
   */
  public CacheService(final CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  /**
   * To clear all the caches at regular intervals.
   */
  @Scheduled(fixedRateString = "${via.scheduler.cache.evict.value}")
  public void evictAllCachesAtIntervals() {
    LOGGER.debug("Caches are: {}", cacheManager.getCacheNames());
    cacheManager.getCacheNames()
        .forEach(cacheName -> Objects.requireNonNull(
            cacheManager.getCache(cacheName)).clear());
    LOGGER.debug("Caches cleared!");
  }
}
