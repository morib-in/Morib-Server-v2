package org.morib.server.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.common.ApiResponseUtil;
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
        return ApiResponseUtil.failure(ErrorMessage.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<BaseResponse<?>> handleUnauthorizedException(final UnauthorizedException e) {
        log.error(">>> handle: UnauthorizedException ", e);
        return ApiResponseUtil.failure(ErrorMessage.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        return ApiResponseUtil.failure(ErrorMessage.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        return ApiResponseUtil.failure(ErrorMessage.TYPE_MISMATCH);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<BaseResponse<?>> handleIllegalArgumentException(final IllegalArgumentException e) {
        log.error(">>> handle: IllegalArgumentException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidURLException.class)
    protected ResponseEntity<BaseResponse<?>> handleInvalidURLException(final InvalidURLException e) {
        log.error(">>> handle: InvalidURLException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidQueryParameterException.class)
    protected ResponseEntity<BaseResponse<?>> handleInvalidQueryParameterException(final InvalidQueryParameterException e) {
        log.error(">>> handle: InvalidQueryParameterException ", e);
        return ApiResponseUtil.failure(ErrorMessage.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyFriendException.class)
    protected ResponseEntity<BaseResponse<?>> handleAlreadyFriendException(final AlreadyFriendException e) {
        log.error(">>> handle: AlreadyFriendException ", e);
        return ApiResponseUtil.failure(ErrorMessage.ALREADY_FRIEND);
    }

    @ExceptionHandler(AlreadyFriendRequestException.class)
    protected ResponseEntity<BaseResponse<?>> handleAlreadyFriendRequestException(final AlreadyFriendRequestException e) {
        log.error(">>> handle: AlreadyFriendRequestException ", e);
        return ApiResponseUtil.failure(ErrorMessage.ALREADY_FRIEND_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);
        return ApiResponseUtil.failure(ErrorMessage.INVALID_EMAIL);
    }

    @ExceptionHandler(SSEConnectionException.class)
    protected ResponseEntity<BaseResponse<?>> handleSSEConnectionException(final SSEConnectionException e) {
        log.error(">>> handle: SSEConnectionException ", e);
        return ApiResponseUtil.failure(ErrorMessage.SSE_CONNECT_FAILED);
    }

}

