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
    private Integer quantityLimit;

    @Column(nullable = false)
    private Integer issuedQuantity;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    public static Coupon create(String name, Integer quantityLimit, LocalDateTime startAt, LocalDateTime endAt) {
        Coupon coupon = new Coupon();
        coupon.name = name;
        coupon.quantityLimit = quantityLimit;
        coupon.issuedQuantity = 0;
        coupon.startAt = startAt;
        coupon.endAt = endAt;
        coupon.status = CouponStatus.ENABLED;
        return coupon;
    }

    public void disable() {
        this.status = CouponStatus.DISABLED;
    }

    public boolean isValidDate() {
        LocalDateTime now = LocalDateTime.now();
        return (now.isEqual(startAt) || now.isAfter(startAt)) && now.isBefore(endAt);
    }

    public void issue() {
        if (++issuedQuantity > quantityLimit) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량이 없습니다.");
        }
        if (!isValidDate()) {
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE, "쿠폰 발급 기간이 아닙니다.");
        }
        if (status != CouponStatus.ENABLED) {
            throw new CouponIssueException(ErrorCode.COUPON_NOT_AVAILABLE, "유효하지 않은 쿠폰입니다.");
        }
    }
}
