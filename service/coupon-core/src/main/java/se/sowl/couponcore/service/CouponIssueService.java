package se.sowl.couponcore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private final CouponIssueQueueService couponIssueQueueService;

    @Transactional
    public void issue(Long couponId, Long userId) {
        Coupon coupon = findCoupon(couponId);
        issueCoupon(couponId, userId, coupon);
    }

    private Coupon findCoupon(Long couponId) {
        return couponRepository.findCouponWithLock(couponId)
            .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. couponId=" + couponId));
    }

    private void issueCoupon(Long couponId, Long userId, Coupon coupon) {
        checkIsAlreadyIssued(couponId, userId);
        coupon.issue();
        couponRepository.save(coupon);
        couponIssueRepository.save(CouponIssue.success(userId, couponId));
        log.info("쿠폰 발급 완료. couponId=%s, userId=%s".formatted(couponId, userId));
    }

    private void checkIsAlreadyIssued(Long couponId, Long userId) {
        if (couponIssueRepository.existsByCouponIdAndUserId(couponId, userId)) {
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다. couponId=" + couponId);
        }
    }
}

