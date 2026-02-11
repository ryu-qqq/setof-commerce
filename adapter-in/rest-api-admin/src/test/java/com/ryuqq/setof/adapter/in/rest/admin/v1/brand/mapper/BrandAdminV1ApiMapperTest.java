package com.ryuqq.setof.adapter.in.rest.admin.v1.brand.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.BrandAdminApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.request.BrandSearchV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.brand.dto.response.BrandV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.common.dto.CustomPageableV1ApiResponse;
import com.ryuqq.setof.application.brand.dto.query.BrandSearchParams;
import com.ryuqq.setof.application.brand.dto.response.BrandPageResult;
import com.ryuqq.setof.application.brand.dto.response.BrandResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * BrandAdminV1ApiMapper 단위 테스트.
 *
 * <p>Admin API Mapper의 변환 로직을 테스트합니다.
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
    @DisplayName("toSearchParams 메서드 테스트")
    class ToSearchParamsTest {

        @Test
        @DisplayName("US mainDisplayType은 displayEnglishName 필드로 변환한다")
        void toSearchParams_US_Success() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequest();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("displayEnglishName");
            assertThat(params.searchWord()).isEqualTo("Nike");
            assertThat(params.searchParams().page()).isZero();
            assertThat(params.searchParams().size()).isEqualTo(20);
            assertThat(params.searchParams().sortDirection()).isEqualTo("ASC");
        }

        @Test
        @DisplayName("KR mainDisplayType은 displayKoreanName 필드로 변환한다")
        void toSearchParams_KR_Success() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestKr();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("displayKoreanName");
            assertThat(params.searchWord()).isEqualTo("나이키");
        }

        @Test
        @DisplayName("null mainDisplayType은 US(displayEnglishName)로 기본값 처리한다")
        void toSearchParams_NullMainDisplayType_DefaultUS() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestDefaultValues();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("displayEnglishName");
        }

        @Test
        @DisplayName("null brandName은 null로 변환한다")
        void toSearchParams_NullBrandName() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestNullBrandName();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
        }

        @Test
        @DisplayName("빈 문자열 brandName은 null로 변환한다")
        void toSearchParams_BlankBrandName() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestBlankBrandName();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isNull();
        }

        @Test
        @DisplayName("공백이 포함된 brandName은 trim 처리한다")
        void toSearchParams_TrimBrandName() {
            // given
            BrandSearchV1ApiRequest request =
                    new BrandSearchV1ApiRequest(null, "  Nike  ", "US", 0, 20, "ASC");

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchWord()).isEqualTo("Nike");
        }

        @Test
        @DisplayName("null page는 0으로 기본값 처리한다")
        void toSearchParams_NullPage_DefaultZero() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestDefaultValues();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isZero();
        }

        @Test
        @DisplayName("null size는 20으로 기본값 처리한다")
        void toSearchParams_NullSize_DefaultTwenty() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestDefaultValues();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().size()).isEqualTo(20);
        }

        @Test
        @DisplayName("null sortDirection은 ASC로 기본값 처리한다")
        void toSearchParams_NullSortDirection_DefaultASC() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestDefaultValues();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().sortDirection()).isEqualTo("ASC");
        }

        @Test
        @DisplayName("커스텀 page와 size 값이 정상 변환된다")
        void toSearchParams_CustomPageSize() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequestWithPage(2, 50);

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isEqualTo(2);
            assertThat(params.searchParams().size()).isEqualTo(50);
        }

        @Test
        @DisplayName("sortDirection DESC가 정상 변환된다")
        void toSearchParams_SortDirectionDESC() {
            // given
            BrandSearchV1ApiRequest request =
                    BrandAdminApiFixtures.searchRequestWithSortDirection("DESC");

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("sortKey는 createdAt으로 고정된다")
        void toSearchParams_SortKeyCreatedAt() {
            // given
            BrandSearchV1ApiRequest request = BrandAdminApiFixtures.searchRequest();

            // when
            BrandSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().sortKey()).isEqualTo("createdAt");
        }
    }

    @Nested
    @DisplayName("toPageResponse 메서드 테스트")
    class ToPageResponseTest {

        @Test
        @DisplayName("BrandPageResult를 CustomPageableV1ApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.brandPageResult();

            // when
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(2);
            assertThat(response.number()).isZero();
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalElements()).isEqualTo(2L);
            assertThat(response.content().get(0).brandId()).isEqualTo(1L);
            assertThat(response.content().get(0).brandName()).isEqualTo("Nike");
            assertThat(response.content().get(1).brandId()).isEqualTo(2L);
            assertThat(response.content().get(1).brandName()).isEqualTo("Adidas");
        }

        @Test
        @DisplayName("빈 BrandPageResult를 빈 CustomPageableV1ApiResponse로 변환한다")
        void toPageResponse_Empty() {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.emptyBrandPageResult();

            // when
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("페이징 메타데이터가 정상 변환된다")
        void toPageResponse_PagingMetadata() {
            // given
            BrandPageResult pageResult = BrandAdminApiFixtures.brandPageResult(1, 10, 25L);

            // when
            CustomPageableV1ApiResponse<BrandV1ApiResponse> response =
                    mapper.toPageResponse(pageResult);

            // then
            assertThat(response.number()).isEqualTo(1);
            assertThat(response.size()).isEqualTo(10);
            assertThat(response.totalElements()).isEqualTo(25L);
            assertThat(response.totalPages()).isEqualTo(3);
            assertThat(response.first()).isFalse();
            assertThat(response.last()).isFalse();
        }
    }

    @Nested
    @DisplayName("toResponse 메서드 테스트")
    class ToResponseTest {

        @Test
        @DisplayName("BrandResult를 BrandV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            BrandResult result = BrandAdminApiFixtures.brandResult(1L);

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandId()).isEqualTo(1L);
            assertThat(response.brandName()).isEqualTo("Nike");
            assertThat(response.mainDisplayType()).isEqualTo("US");
            assertThat(response.displayEnglishName()).isEqualTo("Nike");
            assertThat(response.displayKoreanName()).isEqualTo("나이키");
        }

        @Test
        @DisplayName("null brandId는 0L로 변환한다")
        void toResponse_NullBrandId_DefaultZero() {
            // given
            BrandResult result = BrandAdminApiFixtures.brandResultNullFields();

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandId()).isZero();
        }

        @Test
        @DisplayName("null brandName은 빈 문자열로 변환한다")
        void toResponse_NullBrandName_EmptyString() {
            // given
            BrandResult result = BrandAdminApiFixtures.brandResultNullFields();

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.brandName()).isEmpty();
            assertThat(response.displayEnglishName()).isEmpty();
        }

        @Test
        @DisplayName("null brandNameKo는 빈 문자열로 변환한다")
        void toResponse_NullBrandNameKo_EmptyString() {
            // given
            BrandResult result = BrandAdminApiFixtures.brandResultNullFields();

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.displayKoreanName()).isEmpty();
        }

        @Test
        @DisplayName("mainDisplayType은 항상 US로 고정된다")
        void toResponse_MainDisplayTypeAlwaysUS() {
            // given
            BrandResult result = BrandAdminApiFixtures.brandResult(1L);

            // when
            BrandV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.mainDisplayType()).isEqualTo("US");
        }
    }
}
