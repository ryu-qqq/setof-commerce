package com.ryuqq.setof.application.productgroup.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.port.out.query.ProductGroupQueryPort;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.exception.ProductGroupNotFoundException;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
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
@DisplayName("ProductGroupReadManager 단위 테스트")
class ProductGroupReadManagerTest {

    @InjectMocks private ProductGroupReadManager sut;

    @Mock private ProductGroupQueryPort queryPort;

    @Nested
    @DisplayName("getById() - ID로 상품그룹 조회")
    class GetByIdTest {

        @Test
        @DisplayName("존재하는 ID로 조회하면 상품그룹을 반환한다")
        void getById_ExistingProductGroup_ReturnsProductGroup() {
            // given
            ProductGroupId id = ProductGroupId.of(1L);
            ProductGroup expected = ProductGroupFixtures.activeProductGroup(1L);

            given(queryPort.findById(id)).willReturn(Optional.of(expected));

            // when
            ProductGroup result = sut.getById(id);

            // then
            assertThat(result).isEqualTo(expected);
            then(queryPort).should().findById(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 ProductGroupNotFoundException이 발생한다")
        void getById_NonExistingProductGroup_ThrowsProductGroupNotFoundException() {
            // given
            ProductGroupId id = ProductGroupId.of(999L);

            given(queryPort.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> sut.getById(id))
                    .isInstanceOf(ProductGroupNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findByIds() - ID 목록으로 상품그룹 목록 조회")
    class FindByIdsTest {

        @Test
        @DisplayName("ID 목록으로 상품그룹 목록을 반환한다")
        void findByIds_ValidIds_ReturnsProductGroups() {
            // given
            List<ProductGroupId> ids = List.of(ProductGroupId.of(1L), ProductGroupId.of(2L));
            List<ProductGroup> expected =
                    List.of(
                            ProductGroupFixtures.activeProductGroup(1L),
                            ProductGroupFixtures.activeProductGroup(2L));

            given(queryPort.findByIds(ids)).willReturn(expected);

            // when
            List<ProductGroup> result = sut.findByIds(ids);

            // then
            assertThat(result).hasSize(2);
            then(queryPort).should().findByIds(ids);
        }

        @Test
        @DisplayName("빈 ID 목록으로 조회하면 빈 결과를 반환한다")
        void findByIds_EmptyIds_ReturnsEmptyList() {
            // given
            List<ProductGroupId> ids = Collections.emptyList();

            given(queryPort.findByIds(ids)).willReturn(Collections.emptyList());

            // when
            List<ProductGroup> result = sut.findByIds(ids);

            // then
            assertThat(result).isEmpty();
        }
    }
}
