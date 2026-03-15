package com.ryuqq.setof.adapter.out.persistence.discountpolicy.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.ryuqq.setof.adapter.out.persistence.discountpolicy.DiscountPolicyJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.entity.DiscountPolicyJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.mapper.DiscountPolicyJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.discountpolicy.repository.DiscountPolicyJpaRepository;
import com.ryuqq.setof.domain.discount.DiscountFixtures;
import com.ryuqq.setof.domain.discount.aggregate.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * DiscountPolicyCommandAdapterTest - 할인 정책 Command Adapter 단위 테스트.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-003: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("DiscountPolicyCommandAdapter 단위 테스트")
class DiscountPolicyCommandAdapterTest {

    @Mock private DiscountPolicyJpaRepository jpaRepository;

    @Mock private DiscountPolicyJpaEntityMapper mapper;

    @InjectMocks private DiscountPolicyCommandAdapter commandAdapter;

    // ========================================================================
    // 1. persist 테스트
    // ========================================================================

    @Nested
    @DisplayName("persist 메서드 테스트")
    class PersistTest {

        @Test
        @DisplayName("Domain을 Entity로 변환 후 저장하고 ID를 반환합니다")
        void persist_WithValidDomain_SavesAndReturnsId() {
            // given
            DiscountPolicy domain = DiscountFixtures.newRateInstantPolicy();
            DiscountPolicyJpaEntity entityToSave = DiscountPolicyJpaEntityFixtures.newRateEntity();
            DiscountPolicyJpaEntity savedEntity =
                    DiscountPolicyJpaEntityFixtures.activeRateEntity(100L);

            given(mapper.toEntity(domain)).willReturn(entityToSave);
            given(jpaRepository.save(entityToSave)).willReturn(savedEntity);

            // when
            long savedId = commandAdapter.persist(domain);

            // then
            assertThat(savedId).isEqualTo(100L);
            then(mapper).should().toEntity(domain);
            then(jpaRepository).should().save(entityToSave);
        }

        @Test
        @DisplayName("Mapper가 정확히 한 번 호출됩니다")
        void persist_CallsMapperOnce() {
            // given
            DiscountPolicy domain = DiscountFixtures.newRateInstantPolicy();
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            commandAdapter.persist(domain);

            // then
            then(mapper).should(times(1)).toEntity(domain);
        }

        @Test
        @DisplayName("JpaRepository.save가 정확히 한 번 호출됩니다")
        void persist_CallsRepositorySaveOnce() {
            // given
            DiscountPolicy domain = DiscountFixtures.newRateInstantPolicy();
            DiscountPolicyJpaEntity entity = DiscountPolicyJpaEntityFixtures.activeRateEntity();

            given(mapper.toEntity(domain)).willReturn(entity);
            given(jpaRepository.save(entity)).willReturn(entity);

            // when
            commandAdapter.persist(domain);

            // then
            then(jpaRepository).should(times(1)).save(entity);
        }
    }
}
