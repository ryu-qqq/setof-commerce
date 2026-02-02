package com.ryuqq.setof.domain.brand.exception;

/**
 * 브랜드를 찾을 수 없는 경우 예외.
 *
 * <p>요청한 ID에 해당하는 브랜드가 존재하지 않을 때 발생합니다.
 */
public class BrandNotFoundException extends BrandException {

    private static final BrandErrorCode ERROR_CODE = BrandErrorCode.BRAND_NOT_FOUND;

    public BrandNotFoundException() {
        super(ERROR_CODE);
    }

    public BrandNotFoundException(Long id) {
        super(ERROR_CODE, String.format("ID가 %d인 브랜드를 찾을 수 없습니다", id));
    }
}
