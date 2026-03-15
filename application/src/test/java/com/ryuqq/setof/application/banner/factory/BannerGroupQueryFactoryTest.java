package com.ryuqq.setof.application.banner.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.banner.BannerQueryFixtures;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import com.ryuqq.setof.domain.banner.query.BannerGroupSortKey;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("BannerGroupQueryFactory 단위 테스트")
class BannerGroupQueryFactoryTest {

    private final BannerGroupQueryFactory sut = new BannerGroupQueryFactory();

    @Nested
    @DisplayName("create() - SearchParams → SearchCriteria 변환")
    class CreateTest {

        @Test
        @DisplayName("기본 SearchParams로부터 BannerGroupSearchCriteria를 생성한다")
        void create_DefaultParams_CreatesCriteria() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams();

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.bannerType()).isNull();
            assertThat(result.active()).isNull();
            assertThat(result.queryContext()).isNotNull();
        }

        @Test
        @DisplayName("유효한 BannerType 문자열을 enum으로 변환한다")
        void create_ValidBannerType_ConvertsToBannerTypeEnum() {
            // given
            BannerGroupSearchParams params =
                    BannerQueryFixtures.searchParamsWithBannerType("RECOMMEND");

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.bannerType()).isEqualTo(BannerType.RECOMMEND);
        }

        @Test
        @DisplayName("소문자 BannerType 문자열도 대소문자 무관하게 변환한다")
        void create_LowerCaseBannerType_ConvertsToBannerTypeEnum() {
            // given
            BannerGroupSearchParams params =
                    BannerQueryFixtures.searchParamsWithBannerType("recommend");

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.bannerType()).isEqualTo(BannerType.RECOMMEND);
        }

        @Test
        @DisplayName("유효하지 않은 BannerType 문자열이면 null로 처리한다")
        void create_InvalidBannerType_ReturnsNullBannerType() {
            // given
            BannerGroupSearchParams params =
                    BannerQueryFixtures.searchParamsWithInvalidBannerType();

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.bannerType()).isNull();
        }

        @Test
        @DisplayName("빈 문자열 BannerType이면 null로 처리한다")
        void create_BlankBannerType_ReturnsNullBannerType() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParamsWithBlankBannerType();

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.bannerType()).isNull();
        }

        @Test
        @DisplayName("null BannerType이면 null로 처리한다")
        void create_NullBannerType_ReturnsNullBannerType() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams();

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.bannerType()).isNull();
        }

        @Test
        @DisplayName("active 필터가 설정된 SearchCriteria를 생성한다")
        void create_WithActiveFilter_CreatesWithActiveFilter() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParamsActiveOnly();

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.active()).isTrue();
        }

        @Test
        @DisplayName("전시 기간 필터가 설정된 SearchCriteria를 생성한다")
        void create_WithDisplayPeriod_CreatesWithPeriodFilter() {
            // given
            Instant start = Instant.parse("2025-01-01T00:00:00Z");
            Instant end = Instant.parse("2025-12-31T23:59:59Z");
            BannerGroupSearchParams params = BannerQueryFixtures.searchParamsWithPeriod(start, end);

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.displayPeriodStart()).isEqualTo(start);
            assertThat(result.displayPeriodEnd()).isEqualTo(end);
        }

        @Test
        @DisplayName("제목 키워드 필터가 설정된 SearchCriteria를 생성한다")
        void create_WithTitleKeyword_CreatesWithKeywordFilter() {
            // given
            BannerGroupSearchParams params =
                    BannerQueryFixtures.searchParamsWithTitleKeyword("테스트 배너");

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.titleKeyword()).isEqualTo("테스트 배너");
        }

        @Test
        @DisplayName("NoOffset 페이징용 lastDomainId가 설정된 SearchCriteria를 생성한다")
        void create_WithLastDomainId_CreatesWithLastDomainId() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParamsWithLastDomainId(10L);

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.lastDomainId()).isEqualTo(10L);
            assertThat(result.isNoOffset()).isTrue();
        }

        @Test
        @DisplayName("정렬 기준은 CREATED_AT DESC로 고정된다")
        void create_SortKeyIsCreatedAtDesc() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams();

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.queryContext().sortKey()).isEqualTo(BannerGroupSortKey.CREATED_AT);
            assertThat(result.queryContext().sortDirection()).isEqualTo(SortDirection.DESC);
        }

        @Test
        @DisplayName("페이지 정보가 QueryContext에 반영된다")
        void create_WithPagination_PageIsReflectedInQueryContext() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams(2, 10);

            // when
            BannerGroupSearchCriteria result = sut.create(params);

            // then
            assertThat(result.queryContext().pageRequest().page()).isEqualTo(2);
            assertThat(result.queryContext().pageRequest().size()).isEqualTo(10);
            assertThat(result.page()).isEqualTo(2);
            assertThat(result.size()).isEqualTo(10);
        }
    }
}
