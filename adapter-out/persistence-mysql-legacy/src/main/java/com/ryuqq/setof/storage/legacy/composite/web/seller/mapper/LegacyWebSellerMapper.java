package com.ryuqq.setof.storage.legacy.composite.web.seller.mapper;

import com.ryuqq.setof.application.seller.dto.composite.SellerCompositeResult;
import com.ryuqq.setof.storage.legacy.composite.web.seller.dto.LegacyWebSellerQueryDto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * LegacyWebSellerMapper - 레거시 Web 판매자 Mapper.
 *
 * <p>QueryDto → Application Layer DTO 변환을 담당합니다.
 *
 * <p>레거시 DB에 없는 필드는 null 또는 기본값으로 설정합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebSellerMapper {

    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * QueryDto → SellerCompositeResult 변환 (Customer용).
     *
     * @param dto QueryDto
     * @return SellerCompositeResult
     */
    public SellerCompositeResult toCompositeResult(LegacyWebSellerQueryDto dto) {
        if (dto == null) {
            return null;
        }

        SellerCompositeResult.SellerInfo sellerInfo =
                new SellerCompositeResult.SellerInfo(
                        dto.sellerId(),
                        dto.sellerName(),
                        dto.sellerName(),
                        dto.logoUrl(),
                        dto.sellerDescription(),
                        true,
                        toInstant(dto.createdAt()),
                        toInstant(dto.updatedAt()));

        SellerCompositeResult.AddressInfo addressInfo =
                new SellerCompositeResult.AddressInfo(
                        dto.sellerId(),
                        "BUSINESS",
                        null,
                        dto.businessAddressZipCode(),
                        dto.businessAddressLine1(),
                        dto.businessAddressLine2(),
                        null,
                        dto.csPhoneNumber(),
                        true);

        SellerCompositeResult.BusinessInfo businessInfo =
                new SellerCompositeResult.BusinessInfo(
                        dto.sellerId(),
                        dto.registrationNumber(),
                        dto.companyName(),
                        dto.representative(),
                        dto.saleReportNumber(),
                        dto.businessAddressZipCode(),
                        dto.businessAddressLine1(),
                        dto.businessAddressLine2());

        SellerCompositeResult.CsInfo csInfo =
                new SellerCompositeResult.CsInfo(
                        dto.sellerId(),
                        dto.csNumber() != null ? dto.csNumber() : dto.csPhoneNumber(),
                        dto.csPhoneNumber(),
                        dto.csEmail(),
                        null,
                        null,
                        null,
                        null);

        return new SellerCompositeResult(sellerInfo, addressInfo, businessInfo, csInfo);
    }

    /**
     * QueryDto Optional → SellerCompositeResult Optional 변환.
     *
     * @param dto QueryDto Optional
     * @return SellerCompositeResult Optional
     */
    public Optional<SellerCompositeResult> toCompositeResult(
            Optional<LegacyWebSellerQueryDto> dto) {
        return dto.map(this::toCompositeResult);
    }

    private Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(SEOUL_ZONE).toInstant();
    }
}
