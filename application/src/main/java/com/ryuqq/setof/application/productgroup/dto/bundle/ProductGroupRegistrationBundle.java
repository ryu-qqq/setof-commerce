package com.ryuqq.setof.application.productgroup.dto.bundle;

import com.ryuqq.setof.application.product.dto.command.RegisterProductsCommand;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import java.time.Instant;
import java.util.List;

/**
 * 상품그룹 등록 번들.
 *
 * <p>도메인 객체와 하위 도메인 등록에 필요한 per-package Command inner record를 하나의 번들로 묶어 전달합니다.
 *
 * @param productGroup 상품그룹 도메인 객체
 * @param images 이미지 커맨드 목록
 * @param optionType 옵션 유형 (NONE, SINGLE, COMBINATION)
 * @param optionGroups 옵션 그룹 커맨드 목록 (RegisterProductGroupCommand.OptionGroupCommand 사용)
 * @param descriptionContent 상세설명 HTML 컨텐츠 (nullable)
 * @param descriptionImages 상세설명 이미지 커맨드 목록
 * @param noticeEntries 상품고시 항목 커맨드 목록
 * @param products 상품 데이터 목록
 * @param createdAt 생성 시각
 */
public record ProductGroupRegistrationBundle(
        ProductGroup productGroup,
        List<RegisterProductGroupImagesCommand.ImageCommand> images,
        String optionType,
        List<RegisterProductGroupCommand.OptionGroupCommand> optionGroups,
        String descriptionContent,
        List<RegisterProductGroupCommand.DescriptionImageCommand> descriptionImages,
        List<RegisterProductNoticeCommand.NoticeEntryCommand> noticeEntries,
        List<RegisterProductsCommand.ProductData> products,
        Instant createdAt) {

    public ProductGroupRegistrationBundle {
        images = images != null ? List.copyOf(images) : List.of();
        optionGroups = optionGroups != null ? List.copyOf(optionGroups) : List.of();
        descriptionImages = descriptionImages != null ? List.copyOf(descriptionImages) : List.of();
        noticeEntries = noticeEntries != null ? List.copyOf(noticeEntries) : List.of();
        products = products != null ? List.copyOf(products) : List.of();
    }
}
