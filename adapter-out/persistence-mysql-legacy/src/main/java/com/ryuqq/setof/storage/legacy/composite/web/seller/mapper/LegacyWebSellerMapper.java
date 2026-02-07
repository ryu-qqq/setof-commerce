package com.ryuqq.setof.storage.legacy.composite.web.seller.mapper;

import com.ryuqq.setof.application.legacy.seller.dto.response.LegacySellerResult;
import com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSellerMapper - 레거시 Web 판매자 Mapper.
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
public class LegacyWebSellerMapper {

    /**
     * QueryDto → LegacySellerResult 변환.
     *
     * @param dto QueryDto
     * @return LegacySellerResult
     */
    public LegacySellerResult toResult(LegacyWebSellerQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacySellerResult.of(
                dto.sellerId(),
                dto.sellerName(),
                dto.logoUrl(),
                dto.sellerDescription(),
                dto.address(),
                dto.csPhoneNumber(),
                dto.alimTalkPhoneNumber(),
                dto.registrationNumber(),
                dto.saleReportNumber(),
                dto.representative(),
                dto.email());
    }

    /**
     * QueryDto Optional → LegacySellerResult Optional 변환.
     *
     * @param dto QueryDto Optional
     * @return LegacySellerResult Optional
     */
    public Optional<LegacySellerResult> toResult(Optional<LegacyWebSellerQueryDto> dto) {
        return dto.map(this::toResult);
    }
}
