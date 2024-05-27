package com.example.gameinfoservice.exception;

/** The type Resource not found exception. */
public class ResourceNotFoundException extends RuntimeException {
  /**
   * Instantiates a new Resource not found exception.
   *
   * @param msg the msg
   */
  public ResourceNotFoundException(final String msg) {
    super(msg);
  }
}
