package com.ryuqq.setof.commerce.adapter.out.fileflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FileFlow 클라이언트 커스텀 프로퍼티.
 *
 * <p>SDK AutoConfiguration이 처리하는 base-url, service-name, service-token, timeout 외에 어댑터에서 추가로 필요한
 * 설정을 바인딩합니다.
 *
 * @param cdnDomain CDN 도메인 (accessUrl 생성용)
 * @param bucket S3 버킷명
 * @author ryu-qqq
 * @since 1.1.0
 */
@ConfigurationProperties(prefix = "fileflow")
public record FileflowClientProperties(String cdnDomain, String bucket) {}
