package com.ryuqq.setof.application.productdescription.port.out.command;

import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import java.util.List;

/** 상세설명 이미지 Command Port. */
public interface DescriptionImageCommandPort {

    /**
     * 이미지 목록을 일괄 저장합니다.
     *
     * @param images 저장할 이미지 도메인 객체 목록
     * @param productGroupDescriptionId 상세설명 ID
     */
    void persistAll(List<DescriptionImage> images, Long productGroupDescriptionId);

    /**
     * 상세설명 ID 기준으로 이미지를 삭제합니다.
     *
     * @param productGroupDescriptionId 상세설명 ID
     */
    void deleteByProductGroupDescriptionId(Long productGroupDescriptionId);
}
