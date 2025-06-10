package se.sowl.couponcore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncCouponIssueService {
    private final CouponIssueService couponIssueService;

    public void syncIssue(long couponId, long userId) {
        synchronized (this) {
            couponIssueService.issue(couponId, userId);
        }
    }
}
