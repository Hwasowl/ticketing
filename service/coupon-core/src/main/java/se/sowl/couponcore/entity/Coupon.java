package se.sowl.couponcore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import se.sowl.couponcore.exception.CouponIssueException;
import se.sowl.couponcore.exception.ErrorCode;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "coupon")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Coupon extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer remainCount; // 남은 쿠폰 수는 배치를 통해 주기적 갱신 (실시간 X)

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    public static Coupon create(String name, Integer totalCount, LocalDateTime startAt, LocalDateTime endAt) {
        Coupon coupon = new Coupon();
        coupon.name = name;
        coupon.totalCount = totalCount;
        coupon.remainCount = totalCount;
        coupon.startAt = startAt;
        coupon.endAt = endAt;
        coupon.status = CouponStatus.ENABLED;
        return coupon;
    }

    public boolean isValidDate() {
        return startAt.isAfter(LocalDateTime.now()) || endAt.isBefore(LocalDateTime.now());
    }

    public void issue() {
        if (remainCount <= 0) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량이 없습니다.");
        }
        if (!isValidDate()) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "쿠폰 발급 기간이 아닙니다.");
        }
        if (status != CouponStatus.ENABLED) {
            throw new CouponIssueException(ErrorCode.COUPON_NOT_AVAILABLE, "유효하지 않은 쿠폰입니다.");
        }
        remainCount--;
    }
}
