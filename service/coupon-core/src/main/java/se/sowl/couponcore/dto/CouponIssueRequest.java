package se.sowl.couponcore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueRequest {
    private Long couponId;
    private Long userId;
}
