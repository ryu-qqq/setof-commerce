package com.ryuqq.setof.application.product.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.product.dto.command.ProductDescriptionCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductNoticeCommandDto;
import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.productdescription.factory.command.ProductDescriptionCommandFactory;
import com.ryuqq.setof.application.productnotice.factory.command.ProductNoticeCommandFactory;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.ProductGroupFixture;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productdescription.ProductDescriptionFixture;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.ProductImageFixture;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductChangeDetector")
@ExtendWith(MockitoExtension.class)
class ProductChangeDetectorTest {

    @Mock private ClockHolder clockHolder;
    @Mock private ProductDescriptionCommandFactory descriptionFactory;
    @Mock private ProductNoticeCommandFactory noticeFactory;

    private ProductChangeDetector productChangeDetector;

    private static final Instant FIXED_NOW = Instant.parse("2024-06-01T00:00:00Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_NOW, ZoneId.of("UTC"));

    @BeforeEach
    void setUp() {
        // lenient: clockHolder는 일부 테스트에서만 호출됨
        lenient().when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);
        productChangeDetector =
                new ProductChangeDetector(clockHolder, descriptionFactory, noticeFactory);
    }

    @Nested
    @DisplayName("detect - ProductGroup 변경 감지")
    class DetectProductGroupChangesTest {

