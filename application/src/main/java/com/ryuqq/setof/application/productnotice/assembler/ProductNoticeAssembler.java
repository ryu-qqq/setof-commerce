package com.ryuqq.setof.application.productnotice.assembler;

import com.ryuqq.setof.application.productnotice.dto.response.NoticeItemResponse;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeResponse;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품고시 Assembler
 *
 * <p>Domain ↔ Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticeAssembler {

    /**
     * Domain → Response 변환
     *
     * @param domain 상품고시 도메인
     * @return Response DTO
     */
    public ProductNoticeResponse toResponse(ProductNotice domain) {
        List<NoticeItemResponse> itemResponses =
                domain.getItems().stream().map(this::toItemResponse).toList();

        return new ProductNoticeResponse(
                domain.getIdValue(),
                domain.getProductGroupId(),
                domain.getTemplateIdValue(),
                itemResponses,
                domain.getItemCount(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }

    /**
     * 항목 Domain → Response 변환
     *
     * @param item 항목 VO
     * @return 항목 Response DTO
     */
    private NoticeItemResponse toItemResponse(NoticeItem item) {
        return new NoticeItemResponse(
                item.fieldKey(), item.fieldValue(), item.displayOrder(), item.hasValue());
    }
}
