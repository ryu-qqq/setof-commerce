package com.ryuqq.setof.application.faqcategory.factory.command;

import com.ryuqq.setof.application.faqcategory.dto.command.CreateFaqCategoryCommand;
import com.ryuqq.setof.application.faqcategory.dto.command.UpdateFaqCategoryCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.faq.aggregate.FaqCategory;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryCode;
import org.springframework.stereotype.Component;

/**
 * FAQ 카테고리 Command Factory
 *
 * <p>FAQ 카테고리 도메인 객체 생성을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class FaqCategoryCommandFactory {

    private final ClockHolder clockHolder;

    public FaqCategoryCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 신규 FAQ 카테고리 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 FAQ 카테고리
     */
    public FaqCategory createFaqCategory(CreateFaqCategoryCommand command) {
        FaqCategoryCode code = new FaqCategoryCode(command.code());

        return FaqCategory.forNew(
                code,
                command.name(),
                command.description(),
                command.displayOrder(),
                clockHolder.getClock().instant());
    }

    /**
     * FAQ 카테고리 업데이트 적용
     *
     * @param category 기존 카테고리
     * @param command 업데이트 커맨드
     * @return 업데이트된 FAQ 카테고리
     */
    public FaqCategory applyUpdate(FaqCategory category, UpdateFaqCategoryCommand command) {
        category.updateInfo(
                command.name(), command.description(), clockHolder.getClock().instant());

        category.updateDisplayOrder(command.displayOrder(), clockHolder.getClock().instant());

        return category;
    }
}
