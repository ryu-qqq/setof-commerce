package com.ryuqq.setof.application.contentpage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
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
@DisplayName("GetOnDisplayContentPageIdsService 단위 테스트")
class GetOnDisplayContentPageIdsServiceTest {

    @InjectMocks private GetOnDisplayContentPageIdsService sut;

    @Mock private ContentPageQueryManager queryManager;

    @Nested
    @DisplayName("execute() - 전시 중인 콘텐츠 페이지 ID 목록 조회")
    class ExecuteTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지 ID Set을 반환한다")
        void execute_ReturnsOnDisplayContentPageIds() {
            // given
            Set<Long> expected = Set.of(1L, 2L, 3L);

            given(queryManager.fetchOnDisplayContentPageIds()).willReturn(expected);

            // when
            Set<Long> result = sut.execute();

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result).hasSize(3);
            then(queryManager).should().fetchOnDisplayContentPageIds();
        }

        @Test
        @DisplayName("전시 중인 콘텐츠 페이지가 없으면 빈 Set을 반환한다")
        void execute_NoOnDisplayPages_ReturnsEmptySet() {
            // given
            given(queryManager.fetchOnDisplayContentPageIds()).willReturn(Set.of());

            // when
            Set<Long> result = sut.execute();

            // then
            assertThat(result).isEmpty();
            then(queryManager).should().fetchOnDisplayContentPageIds();
        }

        @Test
        @DisplayName("단일 ID가 있는 경우 단일 요소 Set을 반환한다")
        void execute_SingleOnDisplayPage_ReturnsSingletonSet() {
            // given
            Set<Long> expected = Set.of(10L);

            given(queryManager.fetchOnDisplayContentPageIds()).willReturn(expected);

            // when
            Set<Long> result = sut.execute();

            // then
            assertThat(result).containsExactly(10L);
            then(queryManager).should().fetchOnDisplayContentPageIds();
        }
    }
}
