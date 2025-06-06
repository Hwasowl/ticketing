package se.sowl.ticketdomain.coupon.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import se.sowl.ticketdomain.common.BaseTimeEntity;

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
}
