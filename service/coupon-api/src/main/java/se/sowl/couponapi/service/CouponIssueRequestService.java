package se.sowl.couponapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.couponapi.dto.CouponIssueRequest;
import se.sowl.couponcore.service.CouponIssueService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;

    public void issueRequestV1(CouponIssueRequest request) {
        couponIssueService.issue(request.couponId(), request.userId());
        printLog(request);
    }

    public void issueRequestV2(CouponIssueRequest request) {
        couponIssueService.syncIssue(request.couponId(), request.userId());
        printLog(request);
    }

    public void issueRequestV3(CouponIssueRequest request) {
        couponIssueService.issueEvent(request.couponId(), request.userId());
        log.info("쿠폰 발급 이벤트 발행 완료. couponId: {}, userId: {}", request.couponId(), request.userId());
    }

    private static void printLog(CouponIssueRequest request) {
        log.info("쿠폰 발급 완료. couponId: {}, userId: {}", request.couponId(), request.userId());
    }
}

