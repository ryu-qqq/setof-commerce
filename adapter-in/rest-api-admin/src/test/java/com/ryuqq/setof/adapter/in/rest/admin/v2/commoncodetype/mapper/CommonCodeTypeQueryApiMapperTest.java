package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.commoncodetype.CommonCodeTypeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.query.SearchCommonCodeTypesPageApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncodetype.dto.response.CommonCodeTypeApiResponse;
import com.ryuqq.setof.application.commoncodetype.dto.query.CommonCodeTypeSearchParams;
import com.ryuqq.setof.application.commoncodetype.dto.response.CommonCodeTypePageResult;
import com.ryuqq.setof.application.commoncodetype.dto.response.CommonCodeTypeResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CommonCodeTypeQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeTypeQueryApiMapper 단위 테스트")
class CommonCodeTypeQueryApiMapperTest {

    private CommonCodeTypeQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CommonCodeTypeQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchCommonCodeTypesPageApiRequest request = CommonCodeTypeApiFixtures.searchRequest();

            // when
            CommonCodeTypeSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().sortKey()).isEqualTo(request.sortKey());
            assertThat(params.searchParams().sortDirection()).isEqualTo(request.sortDirection());
            assertThat(params.searchParams().page()).isEqualTo(request.page());
            assertThat(params.searchParams().size()).isEqualTo(request.size());
        }

        @Test
        @DisplayName("null 페이지 값을 기본값으로 처리한다")
        void toSearchParams_NullPage_UsesDefault() {
            // given
            SearchCommonCodeTypesPageApiRequest request =
                    new SearchCommonCodeTypesPageApiRequest(
                            null, null, null, null, "CREATED_AT", "DESC", null, null);

            // when
            CommonCodeTypeSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isZero();
            assertThat(params.searchParams().size()).isEqualTo(20);
        }

        @Test
        @DisplayName("검색 필드/검색어 요청을 변환한다")
        void toSearchParams_WithSearchFieldAndWord_Success() {
            // given
            SearchCommonCodeTypesPageApiRequest request =
                    CommonCodeTypeApiFixtures.searchRequestWithSearch("code", "결제");

            // when
            CommonCodeTypeSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchField()).isEqualTo("code");
            assertThat(params.searchWord()).isEqualTo("결제");
        }

        @Test
        @DisplayName("type(CommonCodeValue) 필터 요청을 변환한다")
        void toSearchParams_WithType_Success() {
            // given
            SearchCommonCodeTypesPageApiRequest request =
                    CommonCodeTypeApiFixtures.searchRequestWithType("CARD");

            // when
            CommonCodeTypeSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.type()).isEqualTo("CARD");
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("Result를 Response로 변환한다")
        void toResponse_Success() {
            // given
            CommonCodeTypeResult result = CommonCodeTypeApiFixtures.typeResult();

            // when
            CommonCodeTypeApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.id()).isEqualTo(result.id());
            assertThat(response.code()).isEqualTo(result.code());
            assertThat(response.name()).isEqualTo(result.name());
            assertThat(response.description()).isEqualTo(result.description());
            assertThat(response.displayOrder()).isEqualTo(result.displayOrder());
            assertThat(response.active()).isEqualTo(result.active());
            assertThat(response.createdAt()).isNotNull();
            assertThat(response.updatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toPageResponse")
    class ToPageResponseTest {

        @Test
        @DisplayName("PageResult를 PageApiResponse로 변환한다")
        void toPageResponse_Success() {
            // given
            CommonCodeTypePageResult pageResult = CommonCodeTypeApiFixtures.pageResult();

            // when
            PageApiResponse<CommonCodeTypeApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(1);
            assertThat(response.page()).isZero();
            assertThat(response.size()).isEqualTo(20);
            assertThat(response.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("빈 PageResult를 빈 PageApiResponse로 변환한다")
        void toPageResponse_Empty_Success() {
            // given
            CommonCodeTypePageResult emptyResult = CommonCodeTypeApiFixtures.emptyPageResult();

            // when
            PageApiResponse<CommonCodeTypeApiResponse> response =
                    mapper.toPageResponse(emptyResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("여러 결과를 포함한 PageResult를 변환한다")
        void toPageResponse_Multiple_Success() {
            // given
            List<CommonCodeTypeResult> results = CommonCodeTypeApiFixtures.multipleResults();
            CommonCodeTypePageResult pageResult =
                    CommonCodeTypeApiFixtures.pageResult(results, 0, 20, 3);

            // when
            PageApiResponse<CommonCodeTypeApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(3);
            assertThat(response.totalElements()).isEqualTo(3);
        }
    }
}
