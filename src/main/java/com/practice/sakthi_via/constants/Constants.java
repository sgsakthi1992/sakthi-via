package com.practice.sakthi_via.constants;

public final class Constants {
    /**
     * Message for Employee Username size.
     */
    public static final String EMPLOYEE_USERNAME_SIZE
            = "Username must have minimum 4 and maximum 10 characters";
    /**
     * Message for Employee minimum age.
     */
    public static final String EMPLOYEE_MINIMUM_AGE
            = "Employee must be 20 years old";
    /**
     * Employee username min value.
     */
    public static final int EMPLOYEE_USERNAME_MIN_VALUE = 4;
    /**
     * Employee username max value.
     */
    public static final int EMPLOYEE_USERNAME_MAX_VALUE = 10;
    /**
     * Employee age minimum value.
     */
    public static final int EMPLOYEE_AGE_MIN_VALUE = 20;
    /**
     * Email Subject.
     */
    public static final String EMAIL_SUBJECT = "Welcome to SAKTHI-VIA!!";
    /**
     * Private Constructor, caller will never create object for Constants class.
     */
    private Constants() {

    }
}
