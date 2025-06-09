package se.sowl.couponapi;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.sowl.couponapi.dto.CouponIssueResponse;
import se.sowl.couponcore.exception.CouponIssueException;

@RestControllerAdvice
public class CouponControllerAdvice {
    @ExceptionHandler(CouponIssueException.class)
    public CouponIssueResponse couponIssueExceptionHandler(CouponIssueException exception) {
        return new CouponIssueResponse(false, exception.getErrorCode().message);
    }
}
