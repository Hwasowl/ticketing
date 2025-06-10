package se.sowl.couponcore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sowl.couponcore.CouponCoreConfiguration;
import se.sowl.couponcore.entity.Coupon;
import se.sowl.couponcore.exception.CouponIssueException;
import se.sowl.couponcore.repository.mysql.CouponRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest(classes = CouponCoreConfiguration.class)
class CouponIssueServiceTest {

    @Autowired
    private CouponIssueService couponIssueService;
    @Autowired
    private CouponRepository couponRepository;

    @Nested
    @DisplayName("issue")
    class Issue {
        @Test
        @DisplayName("미등록 쿠폰은 발급할 수 없다.")
        void couponNotExist() {
            // given
            Long couponId = 1L;
            Long userId = 100L;
            // 쿠폰 메타 추가 X

            // when & then
            assertThatThrownBy(() -> couponIssueService.issue(couponId, userId))
                .isInstanceOf(CouponIssueException.class)
                .hasMessageContaining("쿠폰 정책이 존재하지 않습니다. couponId="+couponId);
        }

        @Test
        @DisplayName("이미 발급된 쿠폰은 재발급할 수 없다.")
        void alreadyIssuedCoupon() {
            // given
            Long userId = 100L;
            Coupon coupon = couponRepository.save(Coupon.create("TEST-COUPON-1", 10, LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
            couponIssueService.issue(coupon.getId(), userId);

            // when & then
            assertThatThrownBy(() -> couponIssueService.issue(coupon.getId(), userId))
                .isInstanceOf(CouponIssueException.class)
                .hasMessageContaining("이미 발급된 쿠폰입니다. couponId=" + coupon.getId());
        }
    }
}
