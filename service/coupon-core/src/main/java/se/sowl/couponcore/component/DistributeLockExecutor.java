package se.sowl.couponcore.component;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DistributeLockExecutor {
    private final RedissonClient redissonClient;

    public void execute(String lockName, long waitTime, long leaseTime, Runnable action) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (!isLocked) throw new IllegalStateException("락 획득 실패: " + lockName);
            action.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

