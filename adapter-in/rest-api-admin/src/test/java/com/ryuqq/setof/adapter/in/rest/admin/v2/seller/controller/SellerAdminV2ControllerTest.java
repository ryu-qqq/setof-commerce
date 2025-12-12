package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ryuqq.setof.adapter.in.rest.admin.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerPageV2ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.response.SellerV2ApiResponse;
import com.ryuqq.setof.application.common.response.PageResponse;
import com.ryuqq.setof.application.seller.dto.command.DeleteSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateApprovalStatusCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.application.seller.dto.response.BusinessInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.dto.response.SellerSummaryResponse;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Seller Admin V2 Controller 통합 테스트
 *
 * <p>셀러 관리 API 통합 테스트
 *
 * @author development-team
 * @since 2.0.0
 */
@DisplayName("SellerAdminV2Controller 통합 테스트")
class SellerAdminV2ControllerTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/admin/sellers";

    @Autowired private RegisterSellerUseCase registerSellerUseCase;

    @Autowired private GetSellerUseCase getSellerUseCase;

    @Autowired private GetSellersUseCase getSellersUseCase;

    @Autowired private UpdateSellerUseCase updateSellerUseCase;

    @Autowired private UpdateApprovalStatusUseCase updateApprovalStatusUseCase;

    @Autowired private DeleteSellerUseCase deleteSellerUseCase;

    @Nested
    @DisplayName("POST /api/v2/admin/sellers - 셀러 등록")
    class RegisterSellerTest {

        @Test
        @DisplayName("유효한 요청으로 셀러를 등록하면 201 Created를 반환한다")
        void registerSeller_success() {
            // Given
            RegisterSellerV2ApiRequest request =
                    new RegisterSellerV2ApiRequest(
                            "테스트 셀러",
                            "https://example.com/logo.png",
                            "테스트 셀러 설명",
                            "1234567890",
                            "2024-서울강남-0001",
                            "홍길동",
                            "서울시 강남구",
                            "테헤란로 123",
                            "06234",
                            "cs@example.com",
                            "01012345678",
                            "0212345678");

            given(registerSellerUseCase.execute(any(RegisterSellerCommand.class))).willReturn(1L);

            // When
            ResponseEntity<ApiResponse<Long>> response =
                    post(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data()).isEqualTo(1L);

            verify(registerSellerUseCase).execute(any(RegisterSellerCommand.class));
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/sellers/{sellerId} - 셀러 상세 조회")
    class GetSellerTest {

        @Test
        @DisplayName("존재하는 셀러 ID로 조회하면 200 OK와 셀러 정보를 반환한다")
        void getSeller_success() {
            // Given
            Long sellerId = 1L;
            BusinessInfoResponse businessInfo =
                    new BusinessInfoResponse(
                            "1234567890", "2024-서울강남-0001", "홍길동", "서울시 강남구", "테헤란로 123", "06234");
            CsInfoResponse csInfo =
                    new CsInfoResponse("cs@example.com", "01012345678", "0212345678");
            SellerResponse sellerResponse =
                    new SellerResponse(
                            sellerId,
                            "테스트 셀러",
                            "https://example.com/logo.png",
                            "테스트 셀러 설명",
                            "APPROVED",
                            businessInfo,
                            csInfo);

            given(getSellerUseCase.execute(sellerId)).willReturn(sellerResponse);

            // When
            ResponseEntity<ApiResponse<SellerV2ApiResponse>> response =
                    get(BASE_URL + "/{sellerId}", new ParameterizedTypeReference<>() {}, sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().sellerId()).isEqualTo(sellerId);
            assertThat(response.getBody().data().sellerName()).isEqualTo("테스트 셀러");

            verify(getSellerUseCase).execute(sellerId);
        }
    }

    @Nested
    @DisplayName("GET /api/v2/admin/sellers - 셀러 목록 조회")
    class GetSellersTest {

        @Test
        @DisplayName("검색 조건으로 셀러 목록을 조회하면 200 OK와 페이징된 목록을 반환한다")
        void searchSellers_success() {
            // Given
            SellerSummaryResponse summaryResponse =
                    new SellerSummaryResponse(
                            1L, "테스트 셀러", "https://example.com/logo.png", "APPROVED");

            PageResponse<SellerSummaryResponse> pageResponse =
                    PageResponse.of(List.of(summaryResponse), 0, 10, 1L, 1, true, true);

            given(getSellersUseCase.execute(any(SellerSearchQuery.class))).willReturn(pageResponse);

            // When
            ResponseEntity<ApiResponse<SellerPageV2ApiResponse>> response =
                    get(BASE_URL + "?page=0&size=10", new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().data().sellers()).hasSize(1);
            assertThat(response.getBody().data().totalCount()).isEqualTo(1L);

            verify(getSellersUseCase).execute(any(SellerSearchQuery.class));
        }
    }

    @Nested
    @DisplayName("PUT /api/v2/admin/sellers/{sellerId} - 셀러 수정")
    class UpdateSellerTest {

        @Test
        @DisplayName("유효한 요청으로 셀러를 수정하면 200 OK를 반환한다")
        void updateSeller_success() {
            // Given
            Long sellerId = 1L;
            UpdateSellerV2ApiRequest request =
                    new UpdateSellerV2ApiRequest(
                            "수정된 셀러",
                            "https://example.com/new-logo.png",
                            "수정된 설명",
                            "1234567890",
                            "2024-서울강남-0002",
                            "김철수",
                            "서울시 서초구",
                            "서초동 456-78",
                            "06789",
                            "updated@example.com",
                            "01098765432",
                            "0298765432");

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    put(
                            BASE_URL + "/{sellerId}",
                            request,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateSellerUseCase).execute(any(UpdateSellerCommand.class));
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/approve - 셀러 승인")
    class ApproveSellerTest {

        @Test
        @DisplayName("셀러를 승인하면 200 OK를 반환한다")
        void approveSeller_success() {
            // Given
            Long sellerId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{sellerId}/approve",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateApprovalStatusUseCase).execute(any(UpdateApprovalStatusCommand.class));
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/reject - 셀러 거절")
    class RejectSellerTest {

        @Test
        @DisplayName("셀러를 거절하면 200 OK를 반환한다")
        void rejectSeller_success() {
            // Given
            Long sellerId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{sellerId}/reject",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateApprovalStatusUseCase).execute(any(UpdateApprovalStatusCommand.class));
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/suspend - 셀러 정지")
    class SuspendSellerTest {

        @Test
        @DisplayName("셀러를 정지하면 200 OK를 반환한다")
        void suspendSeller_success() {
            // Given
            Long sellerId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{sellerId}/suspend",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(updateApprovalStatusUseCase).execute(any(UpdateApprovalStatusCommand.class));
        }
    }

    @Nested
    @DisplayName("PATCH /api/v2/admin/sellers/{sellerId}/delete - 셀러 삭제")
    class DeleteSellerTest {

        @Test
        @DisplayName("셀러를 삭제하면 200 OK를 반환한다")
        void deleteSeller_success() {
            // Given
            Long sellerId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patch(
                            BASE_URL + "/{sellerId}/delete",
                            null,
                            new ParameterizedTypeReference<>() {},
                            sellerId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            verify(deleteSellerUseCase).execute(any(DeleteSellerCommand.class));
        }
    }
}
