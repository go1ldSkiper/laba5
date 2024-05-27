package com.example.gameinfoservice.exception;

/** The type Bad request exception. */
public class BadRequestException extends RuntimeException {
  /**
   * Instantiates a new Bad request exception.
   *
   * @param message the message
   */
  public BadRequestException(final String message) {
    super(message);
  }
}
