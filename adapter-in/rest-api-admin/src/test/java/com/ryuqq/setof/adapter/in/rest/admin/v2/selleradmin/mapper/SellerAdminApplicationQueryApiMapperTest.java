package com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.v2.selleradmin.dto.query.SearchSellerAdminApplicationsApiRequest;
import com.ryuqq.setof.application.selleradmin.dto.query.GetSellerAdminApplicationQuery;
import com.ryuqq.setof.application.selleradmin.dto.query.SellerAdminApplicationSearchParams;
import com.ryuqq.setof.domain.selleradmin.vo.SellerAdminStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerAdminApplicationQueryApiMapper 단위 테스트.
 *
 * <p>Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("SellerAdminApplicationQueryApiMapper 단위 테스트")
class SellerAdminApplicationQueryApiMapperTest {

    private SellerAdminApplicationQueryApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerAdminApplicationQueryApiMapper();
    }

    @Nested
    @DisplayName("toGetQuery(String)")
    class ToGetQueryTest {

        @Test
        @DisplayName("sellerAdminId를 GetQuery로 변환한다")
        void toGetQuery_Success() {
            // given
            String sellerAdminId = "01956f4a-2b3c-7d8e-9f0a-1b2c3d4e5f67";

            // when
            GetSellerAdminApplicationQuery query = mapper.toGetQuery(sellerAdminId);

            // then
            assertThat(query.sellerAdminId()).isEqualTo(sellerAdminId);
        }
    }

    @Nested
    @DisplayName("toSearchParams(SearchSellerAdminApplicationsApiRequest)")
    class ToSearchParamsTest {

        @Test
        @DisplayName("검색 요청을 SearchParams로 변환한다")
        void toSearchParams_Success() {
            // given
            SearchSellerAdminApplicationsApiRequest request =
                    new SearchSellerAdminApplicationsApiRequest(
                            List.of(1L, 2L),
                            List.of("PENDING_APPROVAL"),
                            "loginId",
                            "admin",
                            "createdAt",
                            "DESC",
                            "2025-01-01",
                            "2025-12-31",
                            1,
                            10);

            // when
            SellerAdminApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.sellerIds()).containsExactly(1L, 2L);
            assertThat(params.status()).containsExactly(SellerAdminStatus.PENDING_APPROVAL);
            assertThat(params.searchField()).isEqualTo("loginId");
            assertThat(params.searchWord()).isEqualTo("admin");
            assertThat(params.commonSearchParams().page()).isEqualTo(1);
            assertThat(params.commonSearchParams().size()).isEqualTo(10);
        }

        @Test
        @DisplayName("복수 상태 검색 요청을 변환한다")
        void toSearchParams_MultipleStatus_Success() {
            // given
            SearchSellerAdminApplicationsApiRequest request =
                    new SearchSellerAdminApplicationsApiRequest(
                            null,
                            List.of("PENDING_APPROVAL", "REJECTED"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            0,
                            20);

            // when
            SellerAdminApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.status())
                    .containsExactlyInAnyOrder(
                            SellerAdminStatus.PENDING_APPROVAL, SellerAdminStatus.REJECTED);
        }

        @Test
        @DisplayName("null 값에 대해 기본값을 적용한다")
        void toSearchParams_WithNullValues_AppliesDefaults() {
            // given
            SearchSellerAdminApplicationsApiRequest request =
                    new SearchSellerAdminApplicationsApiRequest(
                            null, null, null, null, null, null, null, null, null, null);

            // when
            SellerAdminApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.status()).isEmpty();
            assertThat(params.sellerIds()).isNull();
            assertThat(params.dateRange()).isNull();
            assertThat(params.commonSearchParams().page()).isEqualTo(0);
            assertThat(params.commonSearchParams().size()).isEqualTo(20);
            assertThat(params.commonSearchParams().sortKey()).isEqualTo("createdAt");
            assertThat(params.commonSearchParams().sortDirection()).isEqualTo("DESC");
        }

        @Test
        @DisplayName("잘못된 상태값은 빈 리스트로 변환한다")
        void toSearchParams_InvalidStatus_ReturnsEmptyList() {
            // given
            SearchSellerAdminApplicationsApiRequest request =
                    new SearchSellerAdminApplicationsApiRequest(
                            null,
                            List.of("INVALID_STATUS"),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null);

            // when
            SellerAdminApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.status()).isEmpty();
        }

        @Test
        @DisplayName("날짜 범위를 DateRange로 변환한다")
        void toSearchParams_WithDateRange_Success() {
            // given
            SearchSellerAdminApplicationsApiRequest request =
                    new SearchSellerAdminApplicationsApiRequest(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "2025-01-01",
                            "2025-12-31",
                            null,
                            null);

            // when
            SellerAdminApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.dateRange()).isNotNull();
            assertThat(params.dateRange().startDate()).isNotNull();
            assertThat(params.dateRange().endDate()).isNotNull();
        }

        @Test
        @DisplayName("잘못된 날짜 형식은 null로 처리한다")
        void toSearchParams_InvalidDateFormat_ReturnsNullDateRange() {
            // given
            SearchSellerAdminApplicationsApiRequest request =
                    new SearchSellerAdminApplicationsApiRequest(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "invalid-date",
                            "2025-12-31",
                            null,
                            null);

            // when
            SellerAdminApplicationSearchParams params = mapper.toSearchParams(request);

            // then
            assertThat(params.dateRange()).isNotNull();
            assertThat(params.dateRange().startDate()).isNull();
        }
    }
}
