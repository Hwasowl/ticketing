package se.sowl.couponconsumer.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.sowl.couponcore.dto.CouponIssueRequest;
import se.sowl.couponcore.repository.redis.RedisRepository;
import se.sowl.couponcore.service.CouponIssueService;
import se.sowl.couponcore.utils.CouponIssueRequestQueueKey;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CouponIssueListener {
    private final RedisRepository redisRepository;
    private final CouponIssueService couponIssueService;
    private final String issueRequestQueueKey = CouponIssueRequestQueueKey.get();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Scheduled(fixedDelay = 1000L)
    public void issue() throws JsonProcessingException {
        while (issueRequestExist()) {
            CouponIssueRequest target = getIssueQueue();
            log.info("[발급 시작] target : %s".formatted(target.getUserId()));
            try {
                couponIssueService.issue(target.getCouponId(), target.getUserId());
            } catch(Exception e) {
                log.error("[발급 실패] target : %s, error: %s".formatted(target.getCouponId() + " / " + target.getUserId(), e.getMessage()));
                removeIssueQueue();
                continue;
            }
            log.info("[발급 완료] target : %s".formatted(target.getCouponId()));
            removeIssueQueue();
        }
    }

    private void removeIssueQueue() {
        redisRepository.lPop(issueRequestQueueKey);
    }

    private boolean issueRequestExist() {
        return redisRepository.lSize(issueRequestQueueKey) > 0;
    }

    private CouponIssueRequest getIssueQueue() throws JsonProcessingException {
        return objectMapper.readValue(redisRepository.lIndex(issueRequestQueueKey, 0), CouponIssueRequest.class);
    }
}
