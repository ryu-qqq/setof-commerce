package com.ryuqq.setof.domain.brand.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 브랜드 미존재 예외
 *
 * <p>요청한 브랜드 ID 또는 코드에 해당하는 브랜드가 존재하지 않을 때 발생합니다.
 */
public class BrandNotFoundException extends DomainException {

    public BrandNotFoundException(Long brandId) {
        super(
                BrandErrorCode.BRAND_NOT_FOUND,
                String.format("브랜드를 찾을 수 없습니다. brandId: %d", brandId),
                Map.of("brandId", brandId));
    }

    public BrandNotFoundException(String brandCode) {
        super(
                BrandErrorCode.BRAND_NOT_FOUND,
                String.format("브랜드를 찾을 수 없습니다. brandCode: %s", brandCode),
                Map.of("brandCode", brandCode));
    }
}
