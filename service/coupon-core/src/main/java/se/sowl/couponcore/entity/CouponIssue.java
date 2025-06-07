package se.sowl.couponcore.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "coupon_issue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Enumerated(EnumType.STRING)
    @Column(name = "fail_reason", length = 200)
    private CouponIssueFailReason failReason;

    public static CouponIssue success(Long userId, Long couponId) {
        CouponIssue issue = new CouponIssue();
        issue.userId = userId;
        issue.couponId = couponId;
        issue.failReason = null;
        return issue;
    }

    public static CouponIssue fail(Long userId, Long couponId, CouponIssueFailReason reason) {
        CouponIssue issue = new CouponIssue();
        issue.userId = userId;
        issue.couponId = couponId;
        issue.failReason = reason;
        return issue;
    }
}
