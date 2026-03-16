package com.ryuqq.setof.application.contentpage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.contentpage.port.out.ContentPageCommandPort;
import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentCommandPort;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.BadgeType;
import com.ryuqq.setof.domain.contentpage.vo.ComponentType;
import com.ryuqq.setof.domain.contentpage.vo.DisplayComponentDiff;
import com.ryuqq.setof.domain.contentpage.vo.DisplayConfig;
import com.ryuqq.setof.domain.contentpage.vo.ListType;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.time.Instant;
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
@DisplayName("ContentPageCommandManager 단위 테스트")
class ContentPageCommandManagerTest {

    @InjectMocks private ContentPageCommandManager sut;

    @Mock private ContentPageCommandPort contentPageCommandPort;
    @Mock private DisplayComponentCommandPort displayComponentCommandPort;

    @Nested
    @DisplayName("persist() - 콘텐츠 페이지 저장")
    class PersistTest {

        @Test
        @DisplayName("콘텐츠 페이지를 저장하고 생성된 ID를 반환한다")
        void persist_ValidContentPage_ReturnsContentPageId() {
            // given
            ContentPage contentPage = ContentPageFixtures.newContentPage();
            Long expectedId = 1L;

            given(contentPageCommandPort.persist(contentPage)).willReturn(expectedId);

            // when
            Long result = sut.persist(contentPage);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(contentPageCommandPort).should().persist(contentPage);
        }
    }

    @Nested
    @DisplayName("persistComponents() - 컴포넌트 목록 신규 저장")
    class PersistComponentsTest {

        @Test
        @DisplayName("비어 있지 않은 컴포넌트 목록을 저장한다")
        void persistComponents_NonEmptyList_PersistsAll() {
            // given
            List<DisplayComponent> components =
                    List.of(ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 10));

            willDoNothing().given(displayComponentCommandPort).persistAll(components);

            // when
            sut.persistComponents(components);

            // then
            then(displayComponentCommandPort).should().persistAll(components);
        }

        @Test
        @DisplayName("빈 컴포넌트 목록은 저장하지 않는다")
        void persistComponents_EmptyList_SkipsPersist() {
            // given
            List<DisplayComponent> emptyComponents = List.of();

            // when
            sut.persistComponents(emptyComponents);

            // then
            then(displayComponentCommandPort).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("persistComponentDiff() - 컴포넌트 Diff 영속")
    class PersistComponentDiffTest {

        @Test
        @DisplayName("추가된 컴포넌트가 있으면 persistAll을 호출한다")
        void persistComponentDiff_WithAddedComponents_CallsPersistAll() {
            // given
            DisplayComponent addedComponent =
                    DisplayComponent.forNew(
                            1L,
                            "신규 컴포넌트",
                            1,
                            ComponentType.PRODUCT,
                            new DisplayConfig(ListType.NONE, OrderType.NONE, BadgeType.NONE, false),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            Instant.now());
            DisplayComponentDiff diff =
                    new DisplayComponentDiff(
                            List.of(addedComponent), List.of(), List.of(), Instant.now());

            willDoNothing().given(displayComponentCommandPort).persistAll(diff.added());

            // when
            sut.persistComponentDiff(diff);

            // then
            then(displayComponentCommandPort).should().persistAll(diff.added());
            then(displayComponentCommandPort).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("dirty 컴포넌트가 있으면 updateAll을 호출한다")
        void persistComponentDiff_WithDirtyComponents_CallsUpdateAll() {
            // given
            DisplayComponent retainedComponent =
                    ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 20);
            DisplayComponentDiff diff =
                    new DisplayComponentDiff(
                            List.of(), List.of(), List.of(retainedComponent), Instant.now());

            willDoNothing().given(displayComponentCommandPort).updateAll(diff.allDirtyComponents());

            // when
            sut.persistComponentDiff(diff);

            // then
            then(displayComponentCommandPort).should().updateAll(diff.allDirtyComponents());
            then(displayComponentCommandPort).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("추가 + dirty 컴포넌트가 모두 있으면 persistAll과 updateAll을 모두 호출한다")
        void persistComponentDiff_WithBothAddedAndDirty_CallsBothPorts() {
            // given
            DisplayComponent addedComponent =
                    DisplayComponent.forNew(
                            1L,
                            "신규 컴포넌트",
                            1,
                            ComponentType.PRODUCT,
                            new DisplayConfig(ListType.NONE, OrderType.NONE, BadgeType.NONE, false),
                            ContentPageFixtures.alwaysPeriod(),
                            true,
                            null,
                            null,
                            Instant.now());
            DisplayComponent retainedComponent =
                    ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 20);
            DisplayComponentDiff diff =
                    new DisplayComponentDiff(
                            List.of(addedComponent),
                            List.of(),
                            List.of(retainedComponent),
                            Instant.now());

            willDoNothing().given(displayComponentCommandPort).persistAll(diff.added());
            willDoNothing().given(displayComponentCommandPort).updateAll(diff.allDirtyComponents());

            // when
            sut.persistComponentDiff(diff);

            // then
            then(displayComponentCommandPort).should().persistAll(diff.added());
            then(displayComponentCommandPort).should().updateAll(diff.allDirtyComponents());
        }

        @Test
        @DisplayName("변경이 없는 Diff는 포트를 호출하지 않는다")
        void persistComponentDiff_NoDirtyComponents_CallsNoPorts() {
            // given
            DisplayComponentDiff diff = DisplayComponentDiff.empty();

            // when
            sut.persistComponentDiff(diff);

            // then
            then(displayComponentCommandPort).shouldHaveNoInteractions();
        }
    }
}
