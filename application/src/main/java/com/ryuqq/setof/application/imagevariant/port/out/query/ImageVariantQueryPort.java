package com.ryuqq.setof.application.imagevariant.port.out.query;

import com.ryuqq.setof.domain.imagevariant.aggregate.ImageVariant;
import java.util.List;

/** 이미지 Variant Query 아웃바운드 포트. */
public interface ImageVariantQueryPort {

    List<ImageVariant> findBySourceImageId(Long sourceImageId);

    List<ImageVariant> findBySourceImageIds(List<Long> sourceImageIds);
}
