package com.ryuqq.setof.application.productdescription.factory.command;

import com.ryuqq.setof.application.productdescription.dto.command.DescriptionImageDto;
import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductDescriptionCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.vo.ProductGroupId;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.vo.HtmlContent;
import com.ryuqq.setof.domain.productdescription.vo.ImageUrl;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품설명 Command Factory
 *
 * <p>Command DTO → Domain 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductDescriptionCommandFactory {

    private final ClockHolder clockHolder;

    public ProductDescriptionCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 현재 시각 반환
     *
     * @return 현재 Instant
     */
    public Instant now() {
        return Instant.now(clockHolder.getClock());
    }

    /**
     * 등록 Command → Domain 변환
     *
     * @param command 등록 Command
     * @return ProductDescription 도메인
     */
    public ProductDescription createProductDescription(RegisterProductDescriptionCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        ProductGroupId productGroupId = ProductGroupId.of(command.productGroupId());
        HtmlContent htmlContent = HtmlContent.of(command.htmlContent());
        List<DescriptionImage> images = toImages(command.images(), now);

        return ProductDescription.create(productGroupId, htmlContent, images, now);
    }

    /**
     * 이미지 DTO 목록 → Domain 목록 변환
     *
     * @param imageDtos 이미지 DTO 목록
     * @param uploadedAt 업로드 일시
     * @return 이미지 Domain 목록
     */
    public List<DescriptionImage> toImages(
            List<DescriptionImageDto> imageDtos, Instant uploadedAt) {
        if (imageDtos == null || imageDtos.isEmpty()) {
            return List.of();
        }

        return imageDtos.stream().map(dto -> toImage(dto, uploadedAt)).toList();
    }

    /**
     * 이미지 DTO → Domain 변환
     *
     * @param dto 이미지 DTO
     * @param uploadedAt 업로드 일시
     * @return 이미지 Domain
     */
    private DescriptionImage toImage(DescriptionImageDto dto, Instant uploadedAt) {
        ImageUrl originUrl = ImageUrl.of(dto.originUrl());
        ImageUrl cdnUrl = dto.cdnUrl() != null ? ImageUrl.of(dto.cdnUrl()) : originUrl;

        return DescriptionImage.of(dto.displayOrder(), originUrl, cdnUrl, uploadedAt);
    }
}
