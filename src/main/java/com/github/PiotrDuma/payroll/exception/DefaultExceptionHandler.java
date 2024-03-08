package com.github.PiotrDuma.payroll.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class DefaultExceptionHandler extends ResponseEntityExceptionHandler {
    private final Clock clock;

    public DefaultExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    private ResponseEntity<ExceptionDto> handleResourceNotFoundException(Exception ex, HttpServletRequest request) {
        ExceptionDto exceptionDto = new ExceptionDto(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now(clock)
        );
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    private ResponseEntity<ExceptionDto> handleArgumentException(Exception ex, HttpServletRequest request) {
        ExceptionDto exceptionDto = new ExceptionDto(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(clock)
        );
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @Override //NoHandlerFoundException.class
    @Nullable
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpServletRequest httpRequest = getHttpServletRequest(request);
        ExceptionDto exceptionDto = new ExceptionDto(
            httpRequest.getRequestURI(),
            "The requested URL does not exist",
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now(clock)
        );
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @Override //HttpRequestMethodNotSupportedException.class
    @Nullable
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpServletRequest httpRequest = getHttpServletRequest(request);
        ExceptionDto exceptionDto = new ExceptionDto(
            httpRequest.getRequestURI(),
            "Request method '" + httpRequest.getMethod() + "' is not supported",
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(clock)
        );
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    private ResponseEntity<ExceptionDto> handleDefaultException(HttpServletRequest request) {
        ExceptionDto exceptionDto = new ExceptionDto(
                request.getRequestURI(),
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(clock)
        );
        return new ResponseEntity<>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpServletRequest getHttpServletRequest(WebRequest request) {
        return ((ServletWebRequest) request).getRequest();
    }

    private record ExceptionDto(
            String requestURL,
            String errorMessage,
            int statusCode,
            LocalDateTime timestamp
    ) {
    }
}