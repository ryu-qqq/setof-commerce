package com.ryuqq.setof.application.productgroup.internal;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ryuqq.setof.application.productgroup.manager.ProductGroupCommandManager;
import com.ryuqq.setof.domain.productgroup.ProductGroupFixtures;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductGroupCommandCoordinator 단위 테스트")
class ProductGroupCommandCoordinatorTest {

    @InjectMocks private ProductGroupCommandCoordinator sut;

    @Mock private ProductGroupCommandManager commandManager;

    @Nested
    @DisplayName("updateBasicInfo() - 상품그룹 기본정보 수정")
    class UpdateBasicInfoTest {

        @Test
        @DisplayName("상품그룹에 수정 데이터를 적용하고 CommandManager에 저장을 위임한다")
        void updateBasicInfo_ValidData_UpdatesAndPersists() {
            // given
            long productGroupId = 1L;
            ProductGroup productGroup = ProductGroupFixtures.activeProductGroup(productGroupId);
            ProductGroupUpdateData updateData = Mockito.mock(ProductGroupUpdateData.class);
            Long expectedId = productGroupId;

            given(commandManager.persist(productGroup)).willReturn(expectedId);

            // when
            sut.updateBasicInfo(productGroup, updateData);

            // then
            then(commandManager).should().persist(productGroup);
        }

        @Test
        @DisplayName("CommandManager.persist가 호출되어 변경사항이 저장된다")
        void updateBasicInfo_CallsPersist_SavesChanges() {
            // given
            ProductGroup productGroup = ProductGroupFixtures.activeProductGroup(1L);
            ProductGroupUpdateData updateData = Mockito.mock(ProductGroupUpdateData.class);

            given(commandManager.persist(productGroup)).willReturn(1L);

            // when
            sut.updateBasicInfo(productGroup, updateData);

            // then
            then(commandManager).should().persist(productGroup);
            then(commandManager).shouldHaveNoMoreInteractions();
        }
    }
}
