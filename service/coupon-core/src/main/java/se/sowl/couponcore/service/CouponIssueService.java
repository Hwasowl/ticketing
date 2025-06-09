package se.sowl.couponcore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.couponcore.component.DistributeLockExecutor;
import se.sowl.couponcore.entity.Coupon;
import se.sowl.couponcore.entity.CouponIssue;
import se.sowl.couponcore.exception.CouponIssueException;
import se.sowl.couponcore.exception.ErrorCode;
import se.sowl.couponcore.repository.mysql.CouponIssueRepository;
import se.sowl.couponcore.repository.mysql.CouponRepository;

@Service
@RequiredArgsConstructor
public class CouponIssueService {
    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final DistributeLockExecutor distributeLockExecutor;
    private final CouponIssueQueueService couponIssueQueueService;

    public void syncIssue(long couponId, long userId) {
        synchronized (this) {
            issue(couponId, userId);
        }
    }

    public void issue(long couponId, long userId) {
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. " + couponId));
        issueCoupon(couponId, userId, coupon);
    }

    @Transactional
    public void issueV2(long couponId, long userId) {
        Coupon coupon = couponRepository.findCouponWithLock(couponId)
            .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. " + couponId));
        issueCoupon(couponId, userId, coupon);
    }


    public void issueEvent(long couponId, long userId) {
        String lockKey = "lock:coupon:" + couponId;
        distributeLockExecutor.execute(lockKey, 3000, 5000, () ->
            couponIssueQueueService.pushRequestToQueue(couponId, userId));
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

