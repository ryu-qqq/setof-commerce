package com.ryuqq.setof.application.productgroup.dto.command;

import com.ryuqq.setof.application.product.dto.command.SelectedOption;
import java.util.List;

/**
 * 상품그룹 전체 등록 커맨드.
 *
 * <p>상품그룹 기본정보, 이미지, 옵션, 상품, 상세설명, 고시를 한번에 등록하기 위한 입력 데이터입니다.
 *
 * @param productGroupId 상품그룹 ID (nullable, 마이그레이션 시 레거시 DB ID 동기화용)
 * @param sellerId 셀러 ID
 * @param brandId 브랜드 ID
 * @param categoryId 카테고리 ID
 * @param shippingPolicyId 배송정책 ID
 * @param refundPolicyId 환불정책 ID
 * @param productGroupName 상품그룹명
 * @param optionType 옵션 유형 (NONE, SINGLE, COMBINATION)
 * @param regularPrice 정가
 * @param currentPrice 판매가
 * @param images 상품그룹 이미지 목록
 * @param optionGroups 셀러 옵션 그룹 목록
 * @param products 상품 목록
 * @param description 상세설명
 * @param notice 상품고시
 */
public record RegisterProductGroupCommand(
        Long productGroupId,
        long sellerId,
        long brandId,
        long categoryId,
        long shippingPolicyId,
        long refundPolicyId,
        String productGroupName,
        String optionType,
        int regularPrice,
        int currentPrice,
        List<ImageCommand> images,
        List<OptionGroupCommand> optionGroups,
        List<ProductCommand> products,
        DescriptionCommand description,
        NoticeCommand notice) {

    /**
     * 상품그룹 이미지 커맨드.
     *
     * @param imageType 이미지 유형 (THUMBNAIL, DETAIL)
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     */
    public record ImageCommand(String imageType, String imageUrl, int sortOrder) {}

    /**
     * 셀러 옵션 그룹 커맨드.
     *
     * @param optionGroupName 옵션 그룹명
     * @param sortOrder 정렬 순서
     * @param optionValues 옵션 값 목록
     */
    public record OptionGroupCommand(
            String optionGroupName, int sortOrder, List<OptionValueCommand> optionValues) {}

    /**
     * 셀러 옵션 값 커맨드.
     *
     * @param optionValueName 옵션 값 이름
     * @param sortOrder 정렬 순서
     */
    public record OptionValueCommand(String optionValueName, int sortOrder) {}

    /**
     * 상품 커맨드.
     *
     * @param productId 상품 ID (nullable, 마이그레이션 시 레거시 DB ID 동기화용)
     * @param skuCode SKU 코드
     * @param regularPrice 정가
     * @param currentPrice 판매가
     * @param stockQuantity 재고 수량
     * @param sortOrder 정렬 순서
     * @param selectedOptions 선택된 옵션 목록
     */
    public record ProductCommand(
            Long productId,
            String skuCode,
            int regularPrice,
            int currentPrice,
            int stockQuantity,
            int sortOrder,
            List<SelectedOption> selectedOptions) {}

    /**
     * 상세설명 커맨드.
     *
     * @param content 상세설명 HTML 컨텐츠
     * @param descriptionImages 상세설명 이미지 목록
     */
    public record DescriptionCommand(
            String content, List<DescriptionImageCommand> descriptionImages) {}

    /**
     * 상세설명 이미지 커맨드.
     *
     * @param imageUrl 이미지 URL
     * @param sortOrder 정렬 순서
     */
    public record DescriptionImageCommand(String imageUrl, int sortOrder) {}

    /**
     * 상품고시 커맨드.
     *
     * @param entries 고시 항목 목록
     */
    public record NoticeCommand(List<NoticeEntryCommand> entries) {}

    /**
     * 상품고시 항목 커맨드.
     *
     * @param noticeFieldId 고시 필드 ID
     * @param fieldName 필드명
     * @param fieldValue 필드값
     */
    public record NoticeEntryCommand(long noticeFieldId, String fieldName, String fieldValue) {}
}
