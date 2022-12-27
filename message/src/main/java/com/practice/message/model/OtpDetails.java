package com.practice.message.model;

public final class OtpDetails {

  /**
   * Otp number.
   */
  private final Object otp;
  /**
   * Otp start time.
   */
  private final Object otpStartTime;
  /**
   * Otp expiry time.
   */
  private final Object otpExpiryTime;

  /**
   * To get the otp number.
   *
   * @return otp
   */
  public Object getOtp() {
    return otp;
  }

  /**
   * To get the otp start time.
   *
   * @return otp start time
   */
  public Object getOtpStartTime() {
    return otpStartTime;
  }

  /**
   * To get the otp expiry time.
   *
   * @return otp expiry time
   */
  public Object getOtpExpiryTime() {
    return otpExpiryTime;
  }

  /**
   * Private default constructor.
   *
   * @param otp           otp
   * @param otpStartTime  otp start time
   * @param otpExpiryTime otp expiry time
   */
  public OtpDetails(final Object otp, final Object otpStartTime,
      final Object otpExpiryTime) {

    this.otp = otp;
    this.otpStartTime = otpStartTime;
    this.otpExpiryTime = otpExpiryTime;
  }
}
