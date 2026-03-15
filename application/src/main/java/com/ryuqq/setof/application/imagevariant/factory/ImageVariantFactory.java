package com.ryuqq.setof.application.imagevariant.factory;

import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand.VariantCommand;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import com.ryuqq.setof.domain.imagevariant.vo.ImageDimension;
import com.ryuqq.setof.domain.imagevariant.vo.ImageSourceType;
import com.ryuqq.setof.domain.imagevariant.vo.ImageVariantType;
import com.ryuqq.setof.domain.imagevariant.vo.ResultAssetId;
import com.ryuqq.setof.domain.productgroup.vo.ImageUrl;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ImageVariantFactory - 이미지 Variant 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageVariantFactory {

    /**
     * Variant 커맨드 목록으로부터 ImageVariant 도메인 객체 목록을 생성합니다.
     *
     * @param sourceImageId 원본 이미지 ID
     * @param sourceType 이미지 소스 타입
     * @param variantCommands Variant 커맨드 목록
     * @param now 생성 시각
     * @return ImageVariant 도메인 객체 목록
     */
    public List<ImageVariant> createVariants(
            Long sourceImageId,
            ImageSourceType sourceType,
            List<VariantCommand> variantCommands,
            Instant now) {
        if (variantCommands == null || variantCommands.isEmpty()) {
            return List.of();
        }
        return variantCommands.stream()
                .map(cmd -> createVariant(sourceImageId, sourceType, cmd, now))
                .toList();
    }

    private ImageVariant createVariant(
            Long sourceImageId, ImageSourceType sourceType, VariantCommand command, Instant now) {
        return ImageVariant.forNew(
                sourceImageId,
                sourceType,
                ImageVariantType.valueOf(command.variantType()),
                ResultAssetId.of(command.resultAssetId()),
                ImageUrl.of(command.variantUrl()),
                ImageDimension.of(command.width(), command.height()),
                now);
    }
}
