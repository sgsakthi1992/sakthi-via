package com.practice.employee.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TargetCodeValidator.class)
@Documented
public @interface TargetCode {

  /**
   * Validation message.
   *
   * @return String
   */
  String message() default "Not a valid currency code";

  /**
   * Groups.
   *
   * @return Class
   */
  Class<?>[] groups() default {};

  /**
   * Payload.
   *
   * @return Class
   */
  Class<? extends Payload>[] payload() default {};
}
