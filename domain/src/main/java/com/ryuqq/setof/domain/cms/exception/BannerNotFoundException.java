package com.ryuqq.setof.domain.cms.exception;

import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * BannerNotFoundException - Banner를 찾을 수 없을 때 발생
 *
 * <p>HTTP 응답: 404 NOT FOUND
 *
 * @author development-team
 * @since 1.0.0
 */
public class BannerNotFoundException extends DomainException {

    /**
     * ID로 Banner를 찾을 수 없을 때 생성
     *
     * @param bannerId Banner ID
     */
    public BannerNotFoundException(BannerId bannerId) {
        super(
                CmsErrorCode.BANNER_NOT_FOUND,
                String.format("Banner not found: %d", bannerId.value()),
                Map.of("bannerId", bannerId.value()));
    }
}
