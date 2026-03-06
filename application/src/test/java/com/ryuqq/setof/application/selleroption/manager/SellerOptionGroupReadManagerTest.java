package com.ryuqq.setof.application.selleroption.manager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.selleroption.port.out.query.SellerOptionGroupQueryPort;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import java.util.List;
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
@DisplayName("SellerOptionGroupReadManager 단위 테스트")
class SellerOptionGroupReadManagerTest {

    @InjectMocks private SellerOptionGroupReadManager sut;

    @Mock private SellerOptionGroupQueryPort queryPort;

    @Nested
    @DisplayName("getByProductGroupId() - ProductGroupId로 옵션 그룹 조회")
    class GetByProductGroupIdTest {

        @Test
        @DisplayName("ProductGroupId로 활성 SellerOptionGroups를 반환한다")
        void getByProductGroupId_ReturnsSellerOptionGroups() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(1L);
            List<SellerOptionGroup> groups = ProductGroupFixtures.defaultSellerOptionGroups();

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(groups);

            // when
            SellerOptionGroups result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.groups()).hasSize(1);
            then(queryPort).should().findByProductGroupId(productGroupId);
        }

        @Test
        @DisplayName("옵션 그룹이 없는 경우 빈 SellerOptionGroups를 반환한다")
        void getByProductGroupId_NoGroups_ReturnsEmptySellerOptionGroups() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(99L);

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(List.of());

            // when
            SellerOptionGroups result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.groups()).isEmpty();
            then(queryPort).should().findByProductGroupId(productGroupId);
        }

        @Test
        @DisplayName("COMBINATION 옵션 구성에서 두 개 그룹을 반환한다")
        void getByProductGroupId_CombinationGroups_ReturnsTwoGroups() {
            // given
            ProductGroupId productGroupId = ProductGroupId.of(2L);
            List<SellerOptionGroup> groups = ProductGroupFixtures.combinationSellerOptionGroups();

            given(queryPort.findByProductGroupId(productGroupId)).willReturn(groups);

            // when
            SellerOptionGroups result = sut.getByProductGroupId(productGroupId);

            // then
            assertThat(result.groups()).hasSize(2);
        }
    }
}
