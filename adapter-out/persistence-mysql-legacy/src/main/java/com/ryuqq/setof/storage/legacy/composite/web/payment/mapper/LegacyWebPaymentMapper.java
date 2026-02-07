package com.ryuqq.setof.storage.legacy.composite.web.payment.mapper;

import com.ryuqq.setof.application.legacy.payment.dto.response.LegacyPaymentResult;
import com.ryuqq.setof.storage.legacy.composite.web.payment.dto.LegacyWebPaymentQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebPaymentMapper - 레거시 웹 결제 Mapper.
 *
 * <p>QueryDto → Application Result 변환
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebPaymentMapper {

    /**
     * QueryDto를 Result로 변환.
     *
     * @param dto QueryDto
     * @return Result
     */
    public LegacyPaymentResult toResult(LegacyWebPaymentQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyPaymentResult.of(
                dto.paymentId(),
                dto.userId(),
                dto.paymentStatus(),
                dto.paymentAmount(),
                dto.usedMileageAmount(),
                dto.paymentAgencyId(),
                dto.paymentMethodEnum(),
                dto.paymentDate(),
                dto.canceledDate(),
                dto.siteName());
    }

    /**
     * QueryDto 목록을 Result 목록으로 변환.
     *
     * @param dtos QueryDto 목록
     * @return Result 목록
     */
    public List<LegacyPaymentResult> toResults(List<LegacyWebPaymentQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }
}
