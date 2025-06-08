package se.sowl.couponapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.couponapi.dto.CouponIssueRequestDto;
import se.sowl.couponapi.dto.CouponIssueResponseDto;
import se.sowl.couponapi.service.CouponIssueRequestService;

@RestController
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/issue")
    public CouponIssueResponseDto issueV1(@RequestBody CouponIssueRequestDto request) {
        // 쿠폰 발급 요청
        couponIssueRequestService.issueRequest(request);
        return new CouponIssueResponseDto(true, null);
    }
}
