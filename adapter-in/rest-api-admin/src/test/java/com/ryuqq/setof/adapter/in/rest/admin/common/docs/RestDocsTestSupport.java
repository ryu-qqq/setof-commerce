package com.ryuqq.setof.adapter.in.rest.admin.common.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring REST Docs 테스트 지원 추상 클래스
 *
 * <p>모든 REST Docs 테스트는 이 클래스를 상속받아 작성합니다.
 *
 * <p>제공 기능:
 *
 * <ul>
 *   <li>RestDocumentationExtension 자동 적용
 *   <li>MockMvc 자동 설정 (Pretty Print)
 *   <li>ObjectMapper 자동 주입
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    /**
     * MockMvc - HTTP 요청 시뮬레이션
     *
     * <p>하위 클래스에서 HTTP 요청 테스트 시 사용합니다.
     */
    protected MockMvc mockMvc;

    /**
     * ObjectMapper - JSON 변환
     *
     * <p>Request/Response JSON 생성 시 사용합니다.
     */
    @Autowired protected ObjectMapper objectMapper;

    /**
     * MockMvc 초기화 및 REST Docs 설정
     *
     * @param webApplicationContext 웹 애플리케이션 컨텍스트
     * @param restDocumentation REST Docs 컨텍스트 제공자
     */
    @BeforeEach
    void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(
                                documentationConfiguration(restDocumentation)
                                        .operationPreprocessors()
                                        .withRequestDefaults(prettyPrint())
                                        .withResponseDefaults(prettyPrint()))
                        .build();
    }
}
