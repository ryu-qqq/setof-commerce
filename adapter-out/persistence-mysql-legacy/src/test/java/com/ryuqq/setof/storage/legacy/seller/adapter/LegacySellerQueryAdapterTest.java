package com.ryuqq.setof.storage.legacy.seller.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import com.ryuqq.setof.storage.legacy.seller.LegacySellerEntityFixtures;
import com.ryuqq.setof.storage.legacy.seller.entity.LegacySellerEntity;
import com.ryuqq.setof.storage.legacy.seller.mapper.LegacySellerEntityMapper;
import com.ryuqq.setof.storage.legacy.seller.repository.LegacySellerQueryDslRepository;
import java.util.List;
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
 * LegacySellerQueryAdapter 단위 테스트.
 *
 * <p>레거시 셀러 Query 어댑터 로직을 검증합니다.
 */
@DisplayName("레거시 셀러 QueryAdapter 테스트")
@Tag("unit")
@ExtendWith(MockitoExtension.class)
class LegacySellerQueryAdapterTest {

    @Mock private LegacySellerQueryDslRepository queryDslRepository;

    @Mock private LegacySellerEntityMapper mapper;

    @InjectMocks private LegacySellerQueryAdapter adapter;

    @Nested
    @DisplayName("findById 메서드 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("존재하는 셀러를 조회하면 Domain을 반환합니다")
        void shouldReturnSellerWhenExists() {
            // given
            SellerId sellerId = SellerId.of(1L);
            LegacySellerEntity entity = LegacySellerEntityFixtures.builder().id(1L).build();
            Seller expectedSeller = createMockSeller(1L);

            given(queryDslRepository.findById(1L)).willReturn(Optional.of(entity));
            given(mapper.toDomain(entity)).willReturn(expectedSeller);

            // when
            Optional<Seller> result = adapter.findById(sellerId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().idValue()).isEqualTo(1L);
            then(queryDslRepository).should().findById(1L);
            then(mapper).should().toDomain(entity);
        }

        @Test
        @DisplayName("존재하지 않는 셀러를 조회하면 빈 Optional을 반환합니다")
        void shouldReturnEmptyWhenNotExists() {
            // given
            SellerId sellerId = SellerId.of(999L);
            given(queryDslRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Optional<Seller> result = adapter.findById(sellerId);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findById(999L);
            then(mapper).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("findByIds 메서드 테스트")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 셀러 목록을 조회합니다")
        void shouldReturnSellersByIds() {
            // given
            List<SellerId> sellerIds = List.of(SellerId.of(1L), SellerId.of(2L));
            List<Long> idValues = List.of(1L, 2L);
            List<LegacySellerEntity> entities =
                    List.of(
                            LegacySellerEntityFixtures.builder().id(1L).build(),
                            LegacySellerEntityFixtures.builder().id(2L).build());
            List<Seller> expectedSellers = List.of(createMockSeller(1L), createMockSeller(2L));

            given(queryDslRepository.findByIds(idValues)).willReturn(entities);
            given(mapper.toDomain(entities.get(0))).willReturn(expectedSellers.get(0));
            given(mapper.toDomain(entities.get(1))).willReturn(expectedSellers.get(1));

            // when
            List<Seller> result = adapter.findByIds(sellerIds);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findByIds(idValues);
            then(mapper).should().toDomain(entities.get(0));
            then(mapper).should().toDomain(entities.get(1));
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회하면 빈 목록을 반환합니다")
        void shouldReturnEmptyListForEmptyIds() {
            // given
            List<SellerId> emptyIds = List.of();
            given(queryDslRepository.findByIds(anyList())).willReturn(List.of());

            // when
            List<Seller> result = adapter.findByIds(emptyIds);

            // then
            assertThat(result).isEmpty();
            then(queryDslRepository).should().findByIds(List.of());
        }
    }

    @Nested
    @DisplayName("existsById 메서드 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 true를 반환합니다")
        void shouldReturnTrueWhenExists() {
            // given
            SellerId sellerId = SellerId.of(1L);
            given(queryDslRepository.existsById(1L)).willReturn(true);

            // when
            boolean result = adapter.existsById(sellerId);

            // then
            assertThat(result).isTrue();
            then(queryDslRepository).should().existsById(1L);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 false를 반환합니다")
        void shouldReturnFalseWhenNotExists() {
            // given
            SellerId sellerId = SellerId.of(999L);
            given(queryDslRepository.existsById(999L)).willReturn(false);

            // when
            boolean result = adapter.existsById(sellerId);

            // then
            assertThat(result).isFalse();
            then(queryDslRepository).should().existsById(999L);
        }
    }

    @Nested
    @DisplayName("existsBySellerName 메서드 테스트")
    class ExistsBySellerNameTest {

        @Test
        @DisplayName("존재하는 셀러명으로 조회하면 true를 반환합니다")
        void shouldReturnTrueWhenSellerNameExists() {
            // given
            String sellerName = "테스트 셀러";
            given(queryDslRepository.existsBySellerName(sellerName)).willReturn(true);

            // when
            boolean result = adapter.existsBySellerName(sellerName);

            // then
            assertThat(result).isTrue();
            then(queryDslRepository).should().existsBySellerName(sellerName);
        }

        @Test
        @DisplayName("존재하지 않는 셀러명으로 조회하면 false를 반환합니다")
        void shouldReturnFalseWhenSellerNameNotExists() {
            // given
            String sellerName = "존재하지 않는 셀러";
            given(queryDslRepository.existsBySellerName(sellerName)).willReturn(false);

            // when
            boolean result = adapter.existsBySellerName(sellerName);

            // then
            assertThat(result).isFalse();
            then(queryDslRepository).should().existsBySellerName(sellerName);
        }
    }

    @Nested
    @DisplayName("existsBySellerNameExcluding 메서드 테스트")
    class ExistsBySellerNameExcludingTest {

        @Test
        @DisplayName("특정 ID를 제외하고 셀러명이 존재하면 true를 반환합니다")
        void shouldReturnTrueWhenSellerNameExistsExcludingId() {
            // given
            String sellerName = "테스트 셀러";
            SellerId excludeId = SellerId.of(1L);
            given(queryDslRepository.existsBySellerNameExcluding(sellerName, 1L)).willReturn(true);

            // when
            boolean result = adapter.existsBySellerNameExcluding(sellerName, excludeId);

            // then
            assertThat(result).isTrue();
            then(queryDslRepository).should().existsBySellerNameExcluding(sellerName, 1L);
        }

        @Test
        @DisplayName("특정 ID를 제외하고 셀러명이 존재하지 않으면 false를 반환합니다")
        void shouldReturnFalseWhenSellerNameNotExistsExcludingId() {
            // given
            String sellerName = "테스트 셀러";
            SellerId excludeId = SellerId.of(1L);
            given(queryDslRepository.existsBySellerNameExcluding(sellerName, 1L)).willReturn(false);

            // when
            boolean result = adapter.existsBySellerNameExcluding(sellerName, excludeId);

            // then
            assertThat(result).isFalse();
            then(queryDslRepository).should().existsBySellerNameExcluding(sellerName, 1L);
        }
    }

    @Nested
    @DisplayName("findByCriteria 메서드 테스트")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 셀러 목록을 조회합니다")
        void shouldReturnSellersByCriteria() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.defaultCriteria();
            List<LegacySellerEntity> entities =
                    List.of(
                            LegacySellerEntityFixtures.builder().id(1L).build(),
                            LegacySellerEntityFixtures.builder().id(2L).build());
            List<Seller> expectedSellers = List.of(createMockSeller(1L), createMockSeller(2L));

            given(queryDslRepository.findByCriteria(criteria)).willReturn(entities);
            given(mapper.toDomain(entities.get(0))).willReturn(expectedSellers.get(0));
            given(mapper.toDomain(entities.get(1))).willReturn(expectedSellers.get(1));

            // when
            List<Seller> result = adapter.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(queryDslRepository).should().findByCriteria(criteria);
            then(mapper).should().toDomain(entities.get(0));
            then(mapper).should().toDomain(entities.get(1));
        }
    }

    @Nested
    @DisplayName("countByCriteria 메서드 테스트")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 셀러 개수를 조회합니다")
        void shouldReturnCountByCriteria() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.defaultCriteria();
            given(queryDslRepository.countByCriteria(criteria)).willReturn(5L);

            // when
            long result = adapter.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(5L);
            then(queryDslRepository).should().countByCriteria(criteria);
        }
    }

    private Seller createMockSeller(Long id) {
        return Seller.reconstitute(
                SellerId.of(id),
                com.ryuqq.setof.domain.seller.vo.SellerName.of("테스트 셀러"),
                com.ryuqq.setof.domain.seller.vo.DisplayName.of("테스트 셀러"),
                com.ryuqq.setof.domain.seller.vo.LogoUrl.of("https://example.com/logo.png"),
                com.ryuqq.setof.domain.seller.vo.Description.of("테스트 셀러 설명"),
                true,
                null,
                null,
                null,
                java.time.Instant.now(),
                java.time.Instant.now());
    }
}
