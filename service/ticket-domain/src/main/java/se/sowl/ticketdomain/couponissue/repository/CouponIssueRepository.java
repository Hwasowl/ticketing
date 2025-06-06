package se.sowl.ticketdomain.couponissue.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.ticketdomain.couponissue.domain.CouponIssue;

public interface CouponIssueRepository extends JpaRepository<CouponIssue, Long>{
}
