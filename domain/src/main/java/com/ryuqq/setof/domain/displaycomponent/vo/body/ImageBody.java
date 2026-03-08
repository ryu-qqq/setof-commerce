package com.ryuqq.setof.domain.displaycomponent.vo.body;

import com.ryuqq.setof.domain.displaycomponent.vo.ImageLayout;
import com.ryuqq.setof.domain.displaycomponent.vo.ImageSlide;
import java.util.List;

/**
 * ImageBody - IMAGE 컴포넌트 본문.
 *
 * @param imageLayout 이미지 레이아웃 (SINGLE, MULTI)
 * @param slides 이미지 슬라이드 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ImageBody(ImageLayout imageLayout, List<ImageSlide> slides)
        implements ComponentBody {}
