package com.ryuqq.setof.application.product.manager.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ryuqq.setof.application.product.dto.bundle.ProductSubAggregatesPersistBundle;
import com.ryuqq.setof.application.productdescription.manager.command.ProductDescriptionPersistenceManager;
import com.ryuqq.setof.application.productimage.manager.command.ProductImageWriteManager;
import com.ryuqq.setof.application.productnotice.manager.command.ProductNoticePersistenceManager;
import com.ryuqq.setof.domain.product.ProductFixture;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.vo.ProductId;
import com.ryuqq.setof.domain.productdescription.ProductDescriptionFixture;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productimage.ProductImageFixture;
import com.ryuqq.setof.domain.productimage.aggregate.ProductImage;
import com.ryuqq.setof.domain.productnotice.ProductNoticeFixture;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("ProductSubAggregatesPersistenceManager")
@ExtendWith(MockitoExtension.class)
class ProductSubAggregatesPersistenceManagerTest {

    @Mock private ProductWriteManager productManager;
    @Mock private ProductImageWriteManager imageManager;
    @Mock private ProductDescriptionPersistenceManager descriptionManager;
    @Mock private ProductNoticePersistenceManager noticeManager;

    private ProductSubAggregatesPersistenceManager persistenceManager;

    @BeforeEach
    void setUp() {
        persistenceManager =
                new ProductSubAggregatesPersistenceManager(
                        productManager, imageManager, descriptionManager, noticeManager);
    }

    @Nested
    @DisplayName("persist")
    class PersistTest {

        @Test
        @DisplayName("Bundle 일괄 저장 성공")
        void shouldPersistBundle() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            List<ProductImage> images = ProductImageFixture.createImageSet(2);
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, images, description, notice);

            List<ProductId> mockProductIds = List.of(new ProductId(1L));
            when(productManager.saveAll(anyList())).thenReturn(mockProductIds);
            when(imageManager.save(any(ProductImage.class))).thenReturn(1L);
            when(descriptionManager.persist(description)).thenReturn(1L);
            when(noticeManager.persist(notice)).thenReturn(1L);

            // When
            List<ProductId> result = persistenceManager.persist(bundle);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).value());
            verify(productManager, times(1)).saveAll(products);
            verify(imageManager, times(3)).save(any(ProductImage.class));
            verify(descriptionManager, times(1)).persist(description);
            verify(noticeManager, times(1)).persist(notice);
        }

        @Test
        @DisplayName("여러 Product가 있는 Bundle 저장 성공")
        void shouldPersistBundleWithMultipleProducts() {
            // Given
            List<Product> products = ProductFixture.createList();
            List<ProductImage> images = ProductImageFixture.createImageSet(1);
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, images, description, notice);

            List<ProductId> mockProductIds =
                    List.of(
                            new ProductId(1L),
                            new ProductId(2L),
                            new ProductId(3L),
                            new ProductId(4L),
                            new ProductId(5L),
                            new ProductId(6L));
            when(productManager.saveAll(anyList())).thenReturn(mockProductIds);
            when(imageManager.save(any(ProductImage.class))).thenReturn(1L);
            when(descriptionManager.persist(description)).thenReturn(1L);
            when(noticeManager.persist(notice)).thenReturn(1L);

            // When
            List<ProductId> result = persistenceManager.persist(bundle);

            // Then
            assertNotNull(result);
            assertEquals(6, result.size());
            verify(productManager, times(1)).saveAll(products);
        }

        @Test
        @DisplayName("이미지 없는 Bundle 저장 성공")
        void shouldPersistBundleWithoutImages() {
            // Given
            List<Product> products = List.of(ProductFixture.create());
            ProductDescription description = ProductDescriptionFixture.create();
            ProductNotice notice = ProductNoticeFixture.createNotice();
            ProductSubAggregatesPersistBundle bundle =
                    new ProductSubAggregatesPersistBundle(products, List.of(), description, notice);

            List<ProductId> mockProductIds = List.of(new ProductId(1L));
            when(productManager.saveAll(anyList())).thenReturn(mockProductIds);
            when(descriptionManager.persist(description)).thenReturn(1L);
            when(noticeManager.persist(notice)).thenReturn(1L);

            // When
            List<ProductId> result = persistenceManager.persist(bundle);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(productManager, times(1)).saveAll(products);
            verify(imageManager, never()).save(any(ProductImage.class));
            verify(descriptionManager, times(1)).persist(description);
            verify(noticeManager, times(1)).persist(notice);
        }

        @Test
        @DisplayName("bundle이 null이면 예외 발생")
        void shouldThrowExceptionWhenBundleIsNull() {
            // When & Then
            assertThrows(NullPointerException.class, () -> persistenceManager.persist(null));
        }
    }
}
