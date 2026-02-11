package com.ryuqq.setof.storage.legacy.composite.web.banner.mapper;

import com.ryuqq.setof.application.legacy.banner.dto.response.LegacyBannerItemResult;
import com.ryuqq.setof.storage.legacy.composite.web.banner.dto.LegacyWebBannerItemQueryDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBannerMapper - 레거시 Web 배너 Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBannerMapper {

    /**
     * BannerItemQueryDto → BannerItemResult 변환.
     *
     * @param dto BannerItemQueryDto
     * @return LegacyBannerItemResult
     */
    public LegacyBannerItemResult toResult(LegacyWebBannerItemQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyBannerItemResult.of(
                dto.bannerItemId(), dto.title(), dto.imageUrl(), dto.linkUrl());
    }

    /**
     * BannerItemQueryDto 목록 → BannerItemResult 목록 변환.
     *
     * @param dtos BannerItemQueryDto 목록
     * @return LegacyBannerItemResult 목록
     */
    public List<LegacyBannerItemResult> toResults(List<LegacyWebBannerItemQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).collect(Collectors.toList());
    }
}
