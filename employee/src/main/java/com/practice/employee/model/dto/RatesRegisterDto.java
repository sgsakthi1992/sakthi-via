package com.practice.employee.model.dto;

import com.practice.employee.validator.TargetCode;
import java.util.Set;
import javax.validation.constraints.Size;

public class RatesRegisterDto {

  /**
   * Currency code length.
   */
  private static final int CURRENCY_CODE_LENGTH = 3;
  /**
   * Employee id.
   */
  private Long id;
  /**
   * Base currency code.
   */
  @Size(min = CURRENCY_CODE_LENGTH,
      max = CURRENCY_CODE_LENGTH,
      message = "Currency code must be of 3 letters")
  private String base;
  /**
   * Target currency codes.
   */
  @TargetCode
  private Set<String> target;
  /**
   * Generated otp.
   */
  private Integer otp;

  /**
   * Getter for employee id.
   *
   * @return employee id
   */
  public Long getId() {
    return id;
  }

  /**
   * Setter for employee id.
   *
   * @param id employee id
   */
  public void setId(final Long id) {
    this.id = id;
  }

  /**
   * Getter for base currency code.
   *
   * @return base currency code
   */
  public String getBase() {
    return base;
  }

  /**
   * Setter for base currency code.
   *
   * @param base base currency code
   */
  public void setBase(final String base) {
    this.base = base;
  }

  /**
   * Getter for target currency codes.
   *
   * @return target currency codes
   */
  public Set<String> getTarget() {
    return target;
  }

  /**
   * Setter for target currency codes.
   *
   * @param target target currency codes
   */
  public void setTarget(final Set<String> target) {
    this.target = target;
  }

  /**
   * Getter for otp.
   *
   * @return otp.
   */
  public Integer getOtp() {
    return otp;
  }

  /**
   * Setter for otp.
   *
   * @param otp otp.
   */
  public void setOtp(final Integer otp) {
    this.otp = otp;
  }

  /**
   * Parameterized constructor.
   *
   * @param id     employee id
   * @param base   currency code
   * @param target currency codes
   * @param otp    Generated otp
   */
  public RatesRegisterDto(final Long id, final String base,
      final Set<String> target,
      final Integer otp) {
    this.id = id;
    this.base = base;
    this.target = target;
    this.otp = otp;
  }

  /**
   * Overridden toString method.
   *
   * @return String
   */
  @Override
  public String toString() {
    return "RatesRegister{"
        + "id=" + id
        + ", base='" + base
        + ", target=" + target
        + ", otp='" + otp
        + '}';
  }
}
