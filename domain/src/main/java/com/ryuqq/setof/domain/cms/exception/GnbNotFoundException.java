package com.ryuqq.setof.domain.cms.exception;

import com.ryuqq.setof.domain.cms.vo.GnbId;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * GnbNotFoundException - GNB를 찾을 수 없을 때 발생
 *
 * <p>HTTP 응답: 404 NOT FOUND
 *
 * @author development-team
 * @since 1.0.0
 */
public class GnbNotFoundException extends DomainException {

    /**
     * ID로 GNB를 찾을 수 없을 때 생성
     *
     * @param gnbId GNB ID
     */
    public GnbNotFoundException(GnbId gnbId) {
        super(
                CmsErrorCode.GNB_NOT_FOUND,
                String.format("GNB not found: %d", gnbId.value()),
                Map.of("gnbId", gnbId.value()));
    }
}
