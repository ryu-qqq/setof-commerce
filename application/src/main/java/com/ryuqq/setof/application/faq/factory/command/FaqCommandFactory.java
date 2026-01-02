package com.ryuqq.setof.application.faq.factory.command;

import com.ryuqq.setof.application.faq.dto.command.CreateFaqCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.Faq;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import com.ryuqq.setof.domain.faq.vo.FaqContent;
import org.springframework.stereotype.Component;

/**
 * FAQ Command Factory
 *
 * <p>Command DTO를 Domain 객체로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCommandFactory {

    private final ClockHolder clockHolder;

    public FaqCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateFaqCommand → Faq 도메인 변환
     *
     * @param command 생성 커맨드
     * @return Faq 도메인 객체
     */
    public Faq createFaq(CreateFaqCommand command) {
        FaqCategoryCode categoryCode = FaqCategoryCode.of(command.categoryCode());
        FaqContent content = new FaqContent(command.question(), command.answer());

        return Faq.forNew(
                categoryCode, content, command.displayOrder(), clockHolder.getClock().instant());
    }
}
