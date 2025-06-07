package se.sowl.couponapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.couponcore.service.CouponIssueService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;

    public void issueRequest(long couponId, long userId) {
        couponIssueService.issue(couponId, userId);
        log.info("쿠폰 발급 완료. couponId: {}, userId: {}", couponId, userId);
    }
}

