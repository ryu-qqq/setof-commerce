package com.ryuqq.setof.application.gnb.assembler;

import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Gnb Assembler
 *
 * <p>Domain → Response DTO 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class GnbAssembler {

    /**
     * Gnb 도메인 → GnbResponse 변환
     *
     * @param gnb GNB 도메인
     * @return GnbResponse DTO
     */
    public GnbResponse toResponse(Gnb gnb) {
        DisplayPeriod displayPeriod = gnb.displayPeriod();
        Instant displayStartDate = displayPeriod != null ? displayPeriod.startDate() : null;
        Instant displayEndDate = displayPeriod != null ? displayPeriod.endDate() : null;

        return GnbResponse.of(
                gnb.id() != null ? gnb.id().value() : null,
                gnb.title() != null ? gnb.title().value() : null,
                gnb.linkUrl() != null ? gnb.linkUrl().value() : null,
                gnb.displayOrder() != null ? gnb.displayOrder().value() : 0,
                gnb.status() != null ? gnb.status().name() : null,
                displayStartDate,
                displayEndDate,
                gnb.createdAt(),
                gnb.updatedAt());
    }

    /**
     * Gnb 목록 → GnbResponse 목록 변환
     *
     * @param gnbs GNB 목록
     * @return GnbResponse 목록
     */
    public List<GnbResponse> toResponses(List<Gnb> gnbs) {
        return gnbs.stream().map(this::toResponse).toList();
    }
}