        @Test
        @DisplayName("카테고리 변경 시 ProductGroup 변경 감지")
        void shouldDetectCategoryChange() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            200L, // 카테고리 변경
                            1L,
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            null);

            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNotNull(changes.updatedProductGroup());
            assertEquals(200L, changes.updatedProductGroup().getCategoryIdValue());
        }

        @Test
        @DisplayName("브랜드 변경 시 ProductGroup 변경 감지")
        void shouldDetectBrandChange() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            2L, // 브랜드 변경
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            null);

            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNotNull(changes.updatedProductGroup());
            assertEquals(2L, changes.updatedProductGroup().getBrandIdValue());
        }

        @Test
        @DisplayName("이름 변경 시 ProductGroup 변경 감지")
        void shouldDetectNameChange() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            1L,
                            "변경된 상품그룹명", // 이름 변경
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            null);

            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNotNull(changes.updatedProductGroup());
            assertEquals("변경된 상품그룹명", changes.updatedProductGroup().getNameValue());
        }

        @Test
        @DisplayName("가격 변경 시 ProductGroup 변경 감지")
        void shouldDetectPriceChange() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            1L,
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(60000), // 가격 변경
                            BigDecimal.valueOf(55000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            null);

            when(clockHolder.getClock()).thenReturn(FIXED_CLOCK);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNotNull(changes.updatedProductGroup());
            assertEquals(
                    BigDecimal.valueOf(60000),
                    changes.updatedProductGroup().getRegularPriceValue());
        }

        @Test
        @DisplayName("변경 없을 때 null 반환")
        void shouldReturnNullWhenNoChanges() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L, // 기존과 동일
                            1L,
                            "테스트 상품그룹", // 기존과 동일
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            null);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNull(changes.updatedProductGroup());
        }
    }

    @Nested
    @DisplayName("detect - 이미지 변경 감지")
    class DetectImageChangesTest {

        @Test
        @DisplayName("신규 이미지 감지 (ID가 null)")
        void shouldDetectNewImages() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            List<ProductImageCommandDto> newImages =
                    List.of(
                            new ProductImageCommandDto(
                                    null, // ID가 null이면 신규
                                    "MAIN",
                                    "https://origin.example.com/new.jpg",
                                    "https://cdn.example.com/new.jpg",
                                    1));

            UpdateFullProductCommand command = createCommandWithImages(newImages);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertFalse(changes.imageDtosToAdd().isEmpty());
            assertEquals(1, changes.imageDtosToAdd().size());
            assertNull(changes.imageDtosToAdd().get(0).id());
        }

        @Test
        @DisplayName("삭제할 이미지 감지 (기존에만 있는 항목)")
        void shouldDetectRemovedImages() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ProductImage existingImage = ProductImageFixture.reconstitute(100L);
            ExistingProductData existingData =
                    new ExistingProductData(
                            existingProductGroup, List.of(existingImage), null, null);

            UpdateFullProductCommand command = createCommandWithImages(List.of()); // 빈 이미지 목록

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertFalse(changes.imageIdsToDelete().isEmpty());
            assertTrue(changes.imageIdsToDelete().contains(100L));
        }

        @Test
        @DisplayName("수정된 이미지 감지")
        void shouldDetectModifiedImages() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ProductImage existingImage = ProductImageFixture.reconstitute(100L);
            ExistingProductData existingData =
                    new ExistingProductData(
                            existingProductGroup, List.of(existingImage), null, null);

            List<ProductImageCommandDto> modifiedImages =
                    List.of(
                            new ProductImageCommandDto(
                                    100L, // 기존 ID
                                    "SUB", // 타입 변경
                                    "https://origin.example.com/modified.jpg",
                                    "https://cdn.example.com/modified.jpg",
                                    2));

            UpdateFullProductCommand command = createCommandWithImages(modifiedImages);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertFalse(changes.imagesToUpdate().isEmpty());
            assertTrue(changes.imageIdsToDelete().isEmpty());
        }

        private UpdateFullProductCommand createCommandWithImages(
                List<ProductImageCommandDto> images) {
            return new UpdateFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    "ACTIVE",
                    1L,
                    1L,
                    List.of(),
                    images,
                    null,
                    null);
        }
    }

    @Nested
    @DisplayName("detect - Description 변경 감지")
    class DetectDescriptionChangesTest {

        @Test
        @DisplayName("Description이 null이면 변경 없음")
        void shouldReturnNullWhenDescriptionIsNull() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ProductDescription existingDescription = ProductDescriptionFixture.reconstitute(1L);
            ExistingProductData existingData =
                    new ExistingProductData(
                            existingProductGroup, List.of(), existingDescription, null);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            1L,
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null, // description is null
                            null);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNull(changes.updatedDescription());
        }

        @Test
        @DisplayName("기존 Description이 없으면 변경 없음 (신규 생성은 Facade 담당)")
        void shouldReturnNullWhenNoExistingDescription() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            ProductDescriptionCommandDto descriptionDto =
                    new ProductDescriptionCommandDto(null, "<p>새로운 설명</p>", List.of());

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            1L,
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            descriptionDto,
                            null);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNull(changes.updatedDescription()); // 신규 생성은 Facade에서 처리
        }
    }

    @Nested
    @DisplayName("detect - Notice 변경 감지")
    class DetectNoticeChangesTest {

        @Test
        @DisplayName("Notice가 null이면 변경 없음")
        void shouldReturnNullWhenNoticeIsNull() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ProductNotice existingNotice = ProductNoticeFixture.reconstituteNotice(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, existingNotice);

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            1L,
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            null); // notice is null

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNull(changes.updatedNotice());
        }

        @Test
        @DisplayName("기존 Notice가 없으면 변경 없음 (신규 생성은 Facade 담당)")
        void shouldReturnNullWhenNoExistingNotice() {
            // Given
            ProductGroup existingProductGroup = ProductGroupFixture.createWithId(1L);
            ExistingProductData existingData =
                    new ExistingProductData(existingProductGroup, List.of(), null, null);

            ProductNoticeCommandDto noticeDto = new ProductNoticeCommandDto(null, 1L, List.of());

            UpdateFullProductCommand command =
                    new UpdateFullProductCommand(
                            1L,
                            100L,
                            1L,
                            "테스트 상품그룹",
                            "TWO_LEVEL",
                            BigDecimal.valueOf(50000),
                            BigDecimal.valueOf(45000),
                            "ACTIVE",
                            1L,
                            1L,
                            List.of(),
                            List.of(),
                            null,
                            noticeDto);

            // When
            ProductChanges changes = productChangeDetector.detect(existingData, command);

            // Then
            assertNull(changes.updatedNotice()); // 신규 생성은 Facade에서 처리
        }
    }
}
