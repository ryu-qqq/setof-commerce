package com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.imagevariant.dto.command.SyncImageVariantsApiRequest;
import com.ryuqq.setof.application.imagevariant.dto.command.SyncImageVariantsCommand;
import org.springframework.stereotype.Component;

/**
 * ImageVariantCommandApiMapper - 이미지 Variant Command API 변환 매퍼.
 *
 * <p>API-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>API-MAP-005: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageVariantCommandApiMapper {

    /**
     * SyncImageVariantsApiRequest -> SyncImageVariantsCommand 변환.
     *
     * @param request API 요청 DTO
     * @return Application Command DTO
     */
    public SyncImageVariantsCommand toSyncCommand(SyncImageVariantsApiRequest request) {
        return new SyncImageVariantsCommand(
                request.sourceImageId(),
                request.sourceType(),
                request.variants().stream()
                        .map(
                                v ->
                                        new SyncImageVariantsCommand.VariantCommand(
                                                v.variantType(),
                                                v.resultAssetId(),
                                                v.variantUrl(),
                                                v.width(),
                                                v.height()))
                        .toList());
    }
}
