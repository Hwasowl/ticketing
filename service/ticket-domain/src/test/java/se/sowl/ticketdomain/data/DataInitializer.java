package se.sowl.ticketdomain.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import se.sowl.ticketdomain.coupon.domain.Coupon;
import se.sowl.ticketdomain.coupon.repository.CouponRepository;
import se.sowl.ticketdomain.couponissue.domain.CouponIssue;
import se.sowl.ticketdomain.couponissue.repository.CouponIssueRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootTest
public class DataInitializer {

    @Autowired
    CouponRepository couponRepository;
    @Autowired
    CouponIssueRepository couponIssueRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    TransactionTemplate transactionTemplate;

    static final int USER_SIZE = 2_000_000;  // 200만 유저
    static final int COUPON_SIZE = 10;       // 10종 쿠폰
    static final int BULK_INSERT_SIZE = 2000;
    static final int THREAD_POOL_SIZE = 10;

    @Test
    void initializeBulkCouponIssueData() throws InterruptedException {
//        makeCouponData(); // 쿠폰 정책 생성
        makeCouponIssueData(); // 발급 이력 생성
    }

    void makeCouponData() {
        List<Coupon> coupons = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < COUPON_SIZE; i++) {
            coupons.add(Coupon.create(
                "COUPON-" + i,
                USER_SIZE,
                now.minusDays(1),
                now.plusDays(30)
            ));
        }
        couponRepository.saveAll(coupons);
        couponRepository.flush();
        System.out.println("쿠폰 정책 10개 생성 완료");
    }

    void makeCouponIssueData() throws InterruptedException {
        List<Long> couponIds = couponRepository.findAll()
            .stream().map(Coupon::getId).collect(Collectors.toList());

        int batchSize = BULK_INSERT_SIZE;
        List<CouponIssue> batch = new ArrayList<>(batchSize);
        long totalInserted = 0;

        for (long userId = 1; userId <= USER_SIZE; userId++) {
            for (Long couponId : couponIds) {
                batch.add(CouponIssue.success(userId, couponId));
                if (batch.size() == batchSize) {
                    transactionTemplate.executeWithoutResult(status -> {
                        for (CouponIssue issue : batch) {
                            entityManager.persist(issue);
                        }
                        entityManager.flush();
                        entityManager.clear();
                    });
                    totalInserted += batch.size();
                    batch.clear();
                    if (totalInserted % 1_000_000 == 0) {
                        System.out.println("Inserted: " + totalInserted + "건");
                    }
                }
            }
        }
        // 마지막 남은 데이터
        if (!batch.isEmpty()) {
            transactionTemplate.executeWithoutResult(status -> {
                for (CouponIssue issue : batch) {
                    entityManager.persist(issue);
                }
                entityManager.flush();
                entityManager.clear();
            });
            totalInserted += batch.size();
        }
        System.out.println("최종 Inserted: " + totalInserted + "건");
    }

}
