package com.ryuqq.setof.application.productgroup.factory;

import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupName;
import com.ryuqq.setof.domain.productgroup.vo.ProductGroupUpdateData;
import com.ryuqq.setof.domain.refundpolicy.id.RefundPolicyId;
import com.ryuqq.setof.domain.shippingpolicy.id.ShippingPolicyId;
import java.time.Instant;
import org.springframework.stereotype.Component;

/** ProductGroupCommandFactory - 상품그룹 커맨드에서 도메인 수정 데이터를 생성하는 Factory. */
@Component
public class ProductGroupCommandFactory {

    /**
     * 기본정보 수정 커맨드로부터 ProductGroupUpdateData를 생성합니다.
     *
     * <p>optionType은 기본정보 수정에서 변경하지 않으므로 기존 값을 전달받습니다.
     *
     * @param command 기본정보 수정 커맨드
     * @param existingOptionType 기존 옵션 타입
     * @param now 수정 시각
     * @return ProductGroupUpdateData 인스턴스
     */
    public ProductGroupUpdateData createUpdateData(
            UpdateProductGroupBasicInfoCommand command,
            com.ryuqq.setof.domain.productgroup.vo.OptionType existingOptionType,
            Instant now) {
        return ProductGroupUpdateData.of(
                ProductGroupId.of(command.productGroupId()),
                ProductGroupName.of(command.productGroupName()),
                BrandId.of(command.brandId()),
                CategoryId.of(command.categoryId()),
                ShippingPolicyId.of(command.shippingPolicyId()),
                RefundPolicyId.of(command.refundPolicyId()),
                existingOptionType,
                now);
    }
}
