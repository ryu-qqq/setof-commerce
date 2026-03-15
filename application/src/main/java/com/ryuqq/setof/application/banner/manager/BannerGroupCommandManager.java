package com.ryuqq.setof.application.banner.manager;

import com.ryuqq.setof.application.banner.port.out.command.BannerGroupCommandPort;
import com.ryuqq.setof.domain.banner.aggregate.BannerGroup;
import com.ryuqq.setof.domain.banner.vo.BannerSlideDiff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * BannerGroupCommandManager - 배너 그룹 Command 매니저.
 *
 * <p>APP-MGR-001: Manager는 @Transactional을 선언하고 Port-Out을 호출합니다. Service에서 @Transactional을 선언하지
 * 않으며, 트랜잭션 경계는 Manager가 관리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BannerGroupCommandManager {

    private final BannerGroupCommandPort bannerGroupCommandPort;

    public BannerGroupCommandManager(BannerGroupCommandPort bannerGroupCommandPort) {
        this.bannerGroupCommandPort = bannerGroupCommandPort;
    }

    /**
     * 배너 그룹만 저장합니다 (슬라이드 제외).
     *
     * <p>APP-MGR-001: @Transactional로 트랜잭션 경계를 선언합니다.
     *
     * @param bannerGroup 저장할 배너 그룹
     * @return 생성된 배너 그룹 ID
     */
    @Transactional
    public Long persist(BannerGroup bannerGroup) {
        return bannerGroupCommandPort.persist(bannerGroup);
    }

    /**
     * 배너 그룹과 슬라이드를 함께 저장합니다.
     *
     * <p>APP-MGR-001: @Transactional로 트랜잭션 경계를 선언합니다. 그룹 저장 후 반환된 ID로 슬라이드를 연결하여 저장합니다.
     *
     * @param bannerGroup 저장할 배너 그룹 (슬라이드 포함)
     * @return 생성된 배너 그룹 ID
     */
    @Transactional
    public Long persistWithSlides(BannerGroup bannerGroup) {
        Long groupId = bannerGroupCommandPort.persist(bannerGroup);
        bannerGroupCommandPort.persistSlides(groupId, bannerGroup.slides());
        return groupId;
    }

    /**
     * 배너 슬라이드 Diff 결과를 영속합니다.
     *
     * <p>added: 신규 슬라이드 저장. allDirtySlides (retained + removed): 기존 슬라이드 상태 갱신.
     *
     * @param bannerGroupId 배너 그룹 ID
     * @param diff 슬라이드 변경 비교 결과
     */
    @Transactional
    public void persistSlideDiff(long bannerGroupId, BannerSlideDiff diff) {
        if (!diff.added().isEmpty()) {
            bannerGroupCommandPort.persistSlides(bannerGroupId, diff.added());
        }
        if (!diff.allDirtySlides().isEmpty()) {
            bannerGroupCommandPort.updateSlides(bannerGroupId, diff.allDirtySlides());
        }
    }
}
