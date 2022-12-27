package com.practice.exception;

import java.util.Date;

public final class ErrorDetails {

  /**
   * Error timestamp.
   */
  private final Date timestamp;
  /**
   * Error message.
   */
  private final String message;
  /**
   * Details about request uri.
   */
  private final String details;

  /**
   * ErrorDetails Parameterized Constructor.
   *
   * @param timestamp
   * @param message
   * @param details
   */
  public ErrorDetails(final Date timestamp,
      final String message, final String details) {
    super();
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
  }

  /**
   * Getter for timestamp.
   *
   * @return Date
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * Getter for message.
   *
   * @return message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Getter for details.
   *
   * @return details
   */
  public String getDetails() {
    return details;
  }
}
