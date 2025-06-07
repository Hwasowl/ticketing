package se.sowl.couponcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.couponcore.entity.CouponIssue;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long>{
}
