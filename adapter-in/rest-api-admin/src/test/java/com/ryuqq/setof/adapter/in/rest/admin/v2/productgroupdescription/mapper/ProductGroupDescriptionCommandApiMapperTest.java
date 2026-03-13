package com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.productgroupdescription.ProductGroupDescriptionApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.RegisterProductGroupDescriptionApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productgroupdescription.dto.command.UpdateProductGroupDescriptionApiRequest;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ProductGroupDescriptionCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ProductGroupDescriptionCommandApiMapper 단위 테스트")
class ProductGroupDescriptionCommandApiMapperTest {

    private ProductGroupDescriptionCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductGroupDescriptionCommandApiMapper();
    }

    @Nested
    @DisplayName("toRegisterCommand(Long, RegisterProductGroupDescriptionApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 RegisterProductGroupDescriptionCommand로 변환한다")
        void toRegisterCommand_Success() {
            // given
            Long productGroupId = ProductGroupDescriptionApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.registerRequest();

            // when
            RegisterProductGroupDescriptionCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.content()).isEqualTo(request.content());
            assertThat(command.descriptionImages()).hasSize(1);

            RegisterProductGroupDescriptionCommand.DescriptionImageCommand imageCommand =
                    command.descriptionImages().get(0);
            RegisterProductGroupDescriptionApiRequest.DescriptionImageApiRequest requestImage =
                    request.descriptionImages().get(0);

            assertThat(imageCommand.imageUrl()).isEqualTo(requestImage.imageUrl());
            assertThat(imageCommand.sortOrder()).isEqualTo(requestImage.sortOrder());
        }

        @Test
        @DisplayName("이미지 목록이 null인 경우 빈 목록으로 변환한다")
        void toRegisterCommand_NullImages_ReturnsEmptyList() {
            // given
            Long productGroupId = ProductGroupDescriptionApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.registerRequestWithoutImages();

            // when
            RegisterProductGroupDescriptionCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.content()).isEqualTo(request.content());
            assertThat(command.descriptionImages()).isEmpty();
        }

        @Test
        @DisplayName("여러 이미지가 있는 등록 요청을 Command로 변환한다")
        void toRegisterCommand_MultipleImages_Success() {
            // given
            Long productGroupId = ProductGroupDescriptionApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            RegisterProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.registerRequest(
                            "<p>내용</p>",
                            java.util.List.of(
                                    ProductGroupDescriptionApiFixtures
                                            .registerDescriptionImageRequest(
                                                    "https://example.com/img1.jpg", 0),
                                    ProductGroupDescriptionApiFixtures
                                            .registerDescriptionImageRequest(
                                                    "https://example.com/img2.jpg", 1)));

            // when
            RegisterProductGroupDescriptionCommand command =
                    mapper.toRegisterCommand(productGroupId, request);

            // then
            assertThat(command.descriptionImages()).hasSize(2);
            assertThat(command.descriptionImages().get(0).imageUrl())
                    .isEqualTo("https://example.com/img1.jpg");
            assertThat(command.descriptionImages().get(0).sortOrder()).isEqualTo(0);
            assertThat(command.descriptionImages().get(1).imageUrl())
                    .isEqualTo("https://example.com/img2.jpg");
            assertThat(command.descriptionImages().get(1).sortOrder()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("toUpdateCommand(Long, UpdateProductGroupDescriptionApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 UpdateProductGroupDescriptionCommand로 변환한다")
        void toUpdateCommand_Success() {
            // given
            Long productGroupId = ProductGroupDescriptionApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.updateRequest();

            // when
            UpdateProductGroupDescriptionCommand command =
                    mapper.toUpdateCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.content()).isEqualTo(request.content());
            assertThat(command.descriptionImages()).hasSize(1);

            UpdateProductGroupDescriptionCommand.DescriptionImageCommand imageCommand =
                    command.descriptionImages().get(0);
            UpdateProductGroupDescriptionApiRequest.DescriptionImageApiRequest requestImage =
                    request.descriptionImages().get(0);

            assertThat(imageCommand.imageUrl()).isEqualTo(requestImage.imageUrl());
            assertThat(imageCommand.sortOrder()).isEqualTo(requestImage.sortOrder());
        }

        @Test
        @DisplayName("이미지 목록이 null인 경우 빈 목록으로 변환한다")
        void toUpdateCommand_NullImages_ReturnsEmptyList() {
            // given
            Long productGroupId = ProductGroupDescriptionApiFixtures.DEFAULT_PRODUCT_GROUP_ID;
            UpdateProductGroupDescriptionApiRequest request =
                    ProductGroupDescriptionApiFixtures.updateRequestWithoutImages();

            // when
            UpdateProductGroupDescriptionCommand command =
                    mapper.toUpdateCommand(productGroupId, request);

            // then
            assertThat(command.productGroupId()).isEqualTo(productGroupId);
            assertThat(command.content()).isEqualTo(request.content());
            assertThat(command.descriptionImages()).isEmpty();
        }
    }
}
