package com.ryuqq.setof.domain.faq.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.faq.vo.FaqId;
import java.util.Map;

/**
 * FAQ Not Found 예외
 *
 * <p>FAQ를 찾을 수 없을 때 발생합니다. HTTP 응답: 404 NOT FOUND
 */
public class FaqNotFoundException extends DomainException {

    public FaqNotFoundException(FaqId faqId) {
        super(
                FaqErrorCode.FAQ_NOT_FOUND,
                String.format("FAQ를 찾을 수 없습니다: %d", faqId.value()),
                Map.of("faqId", faqId.value()));
    }

    public FaqNotFoundException(Long faqId) {
        super(
                FaqErrorCode.FAQ_NOT_FOUND,
                String.format("FAQ를 찾을 수 없습니다: %d", faqId),
                Map.of("faqId", faqId));
    }

    /**
     * ID 값으로 예외 생성 (Static Factory)
     *
     * @param faqId FAQ ID
     * @return FaqNotFoundException
     */
    public static FaqNotFoundException byId(Long faqId) {
        return new FaqNotFoundException(faqId);
    }
}
