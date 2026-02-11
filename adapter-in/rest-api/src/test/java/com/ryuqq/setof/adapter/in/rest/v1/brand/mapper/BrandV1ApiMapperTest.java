package com.ryuqq.setof.adapter.in.rest.v1.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.brand.BrandApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.request.SearchBrandsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.brand.dto.response.BrandV1ApiResponse;
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
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
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
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 BrandDisplaySearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchBrandsV1ApiRequest request = BrandApiFixtures.searchRequest();

            // when
            BrandDisplaySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isEqualTo("나이키");
            assertThat(params.displayed()).isTrue();
        }

        @Test
        @DisplayName("null searchWord는 null로 변환한다")
        void toSearchParams_NullSearchWord() {
            // given
            SearchBrandsV1ApiRequest request = BrandApiFixtures.searchRequestEmpty();

            // when
            BrandDisplaySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
            assertThat(params.displayed()).isTrue();
        }

        @Test
        @DisplayName("빈 문자열 searchWord는 null로 변환한다")
        void toSearchParams_BlankSearchWord() {
            // given
            SearchBrandsV1ApiRequest request = new SearchBrandsV1ApiRequest("   ");

            // when
            BrandDisplaySearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
        }
    }

    @Nested
    @DisplayName("toListResponse")
    class ToListResponseTest {

        @Test
        @DisplayName("BrandDisplayResult 목록을 BrandV1ApiResponse 목록으로 변환한다")
        void toListResponse_Success() {
            // given
            List<BrandDisplayResult> results = BrandApiFixtures.displayResultList();

            // when
            List<BrandV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).hasSize(2);
            assertThat(response.get(0).brandId()).isEqualTo(1L);
            assertThat(response.get(0).brandName()).isEqualTo("NIKE");
            assertThat(response.get(0).korBrandName()).isEqualTo("나이키");
            assertThat(response.get(1).brandId()).isEqualTo(2L);
            assertThat(response.get(1).brandName()).isEqualTo("ADIDAS");
        }

        @Test
        @DisplayName("빈 목록을 빈 응답으로 변환한다")
        void toListResponse_Empty() {
            // given
            List<BrandDisplayResult> results = List.of();

            // when
            List<BrandV1ApiResponse> response = mapper.toListResponse(results);

            // then
            assertThat(response).isEmpty();
        }
    }

    @Nested
    @DisplayName("toResponse (BrandDisplayResult)")
    class ToResponseFromDisplayResultTest {

        @Test
        @DisplayName("BrandDisplayResult를 BrandV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            BrandDisplayResult result = BrandApiFixtures.displayResult(1L);

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandId()).isEqualTo(1L);
            assertThat(response.brandName()).isEqualTo("NIKE");
            assertThat(response.korBrandName()).isEqualTo("나이키");
            assertThat(response.brandIconImageUrl())
                    .isEqualTo("https://cdn.example.com/brands/nike.png");
        }
    }

    @Nested
    @DisplayName("toResponse (BrandResult)")
    class ToResponseFromBrandResultTest {

        @Test
        @DisplayName("BrandResult를 BrandV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            BrandResult result = BrandApiFixtures.brandResult(1L);

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandId()).isEqualTo(1L);
            assertThat(response.brandName()).isEqualTo("NIKE");
            assertThat(response.korBrandName()).isEqualTo("나이키");
            assertThat(response.brandIconImageUrl())
                    .isEqualTo("https://cdn.example.com/brands/nike.png");
        }
    }
}
