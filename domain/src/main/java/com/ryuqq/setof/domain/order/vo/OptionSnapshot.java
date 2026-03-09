package com.ryuqq.setof.domain.order.vo;

/**
 * 주문 시점 옵션 스냅샷 VO.
 *
 * <p>주문 생성 시 옵션 정보를 복사하여 보관합니다. 이후 옵션이 변경되어도 주문 시점의 정보를 유지합니다.
 *
 * @param optionGroupId 옵션 그룹 ID
 * @param optionDetailId 옵션 상세 ID
 * @param optionName 옵션 그룹명 (예: 사이즈)
 * @param optionValue 옵션 값 (예: 270mm)
 */
public record OptionSnapshot(
        long optionGroupId, long optionDetailId, String optionName, String optionValue) {

    public OptionSnapshot {
        if (optionName == null || optionName.isBlank()) {
            throw new IllegalArgumentException("옵션명은 필수입니다");
        }
    }

    public static OptionSnapshot of(
            long optionGroupId, long optionDetailId, String optionName, String optionValue) {
        return new OptionSnapshot(optionGroupId, optionDetailId, optionName, optionValue);
    }
}
