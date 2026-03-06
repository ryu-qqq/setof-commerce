package com.ryuqq.setof.application.productgroupimage.service.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroupimage.ProductGroupImageCommandFixtures;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productgroupimage.factory.ProductGroupImageFactory;
import com.ryuqq.setof.application.productgroupimage.internal.ImageCommandCoordinator;
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
@DisplayName("RegisterProductGroupImagesService 단위 테스트")
class RegisterProductGroupImagesServiceTest {

    @InjectMocks private RegisterProductGroupImagesService sut;

    @Mock private ProductGroupImageFactory imageFactory;
    @Mock private ImageCommandCoordinator imageCommandCoordinator;

    @Nested
    @DisplayName("execute() - 이미지 등록")
    class ExecuteTest {

        @Test
        @DisplayName("커맨드로 이미지를 등록하고 ID 목록을 반환한다")
        void execute_ValidCommand_ReturnsImageIds() {
            // given
            RegisterProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.registerCommand();
            ProductGroupImages images = ProductGroupImageFixtures.defaultImages();
            List<Long> expectedIds = List.of(1L, 2L);

            given(imageFactory.createFromRegistration(command.images())).willReturn(images);
            given(imageCommandCoordinator.register(command.productGroupId(), images))
                    .willReturn(expectedIds);

            // when
            List<Long> result = sut.execute(command);

            // then
            assertThat(result).isEqualTo(expectedIds);
            assertThat(result).hasSize(2);
            then(imageFactory).should().createFromRegistration(command.images());
            then(imageCommandCoordinator).should().register(command.productGroupId(), images);
        }

        @Test
        @DisplayName("썸네일만 있는 커맨드로 이미지를 등록하고 단일 ID 목록을 반환한다")
        void execute_ThumbnailOnly_ReturnsSingleId() {
            // given
            RegisterProductGroupImagesCommand command =
                    ProductGroupImageCommandFixtures.registerThumbnailOnlyCommand();
            ProductGroupImages images = ProductGroupImageFixtures.thumbnailOnlyImages();
            List<Long> expectedIds = List.of(1L);

            given(imageFactory.createFromRegistration(command.images())).willReturn(images);
            given(imageCommandCoordinator.register(command.productGroupId(), images))
                    .willReturn(expectedIds);

            // when
            List<Long> result = sut.execute(command);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(1L);
            then(imageFactory).should().createFromRegistration(command.images());
            then(imageCommandCoordinator).should().register(command.productGroupId(), images);
        }
    }
}
