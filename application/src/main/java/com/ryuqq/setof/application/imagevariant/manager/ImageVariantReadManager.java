package com.ryuqq.setof.application.imagevariant.manager;

import com.ryuqq.setof.application.imagevariant.port.out.query.ImageVariantQueryPort;
import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImageVariantReadManager - 이미지 Variant 조회 Manager.
 *
 * <p>QueryPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional(readOnly = true)
public class ImageVariantReadManager {

    private final ImageVariantQueryPort queryPort;

    public ImageVariantReadManager(ImageVariantQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<ImageVariant> findBySourceImageId(Long sourceImageId) {
        return queryPort.findBySourceImageId(sourceImageId);
    }

    public List<ImageVariant> findBySourceImageIds(List<Long> sourceImageIds) {
        return queryPort.findBySourceImageIds(sourceImageIds);
    }
}
