package old.spring.cb.circuitBreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CustomCircuitBreaker {

    private final String CIRCUIT_NAME = "MyCircuit";

    @Bean
    public CircuitBreaker getCircuit() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config());
        return circuitBreakerRegistry
                .circuitBreaker(CIRCUIT_NAME);
    }

    private CircuitBreakerConfig config() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(40)
                .waitDurationInOpenState(Duration.ofMinutes(1))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .minimumNumberOfCalls(3)
                .permittedNumberOfCallsInHalfOpenState(1)
                .build();
    }
}
