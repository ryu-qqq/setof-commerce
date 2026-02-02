package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.brand.BrandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.ExtendedBrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BrandAdminV1ApiMapper 단위 테스트.
 *
 * <p>V1 Admin 브랜드 API Mapper의 변환 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandAdminV1ApiMapper 단위 테스트")
class BrandAdminV1ApiMapperTest {

    private BrandAdminV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BrandAdminV1ApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams 테스트")
    class ToSearchParamsTest {

        @Test
        @DisplayName("기본 요청을 검색 파라미터로 변환")
        void shouldConvertRequestToParams() {
            // given
            BrandSearchV1ApiRequest request = BrandV1ApiFixtures.searchRequest();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params).isNotNull();
            assertThat(params.brandName()).isNull();
        }

        @Test
        @DisplayName("브랜드명 검색 요청 변환")
        void shouldConvertRequestWithBrandName() {
            // given
            BrandSearchV1ApiRequest request = BrandV1ApiFixtures.searchRequest("NIKE");

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params).isNotNull();
            assertThat(params.brandName()).isEqualTo("NIKE");
        }

        @Test
        @DisplayName("페이지 null 시 기본값 적용")
        void shouldApplyDefaultPageWhenNull() {
            // given
            BrandSearchV1ApiRequest request = new BrandSearchV1ApiRequest("NIKE", null, null);

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params).isNotNull();
            assertThat(params.page()).isZero();
            assertThat(params.size()).isEqualTo(20);
        }

        @Test
        @DisplayName("커스텀 페이징 요청 변환")
        void shouldConvertRequestWithCustomPaging() {
            // given
            BrandSearchV1ApiRequest request = BrandV1ApiFixtures.searchRequest("NIKE", 2, 50);

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.page()).isEqualTo(2);
            assertThat(params.size()).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("toPageResponse 테스트")
    class ToPageResponseTest {

        @Test
        @DisplayName("페이지 결과를 V1 응답으로 변환")
        void shouldConvertPageResultToResponse() {
            // given
            BrandPageResult pageResult = BrandV1ApiFixtures.pageResult();

            // when
            CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response).isNotNull();
            assertThat(response.content()).hasSize(1);
            assertThat(response.number()).isZero();
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalElements()).isEqualTo(1L);
        }

        @Test
        @DisplayName("빈 페이지 결과 변환")
        void shouldConvertEmptyPageResult() {
            // given
            BrandPageResult pageResult = BrandV1ApiFixtures.emptyPageResult();

            // when
            CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("여러 브랜드 결과 변환")
        void shouldConvertMultipleBrands() {
            // given
            List<BrandResult> brands = BrandV1ApiFixtures.multipleBrandResults();
            BrandPageResult pageResult = BrandV1ApiFixtures.pageResult(brands, 3L, 0, 20);

            // when
            CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(3);
            assertThat(response.content().get(0).brandName()).isEqualTo("NIKE");
            assertThat(response.content().get(1).brandName()).isEqualTo("ADIDAS");
            assertThat(response.content().get(2).brandName()).isEqualTo("PUMA");
        }

        @Test
        @DisplayName("브랜드 응답 필드 매핑 검증")
        void shouldMapBrandResponseFieldsCorrectly() {
            // given
            BrandResult brand = BrandV1ApiFixtures.brandResult(1L, "NIKE", "나이키");
            BrandPageResult pageResult = BrandV1ApiFixtures.pageResult(List.of(brand), 1L, 0, 20);

            // when
            CustomPageableV1ApiResponse<ExtendedBrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            ExtendedBrandV1ApiResponse brandResponse = response.content().get(0);
            assertThat(brandResponse.brandId()).isEqualTo(1L);
            assertThat(brandResponse.brandName()).isEqualTo("NIKE");
            assertThat(brandResponse.mainDisplayType()).isEqualTo("ENGLISH");
            assertThat(brandResponse.displayEnglishName()).isEqualTo("NIKE");
            assertThat(brandResponse.displayKoreanName()).isEqualTo("나이키");
        }
    }
}
