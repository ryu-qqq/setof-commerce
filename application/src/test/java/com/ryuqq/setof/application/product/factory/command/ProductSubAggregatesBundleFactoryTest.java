package com.ryuqq.setof.application.product.factory.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.product.dto.bundle.ProductSubAggregatesPersistBundle;
import com.ryuqq.setof.application.product.dto.command.ProductDescriptionCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductImageCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductNoticeCommandDto;
import com.ryuqq.setof.application.product.dto.command.ProductSkuCommandDto;
import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductDescriptionCommand;
import com.ryuqq.setof.application.productdescription.factory.command.ProductDescriptionCommandFactory;
import com.ryuqq.setof.application.productimage.dto.command.RegisterProductImageCommand;
import com.ryuqq.setof.application.productimage.factory.command.ProductImageCommandFactory;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.factory.command.ProductNoticeCommandFactory;
import com.ryuqq.setof.domain.product.ProductFixture;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.OptionType;
import com.ryuqq.setof.domain.productdescription.ProductDescriptionFixture;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.ProductImageFixture;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductSubAggregatesBundleFactory")
@ExtendWith(MockitoExtension.class)
class ProductSubAggregatesBundleFactoryTest {

    @Mock private ProductGroupCommandFactory productFactory;
    @Mock private ProductImageCommandFactory imageFactory;
    @Mock private ProductDescriptionCommandFactory descriptionFactory;
    @Mock private ProductNoticeCommandFactory noticeFactory;

    private ProductSubAggregatesBundleFactory bundleFactory;

    @BeforeEach
    void setUp() {
        bundleFactory =
                new ProductSubAggregatesBundleFactory(
                        productFactory, imageFactory, descriptionFactory, noticeFactory);
    }

    @Nested
    @DisplayName("create")
    class CreateTest {

        @Test
        @DisplayName("Bundle 생성 성공")
        void shouldCreateBundle() {
            // Given
            Long productGroupId = 1L;
            RegisterFullProductCommand command = createValidCommand();
            List<Product> mockProducts = List.of(ProductFixture.create());
            ProductImage mockImage = ProductImageFixture.createMain();
            ProductDescription mockDescription = ProductDescriptionFixture.create();
            ProductNotice mockNotice = ProductNoticeFixture.createNotice();

            when(productFactory.createProducts(
                            eq(productGroupId), any(OptionType.class), anyList()))
                    .thenReturn(mockProducts);
            when(imageFactory.createFromRegisterCommand(any(RegisterProductImageCommand.class)))
                    .thenReturn(mockImage);
            when(descriptionFactory.createProductDescription(
                            any(RegisterProductDescriptionCommand.class)))
                    .thenReturn(mockDescription);
            when(noticeFactory.createProductNotice(any(RegisterProductNoticeCommand.class)))
                    .thenReturn(mockNotice);

            // When
            ProductSubAggregatesPersistBundle bundle =
                    bundleFactory.create(productGroupId, command);

            // Then
            assertNotNull(bundle);
            assertEquals(1, bundle.products().size());
            assertEquals(2, bundle.images().size());
            assertNotNull(bundle.description());
            assertNotNull(bundle.notice());
            verify(productFactory, times(1)).createProducts(anyLong(), any(), anyList());
            verify(imageFactory, times(2)).createFromRegisterCommand(any());
            verify(descriptionFactory, times(1)).createProductDescription(any());
            verify(noticeFactory, times(1)).createProductNotice(any());
        }

        @Test
        @DisplayName("여러 Product가 있는 Bundle 생성 성공")
        void shouldCreateBundleWithMultipleProducts() {
            // Given
            Long productGroupId = 1L;
            RegisterFullProductCommand command = createCommandWithMultipleProducts();
            List<Product> mockProducts = ProductFixture.createList();
            ProductImage mockImage = ProductImageFixture.createMain();
            ProductDescription mockDescription = ProductDescriptionFixture.create();
            ProductNotice mockNotice = ProductNoticeFixture.createNotice();

            when(productFactory.createProducts(
                            eq(productGroupId), any(OptionType.class), anyList()))
                    .thenReturn(mockProducts);
            when(imageFactory.createFromRegisterCommand(any(RegisterProductImageCommand.class)))
                    .thenReturn(mockImage);
            when(descriptionFactory.createProductDescription(
                            any(RegisterProductDescriptionCommand.class)))
                    .thenReturn(mockDescription);
            when(noticeFactory.createProductNotice(any(RegisterProductNoticeCommand.class)))
                    .thenReturn(mockNotice);

            // When
            ProductSubAggregatesPersistBundle bundle =
                    bundleFactory.create(productGroupId, command);

            // Then
            assertNotNull(bundle);
            assertEquals(6, bundle.products().size());
            assertEquals(6, bundle.productCount());
        }

