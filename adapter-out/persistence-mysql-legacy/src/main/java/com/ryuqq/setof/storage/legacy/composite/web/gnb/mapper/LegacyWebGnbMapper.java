package com.ryuqq.setof.storage.legacy.composite.web.gnb.mapper;

import com.ryuqq.setof.application.legacy.web.gnb.dto.response.LegacyWebGnbResult;
import com.ryuqq.setof.storage.legacy.composite.web.gnb.dto.LegacyWebGnbQueryDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebGnbMapper - 레거시 Web GNB Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebGnbMapper {

    /**
     * GnbQueryDto → GnbResult 변환.
     *
     * @param dto GnbQueryDto
     * @return LegacyWebGnbResult
     */
    public LegacyWebGnbResult toResult(LegacyWebGnbQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyWebGnbResult.of(dto.gnbId(), dto.title(), dto.linkUrl());
    }

    /**
     * GnbQueryDto 목록 → GnbResult 목록 변환.
     *
     * @param dtos GnbQueryDto 목록
     * @return LegacyWebGnbResult 목록
     */
    public List<LegacyWebGnbResult> toResults(List<LegacyWebGnbQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).collect(Collectors.toList());
    }
}
