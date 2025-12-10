package com.ryuqq.setof.adapter.in.rest.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Documentation Controller
 *
 * <p>Spring REST Docs로 생성된 API 문서를 제공합니다.
 *
 * <p>빌드 시 asciidoctor 태스크가 생성한 HTML 문서를 static resource로 제공합니다.
 *
 * <p>접근 경로:
 *
 * <ul>
 *   <li>/docs - 메인 API 문서
 *   <li>/docs/v2/auth/auth.html - Auth API 문서
 *   <li>/docs/v2/member/member.html - Member API 문서
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Tag(name = "API Documentation", description = "API 문서 제공")
@RestController
public class ApiDocsController {

    private static final String DOCS_BASE_PATH = "static/docs/";

    /**
     * API 문서 메인 페이지 제공
     *
     * <p>Spring REST Docs로 생성된 index.html을 반환합니다.
     *
     * @return API 문서 HTML
     */
    @Operation(
            summary = "API 문서 메인 페이지",
            description = "Spring REST Docs로 생성된 API 문서 메인 페이지를 제공합니다.")
    @GetMapping(value = "/docs", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> getApiDocs() {
        Resource resource = new ClassPathResource(DOCS_BASE_PATH + "index.html");
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(resource);
    }

    /**
     * API 버전별/도메인별 문서 페이지 제공
     *
     * <p>예시: /docs/v2/auth/auth.html
     *
     * @param version API 버전 (v2)
     * @param domain 도메인 (auth, member 등)
     * @param file 파일명 (auth.html, member.html)
     * @return API 문서 HTML
     */
    @Operation(summary = "API 도메인별 문서 페이지", description = "버전별/도메인별 API 문서를 제공합니다.")
    @GetMapping(value = "/docs/{version}/{domain}/{file}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<Resource> getApiDocsByDomain(
            @Parameter(description = "API 버전 (예: v2)") @PathVariable String version,
            @Parameter(description = "도메인 (예: auth, member)") @PathVariable String domain,
            @Parameter(description = "파일명 (예: auth.html)") @PathVariable String file) {
        String path = DOCS_BASE_PATH + version + "/" + domain + "/" + file;
        Resource resource = new ClassPathResource(path);
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(resource);
    }
}
