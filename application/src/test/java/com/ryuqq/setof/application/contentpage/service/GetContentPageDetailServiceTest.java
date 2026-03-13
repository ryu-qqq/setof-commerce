package com.ryuqq.setof.application.contentpage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageQueryFixtures;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.facade.ContentPageDetailReadFacade;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
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
@DisplayName("GetContentPageDetailService 단위 테스트")
class GetContentPageDetailServiceTest {

    @InjectMocks private GetContentPageDetailService sut;

    @Mock private ContentPageDetailReadFacade readFacade;

    @Nested
    @DisplayName("execute() - 콘텐츠 페이지 상세 조회")
    class ExecuteTest {

        @Test
        @DisplayName("유효한 검색 조건으로 콘텐츠 페이지 상세 결과를 반환한다")
        void execute_ValidCriteria_ReturnsContentPageDetailResult() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.defaultSearchCriteria();
            ContentPageDetailResult expected =
                    new ContentPageDetailResult(
                            ContentPageFixtures.activeContentPage(),
                            List.of(ContentPageFixtures.textComponent(1L)),
                            ComponentProductBundle.empty());

            given(readFacade.getContentPageDetail(criteria)).willReturn(expected);

            // when
            ContentPageDetailResult result = sut.execute(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.contentPage()).isNotNull();
            then(readFacade).should().getContentPageDetail(criteria);
        }

        @Test
        @DisplayName("bypass 옵션이 있는 검색 조건으로도 결과를 반환한다")
        void execute_CriteriaWithBypass_ReturnsContentPageDetailResult() {
            // given
            ContentPageSearchCriteria criteria =
                    ContentPageQueryFixtures.searchCriteriaWithBypass(2L);
            ContentPageDetailResult expected =
                    new ContentPageDetailResult(
                            ContentPageFixtures.activeContentPage(2L),
                            List.of(),
                            ComponentProductBundle.empty());

            given(readFacade.getContentPageDetail(criteria)).willReturn(expected);

            // when
            ContentPageDetailResult result = sut.execute(criteria);

            // then
            assertThat(result).isEqualTo(expected);
            then(readFacade).should().getContentPageDetail(criteria);
        }
    }
}
