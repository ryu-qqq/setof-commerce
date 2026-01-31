package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.PageApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.commoncode.CommonCodeApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.query.SearchCommonCodesPageApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.response.CommonCodeApiResponse;
import com.ryuqq.setof.application.commoncode.dto.query.CommonCodeSearchParams;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodePageResult;
import com.ryuqq.setof.application.commoncode.dto.response.CommonCodeResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * CommonCodeQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommonCodeQueryApiMapper 단위 테스트")
class CommonCodeQueryApiMapperTest {

    private CommonCodeQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CommonCodeQueryApiMapper();
    }

    @Nested
    @DisplayName("toSearchParams")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchCommonCodesPageApiRequest request = CommonCodeApiFixtures.searchRequest();

            // when
            CommonCodeSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.commonCodeTypeId()).isEqualTo(request.commonCodeTypeId());
            assertThat(params.searchParams().sortKey()).isEqualTo(request.sortKey());
            assertThat(params.searchParams().sortDirection()).isEqualTo(request.sortDirection());
            assertThat(params.searchParams().page()).isEqualTo(request.page());
            assertThat(params.searchParams().size()).isEqualTo(request.size());
        }

        @Test
        @DisplayName("null 페이지 값을 기본값으로 처리한다")
        void toSearchParams_NullPage_UsesDefault() {
            // given
            SearchCommonCodesPageApiRequest request =
                    new SearchCommonCodesPageApiRequest(
                            1L, null, null, "CREATED_AT", "DESC", null, null);

            // when
            CommonCodeSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.searchParams().page()).isZero();
            assertThat(params.searchParams().size()).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("toResponse")
    class ToResponseTest {

        @Test
        @DisplayName("Result를 Response로 변환한다")
        void toResponse_Success() {
            // given
            CommonCodeResult result = CommonCodeApiFixtures.codeResult();

            // when
            CommonCodeApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.id()).isEqualTo(result.id());
            assertThat(response.commonCodeTypeId()).isEqualTo(result.commonCodeTypeId());
            assertThat(response.code()).isEqualTo(result.code());
            assertThat(response.displayName()).isEqualTo(result.displayName());
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
            CommonCodePageResult pageResult = CommonCodeApiFixtures.pageResult();

            // when
            PageApiResponse<CommonCodeApiResponse> response = mapper.toPageResponse(pageResult);

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
            CommonCodePageResult emptyResult = CommonCodeApiFixtures.emptyPageResult();

            // when
            PageApiResponse<CommonCodeApiResponse> response = mapper.toPageResponse(emptyResult);

            // then
            assertThat(response.content()).isEmpty();
            assertThat(response.totalElements()).isZero();
        }

        @Test
        @DisplayName("여러 결과를 포함한 PageResult를 변환한다")
        void toPageResponse_Multiple_Success() {
            // given
            List<CommonCodeResult> results = CommonCodeApiFixtures.multipleResults();
            CommonCodePageResult pageResult = CommonCodeApiFixtures.pageResult(results, 0, 20, 3);

            // when
            PageApiResponse<CommonCodeApiResponse> response = mapper.toPageResponse(pageResult);

            // then
            assertThat(response.content()).hasSize(3);
            assertThat(response.totalElements()).isEqualTo(3);
        }
    }
}
