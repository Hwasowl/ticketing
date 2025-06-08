package se.sowl.couponapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.sowl.couponapi.dto.CouponIssueRequestDto;
import se.sowl.couponapi.dto.CouponIssueResponseDto;
import se.sowl.couponcore.service.CouponIssueService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;

    public void issueRequest(CouponIssueRequestDto request) {
        couponIssueService.syncIssue(request.couponId(), request.userId());
        log.info("쿠폰 발급 완료. couponId: {}, userId: {}", request.couponId(), request.userId());
    }
}

