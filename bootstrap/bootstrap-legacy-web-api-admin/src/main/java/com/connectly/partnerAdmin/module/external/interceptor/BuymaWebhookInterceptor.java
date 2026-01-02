package com.connectly.partnerAdmin.module.external.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class BuymaWebhookInterceptor implements HandlerInterceptor {

    protected static final String BUYMA_EVENT_HEADER = "X-Buyma-Event";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String webhookHeader = request.getHeader(BUYMA_EVENT_HEADER);

        if (webhookHeader != null) {
            if (webhookHeader.startsWith("product/")) {
                if (webhookHeader.contains("fail")) {
                    log.error("Failed webhook event received: {}", webhookHeader);
                }
            }

            if (webhookHeader.startsWith("order/")) {
                if (webhookHeader.contains("fail")) {
                    log.error("Failed webhook event received: {}", webhookHeader);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed webhook event");
                    return false;
                }
            }

        } else {
            log.error("Webhook header is missing");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Webhook header missing");
            return false;
        }

        return true;
    }
}
