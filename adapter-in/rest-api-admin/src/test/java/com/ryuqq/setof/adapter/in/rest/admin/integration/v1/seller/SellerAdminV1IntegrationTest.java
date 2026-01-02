package com.ryuqq.setof.adapter.in.rest.admin.integration.v1.seller;

import static com.ryuqq.setof.adapter.in.rest.admin.integration.fixture.SellerAdminTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.common.v1.dto.V1PageResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.CreateSellerSettlementAccountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerApprovalStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerInfoContextV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.command.SellerUpdateDetailV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerContextV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerDetailV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.refundaccount.component.RefundAccountValidator;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
import com.ryuqq.setof.domain.seller.vo.RegistrationNumber;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Seller Admin V1 API 통합 테스트 (Legacy)
 *
 * <p>레거시 V1 Seller Admin API의 통합 테스트를 수행합니다.
 *
 * <p>테스트 범위:
 *
 * <ul>
 *   <li>Query API: 인증된 셀러 조회, 셀러 상세 조회, 셀러 목록 조회, 사업자 등록번호 검증
 *   <li>Command API: 셀러 등록, 계좌 검증, 셀러 수정, 승인 상태 수정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API 통합 테스트로 마이그레이션 권장
 */
@DisplayName("Admin Seller V1 API 통합 테스트 (Legacy)")
class SellerAdminV1IntegrationTest extends ApiIntegrationTestSupport {

    private static final String V1_SELLER_BASE_URL = "/api/v1/seller";
    private static final String V1_SELLERS_BASE_URL = "/api/v1/sellers";

    // ============================================================
    // Query UseCases
    // ============================================================

    @Autowired private GetSellerUseCase getSellerUseCase;

    @Autowired private GetSellersUseCase getSellersUseCase;

    @Autowired private SellerQueryPort sellerQueryPort;

    // ============================================================
    // Command UseCases
    // ============================================================

    @Autowired private RegisterSellerUseCase registerSellerUseCase;

    @Autowired private UpdateSellerUseCase updateSellerUseCase;

    @Autowired private UpdateApprovalStatusUseCase updateApprovalStatusUseCase;

    @Autowired private RefundAccountValidator refundAccountValidator;

    // ============================================================
    // Query Tests
    // ============================================================

    @Nested
    @DisplayName("인증된 셀러 조회 (V1)")
    class FetchAuthenticatedSellerTest {

        @Test
        @DisplayName("ASLR-V1-001: 인증된 셀러 조회 시 현재 임시 응답이 반환된다")
        void fetchAuthenticatedSeller_success() {
            // given
            // 현재 구현은 TODO 상태로 임시 응답 반환

            // when
            ResponseEntity<V1ApiResponse<SellerContextV1ApiResponse>> response =
                    get(
                            V1_SELLER_BASE_URL,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<SellerContextV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().roleType()).isEqualTo("SELLER");
        }
    }

    @Nested
    @DisplayName("셀러 상세 조회 (V1)")
    class FetchSellerDetailTest {

        @Test
        @DisplayName("ASLR-V1-002: 유효한 셀러 ID로 조회하면 성공한다")
        void fetchSellerDetail_withValidId_success() {
            // given
            SellerResponse sellerResponse = createSellerResponse();
            given(getSellerUseCase.execute(DEFAULT_SELLER_ID)).willReturn(sellerResponse);

            String url = V1_SELLER_BASE_URL + "/" + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<V1ApiResponse<SellerDetailV1ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<SellerDetailV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isNotNull();
            assertThat(response.getBody().data().sellerId()).isEqualTo(DEFAULT_SELLER_ID);
        }

