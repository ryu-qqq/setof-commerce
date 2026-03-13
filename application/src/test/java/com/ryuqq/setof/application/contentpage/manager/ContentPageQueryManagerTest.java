package com.ryuqq.setof.application.contentpage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageQueryFixtures;
import com.ryuqq.setof.application.contentpage.port.out.ContentPageQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.exception.ContentPageNotFoundException;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
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

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ContentPageQueryManager 단위 테스트")
class ContentPageQueryManagerTest {

    @InjectMocks private ContentPageQueryManager sut;

    @Mock private ContentPageQueryPort queryPort;

    @Nested
    @DisplayName("fetchOnDisplayContentPageIds() - 전시 중인 ID 목록 조회")
    class FetchOnDisplayContentPageIdsTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지 ID Set을 반환한다")
        void fetchOnDisplayContentPageIds_ReturnsIdSet() {
            // given
            Set<Long> expected = Set.of(1L, 2L, 5L);

            given(queryPort.fetchOnDisplayContentPageIds()).willReturn(expected);

            // when
            Set<Long> result = sut.fetchOnDisplayContentPageIds();

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchOnDisplayContentPageIds();
        }

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지가 없으면 빈 Set을 반환한다")
        void fetchOnDisplayContentPageIds_NoneDisplayed_ReturnsEmptySet() {
            // given
            given(queryPort.fetchOnDisplayContentPageIds()).willReturn(Set.of());

            // when
            Set<Long> result = sut.fetchOnDisplayContentPageIds();

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().fetchOnDisplayContentPageIds();
        }
    }

    @Nested
    @DisplayName("fetchContentPageMeta() - 메타 조회")
    class FetchContentPageMetaTest {

        @Test
        @DisplayName("존재하는 ID로 메타 정보를 반환한다")
        void fetchContentPageMeta_ExistingId_ReturnsContentPage() {
            // given
            long contentPageId = 1L;
            ContentPage expected = ContentPageFixtures.activeContentPage();

            given(queryPort.fetchContentPageMeta(contentPageId)).willReturn(Optional.of(expected));

            // when
            ContentPage result = sut.fetchContentPageMeta(contentPageId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchContentPageMeta(contentPageId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ContentPageNotFoundException이 발생한다")
        void fetchContentPageMeta_NonExistingId_ThrowsException() {
            // given
            long contentPageId = 999L;

            given(queryPort.fetchContentPageMeta(contentPageId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.fetchContentPageMeta(contentPageId))
                    .isInstanceOf(ContentPageNotFoundException.class);
            then(queryPort).should().fetchContentPageMeta(contentPageId);
        }
    }

    @Nested
    @DisplayName("fetchContentPage() - 콘텐츠 페이지 상세 조회")
    class FetchContentPageTest {

        @Test
        @DisplayName("유효한 검색 조건으로 콘텐츠 페이지를 반환한다")
        void fetchContentPage_ValidCriteria_ReturnsContentPage() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.defaultSearchCriteria();
            ContentPage expected = ContentPageFixtures.activeContentPage();

            given(queryPort.fetchContentPage(criteria)).willReturn(Optional.of(expected));

            // when
            ContentPage result = sut.fetchContentPage(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchContentPage(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 ContentPageNotFoundException이 발생한다")
        void fetchContentPage_NoResult_ThrowsException() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.searchCriteria(999L);

            given(queryPort.fetchContentPage(criteria)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.fetchContentPage(criteria))
                    .isInstanceOf(ContentPageNotFoundException.class);
            then(queryPort).should().fetchContentPage(criteria);
        }

        @Test
        @DisplayName("bypass 옵션이 있는 조건으로도 콘텐츠 페이지를 반환한다")
        void fetchContentPage_CriteriaWithBypass_ReturnsContentPage() {
            // given
            ContentPageSearchCriteria criteria =
                    ContentPageQueryFixtures.searchCriteriaWithBypass(2L);
            ContentPage expected = ContentPageFixtures.activeContentPage(2L);

            given(queryPort.fetchContentPage(criteria)).willReturn(Optional.of(expected));

            // when
            ContentPage result = sut.fetchContentPage(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().fetchContentPage(criteria);
        }
    }
}
