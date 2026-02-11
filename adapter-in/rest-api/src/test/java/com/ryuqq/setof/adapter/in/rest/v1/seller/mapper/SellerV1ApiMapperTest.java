package com.ryuqq.setof.adapter.in.rest.v1.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse.BusinessInfoResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse.CsInfoResponse;
import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerV1ApiMapper 단위 테스트.
 *
 * <p>Seller V1 API Mapper의 변환 로직을 테스트합니다.
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
        @DisplayName("SellerCompositeResult를 SellerV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.sellerName()).isEqualTo("나이키코리아");
            assertThat(response.displayName()).isEqualTo("나이키 공식스토어");
            assertThat(response.logoUrl()).isEqualTo("https://cdn.example.com/sellers/nike.png");
            assertThat(response.description()).isEqualTo("나이키 공식 판매처");
        }

        @Test
        @DisplayName("CsInfo가 포함된 경우 CsInfoResponse로 변환한다")
        void toResponse_WithCsInfo() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            CsInfoResponse csInfo = response.csInfo();
            assertThat(csInfo).isNotNull();
            assertThat(csInfo.csPhone()).isEqualTo("1588-0000");
            assertThat(csInfo.csEmail()).isEqualTo("cs@nike.co.kr");
            assertThat(csInfo.operatingStartTime()).isEqualTo("09:00");
            assertThat(csInfo.operatingEndTime()).isEqualTo("18:00");
            assertThat(csInfo.operatingDays()).isEqualTo("월~금");
            assertThat(csInfo.kakaoChannelUrl()).isEqualTo("https://pf.kakao.com/nike");
        }

        @Test
        @DisplayName("BusinessInfo가 포함된 경우 BusinessInfoResponse로 변환한다")
        void toResponse_WithBusinessInfo() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResult(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            BusinessInfoResponse businessInfo = response.businessInfo();
            assertThat(businessInfo).isNotNull();
            assertThat(businessInfo.registrationNumber()).isEqualTo("123-45-67890");
            assertThat(businessInfo.companyName()).isEqualTo("나이키코리아 유한회사");
            assertThat(businessInfo.representative()).isEqualTo("홍길동");
            assertThat(businessInfo.saleReportNumber()).isEqualTo("2024-서울강남-12345");
        }

        @Test
        @DisplayName("CsInfo가 null인 경우 CsInfoResponse도 null이다")
        void toResponse_NullCsInfo() {
            // given
            SellerCompositeResult result = SellerApiFixtures.sellerCompositeResultWithoutCsInfo(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.csInfo()).isNull();
            assertThat(response.businessInfo()).isNotNull();
        }

        @Test
        @DisplayName("BusinessInfo가 null인 경우 BusinessInfoResponse도 null이다")
        void toResponse_NullBusinessInfo() {
            // given
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithoutBusinessInfo(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.csInfo()).isNotNull();
            assertThat(response.businessInfo()).isNull();
        }

        @Test
        @DisplayName("CsInfo와 BusinessInfo가 모두 null인 경우 응답도 null이다")
        void toResponse_NullOptionalFields() {
            // given
            SellerCompositeResult result =
                    SellerApiFixtures.sellerCompositeResultWithNullOptionalFields(1L);

            // when
            SellerV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.sellerId()).isEqualTo(1L);
            assertThat(response.sellerName()).isEqualTo("나이키코리아");
            assertThat(response.csInfo()).isNull();
            assertThat(response.businessInfo()).isNull();
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
            assertThat(response.sellerName()).isEqualTo("나이키코리아");
        }
    }
}
