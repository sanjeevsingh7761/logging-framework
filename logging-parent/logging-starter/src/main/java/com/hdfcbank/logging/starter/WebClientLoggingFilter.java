package com.hdfcbank.logging.starter;

import com.hdfcbank.logging.core.LogHelper;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class WebClientLoggingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        long start = System.currentTimeMillis();

        return next.exchange(request)
                .doOnNext(resp -> {
                    long duration = System.currentTimeMillis() - start;
                    LogHelper.logInterface(
                            request.url().toString(),
                            resp.statusCode().value(),
                            duration,
                            null,   // request body if needed
                            null    // response body if needed
                    );
                });
    }
}