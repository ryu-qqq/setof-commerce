package com.ryuqq.setof.application.imagevariant.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.imagevariant.manager.ImageVariantReadManager;
import com.ryuqq.setof.application.productgroupimage.manager.ProductGroupImageReadManager;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.Collections;
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
@DisplayName("ImageVariantUrlResolver 단위 테스트")
class ImageVariantUrlResolverTest {

    @InjectMocks private ImageVariantUrlResolver sut;

    @Mock private ImageVariantReadManager variantReadManager;
    @Mock private ProductGroupImageReadManager imageReadManager;

    @Nested
    @DisplayName("resolveByImageIds() - 이미지 ID 목록으로 Variant URL 해석")
    class ResolveByImageIdsTest {

        @Test
        @DisplayName("이미지 ID 목록으로 preferredType의 Variant URL을 반환한다")
        void resolveByImageIds_WithPreferredType_ReturnsMatchingVariantUrl() {
            // given
            List<Long> imageIds = List.of(100L);
            ImageVariant mediumVariant = ImageVariantFixtures.activeImageVariant();
            ImageVariantType preferredType = ImageVariantType.MEDIUM_WEBP;

            given(variantReadManager.findBySourceImageIds(imageIds))
                    .willReturn(List.of(mediumVariant));

            // when
            Map<Long, String> result = sut.resolveByImageIds(imageIds, preferredType);

            // then
            assertThat(result).containsKey(100L);
            assertThat(result.get(100L)).isEqualTo(mediumVariant.variantUrlValue());
            then(variantReadManager).should().findBySourceImageIds(imageIds);
        }

        @Test
        @DisplayName("preferredType Variant가 없으면 같은 sourceImageId의 다른 Variant를 반환한다")
        void resolveByImageIds_NoPreferredType_ReturnsFallbackVariantUrl() {
            // given
            List<Long> imageIds = List.of(100L);
            ImageVariant smallVariant = ImageVariantFixtures.activeImageVariant(1L);
            ImageVariantType preferredType = ImageVariantType.LARGE_WEBP;

            given(variantReadManager.findBySourceImageIds(imageIds))
                    .willReturn(List.of(smallVariant));

            // when
            Map<Long, String> result = sut.resolveByImageIds(imageIds, preferredType);

            // then
            assertThat(result).containsKey(100L);
            assertThat(result.get(100L)).isEqualTo(smallVariant.variantUrlValue());
        }

        @Test
        @DisplayName("imageIds가 null이면 빈 맵을 반환한다")
        void resolveByImageIds_NullImageIds_ReturnsEmptyMap() {
            // when
            Map<Long, String> result = sut.resolveByImageIds(null, ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).isEmpty();
            then(variantReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("imageIds가 빈 목록이면 빈 맵을 반환한다")
        void resolveByImageIds_EmptyImageIds_ReturnsEmptyMap() {
            // when
            Map<Long, String> result =
                    sut.resolveByImageIds(Collections.emptyList(), ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).isEmpty();
            then(variantReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("조회된 Variant가 없으면 빈 맵을 반환한다")
        void resolveByImageIds_NoVariantsFound_ReturnsEmptyMap() {
            // given
            List<Long> imageIds = List.of(100L);
            given(variantReadManager.findBySourceImageIds(imageIds))
                    .willReturn(Collections.emptyList());

            // when
            Map<Long, String> result =
                    sut.resolveByImageIds(imageIds, ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("resolveByProductGroupIds() - 상품그룹 ID 목록으로 Variant URL 해석")
    class ResolveByProductGroupIdsTest {

        @Test
        @DisplayName("상품그룹 ID 목록으로 대표 이미지의 Variant URL을 반환한다")
        void resolveByProductGroupIds_ValidIds_ReturnsVariantUrlMap() {
            // given
            List<Long> productGroupIds = List.of(10L);
            List<ProductGroupId> pgIds = List.of(ProductGroupId.of(10L));
            Map<Long, Long> thumbnailMap = Map.of(10L, 100L);

            ImageVariant mediumVariant = ImageVariantFixtures.activeImageVariant();
            List<Long> imageIds = List.of(100L);
            Map<Long, String> variantUrlMap = Map.of(100L, mediumVariant.variantUrlValue());

            given(imageReadManager.getThumbnailImageIdsByProductGroupIds(pgIds))
                    .willReturn(thumbnailMap);
            given(variantReadManager.findBySourceImageIds(imageIds))
                    .willReturn(List.of(mediumVariant));

            // when
            Map<Long, String> result =
                    sut.resolveByProductGroupIds(productGroupIds, ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).containsKey(10L);
            assertThat(result.get(10L)).isEqualTo(mediumVariant.variantUrlValue());
        }

        @Test
        @DisplayName("productGroupIds가 null이면 빈 맵을 반환한다")
        void resolveByProductGroupIds_NullIds_ReturnsEmptyMap() {
            // when
            Map<Long, String> result =
                    sut.resolveByProductGroupIds(null, ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).isEmpty();
            then(imageReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("productGroupIds가 빈 목록이면 빈 맵을 반환한다")
        void resolveByProductGroupIds_EmptyIds_ReturnsEmptyMap() {
            // when
            Map<Long, String> result =
                    sut.resolveByProductGroupIds(
                            Collections.emptyList(), ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).isEmpty();
            then(imageReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("대표 이미지 맵이 비어있으면 빈 맵을 반환한다")
        void resolveByProductGroupIds_EmptyThumbnailMap_ReturnsEmptyMap() {
            // given
            List<Long> productGroupIds = List.of(10L);
            List<ProductGroupId> pgIds = List.of(ProductGroupId.of(10L));

            given(imageReadManager.getThumbnailImageIdsByProductGroupIds(pgIds))
                    .willReturn(Collections.emptyMap());

            // when
            Map<Long, String> result =
                    sut.resolveByProductGroupIds(productGroupIds, ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).isEmpty();
            then(variantReadManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("대표 이미지에 Variant가 없으면 해당 상품그룹은 결과 맵에 포함되지 않는다")
        void resolveByProductGroupIds_NoVariantForThumbnail_ExcludesFromResult() {
            // given
            List<Long> productGroupIds = List.of(10L);
            List<ProductGroupId> pgIds = List.of(ProductGroupId.of(10L));
            Map<Long, Long> thumbnailMap = Map.of(10L, 100L);
            List<Long> imageIds = List.of(100L);

            given(imageReadManager.getThumbnailImageIdsByProductGroupIds(pgIds))
                    .willReturn(thumbnailMap);
            given(variantReadManager.findBySourceImageIds(imageIds))
                    .willReturn(Collections.emptyList());

            // when
            Map<Long, String> result =
                    sut.resolveByProductGroupIds(productGroupIds, ImageVariantType.MEDIUM_WEBP);

            // then
            assertThat(result).doesNotContainKey(10L);
        }
    }
}
