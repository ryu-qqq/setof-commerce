package com.ryuqq.setof.adapter.out.persistence.productgroup.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.ryuqq.setof.adapter.out.persistence.productgroup.ProductGroupJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.productgroup.entity.ProductGroupJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroup.mapper.ProductGroupJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productgroup.repository.ProductGroupJpaRepository;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * ProductGroupCommandAdapterTest - 상품 그룹 Command Adapter 단위 테스트.
 *
 * <p>일반 등록(ID null)과 마이그레이션(ID 지정) 분기를 검증합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupCommandAdapter 단위 테스트")
class ProductGroupCommandAdapterTest {

    @Mock private ProductGroupJpaRepository jpaRepository;

    @Mock private ProductGroupJpaEntityMapper mapper;

    @Mock private EntityManager entityManager;

    @InjectMocks private ProductGroupCommandAdapter commandAdapter;

    @Nested
    @DisplayName("persist 메서드 테스트 - 일반 등록 (ID null)")
    class PersistNewTest {

        @Test
        @DisplayName("ID가 null이면 JPA save를 호출하고 생성된 ID를 반환합니다")
        void persist_WithNullId_UsesJpaSave() {
            // given
            ProductGroup domain = ProductGroupFixtures.newProductGroup();
            ProductGroupJpaEntity entityToSave = ProductGroupJpaEntityFixtures.newEntity();
            ProductGroupJpaEntity savedEntity = ProductGroupJpaEntityFixtures.activeEntity(100L);

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
        @DisplayName("Mapper가 정확히 한 번 호출됩니다")
        void persist_WithNullId_CallsMapperOnce() {
            // given
            ProductGroup domain = ProductGroupFixtures.newProductGroup();
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.newEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            commandAdapter.persist(domain);

            // then
            then(mapper).should().toEntity(domain);
        }
    }

    @Nested
    @DisplayName("persist 메서드 테스트 - 마이그레이션 (ID 지정)")
    class PersistMigrationTest {

        @Test
        @DisplayName("ID가 있으면 native INSERT를 실행하고 지정된 ID를 반환합니다")
        void persist_WithSpecifiedId_UsesNativeInsert() {
            // given
            Long migrationId = 12345L;
            ProductGroup domain = ProductGroupFixtures.activeProductGroup(migrationId);
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.activeEntity(migrationId);

            given(mapper.toEntity(domain)).willReturn(entity);

            Query nativeQuery = mock(Query.class);
            given(entityManager.createNativeQuery(org.mockito.ArgumentMatchers.anyString()))
                    .willReturn(nativeQuery);
            given(
                            nativeQuery.setParameter(
                                    org.mockito.ArgumentMatchers.anyString(),
                                    org.mockito.ArgumentMatchers.any()))
                    .willReturn(nativeQuery);
            given(nativeQuery.executeUpdate()).willReturn(1);

            // when
            Long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(migrationId);
            then(entityManager)
                    .should()
                    .createNativeQuery(org.mockito.ArgumentMatchers.anyString());
            then(nativeQuery).should().executeUpdate();
            then(jpaRepository).should(never()).save(org.mockito.ArgumentMatchers.any());
        }

        @Test
        @DisplayName("native INSERT 시 JPA save를 호출하지 않습니다")
        void persist_WithSpecifiedId_DoesNotCallJpaSave() {
            // given
            ProductGroup domain = ProductGroupFixtures.activeProductGroup(999L);
            ProductGroupJpaEntity entity = ProductGroupJpaEntityFixtures.activeEntity(999L);

            given(mapper.toEntity(domain)).willReturn(entity);

            Query nativeQuery = mock(Query.class);
            given(entityManager.createNativeQuery(org.mockito.ArgumentMatchers.anyString()))
                    .willReturn(nativeQuery);
            given(
                            nativeQuery.setParameter(
                                    org.mockito.ArgumentMatchers.anyString(),
                                    org.mockito.ArgumentMatchers.any()))
                    .willReturn(nativeQuery);
            given(nativeQuery.executeUpdate()).willReturn(1);

            // when
            commandAdapter.persist(domain);

            // then
            then(jpaRepository).shouldHaveNoInteractions();
        }
    }
}
