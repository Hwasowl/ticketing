package se.sowl.couponcore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sowl.couponcore.dto.CouponIssueRequest;
import se.sowl.couponcore.repository.redis.RedisRepository;
import se.sowl.couponcore.utils.CouponIssueRequestQueueKey;

@Service
@RequiredArgsConstructor
public class CouponIssueQueueService {
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String issueRequestQueueKey = CouponIssueRequestQueueKey.get();

    public void pushRequestToQueue(Long couponId, Long userId) {
        CouponIssueRequest request = new CouponIssueRequest(couponId, userId);
        try {
            String json = objectMapper.writeValueAsString(request);
            redisRepository.rPush(issueRequestQueueKey, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("쿠폰 발급 요청 직렬화 오류", e);
        }
    }
}
