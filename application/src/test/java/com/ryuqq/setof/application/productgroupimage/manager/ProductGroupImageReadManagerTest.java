package com.ryuqq.setof.application.productgroupimage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroupimage.port.out.query.ProductGroupImageQueryPort;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
import com.ryuqq.setof.domain.productgroupimage.vo.ProductGroupImages;
import com.setof.commerce.domain.productgroupimage.ProductGroupImageFixtures;
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
@DisplayName("ProductGroupImageReadManager 단위 테스트")
class ProductGroupImageReadManagerTest {

    @InjectMocks private ProductGroupImageReadManager sut;

    @Mock private ProductGroupImageQueryPort queryPort;

    @Nested
    @DisplayName("getByProductGroupId() - 상품그룹 ID로 이미지 목록 조회")
    class GetByProductGroupIdTest {

        @Test
        @DisplayName("상품그룹 ID로 활성 이미지 목록을 조회하고 ProductGroupImages를 반환한다")
        void getByProductGroupId_ReturnsProductGroupImages() {
            // given
            long productGroupId = 100L;
            List<ProductGroupImage> images =
                    List.of(
                            ProductGroupImageFixtures.persistedThumbnailImage(),
                            ProductGroupImageFixtures.persistedDetailImage());

            given(queryPort.findActiveByProductGroupId(productGroupId)).willReturn(images);

            // when
            ProductGroupImages result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(2);
            then(queryPort).should().findActiveByProductGroupId(productGroupId);
        }

        @Test
        @DisplayName("이미지가 없는 상품그룹 ID로 조회하면 빈 ProductGroupImages를 반환한다")
        void getByProductGroupId_NoImages_ReturnsEmptyProductGroupImages() {
            // given
            long productGroupId = 999L;

            given(queryPort.findActiveByProductGroupId(productGroupId)).willReturn(List.of());

            // when
            ProductGroupImages result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.isEmpty()).isTrue();
            then(queryPort).should().findActiveByProductGroupId(productGroupId);
        }

        @Test
        @DisplayName("썸네일만 있는 경우 ProductGroupImages를 반환한다")
        void getByProductGroupId_ThumbnailOnly_ReturnsProductGroupImages() {
            // given
            long productGroupId = 100L;
            List<ProductGroupImage> images =
                    List.of(ProductGroupImageFixtures.persistedThumbnailImage());

            given(queryPort.findActiveByProductGroupId(productGroupId)).willReturn(images);

            // when
            ProductGroupImages result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.toList()).hasSize(1);
            assertThat(result.thumbnail()).isNotNull();
            assertThat(result.thumbnail().isThumbnail()).isTrue();
        }
    }
}
