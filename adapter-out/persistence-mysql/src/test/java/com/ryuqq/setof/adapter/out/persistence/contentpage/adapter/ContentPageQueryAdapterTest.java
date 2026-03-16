package com.ryuqq.setof.adapter.out.persistence.contentpage.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.contentpage.mapper.ContentPageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageQueryDslRepository;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.query.ContentPageListSearchCriteria;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ContentPageQueryAdapterTest - 콘텐츠 페이지 Query Adapter 단위 테스트.
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
@DisplayName("ContentPageQueryAdapter 단위 테스트")
class ContentPageQueryAdapterTest {

    @Mock private ContentPageQueryDslRepository queryDslRepository;

    @Mock private ContentPageJpaEntityMapper mapper;

    @Mock private ContentPageListSearchCriteria listCriteria;

    @InjectMocks private ContentPageQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findOnDisplayContentPageIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("findOnDisplayContentPageIds 메서드 테스트")
    class FindOnDisplayContentPageIdsTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지 ID 목록을 Set으로 반환합니다")
        void findOnDisplayContentPageIds_ReturnsIdSet() {
            // given
            given(queryDslRepository.findOnDisplayContentPageIds()).willReturn(List.of(1L, 2L, 3L));

            // when
            Set<Long> result = queryAdapter.findOnDisplayContentPageIds();

            // then
            assertThat(result).hasSize(3);
            assertThat(result).containsExactlyInAnyOrder(1L, 2L, 3L);
            then(queryDslRepository).should().findOnDisplayContentPageIds();
        }

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지가 없으면 빈 Set을 반환합니다")
        void findOnDisplayContentPageIds_WithNoResults_ReturnsEmptySet() {
            // given
            given(queryDslRepository.findOnDisplayContentPageIds()).willReturn(List.of());

            // when
            Set<Long> result = queryAdapter.findOnDisplayContentPageIds();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("중복 ID가 있어도 Set으로 중복 제거되어 반환됩니다")
        void findOnDisplayContentPageIds_WithDuplicates_ReturnsDistinctSet() {
            // given
            given(queryDslRepository.findOnDisplayContentPageIds()).willReturn(List.of(1L, 1L, 2L));

            // when
            Set<Long> result = queryAdapter.findOnDisplayContentPageIds();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(1L, 2L);
        }
    }

    // ========================================================================
    // 2. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 ContentPage를 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            long contentPageId = 1L;
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.activeEntity();
            ContentPage domain = ContentPageFixtures.activeContentPage();

            given(queryDslRepository.fetchById(contentPageId)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<ContentPage> result = queryAdapter.findById(contentPageId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            long contentPageId = 999L;
            given(queryDslRepository.fetchById(contentPageId)).willReturn(Optional.empty());

            // when
            Optional<ContentPage> result = queryAdapter.findById(contentPageId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 3. findByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("유효한 contentPageId와 bypass=false로 활성 페이지를 반환합니다")
        void findByCriteria_WithValidIdAndNoBypass_ReturnsActivePage() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(1L, false);
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.activeEntity();
            ContentPage domain = ContentPageFixtures.activeContentPage();

            given(queryDslRepository.fetchByIdWithBypass(1L, false))
                    .willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<ContentPage> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("bypass=true이면 비활성 페이지도 반환합니다")
        void findByCriteria_WithBypassTrue_ReturnsInactivePage() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(2L, true);
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.inactiveEntity();
            ContentPage domain = ContentPageFixtures.inactiveContentPage();

            given(queryDslRepository.fetchByIdWithBypass(2L, true)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<ContentPage> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("contentPageId가 null이면 빈 Optional을 즉시 반환합니다")
        void findByCriteria_WithNullId_ReturnsEmptyWithoutRepositoryCall() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(null, false);

            // when
            Optional<ContentPage> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findByCriteria_WithNonExistingId_ReturnsEmpty() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(999L, false);
            given(queryDslRepository.fetchByIdWithBypass(999L, false)).willReturn(Optional.empty());

            // when
            Optional<ContentPage> result = queryAdapter.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 4. findAllByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("findAllByCriteria 메서드 테스트")
    class FindAllByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 콘텐츠 페이지 목록을 반환합니다")
        void findAllByCriteria_WithValidCriteria_ReturnsDomainList() {
            // given
            ContentPageJpaEntity entity1 = ContentPageJpaEntityFixtures.activeEntity(1L);
            ContentPageJpaEntity entity2 = ContentPageJpaEntityFixtures.activeEntity(2L);
            ContentPage domain1 = ContentPageFixtures.activeContentPage(1L);
            ContentPage domain2 = ContentPageFixtures.activeContentPage(2L);

            given(queryDslRepository.searchContentPages(listCriteria))
                    .willReturn(List.of(entity1, entity2));
            given(mapper.toDomain(entity1)).willReturn(domain1);
            given(mapper.toDomain(entity2)).willReturn(domain2);

            // when
            List<ContentPage> result = queryAdapter.findAllByCriteria(listCriteria);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().searchContentPages(listCriteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 리스트를 반환합니다")
        void findAllByCriteria_WithNoResults_ReturnsEmptyList() {
            // given
            given(queryDslRepository.searchContentPages(listCriteria)).willReturn(List.of());

            // when
            List<ContentPage> result = queryAdapter.findAllByCriteria(listCriteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 5. countByCriteria 테스트
    // ========================================================================

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 콘텐츠 페이지 개수를 반환합니다")
        void countByCriteria_WithValidCriteria_ReturnsCount() {
            // given
            given(queryDslRepository.countContentPages(listCriteria)).willReturn(5L);

            // when
            long result = queryAdapter.countByCriteria(listCriteria);

            // then
            assertThat(result).isEqualTo(5L);
        }

        @Test
        @DisplayName("검색 결과가 없으면 0을 반환합니다")
        void countByCriteria_WithNoResults_ReturnsZero() {
            // given
            given(queryDslRepository.countContentPages(listCriteria)).willReturn(0L);

            // when
            long result = queryAdapter.countByCriteria(listCriteria);

            // then
            assertThat(result).isZero();
        }
    }
}
