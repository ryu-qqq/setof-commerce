package com.ryuqq.setof.storage.legacy.composite.web.gnb.mapper;

import com.ryuqq.setof.application.legacy.gnb.dto.response.LegacyGnbResult;
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
     * @return LegacyGnbResult
     */
    public LegacyGnbResult toResult(LegacyWebGnbQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyGnbResult.of(dto.gnbId(), dto.title(), dto.linkUrl());
    }

    /**
     * GnbQueryDto 목록 → GnbResult 목록 변환.
     *
     * @param dtos GnbQueryDto 목록
     * @return LegacyGnbResult 목록
     */
    public List<LegacyGnbResult> toResults(List<LegacyWebGnbQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).collect(Collectors.toList());
    }
}
