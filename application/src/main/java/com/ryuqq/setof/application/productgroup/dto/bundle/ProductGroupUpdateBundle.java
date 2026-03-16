package com.ryuqq.setof.application.productgroup.dto.bundle;

import com.ryuqq.setof.application.product.dto.command.ProductDiffUpdateEntry;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import java.time.Instant;
import java.util.List;

/**
 * 상품그룹 수정 번들.
 *
 * <p>기존 도메인 객체와 per-package Update Command들을 하나의 번들로 묶어 Coordinator에 전달합니다. Factory에서 Command 변환을
 * 책임지며, Coordinator는 위임만 수행합니다.
 *
 * @param productGroup 기존 상품그룹 도메인 객체
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param imageCommand 이미지 수정 커맨드 (null 허용)
 * @param optionGroupCommand 셀러 옵션 그룹 수정 커맨드 (null 허용)
 * @param descriptionCommand 상세설명 수정 커맨드 (null 허용)
 * @param noticeCommand 상품고시 수정 커맨드 (null 허용)
 * @param productEntries 상품 diff 수정 엔트리 목록
 * @param updatedAt 수정 시각
 */
public record ProductGroupUpdateBundle(
        ProductGroup productGroup,
        int regularPrice,
        int currentPrice,
        UpdateProductGroupImagesCommand imageCommand,
        UpdateSellerOptionGroupsCommand optionGroupCommand,
        UpdateProductGroupDescriptionCommand descriptionCommand,
        UpdateProductNoticeCommand noticeCommand,
        List<ProductDiffUpdateEntry> productEntries,
        Instant updatedAt) {

    public ProductGroupUpdateBundle {
        productEntries = productEntries != null ? List.copyOf(productEntries) : List.of();
    }
}
