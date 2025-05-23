package th.co.priorsolution.training.restaurant.controller.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import th.co.priorsolution.training.restaurant.exception.CustomException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                ex.getStatus().value(),
                Instant.now().toString()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse error = new ErrorResponse(
                "AUTH_FAILED",
                "Unauthorized: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                Instant.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(
                "ACCESS_DENIED",
                "Access denied: " + ex.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                Instant.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "Unexpected error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ✅ Response Model
    public record ErrorResponse(String errorCode, String errorMessage, int status, String timestamp) {
    }
}
