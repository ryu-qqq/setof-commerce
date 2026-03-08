package com.ryuqq.setof.storage.legacy.composite.web.review.mapper;

import com.ryuqq.setof.application.legacy.review.dto.response.LegacyReviewResult;
import com.ryuqq.setof.domain.review.vo.WrittenReview;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewImageQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewOptionQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.review.dto.LegacyWebReviewQueryDto;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * 레거시 Web Review Mapper.
 *
 * <p>QueryDto → 도메인 VO / Application Result 변환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebReviewMapper {

    private static final ZoneId LEGACY_ZONE = ZoneId.of("Asia/Seoul");

    /**
     * 리뷰 QueryDto → 도메인 VO 변환 (목록).
     *
     * @param dtos 리뷰 QueryDto 목록
     * @param allImages 전체 이미지 목록
     * @param allOptions 전체 옵션 목록
     * @return WrittenReview 도메인 VO 목록
     */
    public List<WrittenReview> toDomainList(
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
                            return toDomain(dto, images, options);
                        })
                .toList();
    }

    /**
     * 리뷰 QueryDto → LegacyReviewResult 변환 (목록, 레거시 호환용).
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

    private WrittenReview toDomain(
            LegacyWebReviewQueryDto dto,
            List<LegacyWebReviewImageQueryDto> images,
            List<LegacyWebReviewOptionQueryDto> options) {

        List<WrittenReview.WrittenReviewImage> reviewImages =
                images.stream()
                        .map(
                                img ->
                                        new WrittenReview.WrittenReviewImage(
                                                img.reviewImageType(), img.imageUrl()))
                        .toList();

        String optionString = buildOptionString(options);

        return new WrittenReview(
                dto.reviewId(),
                dto.orderId(),
                null,
                dto.userName(),
                dto.rating(),
                dto.content(),
                new WrittenReview.ProductGroupSnapshot(
                        dto.productGroupId(),
                        dto.productGroupName(),
                        dto.productGroupImageUrl(),
                        optionString),
                new WrittenReview.BrandSnapshot(dto.brandId(), dto.brandName()),
                new WrittenReview.CategorySnapshot(dto.categoryId(), dto.categoryName()),
                reviewImages,
                dto.insertDate() != null ? dto.insertDate().atZone(LEGACY_ZONE).toInstant() : null,
                dto.updateDate() != null ? dto.updateDate().atZone(LEGACY_ZONE).toInstant() : null,
                dto.paymentDate() != null
                        ? dto.paymentDate().atZone(LEGACY_ZONE).toInstant()
                        : null);
    }

    private LegacyReviewResult toResult(
            LegacyWebReviewQueryDto dto,
            List<LegacyWebReviewImageQueryDto> images,
            List<LegacyWebReviewOptionQueryDto> options) {

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
