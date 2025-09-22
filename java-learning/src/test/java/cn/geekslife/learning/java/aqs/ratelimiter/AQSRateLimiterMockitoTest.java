package cn.geekslife.learning.java.aqs.ratelimiter;

import cn.geekslife.learning.java.aqs.ratelimiter.algorithm.RateLimiterAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AQSRateLimiter的Mockito测试示例
 */
@ExtendWith(MockitoExtension.class)
class AQSRateLimiterMockitoTest {

    @Mock
    private RateLimiterAlgorithm mockAlgorithm;

    @Test
    void testAcquireWithMockedAlgorithm() throws InterruptedException {
        // Given
        AQSRateLimiter rateLimiter = new AQSRateLimiter(mockAlgorithm);
        when(mockAlgorithm.reserve(1)).thenReturn(0L);

        // When
        double waitTime = rateLimiter.acquire(1);

        // Then
        assertEquals(0.0, waitTime, 0.001);
        verify(mockAlgorithm).reserve(1);
    }

    @Test
    void testSetRateWithTokenBucketAlgorithm() {
        // Given
        cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm tokenBucketAlgorithm = 
            new cn.geekslife.learning.java.aqs.ratelimiter.algorithm.TokenBucketAlgorithm(5.0);
        AQSRateLimiter rateLimiter = new AQSRateLimiter(tokenBucketAlgorithm);

        // When
        rateLimiter.setRate(10.0);

        // Then
        assertEquals(10.0, rateLimiter.getRate(), 0.001);
    }

    @Test
    void testSetRateWithSlidingWindowAlgorithmThrowsException() {
        // Given
        cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm slidingWindowAlgorithm = 
            new cn.geekslife.learning.java.aqs.ratelimiter.algorithm.SlidingWindowAlgorithm(10, 1000);
        AQSRateLimiter rateLimiter = new AQSRateLimiter(slidingWindowAlgorithm);

        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            rateLimiter.setRate(5.0);
        });
    }
}