package com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.common.ApiIntegrationTestSupport;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.RegisterShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.command.UpdateShippingAddressV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.shippingaddress.dto.response.ShippingAddressV2ApiResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

/**
 * ShippingAddress API 통합 테스트
 *
 * <p>배송지 CRUD 및 기본 배송지 설정 기능을 검증합니다.
 *
 * <p>엔드포인트:
 *
 * <ul>
 *   <li>GET /v2/members/me/shipping-addresses - 목록 조회
 *   <li>GET /v2/members/me/shipping-addresses/{id} - 단건 조회
 *   <li>POST /v2/members/me/shipping-addresses - 등록
 *   <li>PUT /v2/members/me/shipping-addresses/{id} - 수정
 *   <li>PATCH /v2/members/me/shipping-addresses/{id}/delete - 삭제 (Soft Delete)
 *   <li>PATCH /v2/members/me/shipping-addresses/{id}/default - 기본 배송지 설정
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 */
@DisplayName("ShippingAddress API 통합 테스트")
class ShippingAddressApiIntegrationTest extends ApiIntegrationTestSupport {

    private static final String BASE_URL = "/api/v2/members/me/shipping-addresses";
    private static final String DETAIL_URL = BASE_URL + "/{id}";
    private static final String DELETE_URL = DETAIL_URL + "/delete";
    private static final String DEFAULT_URL = DETAIL_URL + "/default";