        @Test
        @DisplayName("이미지 없이 Bundle 생성 성공")
        void shouldCreateBundleWithoutImages() {
            // Given
            Long productGroupId = 1L;
            RegisterFullProductCommand command = createCommandWithoutImages();
            List<Product> mockProducts = List.of(ProductFixture.create());
            ProductDescription mockDescription = ProductDescriptionFixture.create();
            ProductNotice mockNotice = ProductNoticeFixture.createNotice();

            when(productFactory.createProducts(
                            eq(productGroupId), any(OptionType.class), anyList()))
                    .thenReturn(mockProducts);
            when(descriptionFactory.createProductDescription(
                            any(RegisterProductDescriptionCommand.class)))
                    .thenReturn(mockDescription);
            when(noticeFactory.createProductNotice(any(RegisterProductNoticeCommand.class)))
                    .thenReturn(mockNotice);

            // When
            ProductSubAggregatesPersistBundle bundle =
                    bundleFactory.create(productGroupId, command);

            // Then
            assertNotNull(bundle);
            assertTrue(bundle.images().isEmpty());
            verify(imageFactory, never()).createFromRegisterCommand(any());
        }

        @Test
        @DisplayName("productGroupId가 null이면 예외 발생")
        void shouldThrowExceptionWhenProductGroupIdIsNull() {
            // Given
            RegisterFullProductCommand command = createValidCommand();

            // When & Then
            assertThrows(NullPointerException.class, () -> bundleFactory.create(null, command));
        }

        @Test
        @DisplayName("command가 null이면 예외 발생")
        void shouldThrowExceptionWhenCommandIsNull() {
            // Given
            Long productGroupId = 1L;

            // When & Then
            assertThrows(
                    NullPointerException.class, () -> bundleFactory.create(productGroupId, null));
        }

        @Test
        @DisplayName("products가 null이면 예외 발생")
        void shouldThrowExceptionWhenProductsIsNull() {
            // Given
            Long productGroupId = 1L;
            RegisterFullProductCommand command = createCommandWithNullProducts();

            // When & Then
            assertThrows(
                    NullPointerException.class,
                    () -> bundleFactory.create(productGroupId, command));
        }

        @Test
        @DisplayName("description이 null이면 예외 발생")
        void shouldThrowExceptionWhenDescriptionIsNull() {
            // Given
            Long productGroupId = 1L;
            RegisterFullProductCommand command = createCommandWithNullDescription();

            // When & Then
            assertThrows(
                    NullPointerException.class,
                    () -> bundleFactory.create(productGroupId, command));
        }

        @Test
        @DisplayName("notice가 null이면 예외 발생")
        void shouldThrowExceptionWhenNoticeIsNull() {
            // Given
            Long productGroupId = 1L;
            RegisterFullProductCommand command = createCommandWithNullNotice();

            // When & Then
            assertThrows(
                    NullPointerException.class,
                    () -> bundleFactory.create(productGroupId, command));
        }

        private RegisterFullProductCommand createValidCommand() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    List.of(
                            new ProductSkuCommandDto(
                                    "색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100)),
                    createImageCommands(),
                    createDescriptionCommand(),
                    createNoticeCommand());
        }

        private RegisterFullProductCommand createCommandWithMultipleProducts() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    List.of(
                            new ProductSkuCommandDto("색상", "Red", "사이즈", "S", BigDecimal.ZERO, 100),
                            new ProductSkuCommandDto("색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100),
                            new ProductSkuCommandDto("색상", "Red", "사이즈", "L", BigDecimal.ZERO, 100),
                            new ProductSkuCommandDto(
                                    "색상", "Blue", "사이즈", "S", BigDecimal.ZERO, 100),
                            new ProductSkuCommandDto(
                                    "색상", "Blue", "사이즈", "M", BigDecimal.ZERO, 100),
                            new ProductSkuCommandDto(
                                    "색상", "Blue", "사이즈", "L", BigDecimal.ZERO, 100)),
                    createImageCommands(),
                    createDescriptionCommand(),
                    createNoticeCommand());
        }

        private RegisterFullProductCommand createCommandWithoutImages() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    List.of(
                            new ProductSkuCommandDto(
                                    "색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100)),
                    List.of(),
                    createDescriptionCommand(),
                    createNoticeCommand());
        }

        private RegisterFullProductCommand createCommandWithNullProducts() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    null,
                    createImageCommands(),
                    createDescriptionCommand(),
                    createNoticeCommand());
        }

        private RegisterFullProductCommand createCommandWithNullDescription() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    List.of(
                            new ProductSkuCommandDto(
                                    "색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100)),
                    createImageCommands(),
                    null,
                    createNoticeCommand());
        }

        private RegisterFullProductCommand createCommandWithNullNotice() {
            return new RegisterFullProductCommand(
                    1L,
                    100L,
                    1L,
                    "테스트 상품그룹",
                    "TWO_LEVEL",
                    BigDecimal.valueOf(50000),
                    BigDecimal.valueOf(45000),
                    1L,
                    1L,
                    List.of(
                            new ProductSkuCommandDto(
                                    "색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100)),
                    createImageCommands(),
                    createDescriptionCommand(),
                    null);
        }

        private List<ProductImageCommandDto> createImageCommands() {
            return List.of(
                    new ProductImageCommandDto(
                            null,
                            "MAIN",
                            "https://origin.com/main.jpg",
                            "https://cdn.com/main.jpg",
                            1),
                    new ProductImageCommandDto(
                            null,
                            "SUB",
                            "https://origin.com/sub.jpg",
                            "https://cdn.com/sub.jpg",
                            2));
        }

        private ProductDescriptionCommandDto createDescriptionCommand() {
            return new ProductDescriptionCommandDto(null, "<p>상품 설명</p>", List.of());
        }

        private ProductNoticeCommandDto createNoticeCommand() {
            return new ProductNoticeCommandDto(null, 1L, List.of());
        }
    }
}
