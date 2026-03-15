package com.ryuqq.setof.application.banner.validator;

import com.ryuqq.setof.application.banner.port.out.BannerGroupQueryPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.exception.BannerGroupNotFoundException;
import com.ryuqq.setof.domain.banner.id.BannerGroupId;
import org.springframework.stereotype.Component;

/**
 * BannerGroupValidator - 배너 그룹 검증 컴포넌트.
 *
 * <p>APP-VAL-001: Validator는 존재 여부 확인 및 비즈니스 제약 검증을 담당합니다. findExistingOrThrow(id) 패턴으로 존재하지 않는 경우
 * 예외를 발생시킵니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupValidator {

    private final BannerGroupQueryPort bannerGroupQueryPort;

    public BannerGroupValidator(BannerGroupQueryPort bannerGroupQueryPort) {
        this.bannerGroupQueryPort = bannerGroupQueryPort;
    }

    /**
     * ID로 배너 그룹을 조회하고, 존재하지 않으면 예외를 발생시킵니다.
     *
     * <p>APP-VAL-001: 존재하지 않는 엔티티 접근 시 도메인 예외(BannerGroupNotFoundException)를 발생시킵니다.
     *
     * @param id 배너 그룹 ID VO
     * @return 조회된 BannerGroup
     * @throws BannerGroupNotFoundException 배너 그룹이 존재하지 않을 경우
     */
    public BannerGroup findExistingOrThrow(BannerGroupId id) {
        return bannerGroupQueryPort
                .findById(id.value())
                .orElseThrow(BannerGroupNotFoundException::new);
    }
}
