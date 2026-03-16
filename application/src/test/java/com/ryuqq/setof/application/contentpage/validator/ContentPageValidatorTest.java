package com.ryuqq.setof.application.contentpage.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.exception.ContentPageNotFoundException;
import com.ryuqq.setof.domain.contentpage.id.ContentPageId;
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
@DisplayName("ContentPageValidator 단위 테스트")
class ContentPageValidatorTest {

    @InjectMocks private ContentPageValidator sut;

    @Mock private ContentPageQueryManager contentPageQueryManager;

    @Nested
    @DisplayName("findExistingOrThrow() - 콘텐츠 페이지 조회 또는 예외 발생")
    class FindExistingOrThrowTest {

        @Test
        @DisplayName("존재하는 ID로 ContentPage를 반환한다")
        void findExistingOrThrow_ExistingId_ReturnsContentPage() {
            // given
            ContentPageId id = ContentPageFixtures.defaultContentPageId();
            ContentPage expected = ContentPageFixtures.activeContentPage();

            given(contentPageQueryManager.findByIdOrThrow(id.value())).willReturn(expected);

            // when
            ContentPage result = sut.findExistingOrThrow(id);

            // then
            assertThat(result).isEqualTo(expected);
            then(contentPageQueryManager).should().findByIdOrThrow(id.value());
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ContentPageNotFoundException이 발생한다")
        void findExistingOrThrow_NonExistingId_ThrowsException() {
            // given
            ContentPageId id = ContentPageFixtures.contentPageId(999L);

            given(contentPageQueryManager.findByIdOrThrow(id.value()))
                    .willThrow(new ContentPageNotFoundException(String.valueOf(id.value())));

            // when & then
            assertThatThrownBy(() -> sut.findExistingOrThrow(id))
                    .isInstanceOf(ContentPageNotFoundException.class);

            then(contentPageQueryManager).should().findByIdOrThrow(id.value());
        }

        @Test
        @DisplayName("비활성 상태인 ContentPage도 정상 반환한다")
        void findExistingOrThrow_InactiveContentPage_ReturnsContentPage() {
            // given
            ContentPageId id = ContentPageFixtures.contentPageId(2L);
            ContentPage inactivePage = ContentPageFixtures.inactiveContentPage();

            given(contentPageQueryManager.findByIdOrThrow(id.value())).willReturn(inactivePage);

            // when
            ContentPage result = sut.findExistingOrThrow(id);

            // then
            assertThat(result).isEqualTo(inactivePage);
            then(contentPageQueryManager).should().findByIdOrThrow(id.value());
        }
    }
}
