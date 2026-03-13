package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.productgroupimage.ProductGroupImageApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.RegisterProductGroupImagesApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupimage.dto.command.UpdateProductGroupImagesApiRequest;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupImageCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupImageCommandApiMapper 단위 테스트")
class ProductGroupImageCommandApiMapperTest {

    private ProductGroupImageCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupImageCommandApiMapper();
    }

    @Nested
    @DisplayName("toRegisterCommand(Long, RegisterProductGroupImagesApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 RegisterProductGroupImagesCommand로 변환한다")
        void toRegisterCommand_Success() {
            // given
            Long productGroupId = ProductGroupImageApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.registerRequest();

            // when
            RegisterProductGroupImagesCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.images()).hasSize(2);

            RegisterProductGroupImagesCommand.ImageCommand firstImage = command.images().get(0);
            RegisterProductGroupImagesApiRequest.ImageApiRequest firstRequestImage =
                    request.images().get(0);

            assertThat(firstImage.imageType()).isEqualTo(firstRequestImage.imageType());
            assertThat(firstImage.imageUrl()).isEqualTo(firstRequestImage.imageUrl());
            assertThat(firstImage.sortOrder()).isEqualTo(firstRequestImage.sortOrder());
        }

        @Test
        @DisplayName("단일 이미지 등록 요청을 Command로 변환한다")
        void toRegisterCommand_SingleImage_Success() {
            // given
            Long productGroupId = ProductGroupImageApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.registerRequest(
                            java.util.List.of(
                                    ProductGroupImageApiFixtures.registerImageRequest(
                                            "THUMBNAIL", "https://example.com/thumb.jpg", 0)));

            // when
            RegisterProductGroupImagesCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.images()).hasSize(1);
            assertThat(command.images().get(0).imageType()).isEqualTo("THUMBNAIL");
            assertThat(command.images().get(0).imageUrl())
                    .isEqualTo("https://example.com/thumb.jpg");
            assertThat(command.images().get(0).sortOrder()).isEqualTo(0);
        }

        @Test
        @DisplayName("모든 이미지 필드가 Command로 올바르게 매핑된다")
        void toRegisterCommand_AllFieldsMapped() {
            // given
            Long productGroupId = ProductGroupImageApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.registerRequest();

            // when
            RegisterProductGroupImagesCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.images()).hasSize(2);
            assertThat(command.images().get(0).imageType())
                    .isEqualTo(ProductGroupImageApiFixtures.DEFAULT_IMAGE_TYPE_THUMBNAIL);
            assertThat(command.images().get(1).imageType())
                    .isEqualTo(ProductGroupImageApiFixtures.DEFAULT_IMAGE_TYPE_DETAIL);
        }
    }

    @Nested
    @DisplayName("toUpdateCommand(Long, UpdateProductGroupImagesApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 UpdateProductGroupImagesCommand로 변환한다")
        void toUpdateCommand_Success() {
            // given
            Long productGroupId = ProductGroupImageApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.updateRequest();

            // when
            UpdateProductGroupImagesCommand command =
                    mapper.toUpdateCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.images()).hasSize(2);

            UpdateProductGroupImagesCommand.ImageCommand firstImage = command.images().get(0);
            UpdateProductGroupImagesApiRequest.ImageApiRequest firstRequestImage =
                    request.images().get(0);

            assertThat(firstImage.imageType()).isEqualTo(firstRequestImage.imageType());
            assertThat(firstImage.imageUrl()).isEqualTo(firstRequestImage.imageUrl());
            assertThat(firstImage.sortOrder()).isEqualTo(firstRequestImage.sortOrder());
        }

        @Test
        @DisplayName("수정 요청의 모든 이미지 필드가 Command로 올바르게 매핑된다")
        void toUpdateCommand_AllFieldsMapped() {
            // given
            Long productGroupId = ProductGroupImageApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductGroupImagesApiRequest request =
                    ProductGroupImageApiFixtures.updateRequest(
                            java.util.List.of(
                                    ProductGroupImageApiFixtures.updateImageRequest(
                                            "DETAIL", "https://example.com/detail.jpg", 0)));

            // when
            UpdateProductGroupImagesCommand command =
                    mapper.toUpdateCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.images()).hasSize(1);
            assertThat(command.images().get(0).imageType()).isEqualTo("DETAIL");
            assertThat(command.images().get(0).imageUrl())
                    .isEqualTo("https://example.com/detail.jpg");
            assertThat(command.images().get(0).sortOrder()).isEqualTo(0);
        }
    }
}
