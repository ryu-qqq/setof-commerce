package com.ryuqq.setof.storage.legacy.composite.web.user.mapper;

import com.ryuqq.setof.application.legacy.user.dto.response.LegacyUserFavoriteResult;
import com.ryuqq.setof.application.legacy.user.dto.response.LegacyUserFavoriteSliceResult;
import com.ryuqq.setof.storage.legacy.composite.web.user.dto.LegacyWebUserFavoriteQueryDto;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 레거시 찜 목록 Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebUserFavoriteMapper {

    public LegacyUserFavoriteResult toResult(LegacyWebUserFavoriteQueryDto dto) {
        if (dto == null) {
            return null;
        }
        return LegacyUserFavoriteResult.of(
                dto.userFavoriteId(),
                dto.productGroupId(),
                dto.sellerId(),
                dto.productGroupName(),
                dto.brandId(),
                dto.brandName(),
                dto.productImageUrl(),
                dto.regularPrice(),
                dto.currentPrice(),
                dto.discountRate(),
                dto.soldOutYn(),
                dto.displayYn(),
                dto.insertDate());
    }

    public List<LegacyUserFavoriteResult> toResults(List<LegacyWebUserFavoriteQueryDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream().map(this::toResult).toList();
    }

    /**
     * Slice 결과 생성.
     *
     * <p>pageSize + 1개 조회 후 hasNext 판단.
     *
     * @param dtos 조회 결과 (pageSize + 1개)
     * @param pageSize 페이지 크기
     * @param totalElements 전체 요소 수
     * @return Slice 결과
     */
    public LegacyUserFavoriteSliceResult toSliceResult(
            List<LegacyWebUserFavoriteQueryDto> dtos, int pageSize, long totalElements) {
        if (dtos == null || dtos.isEmpty()) {
            return LegacyUserFavoriteSliceResult.empty();
        }

        boolean hasNext = dtos.size() > pageSize;
        List<LegacyWebUserFavoriteQueryDto> content = hasNext ? dtos.subList(0, pageSize) : dtos;

        return LegacyUserFavoriteSliceResult.of(toResults(content), hasNext, totalElements);
    }
}
