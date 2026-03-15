package com.ryuqq.setof.adapter.out.persistence.banner.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.banner.BannerJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.entity.BannerSlideJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.mapper.BannerSlideJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.banner.repository.BannerSlideQueryDslRepository;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.entity.BannerSlide;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import com.ryuqq.setof.domain.banner.query.BannerGroupSortKey;
import com.ryuqq.setof.domain.banner.vo.BannerType;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.setof.commerce.domain.banner.BannerFixtures;
import java.time.Instant;
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

/**
 * BannerGroupQueryAdapterTest - 배너 그룹 Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("BannerGroupQueryAdapter 단위 테스트")
class BannerGroupQueryAdapterTest {

    @Mock private BannerSlideQueryDslRepository queryDslRepository;

    @Mock private BannerGroupJpaEntityMapper bannerGroupMapper;

    @Mock private BannerSlideJpaEntityMapper bannerSlideMapper;

    @InjectMocks private BannerGroupQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 BannerGroup Domain을 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerGroupJpaEntity groupEntity = BannerJpaEntityFixtures.activeGroupEntity();
            BannerSlideJpaEntity slideEntity =
                    BannerJpaEntityFixtures.activeSlideEntity(1L, bannerGroupId);
            BannerSlide slide = BannerFixtures.activeBannerSlide(1L);
            BannerGroup domain = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(queryDslRepository.findBannerGroupById(bannerGroupId)).willReturn(groupEntity);
            given(queryDslRepository.findSlidesByGroupId(bannerGroupId))
                    .willReturn(List.of(slideEntity));
            given(bannerSlideMapper.toDomain(slideEntity)).willReturn(slide);
            given(bannerGroupMapper.toDomain(groupEntity, List.of(slide))).willReturn(domain);

            // when
            Optional<BannerGroup> result = queryAdapter.findById(bannerGroupId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
            then(queryDslRepository).should().findBannerGroupById(bannerGroupId);
            then(queryDslRepository).should().findSlidesByGroupId(bannerGroupId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            long nonExistentId = 999L;
            given(queryDslRepository.findBannerGroupById(nonExistentId)).willReturn(null);

            // when
            Optional<BannerGroup> result = queryAdapter.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findBannerGroupById(nonExistentId);
            then(queryDslRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("슬라이드가 없는 배너 그룹 조회 시 빈 슬라이드 목록을 가진 Domain을 반환합니다")
        void findById_WithNoSlides_ReturnsDomainWithEmptySlides() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerGroupJpaEntity groupEntity = BannerJpaEntityFixtures.activeGroupEntity();
            BannerGroup domain = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(queryDslRepository.findBannerGroupById(bannerGroupId)).willReturn(groupEntity);
            given(queryDslRepository.findSlidesByGroupId(bannerGroupId)).willReturn(List.of());
            given(bannerGroupMapper.toDomain(groupEntity, List.of())).willReturn(domain);

            // when
            Optional<BannerGroup> result = queryAdapter.findById(bannerGroupId);

            // then
            assertThat(result).isPresent();
            then(bannerSlideMapper).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("여러 슬라이드가 있는 배너 그룹 조회 시 모든 슬라이드를 포함한 Domain을 반환합니다")
        void findById_WithMultipleSlides_ReturnsDomainWithAllSlides() {
            // given
            long bannerGroupId = BannerJpaEntityFixtures.DEFAULT_GROUP_ID;
            BannerGroupJpaEntity groupEntity = BannerJpaEntityFixtures.activeGroupEntity();
            BannerSlideJpaEntity slideEntity1 =
                    BannerJpaEntityFixtures.activeSlideEntity(1L, bannerGroupId);
            BannerSlideJpaEntity slideEntity2 =
                    BannerJpaEntityFixtures.activeSlideEntity(2L, bannerGroupId);
            BannerSlide slide1 = BannerFixtures.activeBannerSlide(1L);
            BannerSlide slide2 = BannerFixtures.activeBannerSlide(2L);
            BannerGroup domain = BannerFixtures.activeBannerGroup(bannerGroupId);

            given(queryDslRepository.findBannerGroupById(bannerGroupId)).willReturn(groupEntity);
            given(queryDslRepository.findSlidesByGroupId(bannerGroupId))
                    .willReturn(List.of(slideEntity1, slideEntity2));
            given(bannerSlideMapper.toDomain(slideEntity1)).willReturn(slide1);
            given(bannerSlideMapper.toDomain(slideEntity2)).willReturn(slide2);
            given(bannerGroupMapper.toDomain(groupEntity, List.of(slide1, slide2)))
                    .willReturn(domain);

            // when
            Optional<BannerGroup> result = queryAdapter.findById(bannerGroupId);

            // then
            assertThat(result).isPresent();
            then(bannerSlideMapper).should().toDomain(slideEntity1);
            then(bannerSlideMapper).should().toDomain(slideEntity2);
            then(bannerGroupMapper).should().toDomain(groupEntity, List.of(slide1, slide2));
        }

        @Test
        @DisplayName("그룹 조회 실패 시 슬라이드 조회를 수행하지 않습니다")
        void findById_WhenGroupNotFound_DoesNotFetchSlides() {
            // given
            long bannerGroupId = 999L;
            given(queryDslRepository.findBannerGroupById(bannerGroupId)).willReturn(null);

            // when
            queryAdapter.findById(bannerGroupId);

            // then
            then(queryDslRepository).should().findBannerGroupById(bannerGroupId);
            then(bannerGroupMapper).shouldHaveNoInteractions();
            then(bannerSlideMapper).shouldHaveNoInteractions();
        }
    }

    // ========================================================================
    // 2. findByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 배너 그룹 목록을 반환합니다")
        void findByCriteria_WithConditions_ReturnsDomainList() {
            // given
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            BannerType.RECOMMEND,
                            true,
                            null,
                            null,
                            null,
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            BannerGroupJpaEntity entity1 = BannerJpaEntityFixtures.activeGroupEntity(1L);
            BannerGroupJpaEntity entity2 = BannerJpaEntityFixtures.activeGroupEntity(2L);
            BannerGroup domain1 = BannerFixtures.activeBannerGroup(1L);
            BannerGroup domain2 = BannerFixtures.activeBannerGroup(2L);

            given(
                            queryDslRepository.searchBannerGroups(
                                    "RECOMMEND",
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    criteria.offset(),
                                    criteria.size(),
                                    criteria.isNoOffset()))
                    .willReturn(List.of(entity1, entity2));
            given(bannerGroupMapper.toDomain(entity1, List.of())).willReturn(domain1);
            given(bannerGroupMapper.toDomain(entity2, List.of())).willReturn(domain2);

            // when
            List<BannerGroup> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(domain1, domain2);
            then(queryDslRepository)
                    .should()
                    .searchBannerGroups(
                            "RECOMMEND",
                            true,
                            null,
                            null,
                            null,
                            null,
                            criteria.offset(),
                            criteria.size(),
                            criteria.isNoOffset());
        }

        @Test
        @DisplayName("결과가 없으면 빈 목록을 반환합니다")
        void findByCriteria_WithNoResults_ReturnsEmptyList() {
            // given
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(
                            queryDslRepository.searchBannerGroups(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    criteria.offset(),
                                    criteria.size(),
                                    criteria.isNoOffset()))
                    .willReturn(List.of());

            // when
            List<BannerGroup> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("No-Offset 조건으로 조회 시 lastDomainId를 전달하고 isNoOffset이 true입니다")
        void findByCriteria_WithNoOffset_PassesLastDomainIdAndIsNoOffsetTrue() {
            // given
            Long lastDomainId = 50L;
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            null,
                            lastDomainId,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(
                            queryDslRepository.searchBannerGroups(
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    lastDomainId,
                                    criteria.offset(),
                                    criteria.size(),
                                    true))
                    .willReturn(List.of());

            // when
            queryAdapter.findByCriteria(criteria);

            // then
            then(queryDslRepository)
                    .should()
                    .searchBannerGroups(
                            null,
                            null,
                            null,
                            null,
                            null,
                            lastDomainId,
                            criteria.offset(),
                            criteria.size(),
                            true);
        }

        @Test
        @DisplayName("bannerType이 null이면 null로 전달합니다")
        void findByCriteria_WithNullBannerType_PassesNull() {
            // given
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            null,
                            true,
                            null,
                            null,
                            null,
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(
                            queryDslRepository.searchBannerGroups(
                                    null,
                                    true,
                                    null,
                                    null,
                                    null,
                                    null,
                                    criteria.offset(),
                                    criteria.size(),
                                    false))
                    .willReturn(List.of());

            // when
            queryAdapter.findByCriteria(criteria);

            // then
            then(queryDslRepository)
                    .should()
                    .searchBannerGroups(
                            null,
                            true,
                            null,
                            null,
                            null,
                            null,
                            criteria.offset(),
                            criteria.size(),
                            false);
        }

        @Test
        @DisplayName("전시 기간 조건으로 조회합니다")
        void findByCriteria_WithDisplayPeriodCondition_PassesDates() {
            // given
            Instant startAfter = Instant.now().minusSeconds(86400);
            Instant endBefore = Instant.now().plusSeconds(86400);
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            null,
                            null,
                            startAfter,
                            endBefore,
                            null,
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(
                            queryDslRepository.searchBannerGroups(
                                    null,
                                    null,
                                    startAfter,
                                    endBefore,
                                    null,
                                    null,
                                    criteria.offset(),
                                    criteria.size(),
                                    false))
                    .willReturn(List.of());

            // when
            queryAdapter.findByCriteria(criteria);

            // then
            then(queryDslRepository)
                    .should()
                    .searchBannerGroups(
                            null,
                            null,
                            startAfter,
                            endBefore,
                            null,
                            null,
                            criteria.offset(),
                            criteria.size(),
                            false);
        }
    }

    // ========================================================================
    // 3. countByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 배너 그룹 카운트를 반환합니다")
        void countByCriteria_WithConditions_ReturnsCount() {
            // given
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            BannerType.RECOMMEND,
                            true,
                            null,
                            null,
                            "테스트",
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(queryDslRepository.countBannerGroups("RECOMMEND", true, null, null, "테스트"))
                    .willReturn(5L);

            // when
            long count = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(5L);
            then(queryDslRepository)
                    .should()
                    .countBannerGroups("RECOMMEND", true, null, null, "테스트");
        }

        @Test
        @DisplayName("조건이 없을 때 전체 카운트를 반환합니다")
        void countByCriteria_WithNoConditions_ReturnsTotalCount() {
            // given
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(queryDslRepository.countBannerGroups(null, null, null, null, null))
                    .willReturn(10L);

            // when
            long count = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(count).isEqualTo(10L);
        }

        @Test
        @DisplayName("결과가 없으면 0을 반환합니다")
        void countByCriteria_WithNoResults_ReturnsZero() {
            // given
            BannerGroupSearchCriteria criteria =
                    BannerGroupSearchCriteria.of(
                            null,
                            null,
                            null,
                            null,
                            "존재하지않는제목",
                            null,
                            QueryContext.defaultOf(BannerGroupSortKey.CREATED_AT));

            given(queryDslRepository.countBannerGroups(null, null, null, null, "존재하지않는제목"))
                    .willReturn(0L);

            // when
            long count = queryAdapter.countByCriteria(criteria);

            // then
            assertThat(count).isZero();
        }
    }
}
