package com.ryuqq.setof.storage.legacy.composite.web.order.mapper;

import com.ryuqq.setof.application.legacy.order.dto.response.LegacyOrderResult;
import com.ryuqq.setof.storage.legacy.composite.web.order.dto.LegacyWebOrderQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebOrderMapper - 레거시 주문 Mapper.
 *
 * <p>QueryDto → Application Result 변환을 담당합니다.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebOrderMapper {

    /**
     * QueryDto → LegacyOrderResult 변환.
     *
     * @param dto QueryDto
     * @return LegacyOrderResult
     */
    public LegacyOrderResult toResult(LegacyWebOrderQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyOrderResult.of(
                dto.orderId(),
                dto.paymentId(),
                dto.productId(),
                dto.sellerId(),
                dto.userId(),
                dto.orderAmount(),
                dto.orderStatus(),
                dto.quantity(),
                dto.reviewYn(),
                dto.insertDate(),
                dto.updateDate());
    }

    /**
     * QueryDto 목록 → LegacyOrderResult 목록 변환.
     *
     * @param dtos QueryDto 목록
     * @return LegacyOrderResult 목록
     */
    public List<LegacyOrderResult> toResults(List<LegacyWebOrderQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
