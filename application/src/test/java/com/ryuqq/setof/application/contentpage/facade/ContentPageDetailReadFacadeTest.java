package com.ryuqq.setof.application.contentpage.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.contentpage.ContentPageProductFixtures;
import com.ryuqq.setof.application.contentpage.ContentPageQueryFixtures;
import com.ryuqq.setof.application.contentpage.dto.ContentPageDetailResult;
import com.ryuqq.setof.application.contentpage.manager.ComponentProductReadFacade;
import com.ryuqq.setof.application.contentpage.manager.ContentPageQueryManager;
import com.ryuqq.setof.application.contentpage.manager.DisplayComponentReadManager;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import com.ryuqq.setof.domain.contentpage.vo.OrderType;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.setof.commerce.domain.contentpage.ContentPageFixtures;
import java.util.List;
import java.util.Map;
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
@DisplayName("ContentPageDetailReadFacade 단위 테스트")
class ContentPageDetailReadFacadeTest {

    @InjectMocks private ContentPageDetailReadFacade sut;

    @Mock private ContentPageQueryManager contentPageQueryManager;
    @Mock private DisplayComponentReadManager displayComponentReadManager;
    @Mock private ComponentProductReadFacade componentProductReadFacade;

    @Nested
    @DisplayName("getContentPageDetail() - 콘텐츠 페이지 상세 조회")
    class GetContentPageDetailTest {

        @Test
        @DisplayName("메타 + 컴포넌트 + 상품 번들을 조합하여 상세 결과를 반환한다")
        void getContentPageDetail_ValidCriteria_ReturnsDetailResult() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.defaultSearchCriteria();
            ContentPage contentPage = ContentPageFixtures.activeContentPage();
            List<DisplayComponent> components =
                    List.of(ContentPageFixtures.productComponent(1L, OrderType.RECOMMEND, 10));
            ComponentProductBundle productBundle =
                    new ComponentProductBundle(
                            Map.of(1L, ContentPageProductFixtures.productSnapshots(101L, 102L)),
                            Map.of());

            given(contentPageQueryManager.findByCriteriaOrThrow(criteria)).willReturn(contentPage);
            given(displayComponentReadManager.findByContentPage(criteria)).willReturn(components);
            given(componentProductReadFacade.fetchComponentProducts(components))
                    .willReturn(productBundle);

            // when
            ContentPageDetailResult result = sut.getContentPageDetail(criteria);

            // then
            assertThat(result).isNotNull();
            assertThat(result.contentPage()).isEqualTo(contentPage);
            assertThat(result.displayComponents()).isEqualTo(components);
            assertThat(result.productBundle()).isEqualTo(productBundle);
            then(contentPageQueryManager).should().findByCriteriaOrThrow(criteria);
            then(displayComponentReadManager).should().findByContentPage(criteria);
            then(componentProductReadFacade).should().fetchComponentProducts(components);
        }

        @Test
        @DisplayName("컴포넌트가 없는 경우 빈 목록과 빈 번들로 결과를 반환한다")
        void getContentPageDetail_NoComponents_ReturnsResultWithEmptyBundle() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.searchCriteria(2L);
            ContentPage contentPage = ContentPageFixtures.activeContentPage(2L);
            List<DisplayComponent> emptyComponents = List.of();
            ComponentProductBundle emptyBundle = ComponentProductBundle.empty();

            given(contentPageQueryManager.findByCriteriaOrThrow(criteria)).willReturn(contentPage);
            given(displayComponentReadManager.findByContentPage(criteria))
                    .willReturn(emptyComponents);
            given(componentProductReadFacade.fetchComponentProducts(emptyComponents))
                    .willReturn(emptyBundle);

            // when
            ContentPageDetailResult result = sut.getContentPageDetail(criteria);

            // then
            assertThat(result.contentPage()).isEqualTo(contentPage);
            assertThat(result.displayComponents()).isEmpty();
            assertThat(result.productBundle().componentProducts()).isEmpty();
            assertThat(result.productBundle().tabProducts()).isEmpty();
            then(contentPageQueryManager).should().findByCriteriaOrThrow(criteria);
            then(displayComponentReadManager).should().findByContentPage(criteria);
            then(componentProductReadFacade).should().fetchComponentProducts(emptyComponents);
        }

        @Test
        @DisplayName("TAB 컴포넌트가 있는 경우 tabProducts가 포함된 번들로 결과를 반환한다")
        void getContentPageDetail_TabComponents_ReturnsResultWithTabProducts() {
            // given
            ContentPageSearchCriteria criteria = ContentPageQueryFixtures.searchCriteria(3L);
            ContentPage contentPage = ContentPageFixtures.activeContentPage(3L);
            List<DisplayComponent> components =
                    List.of(ContentPageProductFixtures.tabComponent(10L, 100L));

            List<ProductThumbnailSnapshot> tabSnapshots =
                    ContentPageProductFixtures.productSnapshots(301L, 302L);
            ComponentProductBundle productBundle =
                    new ComponentProductBundle(Map.of(), Map.of(100L, tabSnapshots));

            given(contentPageQueryManager.findByCriteriaOrThrow(criteria)).willReturn(contentPage);
            given(displayComponentReadManager.findByContentPage(criteria)).willReturn(components);
            given(componentProductReadFacade.fetchComponentProducts(components))
                    .willReturn(productBundle);

            // when
            ContentPageDetailResult result = sut.getContentPageDetail(criteria);

            // then
            assertThat(result.productBundle().componentProducts()).isEmpty();
            assertThat(result.productBundle().tabProducts()).containsKey(100L);
            then(componentProductReadFacade).should().fetchComponentProducts(components);
        }
    }
}
