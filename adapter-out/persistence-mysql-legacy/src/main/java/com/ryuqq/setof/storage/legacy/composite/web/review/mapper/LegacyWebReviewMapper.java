package com.ryuqq.setof.storage.legacy.composite.web.review.mapper;

import com.ryuqq.setof.application.legacy.review.dto.response.LegacyReviewResult;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewQueryDto;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Review Mapper.
 *
 * <p>QueryDto → Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebReviewMapper {

    /**
     * 리뷰 QueryDto → Result 변환.
     *
     * @param dto 리뷰 QueryDto
     * @param images 리뷰 이미지 목록
     * @param options 리뷰 옵션 목록
     * @return LegacyReviewResult
     */
    public LegacyReviewResult toResult(
            LegacyWebReviewQueryDto dto,
            List<LegacyWebReviewImageQueryDto> images,
            List<LegacyWebReviewOptionQueryDto> options) {

        if (dto == null) {
            return null;
        }

        List<LegacyReviewResult.ReviewImageResult> imageResults =
                images.stream()
                        .map(
                                img ->
                                        LegacyReviewResult.ReviewImageResult.of(
                                                img.reviewImageType(), img.imageUrl()))
                        .toList();

        String optionString = buildOptionString(options);

        return new LegacyReviewResult(
                dto.reviewId(),
                dto.orderId(),
                dto.userName(),
                dto.rating(),
                dto.content(),
                dto.productGroupId(),
                dto.productGroupName(),
                dto.productGroupImageUrl(),
                LegacyReviewResult.BrandResult.of(dto.brandId(), dto.brandName()),
                dto.categoryId(),
                dto.categoryName(),
                imageResults,
                dto.insertDate(),
                dto.paymentDate(),
                optionString);
    }

    /**
     * 리뷰 목록 변환 (이미지/옵션 매핑).
     *
     * @param dtos 리뷰 QueryDto 목록
     * @param allImages 전체 이미지 목록
     * @param allOptions 전체 옵션 목록
     * @return LegacyReviewResult 목록
     */
    public List<LegacyReviewResult> toResults(
            List<LegacyWebReviewQueryDto> dtos,
            List<LegacyWebReviewImageQueryDto> allImages,
            List<LegacyWebReviewOptionQueryDto> allOptions) {

        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }

        Map<Long, List<LegacyWebReviewImageQueryDto>> imageMap =
                allImages.stream()
                        .collect(Collectors.groupingBy(LegacyWebReviewImageQueryDto::reviewId));

        Map<Long, List<LegacyWebReviewOptionQueryDto>> optionMap =
                allOptions.stream()
                        .collect(Collectors.groupingBy(LegacyWebReviewOptionQueryDto::reviewId));

        return dtos.stream()
                .map(
                        dto -> {
                            List<LegacyWebReviewImageQueryDto> images =
                                    imageMap.getOrDefault(dto.reviewId(), List.of());
                            List<LegacyWebReviewOptionQueryDto> options =
                                    optionMap.getOrDefault(dto.reviewId(), List.of());
                            return toResult(dto, images, options);
                        })
                .toList();
    }

    /**
     * 옵션 문자열 생성.
     *
     * <p>옵션그룹 ID 순으로 정렬 후 "/" 구분자로 연결.
     *
     * @param options 옵션 목록
     * @return 옵션 문자열 (e.g., "Black/L")
     */
    private String buildOptionString(List<LegacyWebReviewOptionQueryDto> options) {
        if (options == null || options.isEmpty()) {
            return "";
        }

        return options.stream()
                .sorted(Comparator.comparing(LegacyWebReviewOptionQueryDto::optionGroupId))
                .map(LegacyWebReviewOptionQueryDto::optionValue)
                .collect(Collectors.joining("/"));
    }
}