        @Test
        @DisplayName("ASLR-V1-003: 존재하지 않는 셀러 ID로 조회하면 404를 반환한다")
        void fetchSellerDetail_withNonExistentId_returns404() {
            // given
            given(getSellerUseCase.execute(NON_EXISTENT_SELLER_ID))
                    .willThrow(new SellerNotFoundException(NON_EXISTENT_SELLER_ID));

            String url = V1_SELLER_BASE_URL + "/" + NON_EXISTENT_SELLER_ID;

            // when
            ResponseEntity<V1ApiResponse<SellerDetailV1ApiResponse>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<SellerDetailV1ApiResponse>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("셀러 목록 조회 (V1)")
    class FetchSellersTest {

        @Test
        @DisplayName("ASLR-V1-004: 필터 없이 목록을 조회하면 성공한다")
        void fetchSellers_withoutFilters_success() {
            // given
            PageResponse<SellerSummaryResponse> pageResponse =
                    createSellerSummaryPageResponse(3, 3L);
            given(getSellersUseCase.execute(any(SellerSearchQuery.class))).willReturn(pageResponse);

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<SellerV1ApiResponse>>> response =
                    get(
                            V1_SELLERS_BASE_URL,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<V1PageResponse<SellerV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data().content()).hasSize(3);
        }

        @Test
        @DisplayName("ASLR-V1-005: 페이지네이션 파라미터가 적용된다")
        void fetchSellers_withPagination_success() {
            // given
            PageResponse<SellerSummaryResponse> pageResponse =
                    createSellerSummaryPageResponse(10, 100L);
            given(getSellersUseCase.execute(any(SellerSearchQuery.class))).willReturn(pageResponse);

            String url = V1_SELLERS_BASE_URL + "?page=0&size=10";

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<SellerV1ApiResponse>>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<V1PageResponse<SellerV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(10);
            assertThat(response.getBody().data().totalElements()).isEqualTo(100L);
        }

        @Test
        @DisplayName("ASLR-V1-006: 승인 상태로 필터링할 수 있다")
        void fetchSellers_withApprovalStatusFilter_success() {
            // given
            PageResponse<SellerSummaryResponse> pageResponse =
                    createSellerSummaryPageResponse(5, 5L);
            given(getSellersUseCase.execute(any(SellerSearchQuery.class))).willReturn(pageResponse);

            String url = V1_SELLERS_BASE_URL + "?approvalStatus=APPROVED";

            // when
            ResponseEntity<V1ApiResponse<V1PageResponse<SellerV1ApiResponse>>> response =
                    get(
                            url,
                            new ParameterizedTypeReference<
                                    V1ApiResponse<V1PageResponse<SellerV1ApiResponse>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().content()).hasSize(5);
        }
    }

    @Nested
    @DisplayName("사업자 등록번호 검증 (V1)")
    class BusinessValidationTest {

        private static final String VALIDATION_URL = "/api/v1/sellers/business-validation";

        @Test
        @DisplayName("ASLR-V1-007: 이미 등록된 사업자 등록번호면 true를 반환한다")
        void businessValidation_existingNumber_returnsTrue() {
            // given
            given(sellerQueryPort.existsByRegistrationNumber(any(RegistrationNumber.class)))
                    .willReturn(true);

            String url =
                    VALIDATION_URL + "?businessRegistrationNumber=" + DEFAULT_REGISTRATION_NUMBER;

            // when
            ResponseEntity<V1ApiResponse<Boolean>> response =
                    get(url, new ParameterizedTypeReference<V1ApiResponse<Boolean>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isTrue();
        }

        @Test
        @DisplayName("ASLR-V1-008: 미등록 사업자 등록번호면 false를 반환한다")
        void businessValidation_newNumber_returnsFalse() {
            // given
            given(sellerQueryPort.existsByRegistrationNumber(any(RegistrationNumber.class)))
                    .willReturn(false);

            String url = VALIDATION_URL + "?businessRegistrationNumber=999-99-99999";

            // when
            ResponseEntity<V1ApiResponse<Boolean>> response =
                    get(url, new ParameterizedTypeReference<V1ApiResponse<Boolean>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isFalse();
        }
    }

    // ============================================================
    // Command Tests - 등록
    // ============================================================

    @Nested
    @DisplayName("셀러 등록 (V1)")
    class RegisterSellerTest {

        @Test
        @DisplayName("ASLR-V1-009: 유효한 요청으로 셀러를 등록하면 성공한다")
        void registerSeller_withValidRequest_success() {
            // given
            SellerInfoContextV1ApiRequest request = createSellerInfoContextV1Request();
            given(registerSellerUseCase.execute(any(RegisterSellerCommand.class)))
                    .willReturn(DEFAULT_SELLER_ID);

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    post(
                            V1_SELLER_BASE_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_SELLER_ID);
        }
    }

    @Nested
    @DisplayName("셀러 정산 계좌 검증 (V1)")
    class CheckSettlementAccountTest {

        private static final String ACCOUNT_URL = "/api/v1/seller-account";

        @Test
        @DisplayName("ASLR-V1-010: 유효한 계좌 정보로 검증하면 성공한다")
        void checkSettlementAccount_withValidAccount_success() {
            // given
            CreateSellerSettlementAccountV1ApiRequest request =
                    new CreateSellerSettlementAccountV1ApiRequest(
                            DEFAULT_BANK_NAME, DEFAULT_ACCOUNT_NUMBER, DEFAULT_ACCOUNT_HOLDER_NAME);

            doNothing()
                    .when(refundAccountValidator)
                    .validateAccount(anyString(), anyString(), anyString());

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    post(
                            ACCOUNT_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            // Legacy 동작: 성공 시 null 반환
            assertThat(response.getBody().data()).isNull();
        }

        @Test
        @DisplayName("ASLR-V1-011: 필수 필드 누락 시 400을 반환한다")
        void checkSettlementAccount_withMissingFields_returns400() {
            // given - bankName이 null
            CreateSellerSettlementAccountV1ApiRequest request =
                    new CreateSellerSettlementAccountV1ApiRequest(
                            null, DEFAULT_ACCOUNT_NUMBER, DEFAULT_ACCOUNT_HOLDER_NAME);

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    post(
                            ACCOUNT_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("ASLR-V1-012: 잘못된 계좌 형식으로 요청하면 400을 반환한다")
        void checkSettlementAccount_withInvalidAccountNumber_returns400() {
            // given - accountNumber에 문자 포함 (숫자만 허용)
            CreateSellerSettlementAccountV1ApiRequest request =
                    new CreateSellerSettlementAccountV1ApiRequest(
                            DEFAULT_BANK_NAME, "abc123", DEFAULT_ACCOUNT_HOLDER_NAME);

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    post(
                            ACCOUNT_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    // ============================================================
    // Command Tests - 수정
    // ============================================================

    @Nested
    @DisplayName("셀러 수정 (V1)")
    class UpdateSellerTest {

        @Test
        @DisplayName("ASLR-V1-013: 유효한 요청으로 셀러를 수정하면 성공한다")
        void updateSeller_withValidRequest_success() {
            // given
            SellerUpdateDetailV1ApiRequest request = createSellerUpdateDetailV1Request();
            doNothing().when(updateSellerUseCase).execute(any(UpdateSellerCommand.class));

            String url = V1_SELLER_BASE_URL + "/" + DEFAULT_SELLER_ID;

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    put(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).isEqualTo(DEFAULT_SELLER_ID);
        }

        @Test
        @DisplayName("ASLR-V1-014: 존재하지 않는 셀러 수정 시 404를 반환한다")
        void updateSeller_withNonExistentId_returns404() {
            // given
            SellerUpdateDetailV1ApiRequest request = createSellerUpdateDetailV1Request();
            doThrow(new SellerNotFoundException(NON_EXISTENT_SELLER_ID))
                    .when(updateSellerUseCase)
                    .execute(any(UpdateSellerCommand.class));

            String url = V1_SELLER_BASE_URL + "/" + NON_EXISTENT_SELLER_ID;

            // when
            ResponseEntity<V1ApiResponse<Long>> response =
                    put(url, request, new ParameterizedTypeReference<V1ApiResponse<Long>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("셀러 승인 상태 수정 (V1)")
    class UpdateApprovalStatusTest {

        private static final String APPROVAL_STATUS_URL = "/api/v1/seller/approval-status";

        @Test
        @DisplayName("ASLR-V1-015: 유효한 요청으로 승인 상태를 수정하면 성공한다")
        void updateApprovalStatus_withValidRequest_success() {
            // given
            List<Long> sellerIds = List.of(1L, 2L, 3L);
            SellerApprovalStatusV1ApiRequest request =
                    createSellerApprovalStatusV1Request(sellerIds, APPROVED_STATUS);
            doNothing()
                    .when(updateApprovalStatusUseCase)
                    .execute(any(UpdateApprovalStatusCommand.class));

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    put(
                            APPROVAL_STATUS_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().response().status()).isEqualTo(200);
            assertThat(response.getBody().data()).containsExactly(1L, 2L, 3L);
        }

        @Test
        @DisplayName("ASLR-V1-016: 단일 셀러 승인 상태 수정도 성공한다")
        void updateApprovalStatus_singleSeller_success() {
            // given
            List<Long> sellerIds = List.of(DEFAULT_SELLER_ID);
            SellerApprovalStatusV1ApiRequest request =
                    createSellerApprovalStatusV1Request(sellerIds, REJECTED_STATUS);
            doNothing()
                    .when(updateApprovalStatusUseCase)
                    .execute(any(UpdateApprovalStatusCommand.class));

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    put(
                            APPROVAL_STATUS_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).containsExactly(DEFAULT_SELLER_ID);
        }

        @Test
        @DisplayName("ASLR-V1-017: 존재하지 않는 셀러 승인 상태 수정 시 404를 반환한다")
        void updateApprovalStatus_withNonExistentSeller_returns404() {
            // given
            List<Long> sellerIds = List.of(NON_EXISTENT_SELLER_ID);
            SellerApprovalStatusV1ApiRequest request =
                    createSellerApprovalStatusV1Request(sellerIds, APPROVED_STATUS);
            doThrow(new SellerNotFoundException(NON_EXISTENT_SELLER_ID))
                    .when(updateApprovalStatusUseCase)
                    .execute(any(UpdateApprovalStatusCommand.class));

            // when
            ResponseEntity<V1ApiResponse<List<Long>>> response =
                    put(
                            APPROVAL_STATUS_URL,
                            request,
                            new ParameterizedTypeReference<V1ApiResponse<List<Long>>>() {});

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
