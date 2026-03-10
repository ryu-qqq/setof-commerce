package com.ryuqq.setof.commerce.adapter.out.fileflow.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * FileFlow Client 설정.
 *
 * <p>FileFlow SDK v1.2.0 Spring Boot Starter가 SingleUploadSessionApi, DownloadTaskApi, AssetApi 빈을
 * 자동 구성합니다.
 *
 * <p>이 Configuration은 추가 Properties 바인딩만 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Configuration
@EnableConfigurationProperties(FileflowClientProperties.class)
@ConditionalOnProperty(prefix = "fileflow", name = "base-url")
public class FileflowClientConfig {}
