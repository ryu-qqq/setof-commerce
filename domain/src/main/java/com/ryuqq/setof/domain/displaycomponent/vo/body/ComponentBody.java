package com.ryuqq.setof.domain.displaycomponent.vo.body;

/**
 * ComponentBody - 컴포넌트 타입별 본문 sealed interface.
 *
 * <p>ComponentType에 따라 8종 구현체 중 하나가 선택된다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public sealed interface ComponentBody
        permits TextBody,
                TitleBody,
                BlankBody,
                ImageBody,
                ProductBody,
                CategoryBody,
                BrandBody,
                TabBody {}
