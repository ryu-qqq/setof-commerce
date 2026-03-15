package com.ryuqq.setof.application.banner.port.out;

import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.query.BannerGroupSearchCriteria;
import java.util.List;
import java.util.Optional;

/**
 * BannerGroupQueryPort - 배너 그룹 조회 Port-Out.
 *
 * <p>APP-PRT-001: Port-Out은 interface이며, Adapter-Out이 구현합니다. Application 레이어가 정의하고, Adapter-Out이
 * 의존성 역전(DIP)으로 구현합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface BannerGroupQueryPort {

    /**
     * ID로 배너 그룹을 조회합니다.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @return 배너 그룹 (없으면 empty)
     */
    Optional<BannerGroup> findById(long bannerGroupId);

    /**
     * 검색 조건으로 배너 그룹 목록을 조회합니다.
     *
     * @param criteria 검색 조건
     * @return 배너 그룹 목록
     */
    List<BannerGroup> findByCriteria(BannerGroupSearchCriteria criteria);

    /**
     * 검색 조건에 해당하는 배너 그룹 수를 반환합니다.
     *
     * @param criteria 검색 조건
     * @return 배너 그룹 수
     */
    long countByCriteria(BannerGroupSearchCriteria criteria);
}
