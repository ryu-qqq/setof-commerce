package com.ryuqq.setof.application.imagevariant.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.imagevariant.port.out.query.ImageVariantQueryPort;
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
@DisplayName("ImageVariantReadManager 단위 테스트")
class ImageVariantReadManagerTest {

    @InjectMocks private ImageVariantReadManager sut;

    @Mock private ImageVariantQueryPort queryPort;

    @Nested
    @DisplayName("findBySourceImageId() - sourceImageId로 Variant 목록 조회")
    class FindBySourceImageIdTest {

        @Test
        @DisplayName("존재하는 sourceImageId로 Variant 목록을 조회한다")
        void findBySourceImageId_ExistingId_ReturnsVariantList() {
            // given
            Long sourceImageId = 100L;
            List<ImageVariant> expected = List.of(ImageVariantFixtures.activeImageVariant());

            given(queryPort.findBySourceImageId(sourceImageId)).willReturn(expected);

            // when
            List<ImageVariant> result = sut.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).hasSize(1);
            then(queryPort).should().findBySourceImageId(sourceImageId);
        }

        @Test
        @DisplayName("존재하지 않는 sourceImageId이면 빈 목록을 반환한다")
        void findBySourceImageId_NonExistingId_ReturnsEmptyList() {
            // given
            Long sourceImageId = 999L;

            given(queryPort.findBySourceImageId(sourceImageId)).willReturn(Collections.emptyList());

            // when
            List<ImageVariant> result = sut.findBySourceImageId(sourceImageId);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findBySourceImageId(sourceImageId);
        }
    }

    @Nested
    @DisplayName("findBySourceImageIds() - sourceImageId 목록으로 Variant 목록 조회")
    class FindBySourceImageIdsTest {

        @Test
        @DisplayName("복수의 sourceImageId로 Variant 목록을 조회한다")
        void findBySourceImageIds_MultipleIds_ReturnsVariantList() {
            // given
            List<Long> sourceImageIds = List.of(100L, 200L);
            List<ImageVariant> expected =
                    List.of(
                            ImageVariantFixtures.activeImageVariant(1L),
                            ImageVariantFixtures.activeImageVariant(2L));

            given(queryPort.findBySourceImageIds(sourceImageIds)).willReturn(expected);

            // when
            List<ImageVariant> result = sut.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findBySourceImageIds(sourceImageIds);
        }

        @Test
        @DisplayName("조회 결과가 없으면 빈 목록을 반환한다")
        void findBySourceImageIds_NoResults_ReturnsEmptyList() {
            // given
            List<Long> sourceImageIds = List.of(999L, 1000L);

            given(queryPort.findBySourceImageIds(sourceImageIds))
                    .willReturn(Collections.emptyList());

            // when
            List<ImageVariant> result = sut.findBySourceImageIds(sourceImageIds);

            // then
            assertThat(result).isEmpty();
            then(queryPort).should().findBySourceImageIds(sourceImageIds);
        }
    }
}
