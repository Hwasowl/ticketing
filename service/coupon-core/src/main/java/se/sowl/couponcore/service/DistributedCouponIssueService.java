package se.sowl.couponcore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.couponcore.component.DistributeLockExecutor;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedCouponIssueService {
    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;

    @Transactional
    public void distributedLockIssue(Long couponId, Long userId) {
        distributeLockExecutor.execute("lock_%s".formatted(couponId), 9000, 9000, () ->
            couponIssueService.issue(couponId, userId));}
}
