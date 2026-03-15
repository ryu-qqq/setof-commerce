package com.ryuqq.setof.application.imagevariant.manager;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.imagevariant.port.out.command.ImageVariantCommandPort;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
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
@DisplayName("ImageVariantCommandManager 단위 테스트")
class ImageVariantCommandManagerTest {

    @InjectMocks private ImageVariantCommandManager sut;

    @Mock private ImageVariantCommandPort commandPort;

    @Nested
    @DisplayName("persistAll() - ImageVariant 목록 저장")
    class PersistAllTest {

        @Test
        @DisplayName("ImageVariant 목록을 CommandPort에 위임하여 저장한다")
        void persistAll_ValidVariants_DelegatesToCommandPort() {
            // given
            List<ImageVariant> variants =
                    List.of(
                            ImageVariantFixtures.newImageVariant(),
                            ImageVariantFixtures.newImageVariant(
                                    100L,
                                    com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType
                                            .SMALL_WEBP));
            willDoNothing().given(commandPort).persistAll(variants);

            // when
            sut.persistAll(variants);

            // then
            then(commandPort).should().persistAll(variants);
        }

        @Test
        @DisplayName("단일 ImageVariant도 CommandPort에 위임하여 저장한다")
        void persistAll_SingleVariant_DelegatesToCommandPort() {
            // given
            List<ImageVariant> variants = List.of(ImageVariantFixtures.newImageVariant());
            willDoNothing().given(commandPort).persistAll(variants);

            // when
            sut.persistAll(variants);

            // then
            then(commandPort).should().persistAll(variants);
        }
    }

    @Nested
    @DisplayName("softDeleteBySourceImageId() - 소프트 삭제")
    class SoftDeleteBySourceImageIdTest {

        @Test
        @DisplayName("sourceImageId로 해당 이미지의 Variant를 소프트 삭제한다")
        void softDeleteBySourceImageId_ValidId_DelegatesToCommandPort() {
            // given
            Long sourceImageId = 100L;
            willDoNothing().given(commandPort).softDeleteBySourceImageId(sourceImageId);

            // when
            sut.softDeleteBySourceImageId(sourceImageId);

            // then
            then(commandPort).should().softDeleteBySourceImageId(sourceImageId);
        }
    }
}
