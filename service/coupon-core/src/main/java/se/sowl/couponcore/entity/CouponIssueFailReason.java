package se.sowl.couponcore.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CouponIssueFailReason {
    ALREADY_ISSUED,
    OUT_OF_STOCK,
    TIME_EXPIRED,
    SYSTEM_ERROR
}
