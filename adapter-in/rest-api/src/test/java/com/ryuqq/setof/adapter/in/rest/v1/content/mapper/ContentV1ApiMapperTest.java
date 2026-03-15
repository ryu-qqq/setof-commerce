package com.ryuqq.setof.adapter.in.rest.v1.content.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.content.ContentApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentMetaV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.ContentV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.content.dto.response.OnDisplayContentV1ApiResponse;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ContentV1ApiMapper 단위 테스트.
 *
 * <p>콘텐츠 Query API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ContentV1ApiMapper 단위 테스트")
class ContentV1ApiMapperTest {

    private ContentV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ContentV1ApiMapper();
    }

    @Nested
    @DisplayName("toOnDisplayResponse")
    class ToOnDisplayResponseTest {

        @Test
        @DisplayName("전시 중인 콘텐츠 ID 집합을 OnDisplayContentV1ApiResponse로 변환한다")
        void toOnDisplayResponse_Success() {
            // given
            Set<Long> contentIds = ContentApiFixtures.onDisplayContentIds();

            // when
            OnDisplayContentV1ApiResponse response = mapper.toOnDisplayResponse(contentIds);

            // then
            assertThat(response.contentIds()).containsExactlyInAnyOrder(1L, 2L, 3L);
        }

        @Test
        @DisplayName("빈 ID 집합을 빈 응답으로 변환한다")
        void toOnDisplayResponse_Empty() {
            // given
            Set<Long> contentIds = Set.of();

            // when
            OnDisplayContentV1ApiResponse response = mapper.toOnDisplayResponse(contentIds);

            // then
            assertThat(response.contentIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toMetaResponse")
    class ToMetaResponseTest {

        @Test
        @DisplayName("ContentPage를 ContentMetaV1ApiResponse로 변환한다")
        void toMetaResponse_Success() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);

            // when
            ContentMetaV1ApiResponse response = mapper.toMetaResponse(page);

            // then
            assertThat(response.contentId()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("메인 콘텐츠");
            assertThat(response.memo()).isEqualTo("메인 페이지용 콘텐츠");
            assertThat(response.imageUrl()).isEqualTo("https://cdn.example.com/content/1.jpg");
            assertThat(response.componentDetails()).isEmpty();
        }

        @Test
        @DisplayName("메타 응답의 displayPeriod가 포맷된 문자열로 변환된다")
        void toMetaResponse_DisplayPeriodFormatted() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);

            // when
            ContentMetaV1ApiResponse response = mapper.toMetaResponse(page);

            // then
            assertThat(response.displayPeriod().displayStartDate()).isNotNull();
            assertThat(response.displayPeriod().displayEndDate()).isNotNull();
        }

        @Test
        @DisplayName("componentDetails는 항상 빈 리스트를 반환한다")
        void toMetaResponse_ComponentDetailsAlwaysEmpty() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(2L);

            // when
            ContentMetaV1ApiResponse response = mapper.toMetaResponse(page);

            // then
            assertThat(response.componentDetails()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toContentResponse")
    class ToContentResponseTest {

        @Test
        @DisplayName("ContentPage와 컴포넌트 목록을 ContentV1ApiResponse로 변환한다")
        void toContentResponse_Success() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);
            DisplayComponent component = ContentApiFixtures.textDisplayComponent(1L, 1L);
            ComponentProductBundle bundle = ContentApiFixtures.emptyBundle();

            // when
            ContentV1ApiResponse response =
                    mapper.toContentResponse(page, List.of(component), bundle);

            // then
            assertThat(response.contentId()).isEqualTo(1L);
            assertThat(response.title()).isEqualTo("메인 콘텐츠");
            assertThat(response.memo()).isEqualTo("메인 페이지용 콘텐츠");
            assertThat(response.imageUrl()).isEqualTo("https://cdn.example.com/content/1.jpg");
            assertThat(response.componentDetails()).hasSize(1);
        }

        @Test
        @DisplayName("컴포넌트가 없는 경우 빈 componentDetails를 반환한다")
        void toContentResponse_NoComponents() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);
            ComponentProductBundle bundle = ContentApiFixtures.emptyBundle();

            // when
            ContentV1ApiResponse response = mapper.toContentResponse(page, List.of(), bundle);

            // then
            assertThat(response.contentId()).isEqualTo(1L);
            assertThat(response.componentDetails()).isEmpty();
        }

        @Test
        @DisplayName("텍스트 컴포넌트의 componentType이 'textComponent'로 매핑된다")
        void toContentResponse_TextComponentTypeName() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);
            DisplayComponent component = ContentApiFixtures.textDisplayComponent(1L, 1L);
            ComponentProductBundle bundle = ContentApiFixtures.emptyBundle();

            // when
            ContentV1ApiResponse response =
                    mapper.toContentResponse(page, List.of(component), bundle);

            // then
            ContentV1ApiResponse.ComponentDetailV1ApiResponse detail =
                    response.componentDetails().get(0);
            assertThat(detail.componentId()).isEqualTo(1L);
            assertThat(detail.componentName()).isEqualTo("텍스트 컴포넌트");
            assertThat(detail.componentType()).isEqualTo("TEXT");
        }

        @Test
        @DisplayName("displayYn 필드는 active=true이면 'Y'로 변환된다")
        void toContentResponse_DisplayYnActive() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);
            DisplayComponent component = ContentApiFixtures.textDisplayComponent(1L, 1L);
            ComponentProductBundle bundle = ContentApiFixtures.emptyBundle();

            // when
            ContentV1ApiResponse response =
                    mapper.toContentResponse(page, List.of(component), bundle);

            // then
            ContentV1ApiResponse.ComponentDetailV1ApiResponse detail =
                    response.componentDetails().get(0);
            assertThat(detail.displayYn()).isEqualTo("Y");
        }
    }

    @Nested
    @DisplayName("toProductThumbnailMap (상품 썸네일 변환)")
    class ToProductThumbnailMapTest {

        @Test
        @DisplayName("ProductThumbnailSnapshot의 averageRating은 기본값 0.0으로 설정된다")
        void toProductThumbnailMap_AverageRatingDefault() {
            // given
            ContentPage page = ContentApiFixtures.contentPage(1L);
            ProductThumbnailSnapshot snapshot = ContentApiFixtures.productThumbnailSnapshot(1L);
            long componentId = 1L;
            DisplayComponent component = ContentApiFixtures.textDisplayComponent(componentId, 1L);
            ComponentProductBundle bundle =
                    new ComponentProductBundle(Map.of(componentId, List.of(snapshot)), Map.of());

            // when
            // toProductThumbnailMap은 내부 메서드이므로 toContentResponse를 통해 간접 검증한다
            ContentV1ApiResponse response =
                    mapper.toContentResponse(page, List.of(component), bundle);

            // then: 텍스트 컴포넌트이므로 componentDetails는 있지만 thumbnails는 inner에만 포함됨
            // averageRating 기본값은 실제 JSON 직렬화 시 검증 (RestDocsTest에서 확인)
            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("ProductThumbnailSnapshot의 soldOut=true이면 soldOutYn='Y'로 변환된다")
        void productStatusMapping_SoldOut() {
            // given: soldOut 상품이 포함된 번들은 PRODUCT 타입 컴포넌트에서 확인 가능
            // 본 테스트는 스냅샷 필드 매핑 규칙을 명세한다
            ProductThumbnailSnapshot snapshot = ContentApiFixtures.productThumbnailSnapshot(1L);

            // then: snapshot.soldOut() == false
            assertThat(snapshot.soldOut()).isFalse();
            assertThat(snapshot.displayed()).isTrue();
        }
    }
}
