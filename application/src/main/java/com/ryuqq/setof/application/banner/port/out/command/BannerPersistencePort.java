package com.ryuqq.setof.application.banner.port.out.command;

import com.ryuqq.setof.domain.cms.aggregate.banner.Banner;
import com.ryuqq.setof.domain.cms.vo.BannerId;

/**
 * Banner 영속성 Outbound Port (Command)
 *
 * <p>저장/수정은 persist()로 통합 (JPA merge 활용)
 *
 * <p>삭제는 Soft Delete(도메인에서 상태 변경 후 persist) 권장
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BannerPersistencePort {

    /**
     * 배너 저장/수정 (JPA merge 활용)
     *
     * <p>ID가 없으면 INSERT, 있으면 UPDATE
     *
     * @param banner 저장/수정할 배너
     * @return 저장된 Banner ID
     */
    BannerId persist(Banner banner);
}
