package com.ryuqq.setof.adapter.in.rest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * REST API 모듈 테스트용 Spring Boot 설정
 *
 * <p>rest-api 모듈에 @SpringBootApplication이 없으므로, @WebMvcTest 등 슬라이스 테스트를 위한 테스트용 설정 클래스입니다.
 *
 * <p>@WebMvcTest가 테스트할 컨트롤러를 직접 로드하므로 ComponentScan은 사용하지 않습니다.
 *
 * <p>Spring Security는 각 테스트에서 @TestConfiguration으로 설정하고,
 * SecurityMockMvcConfigurers.springSecurity()를 통해 MockMvc에 적용합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@SpringBootConfiguration
@EnableAutoConfiguration
public class TestWebMvcConfiguration {}
