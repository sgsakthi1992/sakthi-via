package com.practice.sakthi_via.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {
    /**
     * Validation message.
     *
     * @return String
     */
    String message() default "Username already exists";

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
