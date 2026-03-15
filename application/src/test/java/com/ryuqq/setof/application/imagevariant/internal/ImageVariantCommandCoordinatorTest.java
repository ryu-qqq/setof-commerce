package com.ryuqq.setof.application.imagevariant.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.ryuqq.setof.application.imagevariant.ImageVariantCommandFixtures;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import com.ryuqq.setof.application.imagevariant.factory.ImageVariantFactory;
import com.ryuqq.setof.application.imagevariant.manager.ImageVariantCommandManager;
import com.ryuqq.setof.application.imagevariant.manager.ImageVariantReadManager;
import com.ryuqq.setof.domain.imagevariant.ImageVariantFixtures;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.Collections;
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
@DisplayName("ImageVariantCommandCoordinator 단위 테스트")
class ImageVariantCommandCoordinatorTest {

    @InjectMocks private ImageVariantCommandCoordinator sut;

    @Mock private ImageVariantFactory factory;
    @Mock private ImageVariantCommandManager commandManager;
    @Mock private ImageVariantReadManager readManager;

    @Nested
    @DisplayName("sync() - 기존 Variant 없음: 신규 전체 등록")
    class SyncWhenNoExistingVariantsTest {

        @Test
        @DisplayName("기존 Variant가 없으면 신규 Variant를 전체 등록한다")
        void sync_NoExistingVariants_PersistsAllNewVariants() {
            // given
            SyncImageVariantsCommand command = ImageVariantCommandFixtures.syncCommand();
            List<ImageVariant> newVariants =
                    List.of(
                            ImageVariantFixtures.newImageVariant(),
                            ImageVariantFixtures.newImageVariant(
                                    100L,
                                    com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType
                                            .SMALL_WEBP));

            given(
                            factory.createVariants(
                                    eq(command.sourceImageId()),
                                    any(),
                                    eq(command.variants()),
                                    any()))
                    .willReturn(newVariants);
            given(readManager.findBySourceImageId(command.sourceImageId()))
                    .willReturn(Collections.emptyList());
            willDoNothing().given(commandManager).persistAll(newVariants);

            // when
            sut.sync(command);

            // then
            then(commandManager).should().persistAll(newVariants);
        }

        @Test
        @DisplayName("기존 Variant가 없고 신규 Variant도 없으면 persistAll을 호출하지 않는다")
        void sync_NoExistingVariants_EmptyNewVariants_DoesNotPersist() {
            // given
            SyncImageVariantsCommand command =
                    ImageVariantCommandFixtures.syncCommandWithEmptyVariants();

            given(
                            factory.createVariants(
                                    eq(command.sourceImageId()),
                                    any(),
                                    eq(command.variants()),
                                    any()))
                    .willReturn(Collections.emptyList());
            given(readManager.findBySourceImageId(command.sourceImageId()))
                    .willReturn(Collections.emptyList());
            willDoNothing().given(commandManager).persistAll(Collections.emptyList());

            // when
            sut.sync(command);

            // then
            then(commandManager).should().persistAll(Collections.emptyList());
        }
    }

    @Nested
    @DisplayName("sync() - 기존 Variant 있음: diff 기반 동기화")
    class SyncWhenExistingVariantsPresentTest {

        @Test
        @DisplayName("변경 사항이 없으면 commandManager를 호출하지 않는다")
        void sync_NoChangesInDiff_DoesNotCallCommandManager() {
            // given
            SyncImageVariantsCommand command =
                    ImageVariantCommandFixtures.syncCommandWithSingleVariant();
            ImageVariant existingVariant = ImageVariantFixtures.activeImageVariant();
            List<ImageVariant> existingVariants = List.of(existingVariant);

            // 동일한 variantType + variantUrl로 새 variant 생성 시 diff 없음
            List<ImageVariant> newVariants = List.of(existingVariant);

            given(
                            factory.createVariants(
                                    eq(command.sourceImageId()),
                                    any(),
                                    eq(command.variants()),
                                    any()))
                    .willReturn(newVariants);
            given(readManager.findBySourceImageId(command.sourceImageId()))
                    .willReturn(existingVariants);

            // when
            sut.sync(command);

            // then
            then(commandManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("추가/삭제 Variant가 있으면 commandManager.persistAll을 호출한다")
        void sync_WithChangesInDiff_CallsCommandManagerForChanges() {
            // given
            SyncImageVariantsCommand command = ImageVariantCommandFixtures.syncCommand();

            ImageVariant existingVariant = ImageVariantFixtures.activeImageVariant(1L);
            List<ImageVariant> existingVariants = List.of(existingVariant);

            ImageVariant newVariant =
                    ImageVariantFixtures.newImageVariant(
                            100L,
                            com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType.LARGE_WEBP);
            List<ImageVariant> newVariants = List.of(newVariant);

            given(
                            factory.createVariants(
                                    eq(command.sourceImageId()),
                                    any(),
                                    eq(command.variants()),
                                    any()))
                    .willReturn(newVariants);
            given(readManager.findBySourceImageId(command.sourceImageId()))
                    .willReturn(existingVariants);
            willDoNothing().given(commandManager).persistAll(anyList());

            // when
            sut.sync(command);

            // then
            then(commandManager).should(org.mockito.Mockito.atLeastOnce()).persistAll(anyList());
        }
    }
}
