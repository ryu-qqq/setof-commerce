package com.ryuqq.setof.application.imagevariant.port.out.command;

import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;

/** 이미지 Variant Command 아웃바운드 포트. */
public interface ImageVariantCommandPort {

    void persistAll(List<ImageVariant> variants);

    void softDeleteBySourceImageId(Long sourceImageId);
}
