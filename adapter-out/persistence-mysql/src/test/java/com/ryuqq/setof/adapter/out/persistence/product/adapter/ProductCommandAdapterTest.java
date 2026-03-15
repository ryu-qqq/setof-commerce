package com.ryuqq.setof.adapter.out.persistence.product.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.adapter.out.persistence.product.ProductJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.product.entity.ProductJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.product.mapper.ProductJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.product.repository.ProductJpaRepository;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.setof.commerce.domain.product.ProductFixtures;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ProductCommandAdapterTest - 상품 Command Adapter 단위 테스트.
 *
 * <p>일반 등록(ID null)과 마이그레이션(ID 지정) 분기를 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductCommandAdapter 단위 테스트")
class ProductCommandAdapterTest {

    @Mock private ProductJpaRepository jpaRepository;

    @Mock private ProductJpaEntityMapper mapper;

    @Mock private EntityManager entityManager;

    @InjectMocks private ProductCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persist 테스트
    // ========================================================================

    @Nested
    @DisplayName("persist 메서드 테스트")
    class PersistTest {

        @Test
        @DisplayName("ID가 null이면 JPA save를 호출하고 생성된 ID를 반환합니다")
        void persist_WithNullId_UsesJpaSave() {
            // given
            Product domain = ProductFixtures.newProduct();
            ProductJpaEntity entityToSave = ProductJpaEntityFixtures.newEntity();
            ProductJpaEntity savedEntity = ProductJpaEntityFixtures.activeEntity(100L);

            given(mapper.toEntity(domain)).willReturn(entityToSave);
            given(jpaRepository.save(entityToSave)).willReturn(savedEntity);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(100L);
            then(jpaRepository).should().save(entityToSave);
            then(entityManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("ID가 있으면 native INSERT를 실행하고 지정된 ID를 반환합니다")
        void persist_WithSpecifiedId_UsesNativeInsert() {
            // given
            Long migrationId = 12345L;
            Product domain = ProductFixtures.activeProduct(migrationId);
            ProductJpaEntity entity = ProductJpaEntityFixtures.activeEntity(migrationId);

            given(mapper.toEntity(domain)).willReturn(entity);

            Query nativeQuery = mock(Query.class);
            given(entityManager.createNativeQuery(ArgumentMatchers.anyString()))
                    .willReturn(nativeQuery);
            given(nativeQuery.setParameter(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                    .willReturn(nativeQuery);
            given(nativeQuery.executeUpdate()).willReturn(1);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(migrationId);
            then(entityManager).should().createNativeQuery(ArgumentMatchers.anyString());
            then(nativeQuery).should().executeUpdate();
            then(jpaRepository).should(never()).save(ArgumentMatchers.any());
        }
    }

    // ========================================================================
    // 2. persistAll 테스트
    // ========================================================================

    @Nested
    @DisplayName("persistAll 메서드 테스트")
    class PersistAllTest {

        @Test
        @DisplayName("ID가 모두 null이면 JPA saveAll을 호출합니다")
        void persistAll_WithAllNullIds_UsesJpaSaveAll() {
            // given
            Product domain1 = ProductFixtures.newProduct();
            Product domain2 = ProductFixtures.newProduct();
            List<Product> products = List.of(domain1, domain2);

            ProductJpaEntity entity1 = ProductJpaEntityFixtures.newEntity();
            ProductJpaEntity entity2 = ProductJpaEntityFixtures.newEntity();
            ProductJpaEntity savedEntity1 = ProductJpaEntityFixtures.activeEntity(10L);
            ProductJpaEntity savedEntity2 = ProductJpaEntityFixtures.activeEntity(11L);

            given(mapper.toEntity(domain1)).willReturn(entity1);
            given(mapper.toEntity(domain2)).willReturn(entity2);
            given(jpaRepository.saveAll(List.of(entity1, entity2)))
                    .willReturn(List.of(savedEntity1, savedEntity2));

            // when
            List<Long> savedIds = commandAdapter.persistAll(products);

            // then
            assertThat(savedIds).containsExactly(10L, 11L);
            then(entityManager).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("ID가 모두 있으면 native INSERT만 실행합니다")
        void persistAll_WithAllSpecifiedIds_UsesNativeInsertOnly() {
            // given
            Product domain1 = ProductFixtures.activeProduct(100L);
            Product domain2 = ProductFixtures.activeProduct(200L);
            List<Product> products = List.of(domain1, domain2);

            ProductJpaEntity entity1 = ProductJpaEntityFixtures.activeEntity(100L);
            ProductJpaEntity entity2 = ProductJpaEntityFixtures.activeEntity(200L);

            given(mapper.toEntity(domain1)).willReturn(entity1);
            given(mapper.toEntity(domain2)).willReturn(entity2);

            Query nativeQuery = mock(Query.class);
            given(entityManager.createNativeQuery(ArgumentMatchers.anyString()))
                    .willReturn(nativeQuery);
            given(nativeQuery.setParameter(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                    .willReturn(nativeQuery);
            given(nativeQuery.executeUpdate()).willReturn(1);

            // when
            List<Long> savedIds = commandAdapter.persistAll(products);

            // then
            assertThat(savedIds).containsExactly(100L, 200L);
            then(entityManager).should(times(2)).createNativeQuery(ArgumentMatchers.anyString());
            then(jpaRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("ID 혼합(null + 지정) 시 각각 적절한 방식으로 저장합니다")
        void persistAll_WithMixedIds_UsesBothStrategies() {
            // given
            Product newProduct = ProductFixtures.newProduct();
            Product migrationProduct = ProductFixtures.activeProduct(500L);
            List<Product> products = List.of(newProduct, migrationProduct);

            ProductJpaEntity newEntity = ProductJpaEntityFixtures.newEntity();
            ProductJpaEntity migrationEntity = ProductJpaEntityFixtures.activeEntity(500L);
            ProductJpaEntity savedNewEntity = ProductJpaEntityFixtures.activeEntity(10L);

            given(mapper.toEntity(newProduct)).willReturn(newEntity);
            given(mapper.toEntity(migrationProduct)).willReturn(migrationEntity);

            Query nativeQuery = mock(Query.class);
            given(entityManager.createNativeQuery(ArgumentMatchers.anyString()))
                    .willReturn(nativeQuery);
            given(nativeQuery.setParameter(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                    .willReturn(nativeQuery);
            given(nativeQuery.executeUpdate()).willReturn(1);

            given(jpaRepository.saveAll(List.of(newEntity))).willReturn(List.of(savedNewEntity));

            // when
            List<Long> savedIds = commandAdapter.persistAll(products);

            // then
            assertThat(savedIds).containsExactly(500L, 10L);
            then(entityManager).should().createNativeQuery(ArgumentMatchers.anyString());
            then(jpaRepository).should().saveAll(List.of(newEntity));
        }
    }
}
