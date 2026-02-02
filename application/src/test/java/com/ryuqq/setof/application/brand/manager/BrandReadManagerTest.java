package com.ryuqq.setof.application.brand.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.BrandFixtures;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.exception.BrandNotFoundException;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
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
@DisplayName("BrandReadManager 단위 테스트")
class BrandReadManagerTest {

    @InjectMocks private BrandReadManager sut;

    @Mock private BrandQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 브랜드 조회")
    class GetByIdTest {

        @Test
        @DisplayName("ID로 브랜드를 조회한다")
        void getById_ExistingBrand_ReturnsBrand() {
            // given
            BrandId id = BrandId.of(1L);
            Brand expected = BrandFixtures.activeBrand();

            given(queryPort.findById(id)).willReturn(Optional.of(expected));

            // when
            Brand result = sut.getById(id);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다")
        void getById_NonExistingBrand_ThrowsException() {
            // given
            BrandId id = BrandId.of(999L);

            given(queryPort.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(id)).isInstanceOf(BrandNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByCriteria() - 검색 조건으로 브랜드 조회")
    class FindByCriteriaTest {

        @Test
        @DisplayName("검색 조건으로 브랜드 목록을 조회한다")
        void findByCriteria_ReturnsMatchingBrands() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();
            List<Brand> expected =
                    List.of(BrandFixtures.activeBrand(), BrandFixtures.inactiveBrand());

            given(queryPort.findByCriteria(criteria)).willReturn(expected);

            // when
            List<Brand> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("검색 결과가 없으면 빈 목록을 반환한다")
        void findByCriteria_NoResults_ReturnsEmptyList() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();

            given(queryPort.findByCriteria(criteria)).willReturn(Collections.emptyList());

            // when
            List<Brand> result = sut.findByCriteria(criteria);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("countByCriteria() - 검색 조건으로 브랜드 수 조회")
    class CountByCriteriaTest {

        @Test
        @DisplayName("검색 조건에 맞는 브랜드 수를 반환한다")
        void countByCriteria_ReturnsCount() {
            // given
            BrandSearchCriteria criteria = BrandSearchCriteria.displayedOnly();
            long expectedCount = 5L;

            given(queryPort.countByCriteria(criteria)).willReturn(expectedCount);

            // when
            long result = sut.countByCriteria(criteria);

            // then
            assertThat(result).isEqualTo(expectedCount);
        }
    }
}
