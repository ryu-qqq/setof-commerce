package com.ryuqq.setof.application.product.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.product.factory.command.ProductGroupCommandFactory;
import com.ryuqq.setof.application.product.factory.command.ProductSubAggregatesBundleFactory;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.command.ProductSubAggregatesPersistenceManager;
import com.ryuqq.setof.application.productstock.factory.command.ProductStockCommandFactory;
import com.ryuqq.setof.application.productstock.manager.command.ProductStockPersistenceManager;
import com.ryuqq.setof.domain.product.ProductFixture;
import com.ryuqq.setof.domain.product.ProductGroupFixture;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productdescription.ProductDescriptionFixture;
import com.ryuqq.setof.domain.productimage.ProductImageFixture;
import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productstock.ProductStockFixture;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductRegistrationFacade")
@ExtendWith(MockitoExtension.class)
class ProductRegistrationFacadeTest {

    @Mock private ProductGroupCommandFactory productGroupFactory;
    @Mock private ProductGroupPersistenceManager productGroupManager;
    @Mock private ProductSubAggregatesBundleFactory subAggregatesBundleFactory;
    @Mock private ProductSubAggregatesPersistenceManager subAggregatesManager;
    @Mock private ProductStockCommandFactory stockFactory;
    @Mock private ProductStockPersistenceManager stockManager;

    private ProductRegistrationFacade facade;

    @BeforeEach
    void setUp() {
        facade =
                new ProductRegistrationFacade(
                        productGroupFactory,
                        productGroupManager,
                        subAggregatesBundleFactory,
                        subAggregatesManager,
                        stockFactory,
                        stockManager);
    }

    @Nested
    @DisplayName("registerAll")
    class RegisterAllTest {

        @Test
        @DisplayName("전체 상품 등록 성공")
        void shouldRegisterAll() {
            // Given
            RegisterFullProductCommand command = createValidCommand();
            ProductGroup productGroup = ProductGroupFixture.createWithId(1L);
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<Product> products = List.of(ProductFixture.create());
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(
                            products,
                            ProductImageFixture.createImageSet(2),
                            ProductDescriptionFixture.create(),
                            ProductNoticeFixture.createNotice());
            List<ProductId> productIds = List.of(new ProductId(1L));
            ProductStock mockStock = ProductStockFixture.createDefault();

            when(productGroupFactory.createProductGroup(any(RegisterProductGroupCommand.class)))
                    .thenReturn(productGroup);
            when(productGroupManager.persist(productGroup)).thenReturn(productGroupId);
            when(subAggregatesBundleFactory.create(eq(1L), eq(command))).thenReturn(bundle);
            when(subAggregatesManager.persist(bundle)).thenReturn(productIds);
            when(stockFactory.createProductStock(any())).thenReturn(mockStock);
            when(stockManager.persist(mockStock)).thenReturn(1L);

            // When
            Long result = facade.registerAll(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result);
            verify(productGroupFactory, times(1)).createProductGroup(any());
            verify(productGroupManager, times(1)).persist(productGroup);
            verify(subAggregatesBundleFactory, times(1)).create(1L, command);
            verify(subAggregatesManager, times(1)).persist(bundle);
            verify(stockFactory, times(1)).createProductStock(any());
            verify(stockManager, times(1)).persist(mockStock);
        }

        @Test
        @DisplayName("여러 SKU가 있는 전체 상품 등록 성공")
        void shouldRegisterAllWithMultipleSkus() {
            // Given
            RegisterFullProductCommand command = createCommandWithMultipleSkus();
            ProductGroup productGroup = ProductGroupFixture.createWithId(1L);
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<Product> products = ProductFixture.createList();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(
                            products,
                            ProductImageFixture.createImageSet(1),
                            ProductDescriptionFixture.create(),
                            ProductNoticeFixture.createNotice());
            List<ProductId> productIds =
                    List.of(
                            new ProductId(1L),
                            new ProductId(2L),
                            new ProductId(3L),
                            new ProductId(4L),
                            new ProductId(5L),
                            new ProductId(6L));
            ProductStock mockStock = ProductStockFixture.createDefault();

            when(productGroupFactory.createProductGroup(any(RegisterProductGroupCommand.class)))
                    .thenReturn(productGroup);
            when(productGroupManager.persist(productGroup)).thenReturn(productGroupId);
            when(subAggregatesBundleFactory.create(eq(1L), eq(command))).thenReturn(bundle);
            when(subAggregatesManager.persist(bundle)).thenReturn(productIds);
            when(stockFactory.createProductStock(any())).thenReturn(mockStock);
            when(stockManager.persist(mockStock)).thenReturn(1L);

            // When
            Long result = facade.registerAll(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result);
            verify(stockFactory, times(6)).createProductStock(any());
            verify(stockManager, times(6)).persist(mockStock);
        }
    }

    @Nested
    @DisplayName("initializeStocks")
    class InitializeStocksTest {

        @Test
        @DisplayName("재고 초기화 성공")
        void shouldInitializeStocks() {
            // Given
            List<Long> productIds = List.of(1L, 2L, 3L);
            List<Integer> quantities = List.of(10, 20, 30);
            ProductStock mockStock = ProductStockFixture.createDefault();

            when(stockFactory.createProductStock(any())).thenReturn(mockStock);
            when(stockManager.persist(mockStock)).thenReturn(1L);

            // When
            facade.initializeStocks(productIds, quantities);

            // Then
            verify(stockFactory, times(3)).createProductStock(any());
            verify(stockManager, times(3)).persist(mockStock);
        }

        @Test
        @DisplayName("빈 목록으로 재고 초기화 시 아무것도 하지 않음")
        void shouldDoNothingWhenEmptyProductIds() {
            // When
            facade.initializeStocks(List.of(), List.of());

            // Then
            verify(stockFactory, never()).createProductStock(any());
            verify(stockManager, never()).persist(any());
        }

        @Test
        @DisplayName("null 목록으로 재고 초기화 시 아무것도 하지 않음")
        void shouldDoNothingWhenNullProductIds() {
            // When
            facade.initializeStocks(null, null);

            // Then
            verify(stockFactory, never()).createProductStock(any());
            verify(stockManager, never()).persist(any());
        }
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
                List.of(new ProductSkuCommandDto("색상", "Red", "사이즈", "M", BigDecimal.ZERO, 100)),
                List.of(
                        new ProductImageCommandDto(
                                null,
                                "MAIN",
                                "https://origin.com/main.jpg",
                                "https://cdn.com/main.jpg",
                                1)),
                new ProductDescriptionCommandDto(null, "<p>상품 설명</p>", List.of()),
                new ProductNoticeCommandDto(null, 1L, List.of()));
    }

    private RegisterFullProductCommand createCommandWithMultipleSkus() {
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
                        new ProductSkuCommandDto("색상", "Blue", "사이즈", "S", BigDecimal.ZERO, 100),
                        new ProductSkuCommandDto("색상", "Blue", "사이즈", "M", BigDecimal.ZERO, 100),
                        new ProductSkuCommandDto("색상", "Blue", "사이즈", "L", BigDecimal.ZERO, 100)),
                List.of(
                        new ProductImageCommandDto(
                                null,
                                "MAIN",
                                "https://origin.com/main.jpg",
                                "https://cdn.com/main.jpg",
                                1)),
                new ProductDescriptionCommandDto(null, "<p>상품 설명</p>", List.of()),
                new ProductNoticeCommandDto(null, 1L, List.of()));
    }
}