    @Nested
    @DisplayName("배송지 등록 API")
    class RegisterShippingAddress {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("POST /v2/members/me/shipping-addresses - 배송지 등록 성공")
        void registerShippingAddress_success() {
            // Given
            RegisterShippingAddressV2ApiRequest request =
                    new RegisterShippingAddressV2ApiRequest(
                            "집",
                            "홍길동",
                            "01012345678",
                            "서울특별시 강남구 테헤란로 123",
                            "서울특별시 강남구 역삼동 123-45",
                            "101동 1001호",
                            "06234",
                            "부재 시 경비실에 맡겨주세요",
                            true);

            // When
            ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> response =
                    postAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            ShippingAddressV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.id()).isNotNull();
            assertThat(data.addressName()).isEqualTo("집");
            assertThat(data.receiverName()).isEqualTo("홍길동");
            assertThat(data.receiverPhone()).isEqualTo("01012345678");
            assertThat(data.roadAddress()).isEqualTo("서울특별시 강남구 테헤란로 123");
            assertThat(data.zipCode()).isEqualTo("06234");
            assertThat(data.isDefault()).isTrue();
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("POST /v2/members/me/shipping-addresses - 필수 필드 누락 시 400 에러")
        void registerShippingAddress_missingRequiredFields_returns400() {
            // Given - addressName 누락
            RegisterShippingAddressV2ApiRequest request =
                    new RegisterShippingAddressV2ApiRequest(
                            "",
                            "홍길동",
                            "01012345678",
                            "서울특별시 강남구 테헤란로 123",
                            null,
                            "101동 1001호",
                            "06234",
                            null,
                            false);

            // When
            ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> response =
                    postAuthenticated(BASE_URL, request, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("배송지 목록 조회 API")
    class GetShippingAddresses {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/shipping-addresses-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("GET /v2/members/me/shipping-addresses - 배송지 목록 조회 성공")
        void getShippingAddresses_success() {
            // When
            ResponseEntity<ApiResponse<List<ShippingAddressV2ApiResponse>>> response =
                    getAuthenticated(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            List<ShippingAddressV2ApiResponse> data = response.getBody().data();
            assertThat(data).isNotNull().isNotEmpty();
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("GET /v2/members/me/shipping-addresses - 배송지 없으면 빈 목록 반환")
        void getShippingAddresses_empty_returnsEmptyList() {
            // When
            ResponseEntity<ApiResponse<List<ShippingAddressV2ApiResponse>>> response =
                    getAuthenticated(BASE_URL, new ParameterizedTypeReference<>() {});

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
            assertThat(response.getBody().data()).isEmpty();
        }
    }

    @Nested
    @DisplayName("배송지 단건 조회 API")
    class GetShippingAddress {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/shipping-addresses-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("GET /v2/members/me/shipping-addresses/{id} - 배송지 단건 조회 성공")
        void getShippingAddress_success() {
            // Given
            Long shippingAddressId = 1L;

            // When
            ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> response =
                    getAuthenticated(
                            DETAIL_URL, new ParameterizedTypeReference<>() {}, shippingAddressId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            ShippingAddressV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.id()).isEqualTo(shippingAddressId);
        }

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName(
                "GET /v2/members/me/shipping-addresses/{id} - 존재하지 않는 배송지 조회 시 400 에러 (비즈니스 예외)")
        void getShippingAddress_notFound_returns400() {
            // Given
            Long nonExistentId = 9999L;

            // When
            ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> response =
                    getAuthenticated(
                            DETAIL_URL, new ParameterizedTypeReference<>() {}, nonExistentId);

            // Then
            // ShippingAddressNotFoundException은 DomainException으로 400 BAD_REQUEST를 반환
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("배송지 수정 API")
    class UpdateShippingAddress {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/shipping-addresses-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("PUT /v2/members/me/shipping-addresses/{id} - 배송지 수정 성공")
        void updateShippingAddress_success() {
            // Given
            Long shippingAddressId = 1L;
            UpdateShippingAddressV2ApiRequest request =
                    new UpdateShippingAddressV2ApiRequest(
                            "회사",
                            "김철수",
                            "01098765432",
                            "서울특별시 서초구 반포대로 123",
                            "서울특별시 서초구 반포동 123-45",
                            "A동 1502호",
                            "06501",
                            "문 앞에 놓아주세요");

            // When
            ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> response =
                    putAuthenticated(
                            DETAIL_URL,
                            request,
                            new ParameterizedTypeReference<>() {},
                            shippingAddressId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            ShippingAddressV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.addressName()).isEqualTo("회사");
            assertThat(data.receiverName()).isEqualTo("김철수");
            assertThat(data.receiverPhone()).isEqualTo("01098765432");
        }
    }

    @Nested
    @DisplayName("배송지 삭제 API")
    class DeleteShippingAddress {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/shipping-addresses-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName(
                "PATCH /v2/members/me/shipping-addresses/{id}/delete - 배송지 삭제 성공 (Soft Delete)")
        void deleteShippingAddress_success() {
            // Given
            Long shippingAddressId = 1L;

            // When
            ResponseEntity<ApiResponse<Void>> response =
                    patchAuthenticated(
                            DELETE_URL,
                            null,
                            new ParameterizedTypeReference<>() {},
                            shippingAddressId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();
        }
    }

    @Nested
    @DisplayName("기본 배송지 설정 API")
    class SetDefaultShippingAddress {

        @Test
        @Sql(
                scripts = "/sql/schema/schema.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/data/shipping-addresses-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(
                scripts = "/sql/cleanup/cleanup.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @DisplayName("PATCH /v2/members/me/shipping-addresses/{id}/default - 기본 배송지 설정 성공")
        void setDefaultShippingAddress_success() {
            // Given
            Long shippingAddressId = 2L; // 기존 기본이 아닌 배송지

            // When
            ResponseEntity<ApiResponse<ShippingAddressV2ApiResponse>> response =
                    patchAuthenticated(
                            DEFAULT_URL,
                            null,
                            new ParameterizedTypeReference<>() {},
                            shippingAddressId);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().success()).isTrue();

            ShippingAddressV2ApiResponse data = response.getBody().data();
            assertThat(data).isNotNull();
            assertThat(data.id()).isEqualTo(shippingAddressId);
            assertThat(data.isDefault()).isTrue();
        }
    }
}
