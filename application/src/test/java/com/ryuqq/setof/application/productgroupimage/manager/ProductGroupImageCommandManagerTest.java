package com.ryuqq.setof.application.productgroupimage.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroupimage.port.out.command.ProductGroupImageCommandPort;
import com.ryuqq.setof.domain.productgroupimage.aggregate.ProductGroupImage;
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
@DisplayName("ProductGroupImageCommandManager 단위 테스트")
class ProductGroupImageCommandManagerTest {

    @InjectMocks private ProductGroupImageCommandManager sut;

    @Mock private ProductGroupImageCommandPort commandPort;

    @Nested
    @DisplayName("persist() - 단일 이미지 저장")
    class PersistTest {

        @Test
        @DisplayName("이미지를 저장하고 ID를 반환한다")
        void persist_ReturnsImageId() {
            // given
            long productGroupId = 100L;
            ProductGroupImage image = ProductGroupImageFixtures.newThumbnailImage();
            Long expectedId = 1L;

            given(commandPort.persist(productGroupId, image)).willReturn(expectedId);

            // when
            Long result = sut.persist(productGroupId, image);

            // then
            assertThat(result).isEqualTo(expectedId);
            then(commandPort).should().persist(productGroupId, image);
        }
    }

    @Nested
    @DisplayName("persistAll() - 이미지 목록 저장")
    class PersistAllTest {

        @Test
        @DisplayName("이미지 목록을 저장하고 ID 목록을 반환한다")
        void persistAll_ReturnsImageIds() {
            // given
            long productGroupId = 100L;
            ProductGroupImage thumbnail = ProductGroupImageFixtures.newThumbnailImage();
            ProductGroupImage detail = ProductGroupImageFixtures.newDetailImage();
            List<ProductGroupImage> images = List.of(thumbnail, detail);

            given(commandPort.persist(productGroupId, thumbnail)).willReturn(1L);
            given(commandPort.persist(productGroupId, detail)).willReturn(2L);

            // when
            List<Long> result = sut.persistAll(productGroupId, images);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(1L, 2L);
            then(commandPort).should().persist(productGroupId, thumbnail);
            then(commandPort).should().persist(productGroupId, detail);
        }

        @Test
        @DisplayName("빈 목록을 전달하면 빈 ID 목록을 반환한다")
        void persistAll_EmptyList_ReturnsEmptyIds() {
            // given
            long productGroupId = 100L;
            List<ProductGroupImage> images = List.of();

            // when
            List<Long> result = sut.persistAll(productGroupId, images);

            // then
            assertThat(result).isEmpty();
            then(commandPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("단일 이미지 목록을 저장하고 단일 ID를 반환한다")
        void persistAll_SingleImage_ReturnsSingleId() {
            // given
            long productGroupId = 100L;
            ProductGroupImage thumbnail = ProductGroupImageFixtures.newThumbnailImage();
            List<ProductGroupImage> images = List.of(thumbnail);
            Long expectedId = 1L;

            given(commandPort.persist(productGroupId, thumbnail)).willReturn(expectedId);

            // when
            List<Long> result = sut.persistAll(productGroupId, images);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(expectedId);
        }
    }
}
