package com.practice.employee.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.practice.employee.service.OtpService;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

  private OtpService otpService;

  OtpServiceTest() throws NoSuchAlgorithmException {
    otpService = new OtpService();
  }

  @Test
  void generateOTP() {
    //GIVEN
    //WHEN
    Integer otp = (Integer) otpService.generateOTP(1L);
    //THEN
    assertTrue(otp != 0);
  }

  @Test
  void getOtp() {
    //GIVEN
    otpService.generateOTP(1L);
    //WHEN
    Integer otp = (Integer) otpService.getOtp(1L);
    //THEN
    assertTrue(otp != 0);
  }

  @Test
  void getOtpStartTime() {
    //GIVEN
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss");
    otpService.generateOTP(1L);
    //WHEN
    String otpStartTime = (String) otpService.getOtpStartTime(1L);
    //THEN
    assertFalse(otpStartTime.isEmpty());
    assertEquals(LocalDateTime.now().format(formatter), otpStartTime);
  }

  @Test
  void getOtpExpiryTime() {
    //GIVEN
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss");
    otpService.generateOTP(1L);
    //WHEN
    String otpExpiryTime = (String) otpService.getOtpExpiryTime(1L);
    //THEN
    assertFalse(otpExpiryTime.isEmpty());
    assertEquals(LocalDateTime.now().plus(5, ChronoUnit.MINUTES).format(formatter),
        otpExpiryTime);
  }

  @Test
  void clearOTP() {
    //GIVEN
    otpService.generateOTP(1L);
    //WHEN
    otpService.clearOTP(1L);
    //THEN
    assertEquals(0, otpService.getOtp(1L));
    assertEquals(0, otpService.getOtpStartTime(1L));
    assertEquals(0, otpService.getOtpExpiryTime(1L));
  }
}