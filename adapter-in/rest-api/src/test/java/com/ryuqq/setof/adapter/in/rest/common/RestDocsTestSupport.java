package com.ryuqq.setof.adapter.in.rest.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * REST Docs 테스트 지원 베이스 클래스.
 *
 * <p>MockMvc 기반 REST Docs 테스트의 공통 설정을 제공합니다.
 *
 * <p>테스트 규칙:
 *
 * <ul>
 *   <li>단위 테스트는 @WebMvcTest + Mock UseCase 사용
 *   <li>REST Docs 스니펫 자동 생성
 *   <li>BDDMockito 스타일 권장
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTestSupport {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper;
    protected RestDocumentationResultHandler document;

    @BeforeEach
    void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {

        this.objectMapper = createObjectMapper();

        this.document =
                org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document(
                        "{class-name}/{method-name}",
                        preprocessRequest(
                                modifyUris().scheme("https").host("api.setof.com").removePort(),
                                prettyPrint()),
                        preprocessResponse(prettyPrint()));

        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(documentationConfiguration(restDocumentation))
                        .alwaysDo(document)
                        .build();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    /**
     * 객체를 JSON 문자열로 변환.
     *
     * @param object 변환할 객체
     * @return JSON 문자열
     */
    protected String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    /** JSON Content-Type 상수. */
    protected static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
}
