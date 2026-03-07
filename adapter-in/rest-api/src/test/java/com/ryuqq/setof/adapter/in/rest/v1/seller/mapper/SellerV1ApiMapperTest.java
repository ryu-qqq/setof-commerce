package com.ryuqq.setof.adapter.in.rest.v1.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerV1ApiMapper 단위 테스트.
 *
 * <p>Seller V1 API Mapper의 레거시 flat 구조 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerV1ApiMapper 단위 테스트")
class SellerV1ApiMapperTest {

    private SellerV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerV1ApiMapper();
    }

    @Nested
    @DisplayName("toResponse 메서드 테스트")
    class ToResponseTest {

        @Test
        @DisplayName("SellerCompositeResult를 레거시 flat 구조로 변환한다")
        void toResponse_Success() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.sellerName()).isEqualTo("나이키코리아 유한회사");
            assertThat(response.logoUrl()).isEqualTo("https://cdn.example.com/sellers/nike.png");
            assertThat(response.sellerDescription()).isEqualTo("나이키 공식 판매처");
        }

        @Test
        @DisplayName("address는 businessAddress + addressDetail + zipcode를 concat한다")
        void toResponse_AddressConcat() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.address()).isEqualTo("서울특별시 강남구 테헤란로 123 4층 06234");
        }

        @Test
        @DisplayName("csPhoneNumber와 alimTalkPhoneNumber가 올바르게 매핑된다")
        void toResponse_CsPhoneMapping() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.csPhoneNumber()).isEqualTo("1588-0000");
            assertThat(response.alimTalkPhoneNumber()).isEqualTo("010-1234-5678");
            assertThat(response.email()).isEqualTo("cs@nike.co.kr");
        }

        @Test
        @DisplayName("businessInfo에서 registrationNumber, representative, saleReportNumber를 추출한다")
        void toResponse_BusinessInfoFlat() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.registrationNumber()).isEqualTo("123-45-67890");
            assertThat(response.representative()).isEqualTo("홍길동");
            assertThat(response.saleReportNumber()).isEqualTo("2024-서울강남-12345");
        }

        @Test
        @DisplayName("CsInfo가 null인 경우 CS 관련 필드는 빈 문자열이다")
        void toResponse_NullCsInfo() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResultWithoutCsInfo(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.csPhoneNumber()).isEmpty();
            assertThat(response.alimTalkPhoneNumber()).isEmpty();
            assertThat(response.email()).isEmpty();
            assertThat(response.registrationNumber()).isEqualTo("123-45-67890");
        }

        @Test
        @DisplayName("BusinessInfo가 null인 경우 사업자 관련 필드는 빈 문자열이다")
        void toResponse_NullBusinessInfo() {
            // given
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithoutBusinessInfo(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.sellerName()).isEmpty();
            assertThat(response.address()).isEmpty();
            assertThat(response.registrationNumber()).isEmpty();
            assertThat(response.saleReportNumber()).isEmpty();
            assertThat(response.representative()).isEmpty();
            assertThat(response.csPhoneNumber()).isEqualTo("1588-0000");
        }

        @Test
        @DisplayName("CsInfo와 BusinessInfo가 모두 null인 경우 모든 선택 필드는 빈 문자열이다")
        void toResponse_NullOptionalFields() {
            // given
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithNullOptionalFields(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.sellerName()).isEmpty();
            assertThat(response.address()).isEmpty();
            assertThat(response.csPhoneNumber()).isEmpty();
            assertThat(response.email()).isEmpty();
        }

        @Test
        @DisplayName("seller.id가 null인 경우 sellerId는 0L로 변환한다")
        void toResponse_NullSellerId() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResultWithNullId();

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(0L);
            assertThat(response.sellerName()).isEqualTo("나이키코리아 유한회사");
        }
    }
}
