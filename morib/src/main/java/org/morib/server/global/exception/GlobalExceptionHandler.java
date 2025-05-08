package org.morib.server.global.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.common.util.ApiResponseUtil;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<BaseResponse<?>> handleNotFoundException(final NotFoundException e) {
        log.error(">>> handle: NotFoundException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<BaseResponse<?>> handleUnauthorizedException(final UnauthorizedException e) {
        log.error(">>> handle: UnauthorizedException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<BaseResponse<?>> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.error(">>> handle: IllegalArgumentException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidURLException.class)
    protected ResponseEntity<BaseResponse<?>> handleInvalidURLException(final InvalidURLException e) {
        log.error(">>> handle: InvalidURLException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(InvalidQueryParameterException.class)
    protected ResponseEntity<BaseResponse<?>> handleInvalidQueryParameterException(final InvalidQueryParameterException e) {
        log.error(">>> handle: InvalidQueryParameterException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(AlreadyFriendException.class)
    protected ResponseEntity<BaseResponse<?>> handleAlreadyFriendException(final AlreadyFriendException e) {
        log.error(">>> handle: AlreadyFriendException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(AlreadyFriendRequestException.class)
    protected ResponseEntity<BaseResponse<?>> handleAlreadyFriendRequestException(final AlreadyFriendRequestException e) {
        log.error(">>> handle: AlreadyFriendRequestException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", 400);
        e.getBindingResult().getFieldErrors().stream()
                .forEach(error -> errors.put("message", error.getField() + " " + error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<?> handleConstraintViolationException(final ConstraintViolationException e) {
        log.error(">>> handle: ConstraintViolationException ", e);
        Map<String, Object> errors = new TreeMap<>();
        errors.put("status", 400);
        errors.put("message", e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining(",")));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(SSEConnectionException.class)
    protected ResponseEntity<BaseResponse<?>> handleSSEConnectionException(final SSEConnectionException e) {
        log.error(">>> handle: SSEConnectionException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    protected ResponseEntity<BaseResponse<?>> handleDuplicateResourceException(final DuplicateResourceException e) {
        log.error(">>> handle: DuplicateResourceException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(InvalidTaskInTodoException.class)
    protected ResponseEntity<BaseResponse<?>> handleInvalidTaskInTodoException(final InvalidTaskInTodoException e) {
        log.error(">>> handle: InvalidTaskInTodoException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.error(">>> handle: MissingServletRequestParameterException, parameter name: {}", e.getParameterName());
        return ApiResponseUtil.failure(ErrorMessage.valueOf(e.getMessage()));
    }

    @ExceptionHandler(InvalidStateException.class)
    protected ResponseEntity<BaseResponse<?>> handleInvalidStateException(final InvalidStateException e) {
        log.error(">>> handle: InvalidStateException ", e);
        return ApiResponseUtil.failure(e.getErrorMessage());
    }
}

