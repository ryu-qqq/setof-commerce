package com.ryuqq.setof.storage.legacy.composite.payment.mapper;

import com.ryuqq.setof.domain.payment.vo.PaymentOverview;
import com.ryuqq.setof.storage.legacy.composite.payment.dto.LegacyPaymentOverviewFlatDto;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentCompositeMapper - 결제 Composite Flat DTO → 도메인 VO 변환 Mapper.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * <p>결제 1건에 여러 주문(orderId)이 묶이므로 paymentId 기준으로 GROUP BY 후 orderId를 집계합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentCompositeMapper {

    /**
     * 결제 목록 개요 flat DTO 목록 → PaymentOverview 목록 변환.
     *
     * <p>paymentId 기준으로 groupBy 처리하여 orderId를 Set으로 집계합니다.
     *
     * @param dtos 결제 목록 개요 flat DTO 목록
     * @return PaymentOverview 도메인 VO 목록
     */
    public List<PaymentOverview> toOverviews(List<LegacyPaymentOverviewFlatDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }

        Map<Long, GroupedOverview> grouped = new LinkedHashMap<>();

        for (LegacyPaymentOverviewFlatDto dto : dtos) {
            grouped.computeIfAbsent(dto.paymentId(), id -> new GroupedOverview(dto))
                    .addOrderId(dto.orderId());
        }

        return grouped.values().stream()
                .map(g -> toOverview(g.representative, g.orderIds))
                .toList();
    }

    private PaymentOverview toOverview(LegacyPaymentOverviewFlatDto dto, Set<Long> orderIds) {
        return PaymentOverview.of(
                dto.paymentId(),
                dto.paymentStatus(),
                dto.paymentMethod(),
                dto.paymentDate(),
                dto.canceledDate(),
                dto.paymentAmount(),
                dto.usedMileageAmount(),
                dto.paymentAgencyId(),
                dto.cardName(),
                dto.cardNumber(),
                orderIds,
                dto.vBankName(),
                dto.vBankNumber(),
                dto.vBankPaymentAmount(),
                dto.vBankDueDate());
    }

    /** 집계용 내부 클래스. */
    private static final class GroupedOverview {

        private final LegacyPaymentOverviewFlatDto representative;
        private final Set<Long> orderIds;

        private GroupedOverview(LegacyPaymentOverviewFlatDto dto) {
            this.representative = dto;
            this.orderIds = new LinkedHashSet<>();
            this.orderIds.add(dto.orderId());
        }

        private void addOrderId(long orderId) {
            orderIds.add(orderId);
        }
    }
}
