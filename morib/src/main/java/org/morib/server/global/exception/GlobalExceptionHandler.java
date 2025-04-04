package org.morib.server.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.common.util.ApiResponseUtil;
import org.morib.server.global.common.BaseResponse;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    protected ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);
        return ApiResponseUtil.failure(ErrorMessage.TYPE_MISMATCH);
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
}

