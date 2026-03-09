package com.ryuqq.setof.adapter.in.sqs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * SQS Listener 설정 Properties.
 *
 * <p>설정 예시 (application.yml):
 *
 * <pre>{@code
 * aws:
 *   sqs:
 *     listener:
 *       discount-outbox-queue-url: https://sqs.ap-northeast-2.amazonaws.com/.../discount-outbox-queue
 * }</pre>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConfigurationProperties(prefix = "aws.sqs.listener")
public class SqsListenerProperties {

    /** Discount Outbox 큐 URL */
    private String discountOutboxQueueUrl;

    /** Discount Outbox 리스너 활성화 여부 */
    private boolean discountOutboxListenerEnabled = true;

    public String getDiscountOutboxQueueUrl() {
        return discountOutboxQueueUrl;
    }

    public void setDiscountOutboxQueueUrl(String discountOutboxQueueUrl) {
        this.discountOutboxQueueUrl = discountOutboxQueueUrl;
    }

    public boolean isDiscountOutboxListenerEnabled() {
        return discountOutboxListenerEnabled;
    }

    public void setDiscountOutboxListenerEnabled(boolean discountOutboxListenerEnabled) {
        this.discountOutboxListenerEnabled = discountOutboxListenerEnabled;
    }
}
