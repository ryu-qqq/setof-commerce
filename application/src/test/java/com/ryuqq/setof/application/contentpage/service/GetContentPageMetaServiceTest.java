package com.ryuqq.setof.application.contentpage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
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
@DisplayName("GetContentPageMetaService 단위 테스트")
class GetContentPageMetaServiceTest {

    @InjectMocks private GetContentPageMetaService sut;

    @Mock private ContentPageQueryManager queryManager;

    @Nested
    @DisplayName("execute() - 콘텐츠 페이지 메타 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 ID로 콘텐츠 페이지 메타 정보를 반환한다")
        void execute_ValidId_ReturnsContentPage() {
            // given
            long contentPageId = 1L;
            ContentPage expected = ContentPageFixtures.activeContentPage();

            given(queryManager.fetchContentPageMeta(contentPageId)).willReturn(expected);

            // when
            ContentPage result = sut.execute(contentPageId);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.idValue()).isEqualTo(contentPageId);
            then(queryManager).should().fetchContentPageMeta(contentPageId);
        }

        @Test
        @DisplayName("다른 ID로 해당 콘텐츠 페이지를 반환한다")
        void execute_DifferentId_ReturnsCorrespondingContentPage() {
            // given
            long contentPageId = 5L;
            ContentPage expected = ContentPageFixtures.activeContentPage(contentPageId);

            given(queryManager.fetchContentPageMeta(contentPageId)).willReturn(expected);

            // when
            ContentPage result = sut.execute(contentPageId);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryManager).should().fetchContentPageMeta(contentPageId);
        }
    }
}
