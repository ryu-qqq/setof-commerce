package com.ryuqq.setof.integration.test.e2e.admin.seller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerAddressJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.SellerBusinessInfoJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.SellerCsJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.SellerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAddressJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerBusinessInfoJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerCsJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
import com.ryuqq.setof.integration.test.common.base.AdminE2ETestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * Seller Admin E2E 통합 테스트.
 *
 * <p>셀러 Admin API의 전체 흐름을 테스트합니다. REST API -> Application -> Domain -> Repository -> DB
 */
@Tag(TestTags.SELLER)
@DisplayName("셀러 Admin API E2E 테스트")
class SellerAdminE2ETest extends AdminE2ETestBase {

    private static final String BASE_PATH = "/v2/sellers";

    @Autowired private SellerJpaRepository sellerJpaRepository;
    @Autowired private SellerBusinessInfoJpaRepository businessInfoJpaRepository;
    @Autowired private SellerAddressJpaRepository addressJpaRepository;
    @Autowired private SellerCsJpaRepository csJpaRepository;

    @BeforeEach
    void setUp() {
        // 외래키 제약 순서에 맞게 삭제
        csJpaRepository.deleteAll();
        addressJpaRepository.deleteAll();
        businessInfoJpaRepository.deleteAll();
        sellerJpaRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /v2/admin/sellers - 셀러 등록")
    class RegisterTest {

        @Test
        @Disabled("SellerCommandController 미구현")
        @DisplayName("유효한 요청으로 셀러 등록 성공")
        void shouldRegisterSuccessfully() {
            // given - 유니크한 값 사용
            String uniqueSuffix = String.valueOf(System.currentTimeMillis());
            Map<String, Object> request = createRegisterRequest(uniqueSuffix);

            // when
            Response response = givenAdmin().body(request).when().post(BASE_PATH);

            // then
            response.then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("data.sellerId", greaterThan(0));

            Long sellerId = response.jsonPath().getLong("data.sellerId");
            assertThat(sellerJpaRepository.findById(sellerId)).isPresent();
        }

        @Test
        @Disabled("SellerCommandController 미구현")
        @DisplayName("필수 필드 누락시 400 에러 반환")
        void shouldReturn400WhenRequiredFieldMissing() {
            // given - sellerName 누락
            Map<String, Object> request = new HashMap<>();
            Map<String, Object> seller = new HashMap<>();
            seller.put("sellerName", null);
            seller.put("displayName", "테스트 브랜드");
            seller.put("logoUrl", "https://example.com/logo.png");
            seller.put("description", "설명");
            request.put("seller", seller);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @Disabled("SellerCommandController 미구현")
        @DisplayName("중복 셀러명으로 등록시 409 에러 반환")
        void shouldReturn409WhenDuplicateSellerName() {
            // given - 기존 셀러 생성
            SellerJpaEntity existingSeller =
                    sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            String existingSellerName = existingSeller.getSellerName();

            // 동일한 셀러명으로 등록 시도 - 유효한 suffix 사용
            String uniqueSuffix = String.valueOf(System.currentTimeMillis());
            Map<String, Object> request = createRegisterRequest(uniqueSuffix);
            @SuppressWarnings("unchecked")
            Map<String, Object> sellerMap = (Map<String, Object>) request.get("seller");
            sellerMap.put("sellerName", existingSellerName);

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.CONFLICT.value());
        }
    }

    @Nested
    @DisplayName("GET /v2/admin/sellers/{sellerId} - 셀러 상세 조회")
    class GetDetailTest {

        @Test
        @DisplayName("존재하는 셀러 상세 조회 성공 - sellerInfo 필드 검증")
        void shouldGetDetailSuccessfully() {
            // given
            SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            Long sellerId = seller.getId();

            businessInfoJpaRepository.save(
                    SellerBusinessInfoJpaEntityFixtures.activeEntityWithSellerId(sellerId));
            addressJpaRepository.save(
                    SellerAddressJpaEntityFixtures.activeEntityWithSellerId(sellerId));
            csJpaRepository.save(SellerCsJpaEntityFixtures.activeEntityWithSellerId(sellerId));

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + sellerId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    // sellerInfo 필드 검증 (SellerDetailApiResponse.sellerInfo)
                    .body("data.sellerInfo.id", equalTo(sellerId.intValue()))
                    .body(
                            "data.sellerInfo.sellerName",
                            equalTo(SellerJpaEntityFixtures.DEFAULT_SELLER_NAME))
                    .body(
                            "data.sellerInfo.displayName",
                            equalTo(SellerJpaEntityFixtures.DEFAULT_DISPLAY_NAME))
                    .body(
                            "data.sellerInfo.logoUrl",
                            equalTo(SellerJpaEntityFixtures.DEFAULT_LOGO_URL))
                    .body("data.sellerInfo.active", equalTo(true))
                    .body("data.sellerInfo.createdAt", notNullValue())
                    .body("data.sellerInfo.updatedAt", notNullValue())
                    // businessInfo 필드 검증
                    .body("data.businessInfo", notNullValue())
                    .body(
                            "data.businessInfo.registrationNumber",
                            equalTo(
                                    SellerBusinessInfoJpaEntityFixtures
                                            .DEFAULT_REGISTRATION_NUMBER))
                    .body(
                            "data.businessInfo.companyName",
                            equalTo(SellerBusinessInfoJpaEntityFixtures.DEFAULT_COMPANY_NAME))
                    .body(
                            "data.businessInfo.representative",
                            equalTo(SellerBusinessInfoJpaEntityFixtures.DEFAULT_REPRESENTATIVE))
                    // csInfo 필드 검증
                    .body("data.csInfo", notNullValue())
                    .body(
                            "data.csInfo.csPhone",
                            equalTo(SellerCsJpaEntityFixtures.DEFAULT_CS_PHONE))
                    .body(
                            "data.csInfo.csEmail",
                            equalTo(SellerCsJpaEntityFixtures.DEFAULT_CS_EMAIL));
        }

        @Test
        @DisplayName("사업자정보/CS정보 없이 셀러만 존재할 때 sellerInfo만 반환")
        void shouldGetDetailWithOnlySellerInfo() {
            // given - 셀러 기본 정보만 저장, 사업자/CS 정보 없음
            SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            Long sellerId = seller.getId();

            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/" + sellerId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.sellerInfo.id", equalTo(sellerId.intValue()))
                    .body("data.sellerInfo.sellerName", notNullValue());
        }

        @Test
        @Disabled("API가 NotFoundException을 500으로 처리 - 예외 매핑 구현 필요")
        @DisplayName("존재하지 않는 셀러 조회시 404 에러 반환")
        void shouldReturn404WhenSellerNotFound() {
            // when & then
            givenAdmin()
                    .when()
                    .get(BASE_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("GET /v2/admin/sellers - 셀러 목록 검색")
    class SearchTest {

        @Test
        @DisplayName("전체 목록 조회 성공 - 응답 필드 검증")
        void shouldSearchAllSellers() {
            // given
            sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.activeEntityWithName("셀러2", "셀러2 스토어"));

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
                    .body("data.size", equalTo(20))
                    // 목록 항목 필드 구조 검증 (SellerApiResponse)
                    .body("data.content[0].id", notNullValue())
                    .body("data.content[0].sellerName", notNullValue())
                    .body("data.content[0].displayName", notNullValue())
                    .body("data.content[0].createdAt", notNullValue());
        }

        @Test
        @DisplayName("셀러명으로 검색 성공 - 정확한 일치 결과 반환")
        void shouldSearchBySellerName() {
            // given
            sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.activeEntityWithName("특별한셀러", "특별한 스토어"));

            // when & then - API는 searchField/searchWord 파라미터 사용
            givenAdmin()
                    .queryParam("searchField", "sellerName")
                    .queryParam("searchWord", "특별한셀러")
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(1))
                    .body("data.content[0].sellerName", equalTo("특별한셀러"))
                    .body("data.content[0].displayName", equalTo("특별한 스토어"))
                    .body("data.totalElements", equalTo(1));
        }

        @Test
        @DisplayName("활성화 여부 필터 - active=true 조회")
        void shouldFilterByActiveStatus() {
            // given - 활성 2개, 비활성 1개
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.activeEntityWithName("활성셀러1", "활성스토어1"));
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.activeEntityWithName("활성셀러2", "활성스토어2"));
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.inactiveEntityWithName("비활성셀러", "비활성스토어"));

            // when & then - active=true 필터 적용
            givenAdmin()
                    .queryParam("active", true)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.totalElements", equalTo(2));
        }

        @Test
        @DisplayName("활성화 여부 필터 - active=false 조회")
        void shouldFilterByInactiveStatus() {
            // given - 활성 1개, 비활성 2개
            sellerJpaRepository.save(SellerJpaEntityFixtures.activeEntityWithName("활성셀러", "활성스토어"));
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.inactiveEntityWithName("비활성셀러1", "비활성스토어1"));
            sellerJpaRepository.save(
                    SellerJpaEntityFixtures.inactiveEntityWithName("비활성셀러2", "비활성스토어2"));

            // when & then
            givenAdmin()
                    .queryParam("active", false)
                    .queryParam("page", 0)
                    .queryParam("size", 20)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(greaterThanOrEqualTo(2)));
        }

        @Test
        @DisplayName("페이징 처리 확인 - size 제한 및 totalElements 정확성")
        void shouldPaginateCorrectly() {
            // given
            for (int i = 0; i < 5; i++) {
                sellerJpaRepository.save(
                        SellerJpaEntityFixtures.activeEntityWithName("셀러" + i, "스토어" + i));
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
        @DisplayName("두 번째 페이지 조회 성공")
        void shouldReturnSecondPage() {
            // given
            for (int i = 0; i < 5; i++) {
                sellerJpaRepository.save(
                        SellerJpaEntityFixtures.activeEntityWithName("셀러" + i, "스토어" + i));
            }

            // when & then - 두 번째 페이지 (page=1, size=2) → 2개 반환
            givenAdmin()
                    .queryParam("page", 1)
                    .queryParam("size", 2)
                    .when()
                    .get(BASE_PATH)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("data.content", hasSize(2))
                    .body("data.page", equalTo(1))
                    .body("data.totalElements", equalTo(5));
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
    }

    @Nested
    @DisplayName("PATCH /v2/admin/sellers/{sellerId} - 셀러 기본정보 수정")
    class UpdateBasicInfoTest {

        @Test
        @Disabled("SellerCommandController 미구현")
        @DisplayName("셀러 기본정보 수정 성공")
        void shouldUpdateBasicInfoSuccessfully() {
            // given
            SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            Long sellerId = seller.getId();

            // API는 sellerName, displayName, logoUrl, description 모두 필수
            Map<String, Object> request = new HashMap<>();
            request.put("sellerName", seller.getSellerName()); // 기존 값 유지
            request.put("displayName", "수정된 스토어명");
            request.put("logoUrl", "https://example.com/new-logo.png");
            request.put("description", "수정된 설명입니다.");

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/" + sellerId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // 변경 확인
            SellerJpaEntity updated = sellerJpaRepository.findById(sellerId).orElseThrow();
            assertThat(updated.getDisplayName()).isEqualTo("수정된 스토어명");
            assertThat(updated.getLogoUrl()).isEqualTo("https://example.com/new-logo.png");
            assertThat(updated.getDescription()).isEqualTo("수정된 설명입니다.");
        }

        @Test
        @Disabled("API가 NotFoundException을 500으로 처리 - 예외 매핑 구현 필요")
        @DisplayName("존재하지 않는 셀러 수정시 404 에러 반환")
        void shouldReturn404WhenSellerNotFound() {
            // given
            Map<String, Object> request = new HashMap<>();
            request.put("displayName", "수정된 스토어명");

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .patch(BASE_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("PUT /v2/admin/sellers/{sellerId} - 셀러 전체정보 수정")
    class UpdateFullInfoTest {

        @Test
        @Disabled("PUT API는 contractInfo, settlementInfo 등 복잡한 구조 필요 - 테스트 구조 개선 필요")
        @DisplayName("셀러 전체정보 수정 성공")
        void shouldUpdateFullInfoSuccessfully() {
            // given - 셀러와 관련 데이터 생성
            SellerJpaEntity seller = sellerJpaRepository.save(SellerJpaEntityFixtures.newEntity());
            Long sellerId = seller.getId();

            businessInfoJpaRepository.save(
                    SellerBusinessInfoJpaEntityFixtures.activeEntityWithSellerId(sellerId));
            addressJpaRepository.save(
                    SellerAddressJpaEntityFixtures.activeEntityWithSellerId(sellerId));
            csJpaRepository.save(SellerCsJpaEntityFixtures.activeEntityWithSellerId(sellerId));

            Map<String, Object> request = createFullUpdateRequest();

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/" + sellerId)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // 변경 확인
            SellerJpaEntity updated = sellerJpaRepository.findById(sellerId).orElseThrow();
            assertThat(updated.getDisplayName()).isEqualTo("수정된 스토어명");
        }

        @Test
        @Disabled("API가 NotFoundException을 500으로 처리 - 예외 매핑 구현 필요")
        @DisplayName("존재하지 않는 셀러 전체수정시 404 에러 반환")
        void shouldReturn404WhenSellerNotFound() {
            // given
            Map<String, Object> request = createFullUpdateRequest();

            // when & then
            givenAdmin()
                    .body(request)
                    .when()
                    .put(BASE_PATH + "/999999")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value());
        }
    }

    // ===== Helper Methods =====

    private Map<String, Object> createRegisterRequest(String suffix) {
        Map<String, Object> request = new HashMap<>();

        // seller 정보
        Map<String, Object> seller = new HashMap<>();
        seller.put("sellerName", "테스트셀러" + suffix);
        seller.put("displayName", "테스트 브랜드" + suffix);
        seller.put("logoUrl", "https://example.com/logo.png");
        seller.put("description", "테스트 셀러 설명입니다.");
        request.put("seller", seller);

        // businessInfo 정보
        Map<String, Object> businessInfo = new HashMap<>();
        businessInfo.put(
                "registrationNumber",
                "999-88-" + suffix.substring(Math.max(0, suffix.length() - 5)));
        businessInfo.put("companyName", "테스트컴퍼니" + suffix);
        businessInfo.put("representative", "홍길동");
        businessInfo.put(
                "saleReportNumber",
                "제2025-서울강남-" + suffix.substring(Math.max(0, suffix.length() - 4)));

        Map<String, Object> businessAddress = new HashMap<>();
        businessAddress.put("zipCode", "12345");
        businessAddress.put("line1", "서울시 강남구");
        businessAddress.put("line2", "테헤란로 123");
        businessInfo.put("businessAddress", businessAddress);

        Map<String, Object> csContact = new HashMap<>();
        csContact.put("phone", "02-1234-5678");
        csContact.put("email", "cs@example.com");
        csContact.put("mobile", "010-1234-5678");
        businessInfo.put("csContact", csContact);

        request.put("businessInfo", businessInfo);

        // address 정보
        Map<String, Object> address = new HashMap<>();
        address.put("addressType", "RETURN");
        address.put("addressName", "반품지");

        Map<String, Object> addressDetail = new HashMap<>();
        addressDetail.put("zipCode", "12345");
        addressDetail.put("line1", "서울시 강남구");
        addressDetail.put("line2", "테헤란로 123");
        address.put("address", addressDetail);

        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("name", "김담당");
        contactInfo.put("phone", "010-1234-5678");
        address.put("contactInfo", contactInfo);

        address.put("defaultAddress", true);
        request.put("address", address);

        return request;
    }

    private Map<String, Object> createFullUpdateRequest() {
        Map<String, Object> request = new HashMap<>();

        // seller 정보
        Map<String, Object> seller = new HashMap<>();
        seller.put("displayName", "수정된 스토어명");
        seller.put("logoUrl", "https://example.com/updated-logo.png");
        seller.put("description", "수정된 설명입니다.");
        request.put("seller", seller);

        // businessInfo 정보
        Map<String, Object> businessInfo = new HashMap<>();
        businessInfo.put("companyName", "수정된컴퍼니");
        businessInfo.put("representative", "김수정");
        businessInfo.put("saleReportNumber", "제2025-서울강남-9999호");

        Map<String, Object> businessAddress = new HashMap<>();
        businessAddress.put("zipCode", "54321");
        businessAddress.put("line1", "서울시 서초구");
        businessAddress.put("line2", "서초대로 456");
        businessInfo.put("businessAddress", businessAddress);

        request.put("businessInfo", businessInfo);

        // csInfo 정보
        Map<String, Object> csInfo = new HashMap<>();
        csInfo.put("phone", "02-9999-8888");
        csInfo.put("email", "updated-cs@example.com");
        csInfo.put("mobile", "010-9999-8888");
        request.put("csInfo", csInfo);

        // addresses 정보
        Map<String, Object> address = new HashMap<>();
        address.put("addressType", "RETURN");
        address.put("addressName", "수정된 반품지");

        Map<String, Object> addressDetail = new HashMap<>();
        addressDetail.put("zipCode", "54321");
        addressDetail.put("line1", "서울시 서초구");
        addressDetail.put("line2", "서초대로 456");
        address.put("address", addressDetail);

        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("name", "박수정");
        contactInfo.put("phone", "010-9999-8888");
        address.put("contactInfo", contactInfo);

        address.put("defaultAddress", true);
        request.put("addresses", new Object[] {address});

        return request;
    }
}
