package se.sowl.couponcore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.sowl.couponcore.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
