package com.ryuqq.setof.integration.test.e2e.admin.sellerapplication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.ryuqq.setof.adapter.out.persistence.sellerapplication.SellerApplicationJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.repository.SellerApplicationJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * SellerApplication Admin E2E 통합 테스트.
 *
 * <p>셀러 입점 신청 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.SELLER)
@DisplayName("셀러 입점 신청 Admin API E2E 테스트")
class SellerApplicationAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/admin/seller-applications";

    @Autowired private SellerApplicationJpaRepository sellerApplicationJpaRepository;

    @BeforeEach
    void setUp() {
        sellerApplicationJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /v2/admin/seller-applications - 입점 신청")
    class ApplyTest {

        @Test
        @DisplayName("유효한 요청으로 입점 신청 성공")
        void shouldApplySuccessfully() {
            // given
            Map<String, Object> request = createApplyRequest();

            // when
            Response response = givenAdmin().body(request).when().post(BASE_PATH);

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.applicationId", greaterThan(0));

            Long applicationId = response.jsonPath().getLong("data.applicationId");
            assertThat(sellerApplicationJpaRepository.findById(applicationId)).isPresent();
        }

        @Test
        @DisplayName("필수 필드 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given
            Map<String, Object> request = createApplyRequest();
            Map<String, Object> sellerInfo = new HashMap<>();
            sellerInfo.put("sellerName", null);
            sellerInfo.put("displayName", "테스트 브랜드");
            request.put("sellerInfo", sellerInfo);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("이메일 형식이 잘못된 경우 400 에러 반환")
        void shouldReturn400WhenInvalidEmail() {
            // given
            Map<String, Object> request = createApplyRequest();
            Map<String, Object> csContact = new HashMap<>();
            csContact.put("phone", "02-1234-5678");
            csContact.put("email", "invalid-email");
            csContact.put("mobile", "010-1234-5678");
            request.put("csContact", csContact);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("POST /v2/admin/seller-applications/{applicationId}/approve - 입점 승인")
    class ApproveTest {

        @Test
        @DisplayName("대기 중인 신청 승인 성공")
        void shouldApproveSuccessfully() {
            // given - 유니크한 sellerName과 registrationNumber로 생성하여 중복 검증 통과
            String uniqueSellerName = "승인테스트셀러_" + System.currentTimeMillis();
            String uniqueRegistrationNumber = "999-99-" + System.currentTimeMillis() % 100000;
            SellerApplicationJpaEntity entity =
                    sellerApplicationJpaRepository.save(
                            SellerApplicationJpaEntityFixtures.pendingEntityWithCustomInfo(
                                    uniqueSellerName, uniqueRegistrationNumber));
            Long applicationId = entity.getId();

            // when
            Response response =
                    givenAdmin()
                            .when()
                            .post(BASE_PATH + "/" + applicationId + "/approve");

            // then
            response.then().statusCode(HttpStatus.OK.value()).body("data.sellerId", greaterThan(0));
        }

        @Test
        @DisplayName("존재하지 않는 신청 승인시 404 에러 반환")
        void shouldReturn404WhenApplicationNotFound() {
            // given
            // when & then
            givenAdmin()
                    .when()
                    .post(BASE_PATH + "/999999/approve")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("이미 처리된 신청 승인시 409 에러 반환")
        void shouldReturn409WhenAlreadyProcessed() {
            // given
            SellerApplicationJpaEntity entity =
                    sellerApplicationJpaRepository.save(
                            SellerApplicationJpaEntityFixtures.approvedEntity(100L));
            Long applicationId = entity.getId();

            // when & then
            givenAdmin()
                    .when()
                    .post(BASE_PATH + "/" + applicationId + "/approve")
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("POST /v2/admin/seller-applications/{applicationId}/reject - 입점 거절")
    class RejectTest {

        @Test
        @DisplayName("대기 중인 신청 거절 성공")
        void shouldRejectSuccessfully() {
            // given
            SellerApplicationJpaEntity entity =
                    sellerApplicationJpaRepository.save(
                            SellerApplicationJpaEntityFixtures.pendingEntity());
            Long applicationId = entity.getId();

            Map<String, Object> request = new HashMap<>();
            request.put("rejectionReason", "서류 미비로 인한 반려");

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH + "/" + applicationId + "/reject")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            SellerApplicationJpaEntity rejected =
                    sellerApplicationJpaRepository.findById(applicationId).orElseThrow();
            assertThat(rejected.getStatus().name()).isEqualTo("REJECTED");
            assertThat(rejected.getRejectionReason()).isEqualTo("서류 미비로 인한 반려");
        }

        @Test
        @DisplayName("존재하지 않는 신청 거절시 404 에러 반환")
        void shouldReturn404WhenApplicationNotFound() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("rejectionReason", "서류 미비");

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH + "/999999/reject")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }

        @Test
        @DisplayName("거절 사유 누락시 400 에러 반환")
        void shouldReturn400WhenRejectionReasonMissing() {
            // given
            SellerApplicationJpaEntity entity =
                    sellerApplicationJpaRepository.save(
                            SellerApplicationJpaEntityFixtures.pendingEntity());
            Long applicationId = entity.getId();

            Map<String, Object> request = new HashMap<>();

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH + "/" + applicationId + "/reject")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("GET /v2/admin/seller-applications - 입점 신청 목록 조회")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공")
        void shouldSearchAllApplications() {
            // given
            sellerApplicationJpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            sellerApplicationJpaRepository.save(
                    SellerApplicationJpaEntityFixtures.pendingEntityWithRegistrationNumber(
                            "987-65-43210"));

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(2))
                    .body("data.page", equalTo(0))
                    .body("data.size", equalTo(20));
        }

        @Test
        @DisplayName("상태별 필터링 조회 성공")
        void shouldFilterByStatus() {
            // given
            sellerApplicationJpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            sellerApplicationJpaRepository.save(
                    SellerApplicationJpaEntityFixtures.approvedEntity(100L));
            sellerApplicationJpaRepository.save(
                    SellerApplicationJpaEntityFixtures.rejectedEntity());

            // when & then
            givenAdmin()
                    .queryParam("status", "PENDING")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.content[0].status", equalTo("PENDING"));
        }

        @Test
        @DisplayName("페이징 처리 확인")
        void shouldPaginateCorrectly() {
            // given
            for (int i = 0; i < 5; i++) {
                sellerApplicationJpaRepository.save(
                        SellerApplicationJpaEntityFixtures.pendingEntityWithRegistrationNumber(
                                "111-11-1111" + i));
            }

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 2)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(5))
                    .body("data.page", equalTo(0))
                    .body("data.size", equalTo(2));
        }

        @Test
        @DisplayName("빈 결과 조회")
        void shouldReturnEmptyResult() {
            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(0))
                    .body("data.totalElements", equalTo(0));
        }

        @Test
        @DisplayName("응답 필드 검증")
        void shouldContainExpectedFields() {
            // given
            sellerApplicationJpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());

            // when & then
            givenAdmin()
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content[0].id", notNullValue())
                    .body("data.content[0].sellerInfo.sellerName", equalTo("테스트셀러"))
                    .body(
                            "data.content[0].businessInfo.registrationNumber",
                            equalTo("123-45-67890"))
                    .body("data.content[0].csContact.email", equalTo("cs@example.com"))
                    .body("data.content[0].status", equalTo("PENDING"))
                    .body("data.content[0].appliedAt", notNullValue())
                    .body("data.content[0].processedAt", nullValue())
                    .body("data.content[0].approvedSellerId", nullValue());
        }
    }

    @Nested
    @DisplayName("전체 플로우 시나리오 테스트")
    class FullFlowTest {

        @Test
        @DisplayName("입점 신청 -> 승인 전체 플로우")
        void shouldCompleteApplyAndApproveFlow() {
            // 1. 입점 신청
            Map<String, Object> applyRequest = createApplyRequest();
            Response applyResponse = givenAdmin().body(applyRequest).when().post(BASE_PATH);

            applyResponse.then().statusCode(HttpStatus.CREATED.value());

            Long applicationId = applyResponse.jsonPath().getLong("data.applicationId");

            // 2. 신청 조회 확인
            givenAdmin()
                    .queryParam("status", "PENDING")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.content[0].id", equalTo(applicationId.intValue()));

            // 3. 승인
            Response approveResponse =
                    givenAdmin()
                            .when()
                            .post(BASE_PATH + "/" + applicationId + "/approve");

            approveResponse
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.sellerId", greaterThan(0));

            // 4. 승인 후 상태 확인
            givenAdmin()
                    .queryParam("status", "APPROVED")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.content[0].status", equalTo("APPROVED"));
        }

        @Test
        @DisplayName("입점 신청 -> 거절 전체 플로우")
        void shouldCompleteApplyAndRejectFlow() {
            // 1. 입점 신청
            Map<String, Object> applyRequest = createApplyRequest();
            Response applyResponse = givenAdmin().body(applyRequest).when().post(BASE_PATH);

            applyResponse.then().statusCode(HttpStatus.CREATED.value());

            Long applicationId = applyResponse.jsonPath().getLong("data.applicationId");

            // 2. 거절
            Map<String, Object> rejectRequest = new HashMap<>();
            rejectRequest.put("rejectionReason", "사업자등록증 확인 불가");

            givenAdmin()
                    .body(rejectRequest)
                    .when()
                    .post(BASE_PATH + "/" + applicationId + "/reject")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // 3. 거절 후 상태 확인
            givenAdmin()
                    .queryParam("status", "REJECTED")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.content[0].status", equalTo("REJECTED"))
                    .body("data.content[0].rejectionReason", equalTo("사업자등록증 확인 불가"));
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createApplyRequest() {
        Map<String, Object> request = new HashMap<>();

        // sellerInfo
        Map<String, Object> sellerInfo = new HashMap<>();
        sellerInfo.put("sellerName", "테스트셀러");
        sellerInfo.put("displayName", "테스트 브랜드");
        sellerInfo.put("logoUrl", "https://example.com/logo.png");
        sellerInfo.put("description", "테스트 셀러 설명입니다.");
        request.put("sellerInfo", sellerInfo);

        // businessInfo
        Map<String, Object> businessInfo = new HashMap<>();
        businessInfo.put("registrationNumber", "123-45-67890");
        businessInfo.put("companyName", "테스트컴퍼니");
        businessInfo.put("representative", "홍길동");
        businessInfo.put("saleReportNumber", "제2025-서울강남-1234호");

        Map<String, Object> businessAddress = new HashMap<>();
        businessAddress.put("zipCode", "12345");
        businessAddress.put("line1", "서울시 강남구");
        businessAddress.put("line2", "테헤란로 123");
        businessInfo.put("businessAddress", businessAddress);

        request.put("businessInfo", businessInfo);

        // csContact
        Map<String, Object> csContact = new HashMap<>();
        csContact.put("phone", "02-1234-5678");
        csContact.put("email", "cs@example.com");
        csContact.put("mobile", "010-1234-5678");
        request.put("csContact", csContact);

        // returnAddress
        Map<String, Object> returnAddress = new HashMap<>();
        returnAddress.put("addressType", "RETURN");
        returnAddress.put("addressName", "반품지");

        Map<String, Object> address = new HashMap<>();
        address.put("zipCode", "12345");
        address.put("line1", "서울시 강남구");
        address.put("line2", "테헤란로 123");
        returnAddress.put("address", address);

        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("name", "김담당");
        contactInfo.put("phone", "010-1234-5678");
        returnAddress.put("contactInfo", contactInfo);

        request.put("returnAddress", returnAddress);

        // settlementInfo - 정산 정보
        Map<String, Object> settlementInfo = new HashMap<>();
        settlementInfo.put("bankCode", "088");
        settlementInfo.put("bankName", "신한은행");
        settlementInfo.put("accountNumber", "110123456789");
        settlementInfo.put("accountHolderName", "홍길동");
        settlementInfo.put("settlementCycle", "MONTHLY");
        settlementInfo.put("settlementDay", 1);
        request.put("settlementInfo", settlementInfo);

        return request;
    }
}
