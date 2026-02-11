package com.ryuqq.setof.storage.legacy.composite.web.user.mapper;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyRefundAccountResult;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebRefundAccountQueryDto;
import org.springframework.stereotype.Component;

/**
 * 레거시 환불 계좌 Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebRefundAccountMapper {

    public LegacyRefundAccountResult toResult(LegacyWebRefundAccountQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyRefundAccountResult.of(
                dto.refundAccountId(),
                dto.bankName(),
                dto.accountNumber(),
                dto.accountHolderName());
    }
}
