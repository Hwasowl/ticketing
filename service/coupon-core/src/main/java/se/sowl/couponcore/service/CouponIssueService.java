package se.sowl.couponcore.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new CouponIssueException(ErrorCode.COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. " + couponId));
        checkIsAlreadyIssued(couponId, userId);
        coupon.issue();
        couponIssueRepository.save(CouponIssue.success(userId, couponId));
    }

    private void checkIsAlreadyIssued(long couponId, long userId) {
        if (couponIssueRepository.existsByCouponIdAndUserId(couponId, userId)) {
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다.");
        }
    }
}

