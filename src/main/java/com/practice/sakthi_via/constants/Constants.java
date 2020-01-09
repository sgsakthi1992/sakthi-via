package com.practice.sakthi_via.constants;

/**
 * Constants Class.
 *
 * @author Sakthi_Subramaniam
 */
public final class Constants {

    /**
     * Logger message for Employee ID not found.
     */
    public static final String EMPLOYEE_ID_NOT_FOUND_MSG
            = "Employee Id {} not found";
    /**
     * Logger message for Employee Username or Email not found.
     */
    public static final String EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND_MSG
            = "Employee not found with Username: {} or Email: {}";
    /**
     * Logger message for Employee Email not found.
     */
    public static final String EMPLOYEE_EMAIL_NOT_FOUND_MSG
            = "Email {} not found";
    /**
     * Message for failed Email validation.
     */
    public static final String EMAIL_VALIDATION_MSG
            = "Not a well-formed email address";
    /**
     * Message for invalid values in Employee details.
     */
    public static final String INVALID_EMPLOYEE_VALUES
            = "Employee details has invalid values";
    /**
     * Message for Employee Id not found.
     */
    public static final String EMPLOYEE_ID_NOT_FOUND = "Employee Id not found";
    /**
     * Message for Employee email update success.
     */
    public static final String EMPLOYEE_EMAIL_UPDATED_SUCCESS
            = "Successfully updated employee email";
    /**
     * Message for Employee create success.
     */
    public static final String EMPLOYEE_CREATE_SUCCESS
            = "Employee created successfully";
    /**
     * Message for Employee retrieve success.
     */
    public static final String EMPLOYEE_RETRIEVE_SUCCESS
            = "Successfully retrieved employee details";
    /**
     * Message for Employee delete success.
     */
    public static final String EMPLOYEE_DELETE_SUCCESS
            = "Successfully deleted employee details";
    /**
     * Message for Employee Username or Email not found.
     */
    public static final String EMPLOYEE_USERNAME_OR_EMAIL_NOT_FOUND
            = "Employee Username or email id not found";
    /**
     * Message for Employee Email not found.
     */
    public static final String EMPLOYEE_EMAIL_NOT_FOUND
            = "Employee email not found";
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
     * Employee sequence initial value.
     */
    public static final int EMPLOYEE_SEQUENCE_INITIAL = 40000;
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
     * HTTP Status OK value.
     */
    public static final int HTTP_STATUS_OK = 200;
    /**
     * HTTP Status Bad Request value.
     */
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    /**
     * HTTP Status Not Found value.
     */
    public static final int HTTP_STATUS_NOT_FOUND = 404;
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
