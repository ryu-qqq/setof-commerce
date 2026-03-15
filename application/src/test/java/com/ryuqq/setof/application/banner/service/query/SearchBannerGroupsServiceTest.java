package com.ryuqq.setof.application.banner.service.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerQueryFixtures;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupPageResult;
import com.ryuqq.setof.application.banner.dto.query.BannerGroupSearchParams;
import com.ryuqq.setof.application.banner.factory.BannerGroupQueryFactory;
import com.ryuqq.setof.application.banner.manager.BannerGroupReadManager;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import com.ryuqq.setof.domain.banner.query.BannerGroupSortKey;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SearchBannerGroupsService 단위 테스트")
class SearchBannerGroupsServiceTest {

    @InjectMocks private SearchBannerGroupsService sut;

    @Mock private BannerGroupReadManager readManager;
    @Mock private BannerGroupQueryFactory queryFactory;

    @Nested
    @DisplayName("execute() - 배너 그룹 목록 검색")
    class ExecuteTest {

        @Test
        @DisplayName("검색 조건으로 배너 그룹 목록을 조회하고 PageResult를 반환한다")
        void execute_ValidParams_ReturnsBannerGroupPageResult() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams();
            BannerGroupSearchCriteria criteria = defaultCriteria();
            List<BannerGroup> groups = BannerQueryFixtures.activeBannerGroups();
            long totalCount = 2L;

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(groups);
            given(readManager.countByCriteria(criteria)).willReturn(totalCount);

            // when
            BannerGroupPageResult result = sut.execute(params);

            // then
            assertThat(result).isNotNull();
            assertThat(result.items()).hasSize(2);
            assertThat(result.totalCount()).isEqualTo(totalCount);
            assertThat(result.page()).isZero();
            assertThat(result.size()).isEqualTo(20);
            assertThat(result.lastDomainId()).isEqualTo(groups.get(groups.size() - 1).idValue());
            then(queryFactory).should().create(params);
            then(readManager).should().findByCriteria(criteria);
            then(readManager).should().countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 items와 lastDomainId=null인 PageResult를 반환한다")
        void execute_NoResults_ReturnsEmptyPageResult() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams();
            BannerGroupSearchCriteria criteria = defaultCriteria();
            List<BannerGroup> emptyGroups = Collections.emptyList();
            long totalCount = 0L;

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(emptyGroups);
            given(readManager.countByCriteria(criteria)).willReturn(totalCount);

            // when
            BannerGroupPageResult result = sut.execute(params);

            // then
            assertThat(result.items()).isEmpty();
            assertThat(result.totalCount()).isZero();
            assertThat(result.lastDomainId()).isNull();
            then(queryFactory).should().create(params);
            then(readManager).should().findByCriteria(criteria);
            then(readManager).should().countByCriteria(criteria);
        }

        @Test
        @DisplayName("lastDomainId는 반환된 목록의 마지막 요소 ID이다")
        void execute_WithResults_LastDomainIdIsLastGroupId() {
            // given
            BannerGroupSearchParams params = BannerQueryFixtures.searchParams();
            BannerGroupSearchCriteria criteria = defaultCriteria();
            List<BannerGroup> groups = BannerQueryFixtures.activeBannerGroups();
            long totalCount = 2L;
            Long expectedLastId = groups.get(groups.size() - 1).idValue();

            given(queryFactory.create(params)).willReturn(criteria);
            given(readManager.findByCriteria(criteria)).willReturn(groups);
            given(readManager.countByCriteria(criteria)).willReturn(totalCount);

            // when
            BannerGroupPageResult result = sut.execute(params);

            // then
            assertThat(result.lastDomainId()).isEqualTo(expectedLastId);
        }

        private BannerGroupSearchCriteria defaultCriteria() {
            QueryContext<BannerGroupSortKey> queryContext =
                    QueryContext.of(
                            BannerGroupSortKey.defaultKey(),
                            SortDirection.DESC,
                            PageRequest.of(0, 20));
            return BannerGroupSearchCriteria.of(
                    BannerType.RECOMMEND, null, null, null, null, null, queryContext);
        }
    }
}
