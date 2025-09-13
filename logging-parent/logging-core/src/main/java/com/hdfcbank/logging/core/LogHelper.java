package com.hdfcbank.logging.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public final class LogHelper {
    private static final Logger LOG = LoggerFactory.getLogger("structured-logger");
    private LogHelper(){}

    private static Map<String,Object> base() {
        Map<String,Object> m = new HashMap<>();
        m.put("timestamp", Instant.now().toString());
        String corr = MDC.get("correlationId");
        if (corr != null) m.put("correlationId", corr);
        String txn = MDC.get("transactionId");
        if (txn != null) m.put("transactionId", txn);
        String svc = MDC.get("serviceName");
        if (svc != null) m.put("service", svc);
        return m;
    }

    public static void logTransaction(String customerId, String activity, String status, Map<String,Object> details) {
        Map<String,Object> m = base();
        m.put("logType", "TRANSACTION");
        m.put("customerId", customerId);
        m.put("activity", activity);
        m.put("status", status);
        if (details != null) m.put("details", details);
        LOG.info(JsonUtil.toJson(m));
    }

    public static void logInterface(String interfaceName, int httpStatus, long durationMs, Object request, Object response) {
        Map<String,Object> m = base();
        m.put("logType", "INTERFACE");
        m.put("interface", interfaceName);
        m.put("httpStatus", httpStatus);
        m.put("durationMs", durationMs);
        m.put("request", request);
        m.put("response", response);
        LOG.info(JsonUtil.toJson(m));
    }

    public static void logDropped(String reason, Object payload) {
        Map<String,Object> m = base();
        m.put("logType", "DROPPED");
        m.put("reason", reason);
        m.put("payload", payload);
        LOG.warn(JsonUtil.toJson(m));
    }

    public static void logApplication(String level, String message, Throwable t) {
        Map<String,Object> m = base();
        m.put("logType", "APPLICATION");
        m.put("level", level);
        m.put("message", message);
        if (t != null) m.put("exception", t.toString());
        if ("ERROR".equalsIgnoreCase(level)) LOG.error(JsonUtil.toJson(m));
        else if ("WARN".equalsIgnoreCase(level)) LOG.warn(JsonUtil.toJson(m));
        else LOG.debug(JsonUtil.toJson(m));
    }
}
