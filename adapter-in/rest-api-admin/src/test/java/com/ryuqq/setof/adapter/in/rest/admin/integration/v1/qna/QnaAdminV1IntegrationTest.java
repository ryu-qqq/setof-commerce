package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.qna;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Admin QNA V1 API 통합 테스트 (Legacy)
 *
 * <p>QnaV1Controller의 모든 엔드포인트를 통합 테스트합니다.
 *
 * <p><strong>참고:</strong> 모든 엔드포인트가 UnsupportedOperationException을 발생시킵니다.
 *
 * <p><strong>테스트 대상 엔드포인트:</strong>
 *
 * <ul>
 *   <li>GET /api/v1/qna/{qnaId} - QNA 조회
 *   <li>GET /api/v1/qnas - QNA 목록 조회
 *   <li>POST /api/v1/qna/reply - QNA 답변 작성
 *   <li>PUT /api/v1/qna/reply - QNA 답변 수정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@DisplayName("Admin QNA V1 API 통합 테스트 (Legacy)")
class QnaAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_QNA_URL = "/api/v1/qna";
    private static final String V1_QNAS_URL = "/api/v1/qnas";

    // ============================================================
    // 미구현 엔드포인트 테스트 - UnsupportedOperationException
    // ============================================================

    @Nested
    @DisplayName("미구현 엔드포인트 테스트 - 모두 500 에러 반환")
    class UnsupportedEndpoints {

        @Test
        @DisplayName("[AQNA-V1-001] QNA 조회 - 미구현 (500 에러)")
        void fetchQna_unsupported() {
            // When
            ResponseEntity<Map<String, Object>> response = getExpectingError(V1_QNA_URL + "/1");

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AQNA-V1-002] QNA 목록 조회 - 미구현 (500 에러)")
        void fetchQnas_unsupported() {
            // When
            ResponseEntity<Map<String, Object>> response = getExpectingError(V1_QNAS_URL);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AQNA-V1-003] QNA 답변 작성 - 미구현 (500 에러)")
        void replyQna_unsupported() {
            // Given
            Map<String, Object> requestBody =
                    Map.of("qnaId", 1L, "answer", "테스트 답변입니다.");

            // When
            ResponseEntity<Map<String, Object>> response =
                    postExpectingError(V1_QNA_URL + "/reply", requestBody);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("[AQNA-V1-004] QNA 답변 수정 - 미구현 (500 에러)")
        void updateReplyQna_unsupported() {
            // Given
            Map<String, Object> requestBody =
                    Map.of("qnaId", 1L, "answerId", 1L, "answer", "수정된 답변입니다.");

            // When
            ResponseEntity<Map<String, Object>> response =
                    putExpectingError(V1_QNA_URL + "/reply", requestBody);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
