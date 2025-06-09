package se.sowl.couponapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record CouponIssueResponse(boolean isSuccess, String comment) {
}

