package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ryuqq.setof.adapter.out.persistence.seller.SellerAddressJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerAddressJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerAddressJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerAddressQueryDslRepository;
import com.ryuqq.setof.domain.seller.SellerFixtures;
import com.ryuqq.setof.domain.seller.aggregate.SellerAddress;
import com.ryuqq.setof.domain.seller.id.SellerAddressId;
import com.ryuqq.setof.domain.seller.id.SellerId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * SellerAddressQueryAdapterTest - 셀러 주소 Query Adapter 단위 테스트.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerAddressQueryAdapter 단위 테스트")
class SellerAddressQueryAdapterTest {

    @Mock private SellerAddressQueryDslRepository queryDslRepository;

    @Mock private SellerAddressJpaEntityMapper mapper;

    @InjectMocks private SellerAddressQueryAdapter queryAdapter;

    // ========================================================================
    // 1. findById 테스트
    // ========================================================================

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회 시 Domain을 반환합니다")
        void findById_WithExistingId_ReturnsDomain() {
            // given
            SellerAddressId id = SellerAddressId.of(1L);
            SellerAddressJpaEntity entity = SellerAddressJpaEntityFixtures.activeShippingEntity();
            SellerAddress domain = SellerFixtures.activeShippingAddress();

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<SellerAddress> result = queryAdapter.findById(id);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환합니다")
        void findById_WithNonExistingId_ReturnsEmpty() {
            // given
            SellerAddressId id = SellerAddressId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<SellerAddress> result = queryAdapter.findById(id);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 2. findBySellerId 테스트
    // ========================================================================

    @Nested
    @DisplayName("findBySellerId 메서드 테스트")
    class FindBySellerIdTest {

        @Test
        @DisplayName("존재하는 셀러 ID로 조회 시 Domain을 반환합니다")
        void findBySellerId_WithExistingSellerId_ReturnsDomain() {
            // given
            SellerId sellerId = SellerId.of(1L);
            SellerAddressJpaEntity entity = SellerAddressJpaEntityFixtures.activeShippingEntity();
            SellerAddress domain = SellerFixtures.activeShippingAddress();

            given(queryDslRepository.findBySellerId(1L)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(domain);

            // when
            Optional<SellerAddress> result = queryAdapter.findBySellerId(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(domain);
        }

        @Test
        @DisplayName("존재하지 않는 셀러 ID로 조회 시 빈 Optional을 반환합니다")
        void findBySellerId_WithNonExistingSellerId_ReturnsEmpty() {
            // given
            SellerId sellerId = SellerId.of(999L);
            given(queryDslRepository.findBySellerId(999L)).willReturn(Optional.empty());

            // when
            Optional<SellerAddress> result = queryAdapter.findBySellerId(sellerId);

            // then
            assertThat(result).isEmpty();
        }
    }

    // ========================================================================
    // 3. existsBySellerId 테스트
    // ========================================================================

    @Nested
    @DisplayName("existsBySellerId 메서드 테스트")
    class ExistsBySellerIdTest {

        @Test
        @DisplayName("존재하는 셀러 ID로 조회 시 true를 반환합니다")
        void existsBySellerId_WithExistingSellerId_ReturnsTrue() {
            // given
            SellerId sellerId = SellerId.of(1L);
            given(queryDslRepository.existsBySellerId(1L)).willReturn(true);

            // when
            boolean result = queryAdapter.existsBySellerId(sellerId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 셀러 ID로 조회 시 false를 반환합니다")
        void existsBySellerId_WithNonExistingSellerId_ReturnsFalse() {
            // given
            SellerId sellerId = SellerId.of(999L);
            given(queryDslRepository.existsBySellerId(999L)).willReturn(false);

            // when
            boolean result = queryAdapter.existsBySellerId(sellerId);

            // then
            assertThat(result).isFalse();
        }
    }
}
