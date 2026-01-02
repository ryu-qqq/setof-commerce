package com.ryuqq.setof.application.faq.port.in.query;

import com.ryuqq.setof.application.faq.dto.response.FaqResponse;
import com.ryuqq.setof.domain.faq.vo.FaqId;

/**
 * FAQ 단건 조회 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetFaqUseCase {

    /**
     * FAQ ID로 조회
     *
     * @param faqId FAQ ID
     * @return FAQ 응답
     */
    FaqResponse execute(FaqId faqId);
}
