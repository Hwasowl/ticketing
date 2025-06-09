package se.sowl.couponapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.couponapi.dto.CouponIssueRequest;
import se.sowl.couponapi.dto.CouponIssueResponse;
import se.sowl.couponapi.service.CouponIssueRequestService;

@RestController
@RequiredArgsConstructor
public class CouponIssueController {

    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/issue")
    public CouponIssueResponse issueV1(@RequestBody CouponIssueRequest request) {
        couponIssueRequestService.issueRequestV1(request);
        return new CouponIssueResponse(true, null);
    }

    @PostMapping("/v2/issue")
    public CouponIssueResponse issueV2(@RequestBody CouponIssueRequest request) {
        couponIssueRequestService.issueRequestV2(request);
        return new CouponIssueResponse(true, null);
    }

    @PostMapping("/v3/issue")
    public CouponIssueResponse issueV3(@RequestBody CouponIssueRequest request) {
        couponIssueRequestService.issueRequestV3(request);
        return new CouponIssueResponse(true, null);
    }
}
