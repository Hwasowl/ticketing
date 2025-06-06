package se.sowl.ticketdomain.couponissue.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueFailReason {
    ALREADY_ISSUED,
    OUT_OF_STOCK,
    TIME_EXPIRED,
    SYSTEM_ERROR
}
