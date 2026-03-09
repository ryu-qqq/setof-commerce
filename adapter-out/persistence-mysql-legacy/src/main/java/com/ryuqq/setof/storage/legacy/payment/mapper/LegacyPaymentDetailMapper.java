package com.ryuqq.setof.storage.legacy.payment.mapper;

import com.ryuqq.setof.domain.payment.vo.PaymentFullDetail;
import com.ryuqq.setof.storage.legacy.payment.dto.LegacyPaymentDetailFlatDto;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * LegacyPaymentDetailMapper - 결제 단건 상세 Flat DTO → 도메인 VO 변환 Mapper.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: 수동 매핑 구현 (MapStruct 사용 안함).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyPaymentDetailMapper {

    /**
     * 결제 상세 flat DTO + 주문 ID 목록 → PaymentFullDetail 변환.
     *
     * @param dto 결제 상세 flat DTO (1행)
     * @param orderIds 해당 결제에 포함된 주문 ID 목록
     * @return PaymentFullDetail 도메인 VO (dto가 null이면 null)
     */
    public PaymentFullDetail toFullDetail(LegacyPaymentDetailFlatDto dto, Set<Long> orderIds) {
        if (dto == null) {
            return null;
        }

        return PaymentFullDetail.of(
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
                dto.vBankDueDate(),
                dto.buyerName(),
                dto.buyerEmail(),
                dto.buyerPhoneNumber(),
                dto.receiverName(),
                dto.receiverPhoneNumber(),
                dto.addressLine1(),
                dto.addressLine2(),
                dto.zipCode(),
                dto.country(),
                dto.deliveryRequest(),
                dto.phoneNumber(),
                dto.refundBankName(),
                dto.refundAccountNumber(),
                dto.refundAccountId(),
                dto.refundAccountHolderName());
    }
}
