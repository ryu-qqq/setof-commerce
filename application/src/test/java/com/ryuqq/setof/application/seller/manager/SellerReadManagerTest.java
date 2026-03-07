package com.ryuqq.setof.application.seller.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.seller.SellerDomainFixtures;
import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.Collections;
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

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("SellerReadManager 단위 테스트")
class SellerReadManagerTest {

    @InjectMocks private SellerReadManager sut;

    @Mock private SellerQueryPort sellerQueryPort;

    @Nested
    @DisplayName("getById() - ID로 셀러 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 셀러를 ID로 조회한다")
        void getById_ExistingSeller_ReturnsSeller() {
            // given
            Long sellerId = 1L;
            Seller expected = SellerDomainFixtures.activeSeller();

            given(sellerQueryPort.findById(sellerId)).willReturn(Optional.of(expected));

            // when
            Seller result = sut.getById(sellerId);

            // then
            assertThat(result).isEqualTo(expected);
            then(sellerQueryPort).should().findById(sellerId);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getById_NonExistingSeller_ThrowsException() {
            // given
            Long sellerId = 999L;

            given(sellerQueryPort.findById(sellerId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(sellerId))
                    .isInstanceOf(SellerNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getByIds() - ID 목록으로 셀러 조회")
    class GetByIdsTest {

        @Test
        @DisplayName("ID 목록으로 셀러 목록을 조회한다")
        void getByIds_ReturnsSellerList() {
            // given
            List<Long> ids = List.of(1L, 2L);
            List<Seller> expected = SellerDomainFixtures.activeSellers();

            given(sellerQueryPort.findByIds(ids)).willReturn(expected);

            // when
            List<Seller> result = sut.getByIds(ids);

            // then
            assertThat(result).hasSize(2);
            then(sellerQueryPort).should().findByIds(ids);
        }
    }

    @Nested
    @DisplayName("existsById() - ID 존재 여부 확인")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 셀러 ID이면 true를 반환한다")
        void existsById_ExistingSeller_ReturnsTrue() {
            // given
            Long sellerId = 1L;

            given(sellerQueryPort.existsById(sellerId)).willReturn(true);

            // when
            boolean result = sut.existsById(sellerId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 셀러 ID이면 false를 반환한다")
        void existsById_NonExistingSeller_ReturnsFalse() {
            // given
            Long sellerId = 999L;

            given(sellerQueryPort.existsById(sellerId)).willReturn(false);

            // when
            boolean result = sut.existsById(sellerId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsBySellerName() - 셀러명 존재 여부 확인")
    class ExistsBySellerNameTest {

        @Test
        @DisplayName("이미 사용 중인 셀러명이면 true를 반환한다")
        void existsBySellerName_DuplicateName_ReturnsTrue() {
            // given
            String sellerName = "테스트셀러";

            given(sellerQueryPort.existsBySellerName(sellerName)).willReturn(true);

            // when
            boolean result = sut.existsBySellerName(sellerName);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("사용 가능한 셀러명이면 false를 반환한다")
        void existsBySellerName_UniqueName_ReturnsFalse() {
            // given
            String sellerName = "새로운셀러";

            given(sellerQueryPort.existsBySellerName(sellerName)).willReturn(false);

            // when
            boolean result = sut.existsBySellerName(sellerName);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("existsBySellerNameExcluding() - 특정 셀러 제외한 셀러명 존재 여부 확인")
    class ExistsBySellerNameExcludingTest {

        @Test
        @DisplayName("다른 셀러가 동일한 이름을 사용 중이면 true를 반환한다")
        void existsBySellerNameExcluding_OtherSellerUsingSameName_ReturnsTrue() {
            // given
            String sellerName = "중복셀러";
            SellerId excludeId = SellerId.of(1L);

            given(sellerQueryPort.existsBySellerNameExcluding(sellerName, excludeId))
                    .willReturn(true);

            // when
            boolean result = sut.existsBySellerNameExcluding(sellerName, excludeId);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("다른 셀러가 동일한 이름을 사용하지 않으면 false를 반환한다")
        void existsBySellerNameExcluding_NoOtherSellerUsingSameName_ReturnsFalse() {
            // given
            String sellerName = "유일한셀러";
            SellerId excludeId = SellerId.of(1L);

            given(sellerQueryPort.existsBySellerNameExcluding(sellerName, excludeId))
                    .willReturn(false);

            // when
            boolean result = sut.existsBySellerNameExcluding(sellerName, excludeId);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 셀러 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 셀러 목록을 조회한다")
        void findByCriteria_ReturnsMatchingSellers() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.activeOnly();
            List<Seller> expected = SellerDomainFixtures.activeSellers();

            given(sellerQueryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<Seller> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
            then(sellerQueryPort).should().findByCriteria(criteria);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.activeOnly();

            given(sellerQueryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<Seller> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countByCriteria() - 검색 조건으로 셀러 수 조회")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 셀러 수를 반환한다")
        void countByCriteria_ReturnsCount() {
            // given
            SellerSearchCriteria criteria = SellerSearchCriteria.activeOnly();
            long expectedCount = 5L;

            given(sellerQueryPort.countByCriteria(criteria)).willReturn(expectedCount);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
            then(sellerQueryPort).should().countByCriteria(criteria);
        }
    }
}
