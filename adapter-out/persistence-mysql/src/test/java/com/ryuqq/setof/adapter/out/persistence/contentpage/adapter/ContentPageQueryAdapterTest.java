package com.ryuqq.setof.adapter.out.persistence.contentpage.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.adapter.out.persistence.contentpage.ContentPageJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.contentpage.entity.ContentPageJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.contentpage.mapper.ContentPageJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.contentpage.repository.ContentPageQueryDslRepository;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
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

    @InjectMocks private ContentPageQueryAdapter queryAdapter;

    // ========================================================================
    // 1. fetchOnDisplayContentPageIds 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchOnDisplayContentPageIds 메서드 테스트")
    class FetchOnDisplayContentPageIdsTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지 ID 목록을 Set으로 반환합니다")
        void fetchOnDisplayContentPageIds_ReturnsIdSet() {
            // given
            given(queryDslRepository.fetchOnDisplayContentPageIds())
                    .willReturn(List.of(1L, 2L, 3L));

            // when
            Set<Long> result = queryAdapter.fetchOnDisplayContentPageIds();

            // then
            assertThat(result).hasSize(3);
            assertThat(result).containsExactlyInAnyOrder(1L, 2L, 3L);
            then(queryDslRepository).should().fetchOnDisplayContentPageIds();
        }

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지가 없으면 빈 Set을 반환합니다")
        void fetchOnDisplayContentPageIds_WithNoResults_ReturnsEmptySet() {
            // given
            given(queryDslRepository.fetchOnDisplayContentPageIds()).willReturn(List.of());

            // when
            Set<Long> result = queryAdapter.fetchOnDisplayContentPageIds();

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("중복 ID가 있어도 Set으로 중복 제거되어 반환됩니다")
        void fetchOnDisplayContentPageIds_WithDuplicates_ReturnsDistinctSet() {
            // given
            given(queryDslRepository.fetchOnDisplayContentPageIds())
                    .willReturn(List.of(1L, 1L, 2L));

            // when
            Set<Long> result = queryAdapter.fetchOnDisplayContentPageIds();

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(1L, 2L);
        }
    }

    // ========================================================================
    // 2. fetchContentPageMeta 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchContentPageMeta 메서드 테스트")
    class FetchContentPageMetaTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 ContentPage를 반환합니다")
        void fetchContentPageMeta_WithExistingId_ReturnsDomain() {
            // given
            long contentPageId = 1L;
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.activeEntity();
            ContentPage domain = ContentPageFixtures.activeContentPage();

            given(queryDslRepository.fetchById(contentPageId)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<ContentPage> result = queryAdapter.fetchContentPageMeta(contentPageId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void fetchContentPageMeta_WithNonExistingId_ReturnsEmpty() {
            // given
            long contentPageId = 999L;
            given(queryDslRepository.fetchById(contentPageId)).willReturn(Optional.empty());

            // when
            Optional<ContentPage> result = queryAdapter.fetchContentPageMeta(contentPageId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 3. fetchContentPage 테스트
    // ========================================================================

    @Nested
    @DisplayName("fetchContentPage 메서드 테스트")
    class FetchContentPageTest {

        @Test
        @DisplayName("유효한 contentPageId와 bypass=false로 활성 페이지를 반환합니다")
        void fetchContentPage_WithValidIdAndNoBypass_ReturnsActivePage() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(1L, false);
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.activeEntity();
            ContentPage domain = ContentPageFixtures.activeContentPage();

            given(queryDslRepository.fetchByIdWithBypass(1L, false))
                    .willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<ContentPage> result = queryAdapter.fetchContentPage(criteria);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("bypass=true이면 비활성 페이지도 반환합니다")
        void fetchContentPage_WithBypassTrue_ReturnsInactivePage() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(2L, true);
            ContentPageJpaEntity entity = ContentPageJpaEntityFixtures.inactiveEntity();
            ContentPage domain = ContentPageFixtures.inactiveContentPage();

            given(queryDslRepository.fetchByIdWithBypass(2L, true)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<ContentPage> result = queryAdapter.fetchContentPage(criteria);

            // then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("contentPageId가 null이면 빈 Optional을 즉시 반환합니다")
        void fetchContentPage_WithNullId_ReturnsEmptyWithoutRepositoryCall() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(null, false);

            // when
            Optional<ContentPage> result = queryAdapter.fetchContentPage(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void fetchContentPage_WithNonExistingId_ReturnsEmpty() {
            // given
            ContentPageSearchCriteria criteria = new ContentPageSearchCriteria(999L, false);
            given(queryDslRepository.fetchByIdWithBypass(999L, false)).willReturn(Optional.empty());

            // when
            Optional<ContentPage> result = queryAdapter.fetchContentPage(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }
}
