package se.sowl.couponcore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.couponcore.component.DistributeLockExecutor;
import se.sowl.couponcore.entity.Coupon;
import se.sowl.couponcore.entity.CouponIssue;
import se.sowl.couponcore.exception.CouponIssueException;
import se.sowl.couponcore.exception.ErrorCode;
import se.sowl.couponcore.repository.mysql.CouponIssueRepository;
import se.sowl.couponcore.repository.mysql.CouponRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final DistributeLockExecutor distributeLockExecutor;
    private final CouponIssueQueueService couponIssueQueueService;

    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = couponRepository.findCouponWithLock(couponId)
            .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. " + couponId));
        issueCoupon(couponId, userId, coupon);
    }

    @Transactional
    public void distributedLockIssue(long couponId, long userId) {
        distributeLockExecutor.execute("lock_%s".formatted(couponId), 9000, 9000, () -> issue(couponId, userId));
        log.info("쿠폰 발급 완료. couponId: %s, userId : %s".formatted(couponId, userId));
    }

    private void issueCoupon(long couponId, long userId, Coupon coupon) {
        checkIsAlreadyIssued(couponId, userId);
        coupon.issue();
        couponRepository.save(coupon);
        couponIssueRepository.save(CouponIssue.success(userId, couponId));
    }

    private void checkIsAlreadyIssued(long couponId, long userId) {
        if (couponIssueRepository.existsByCouponIdAndUserId(couponId, userId)) {
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다.");
        }
    }
}

