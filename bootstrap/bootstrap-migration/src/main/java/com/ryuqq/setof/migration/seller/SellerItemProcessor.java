package com.ryuqq.setof.migration.seller;

import java.time.Instant;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Seller 데이터 변환 Processor
 *
 * <p>레거시 seller 데이터를 seller_applications 데이터로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class SellerItemProcessor
        implements ItemProcessor<LegacySellerDto, SellerApplicationMigrationData> {

    private static final Logger log = LoggerFactory.getLogger(SellerItemProcessor.class);
    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");

    @Override
    public SellerApplicationMigrationData process(LegacySellerDto legacySeller) {
        log.debug("Processing seller: legacyId={}", legacySeller.sellerId());

        Instant now = Instant.now();
        Instant createdAt = toInstant(legacySeller.insertDate());
        Instant updatedAt = toInstant(legacySeller.updateDate());

        return new SellerApplicationMigrationData(
                legacySeller.sellerId(),
                normalizeSellerName(legacySeller.sellerName()),
                normalizeSellerName(legacySeller.sellerName()), // displayName = sellerName
                legacySeller.logoUrl(),
                legacySeller.description(),
                normalizeRegistrationNumber(legacySeller.registrationNumber()),
                nullToDefault(legacySeller.companyName(), "미등록"),
                nullToDefault(legacySeller.representative(), "미등록"),
                legacySeller.saleReportNumber(),
                nullToDefault(legacySeller.businessZipCode(), "00000"),
                nullToDefault(legacySeller.businessAddress(), "주소 미등록"),
                legacySeller.businessAddressDetail(),
                normalizePhoneNumber(legacySeller.csPhone()),
                normalizeEmail(legacySeller.csEmail()),
                "RETURN", // addressType
                "반품주소", // addressName
                nullToDefault(legacySeller.returnAddressZipCode(), "00000"),
                nullToDefault(legacySeller.returnAddressLine1(), "반품주소 미등록"),
                legacySeller.returnAddressLine2(),
                nullToDefault(legacySeller.representative(), "담당자"), // contactName
                normalizePhoneNumber(legacySeller.csPhone()), // contactPhoneNumber
                createdAt != null ? createdAt : now, // agreedAt
                "PENDING", // status - AuthHub 승인 대기 상태
                createdAt != null ? createdAt : now, // appliedAt
                createdAt != null ? createdAt : now, // processedAt
                "MIGRATION", // processedBy
                createdAt != null ? createdAt : now,
                updatedAt != null ? updatedAt : now);
    }

    private Instant toInstant(java.time.LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(KOREA_ZONE).toInstant();
    }

    private String normalizeSellerName(String sellerName) {
        if (sellerName == null || sellerName.isBlank()) {
            return "미등록셀러";
        }
        return sellerName.trim();
    }

    private String normalizeRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            return "0000000000"; // 임시값 - 실제로는 유효성 검사 필요
        }
        // 숫자와 하이픈만 남김
        return registrationNumber.replaceAll("[^0-9-]", "");
    }

    private String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return "010-0000-0000"; // 기본값
        }
        // 숫자만 추출 후 포맷팅
        String digits = phoneNumber.replaceAll("[^0-9]", "");
        if (digits.length() == 11) {
            return digits.substring(0, 3)
                    + "-"
                    + digits.substring(3, 7)
                    + "-"
                    + digits.substring(7);
        }
        return phoneNumber.trim();
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return "migration@example.com"; // 기본값
        }
        return email.trim().toLowerCase();
    }

    private String nullToDefault(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value.trim();
    }
}
