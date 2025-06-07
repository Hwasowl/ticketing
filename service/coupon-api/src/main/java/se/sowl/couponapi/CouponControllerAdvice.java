package se.sowl.couponapi;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.sowl.couponapi.dto.CouponIssueResponseDto;
import se.sowl.couponcore.exception.CouponIssueException;

@RestControllerAdvice
public class CouponControllerAdvice {
    @ExceptionHandler(CouponIssueException.class)
    public CouponIssueResponseDto couponIssueExceptionHandler(CouponIssueException exception) {
        return new CouponIssueResponseDto(false, exception.getErrorCode().message);
    }
}
