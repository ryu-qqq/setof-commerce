package com.ryuqq.setof.application.gnb.dto.command;

import com.ryuqq.setof.domain.cms.vo.GnbId;

/**
 * Gnb 삭제 Command
 *
 * @param gnbId GNB ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteGnbCommand(GnbId gnbId) {

    /** 유효성 검증 */
    public DeleteGnbCommand {
        if (gnbId == null) {
            throw new IllegalArgumentException("GNB ID는 필수입니다");
        }
    }
}
