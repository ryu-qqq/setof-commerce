package com.ryuqq.setof.application.banner.dto.command;

import com.ryuqq.setof.domain.cms.vo.BannerId;

/**
 * Banner 삭제 Command
 *
 * @param bannerId 배너 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteBannerCommand(BannerId bannerId) {

    /** 유효성 검증 */
    public DeleteBannerCommand {
        if (bannerId == null) {
            throw new IllegalArgumentException("배너 ID는 필수입니다");
        }
    }
}
