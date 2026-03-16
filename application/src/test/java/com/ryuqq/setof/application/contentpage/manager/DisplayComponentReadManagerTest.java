package com.ryuqq.setof.application.contentpage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageQueryFixtures;
import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
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
@DisplayName("DisplayComponentReadManager 단위 테스트")
class DisplayComponentReadManagerTest {

    @InjectMocks private DisplayComponentReadManager sut;

    @Mock private DisplayComponentQueryPort queryPort;

    @Nested
    @DisplayName("findByContentPage() - 디스플레이 컴포넌트 목록 조회")
    class FetchDisplayComponentsTest {

        @Test
        @DisplayName("유효한 검색 조건으로 컴포넌트 목록을 반환한다")
        void findByContentPage_ValidCriteria_ReturnsComponentList() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.defaultSearchCriteria();
            List<DisplayComponent> expected =
                    List.of(
                            ContentPageFixtures.productComponent(
                                    1L,
                                    com.ryuqq.setof.domain.contentpage.vo.OrderType.RECOMMEND,
                                    10),
                            ContentPageFixtures.textComponent(2L));

            given(queryPort.findByContentPage(criteria)).willReturn(expected);

            // when
            List<DisplayComponent> result = sut.findByContentPage(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(2);
            then(queryPort).should().findByContentPage(criteria);
        }

        @Test
        @DisplayName("컴포넌트가 없으면 빈 목록을 반환한다")
        void findByContentPage_NoComponents_ReturnsEmptyList() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.searchCriteria(999L);

            given(queryPort.findByContentPage(criteria)).willReturn(List.of());

            // when
            List<DisplayComponent> result = sut.findByContentPage(criteria);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findByContentPage(criteria);
        }

        @Test
        @DisplayName("단일 컴포넌트를 포함한 목록을 반환한다")
        void findByContentPage_SingleComponent_ReturnsSingletonList() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.searchCriteria(3L);
            DisplayComponent component = ContentPageFixtures.textComponent(3L);
            List<DisplayComponent> expected = List.of(component);

            given(queryPort.findByContentPage(criteria)).willReturn(expected);

            // when
            List<DisplayComponent> result = sut.findByContentPage(criteria);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.getFirst()).isEqualTo(component);
            then(queryPort).should().findByContentPage(criteria);
        }
    }
}
