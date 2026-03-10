package com.ryuqq.setof.adapter.out.sqs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AWS SQS 클라이언트 Properties.
 *
 * <p>설정 예시 (sqs.yml):
 *
 * <pre>{@code
 * sqs:
 *   region: ap-northeast-2
 *   queues:
 *     discount-outbox: https://sqs.ap-northeast-2.amazonaws.com/.../discount-outbox-queue
 * }</pre>
 */
@ConfigurationProperties(prefix = "sqs")
public class SqsClientProperties {

    private String region = "ap-northeast-2";
    private String endpoint;
    private Queues queues = new Queues();

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Queues getQueues() {
        return queues;
    }

    public void setQueues(Queues queues) {
        this.queues = queues;
    }

    public static class Queues {

        private String discountOutbox;
        private String notificationOutbox;

        public String getDiscountOutbox() {
            return discountOutbox;
        }

        public void setDiscountOutbox(String discountOutbox) {
            this.discountOutbox = discountOutbox;
        }

        public String getNotificationOutbox() {
            return notificationOutbox;
        }

        public void setNotificationOutbox(String notificationOutbox) {
            this.notificationOutbox = notificationOutbox;
        }
    }
}
