package com.hdfcbank.logging.starter;

import com.hdfcbank.logging.core.LogHelper;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;

import java.io.IOException;

public class InterfaceLoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long start = System.currentTimeMillis();

        ClientHttpResponse response = execution.execute(request, body);
        long dur = System.currentTimeMillis() - start;

        int status = response.getStatusCode().value(); // ✅ new way

        // Avoid dumping large bodies here in starter; applications can enrich if needed.
        LogHelper.logInterface(request.getURI().toString(), status, dur, null, null);

        return response;
    }
}