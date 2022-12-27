package com.practice.employee.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

  /**
   * Logger Object to log the details.
   */
  private static final Logger LOGGER = LoggerFactory.
      getLogger(OtpService.class);
  /**
   * LadingCache from Guava.
   */
  private LoadingCache<Object, Object> otpCache;
  /**
   * Cache expiry minutes after setting the value.
   */
  private static final Integer OTP_EXPIRE_MINUTES = 5;
  /**
   * Otp random base number.
   */
  private static final Integer RANDOM_BASE_NUMBER = 100000;
  /**
   * Otp random next int.
   */
  private static final Integer RANDOM_NEXT_INT = 900000;
  /**
   * Cache key format for start time.
   */
  private static final String START_TIME_FORMAT = "%d-startTime";
  /**
   * Cache key format for expiry time.
   */
  private static final String EXPIRY_TIME_FORMAT = "%d-expiryTime";
  /**
   * To generate random number.
   */
  private Random random;

  /**
   * Default constructor to build the cache builder.
   *
   * @throws NoSuchAlgorithmException exception for secure random
   */
  public OtpService() throws NoSuchAlgorithmException {
    random = SecureRandom.getInstanceStrong();
    otpCache = CacheBuilder.newBuilder()
        .expireAfterWrite(OTP_EXPIRE_MINUTES, TimeUnit.MINUTES)
        .build(new CacheLoader<>() {
          @Override
          public Integer load(final Object key) {
            return 0;
          }
        });

  }

  /**
   * Method to generate otp.
   *
   * @param key employee id
   * @return generated otp
   */
  public Object generateOTP(final Long key) {
    int otp = RANDOM_BASE_NUMBER + random.nextInt(RANDOM_NEXT_INT);
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss");
    otpCache.put(key, otp);
    otpCache.put(String.format(START_TIME_FORMAT, key),
        LocalDateTime.now().format(formatter));
    otpCache.put(String.format(EXPIRY_TIME_FORMAT, key),
        LocalDateTime.now()
            .plus(OTP_EXPIRE_MINUTES, ChronoUnit.MINUTES)
            .format(formatter));
    LOGGER.debug("OTP added to cache");
    return otp;
  }

  /**
   * Method to get the otp for validation.
   *
   * @param key employee id
   * @return valid or not
   */
  public Object getOtp(final Long key) {
    try {
      return otpCache.get(key);
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Method to get the otp start time.
   *
   * @param key employee id
   * @return valid or not
   */
  public Object getOtpStartTime(final Long key) {
    try {
      if (!otpCache.get(key).equals(0)) {
        return otpCache.get(String.format(START_TIME_FORMAT, key));
      }
    } catch (Exception e) {
      return 0;
    }
    return 0;
  }

  /**
   * Method to get the otp end time.
   *
   * @param key employee id
   * @return valid or not
   */
  public Object getOtpExpiryTime(final Long key) {
    try {
      if (!otpCache.get(key).equals(0)) {
        return otpCache.get(String.format(EXPIRY_TIME_FORMAT, key));
      }
    } catch (Exception e) {
      return 0;
    }
    return 0;
  }

  /**
   * To clear the Otp after using it.
   *
   * @param key employee id.
   */
  public void clearOTP(final Long key) {
    otpCache.invalidate(key);
    otpCache.invalidate(String.format(START_TIME_FORMAT, key));
    otpCache.invalidate(String.format(EXPIRY_TIME_FORMAT, key));
  }
}
