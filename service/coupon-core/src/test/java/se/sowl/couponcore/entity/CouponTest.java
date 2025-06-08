package se.sowl.couponcore.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import se.sowl.couponcore.exception.CouponIssueException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("쿠폰 발급 성공")
    void issueSuccess() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.create("TEST-COUPON-1", 10, now.minusMinutes(1), now.plusMinutes(5));

        // when
        coupon.issue();

        // then
        assertThat(coupon.getIssuedQuantity()).isEqualTo(9);
    }

    @Test
    @DisplayName("쿠폰 발급 시 제한 수량보다 더 많이 발급할 수 없다.")
    void issueFailWhenOutOfStocked() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.create("TEST-COUPON-1", 1, now.minusMinutes(1), now.plusMinutes(5));
        coupon.issue(); // 1개 발급

        // when & then
        assertThatThrownBy(coupon::issue)
            .isInstanceOf(CouponIssueException.class)
            .hasMessageContaining("발급 가능한 수량이 없습니다.");
    }

    @Test
    @DisplayName("쿠폰 유효 기간이 지났다면 발급 불가능")
    void issueFailWhenInvalidDate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.create("TEST-COUPON-1", 10, now.plusMinutes(10), now.plusMinutes(20)); // 미래

        // when & then
        assertThatThrownBy(coupon::issue)
            .isInstanceOf(CouponIssueException.class)
            .hasMessageContaining("쿠폰 발급 기간이 아닙니다.");
    }

    @Test
    @DisplayName("쿠폰이 비활성화 상태라면 발급 불가능")
    void issueFailWhenTryIssueDisabledCoupon() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.create("TEST-COUPON-1", 10, now.minusMinutes(1), now.plusMinutes(5));
        coupon.disable();

        // when & then
        assertThatThrownBy(coupon::issue)
            .isInstanceOf(CouponIssueException.class)
            .hasMessageContaining("유효하지 않은 쿠폰입니다.");
    }
}
