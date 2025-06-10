package se.sowl.couponapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.couponapi.dto.CouponIssueRequest;
import se.sowl.couponcore.service.CouponIssueService;
import se.sowl.couponcore.service.DistributedCouponIssueService;
import se.sowl.couponcore.service.SyncCouponIssueService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {
    private final CouponIssueService couponIssueService;
    private final SyncCouponIssueService syncCouponIssueService;
    private final DistributedCouponIssueService distributedCouponIssueService;

    public void issueRequestV1(CouponIssueRequest request) {
        couponIssueService.issue(request.couponId(), request.userId());
    }

    // 단일 서버 - 스레드 동기화 처리로 문제 해결
    public void issueRequestV2(CouponIssueRequest request) {
        syncCouponIssueService.syncIssue(request.couponId(), request.userId());
    }

    // 분산 서버 - 분산 락을 사용해 문제 해결
    public void issueRequestV3(CouponIssueRequest request) {
        distributedCouponIssueService.distributedLockIssue(request.couponId(), request.userId());
    }
}

