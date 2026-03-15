package com.ryuqq.setof.application.banner.manager;

import com.ryuqq.setof.application.banner.port.out.BannerGroupQueryPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.exception.BannerErrorCode;
import com.ryuqq.setof.domain.banner.exception.BannerException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * BannerGroupReadManager - 배너 그룹 조회 매니저.
 *
 * <p>BannerGroupQueryPort를 래핑하여 조회 실패 시 도메인 예외를 던집니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupReadManager {

    private final BannerGroupQueryPort queryPort;

    public BannerGroupReadManager(BannerGroupQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    /**
     * ID로 배너 그룹을 조회합니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @return 배너 그룹 도메인 객체
     * @throws BannerException 배너 그룹을 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public BannerGroup getById(long bannerGroupId) {
        return queryPort
                .findById(bannerGroupId)
                .orElseThrow(
                        () ->
                                new BannerException(
                                        BannerErrorCode.BANNER_GROUP_NOT_FOUND,
                                        "배너 그룹을 찾을 수 없습니다. bannerGroupId=" + bannerGroupId));
    }
}
