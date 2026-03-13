package com.ryuqq.setof.application.productgroup.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.application.productgroup.ProductGroupCommandFixtures;
import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.domain.productgroup.vo.OptionType;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ProductGroupCommandFactory 단위 테스트")
class ProductGroupCommandFactoryTest {

    private ProductGroupCommandFactory sut;

    @BeforeEach
    void setUp() {
        sut = new ProductGroupCommandFactory();
    }

    @Nested
    @DisplayName("createUpdateData() - 기본정보 수정 데이터 생성")
    class CreateUpdateDataTest {

        @Test
        @DisplayName("기본정보 수정 커맨드를 ProductGroupUpdateData로 변환한다")
        void createUpdateData_ValidCommand_ReturnsProductGroupUpdateData() {
            // given
            long productGroupId = 1L;
            UpdateProductGroupBasicInfoCommand command =
                    ProductGroupCommandFixtures.updateBasicInfoCommand(productGroupId);
            OptionType existingOptionType = OptionType.SINGLE;
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            ProductGroupUpdateData result = sut.createUpdateData(command, existingOptionType, now);

            // then
            assertThat(result).isNotNull();
            assertThat(result.productGroupId().value()).isEqualTo(productGroupId);
            assertThat(result.productGroupName().value()).isEqualTo("수정된 기본정보 상품그룹");
            assertThat(result.brandId().value()).isEqualTo(2L);
            assertThat(result.categoryId().value()).isEqualTo(2L);
            assertThat(result.shippingPolicyId().value()).isEqualTo(2L);
            assertThat(result.refundPolicyId().value()).isEqualTo(2L);
            assertThat(result.optionType()).isEqualTo(existingOptionType);
            assertThat(result.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("기존 optionType이 그대로 유지된다")
        void createUpdateData_PreservesExistingOptionType() {
            // given
            UpdateProductGroupBasicInfoCommand command =
                    ProductGroupCommandFixtures.updateBasicInfoCommand(1L);
            OptionType existingOptionType = OptionType.COMBINATION;
            Instant now = Instant.parse("2024-01-01T00:00:00Z");

            // when
            ProductGroupUpdateData result = sut.createUpdateData(command, existingOptionType, now);

            // then
            assertThat(result.optionType()).isEqualTo(OptionType.COMBINATION);
        }
    }
}
