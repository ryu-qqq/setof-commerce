package com.ryuqq.setof.application.banner.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.banner.BannerQueryFixtures;
import com.ryuqq.setof.application.banner.port.out.BannerGroupQueryPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.exception.BannerException;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import com.ryuqq.setof.domain.banner.query.BannerGroupSortKey;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
@DisplayName("BannerGroupReadManager 단위 테스트")
class BannerGroupReadManagerTest {

    @InjectMocks private BannerGroupReadManager sut;

    @Mock private BannerGroupQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 배너 그룹 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 배너 그룹을 ID로 조회한다")
        void getById_ExistingBannerGroup_ReturnsBannerGroup() {
            // given
            long bannerGroupId = 1L;
            BannerGroup expected = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(queryPort.findById(bannerGroupId)).willReturn(Optional.of(expected));

            // when
            BannerGroup result = sut.getById(bannerGroupId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(bannerGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 BannerException이 발생한다")
        void getById_NonExistingBannerGroup_ThrowsBannerException() {
            // given
            long bannerGroupId = 999L;

            given(queryPort.findById(bannerGroupId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(bannerGroupId))
                    .isInstanceOf(BannerException.class);
            then(queryPort).should().findById(bannerGroupId);
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 배너 그룹 목록 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 배너 그룹 목록을 조회한다")
        void findByCriteria_ValidCriteria_ReturnsBannerGroupList() {
            // given
            BannerGroupSearchCriteria criteria = defaultCriteria();
            List<BannerGroup> expected = BannerQueryFixtures.activeBannerGroups();

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<BannerGroup> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            BannerGroupSearchCriteria criteria = defaultCriteria();

            given(queryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<BannerGroup> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findByCriteria(criteria);
        }
    }

    @Nested
    @DisplayName("countByCriteria() - 검색 조건으로 배너 그룹 수 조회")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 배너 그룹 수를 반환한다")
        void countByCriteria_ValidCriteria_ReturnsCount() {
            // given
            BannerGroupSearchCriteria criteria = defaultCriteria();
            long expectedCount = 5L;

            given(queryPort.countByCriteria(criteria)).willReturn(expectedCount);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(queryPort).should().countByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 0을 반환한다")
        void countByCriteria_NoResults_ReturnsZero() {
            // given
            BannerGroupSearchCriteria criteria = defaultCriteria();

            given(queryPort.countByCriteria(criteria)).willReturn(0L);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isZero();
            then(queryPort).should().countByCriteria(criteria);
        }
    }

    private BannerGroupSearchCriteria defaultCriteria() {
        QueryContext<BannerGroupSortKey> queryContext =
                QueryContext.of(
                        BannerGroupSortKey.defaultKey(), SortDirection.DESC, PageRequest.of(0, 20));
        return BannerGroupSearchCriteria.of(null, null, null, null, null, null, queryContext);
    }
}
