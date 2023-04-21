package old.spring.cb.circuitBreaker;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.function.Supplier;

public class CircuitBreakerInterceptor implements Interceptor {

    private final CircuitBreaker circuitBreaker;

    @Autowired
    public CircuitBreakerInterceptor(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) {
        try {
            Supplier<Response> decoratedSupplier = CircuitBreaker
                    .decorateSupplier(circuitBreaker, () -> {
                        try {
                            Response response =  chain.proceed(chain.request());

                            // Count any failed in the circuit
                            if(!response.isSuccessful()){
                                throw new RuntimeException("Error with code: " + response.code());
                            }

                            return response;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            return decoratedSupplier.get();
        } catch (CallNotPermittedException e) {
            //Do anything
            throw new RuntimeException("Circuit is Open", e);
        }
    }
}