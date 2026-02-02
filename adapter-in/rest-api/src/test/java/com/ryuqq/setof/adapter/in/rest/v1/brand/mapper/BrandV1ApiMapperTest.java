package com.ryuqq.setof.adapter.in.rest.v1.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.brand.BrandV1ApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.query.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandDisplayV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandDisplaySearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandDisplayResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BrandV1ApiMapper 단위 테스트.
 *
 * <p>V1 Public 브랜드 API Mapper의 변환 로직을 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("BrandV1ApiMapper 단위 테스트")
class BrandV1ApiMapperTest {

    private BrandV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BrandV1ApiMapper();
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
            BrandDisplaySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params).isNotNull();
            assertThat(params.brandName()).isNull();
            assertThat(params.displayed()).isNull();
        }

        @Test
        @DisplayName("브랜드명 검색 요청 변환")
        void shouldConvertRequestWithBrandName() {
            // given
            BrandSearchV1ApiRequest request = BrandV1ApiFixtures.searchRequest("NIKE");

            // when
            BrandDisplaySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params).isNotNull();
            assertThat(params.brandName()).isEqualTo("NIKE");
            assertThat(params.displayed()).isNull();
        }

        @Test
        @DisplayName("노출 여부 포함 검색 요청 변환")
        void shouldConvertRequestWithDisplayed() {
            // given
            BrandSearchV1ApiRequest request = BrandV1ApiFixtures.searchRequest("NIKE", true);

            // when
            BrandDisplaySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.brandName()).isEqualTo("NIKE");
            assertThat(params.displayed()).isTrue();
        }
    }

    @Nested
    @DisplayName("toListResponse 테스트")
    class ToListResponseTest {

        @Test
        @DisplayName("단일 결과를 응답 목록으로 변환")
        void shouldConvertSingleResult() {
            // given
            List<BrandDisplayResult> results = List.of(BrandV1ApiFixtures.brandDisplayResult());

            // when
            List<BrandDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(1);
            assertThat(response.get(0).brandId()).isEqualTo(1L);
            assertThat(response.get(0).brandName()).isEqualTo("NIKE");
            assertThat(response.get(0).korBrandName()).isEqualTo("나이키");
        }

        @Test
        @DisplayName("여러 결과를 응답 목록으로 변환")
        void shouldConvertMultipleResults() {
            // given
            List<BrandDisplayResult> results = BrandV1ApiFixtures.multipleBrandDisplayResults();

            // when
            List<BrandDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(3);
            assertThat(response.get(0).brandName()).isEqualTo("NIKE");
            assertThat(response.get(1).brandName()).isEqualTo("ADIDAS");
            assertThat(response.get(2).brandName()).isEqualTo("PUMA");
        }

        @Test
        @DisplayName("빈 결과 리스트 변환")
        void shouldConvertEmptyResultList() {
            // given
            List<BrandDisplayResult> results = List.of();

            // when
            List<BrandDisplayV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).isEmpty();
        }
    }

    @Nested
    @DisplayName("toResponse (BrandResult) 테스트")
    class ToResponseFromBrandResultTest {

        @Test
        @DisplayName("BrandResult를 V1 응답으로 변환")
        void shouldConvertBrandResultToResponse() {
            // given
            BrandResult result = BrandV1ApiFixtures.brandResult();

            // when
            BrandDisplayV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandId()).isEqualTo(1L);
            assertThat(response.brandName()).isEqualTo("NIKE");
            assertThat(response.korBrandName()).isEqualTo("나이키");
            assertThat(response.brandIconImageUrl())
                    .isEqualTo("https://cdn.setof.com/brands/nike.png");
        }

        @Test
        @DisplayName("커스텀 BrandResult를 V1 응답으로 변환")
        void shouldConvertCustomBrandResultToResponse() {
            // given
            BrandResult result = BrandV1ApiFixtures.brandResult(100L, "PUMA", "푸마");

            // when
            BrandDisplayV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandId()).isEqualTo(100L);
            assertThat(response.brandName()).isEqualTo("PUMA");
            assertThat(response.korBrandName()).isEqualTo("푸마");
        }
    }
}
