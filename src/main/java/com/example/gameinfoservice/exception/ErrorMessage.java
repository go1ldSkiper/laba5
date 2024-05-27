package com.example.gameinfoservice.exception;

import java.util.Date;

/**
 * The type Error message.
 */
public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}
