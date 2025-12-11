package com.ryuqq.setof.adapter.in.rest.v2.bank.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.bank.dto.response.BankV2ApiResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * Bank API 통합 테스트
 *
 * <p>Bank V2 REST API 엔드포인트의 통합 동작을 검증합니다.
 *
 * <p><strong>테스트 범위:</strong>
 *
 * <ul>
 *   <li>GET /api/v2/banks - 활성 은행 목록 조회
 * </ul>
 *
 * <p><strong>사용 도구:</strong>
 *
 * <ul>
 *   <li>TestRestTemplate - 실제 HTTP 요청/응답 테스트
 *   <li>TestContainers MySQL - 실제 DB 테스트
 *   <li>@Sql - 테스트 데이터 준비
 * </ul>
 *
 * @author Development Team
 * @since 2.0.0
 * @see BankV2Controller
 */
@DisplayName("Bank API 통합 테스트")
@Sql(scripts = "/sql/schema/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BankApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = ApiV2Paths.Banks.BASE;

    @Nested
    @DisplayName("GET /api/v2/banks - 은행 목록 조회")
    class GetBanks {

        @Test
        @Sql("/sql/bank/banks-test-data.sql")
        @DisplayName("성공 - 활성 은행 목록 조회")
        void getBanks_success() {
            // When
            ResponseEntity<ApiResponse<List<BankV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<BankV2ApiResponse> banks = response.getBody().data();
            assertThat(banks).isNotNull();
            assertThat(banks).isNotEmpty();
            // 비활성 은행(id=99)은 제외되어야 함
            assertThat(banks).noneMatch(bank -> bank.id() == 99L);
        }

        @Test
        @Sql("/sql/bank/banks-test-data.sql")
        @DisplayName("성공 - 은행 목록이 displayOrder로 정렬됨")
        void getBanks_orderedByDisplayOrder() {
            // When
            ResponseEntity<ApiResponse<List<BankV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<BankV2ApiResponse> banks = response.getBody().data();
            assertThat(banks).isNotEmpty();

            // 첫 번째는 KB국민은행 (displayOrder=1)
            assertThat(banks.get(0).bankCode()).isEqualTo("004");
            assertThat(banks.get(0).bankName()).isEqualTo("KB국민은행");

            // 두 번째는 신한은행 (displayOrder=2)
            assertThat(banks.get(1).bankCode()).isEqualTo("088");
            assertThat(banks.get(1).bankName()).isEqualTo("신한은행");

            // 세 번째는 우리은행 (displayOrder=3)
            assertThat(banks.get(2).bankCode()).isEqualTo("020");
            assertThat(banks.get(2).bankName()).isEqualTo("우리은행");
        }

        @Test
        @Sql("/sql/bank/banks-test-data.sql")
        @DisplayName("성공 - 응답 필드 검증")
        void getBanks_responseFieldsVerification() {
            // When
            ResponseEntity<ApiResponse<List<BankV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<BankV2ApiResponse> banks = response.getBody().data();
            BankV2ApiResponse kbBank =
                    banks.stream()
                            .filter(b -> b.bankCode().equals("004"))
                            .findFirst()
                            .orElseThrow();

            assertThat(kbBank.id()).isEqualTo(1L);
            assertThat(kbBank.bankCode()).isEqualTo("004");
            assertThat(kbBank.bankName()).isEqualTo("KB국민은행");
        }

        @Test
        @Sql("/sql/bank/banks-test-data.sql")
        @DisplayName("성공 - 활성 은행만 반환 (비활성 제외)")
        void getBanks_excludeInactiveBanks() {
            // When
            ResponseEntity<ApiResponse<List<BankV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            List<BankV2ApiResponse> banks = response.getBody().data();
            // 활성 은행 10개만 반환 (비활성 1개 제외)
            assertThat(banks).hasSize(10);

            // 비활성 은행(code=999)이 포함되지 않음
            assertThat(banks).extracting(BankV2ApiResponse::bankCode).doesNotContain("999");
        }

        @Test
        @Sql(
                scripts = "/sql/bank/banks-empty-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @DisplayName("성공 - 은행 데이터가 없을 때 빈 리스트 반환")
        void getBanks_emptyList() {
            // When - 테스트 데이터 없이 호출
            ResponseEntity<ApiResponse<List<BankV2ApiResponse>>> response =
                    get(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            // 데이터가 없으면 빈 리스트
            assertThat(response.getBody().data()).isEmpty();
        }
    }
}
