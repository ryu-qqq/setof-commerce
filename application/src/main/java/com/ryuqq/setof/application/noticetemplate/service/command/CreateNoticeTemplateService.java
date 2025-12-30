package com.ryuqq.setof.application.noticetemplate.service.command;

import com.ryuqq.setof.application.noticetemplate.dto.command.CreateNoticeTemplateCommand;
import com.ryuqq.setof.application.noticetemplate.factory.command.NoticeTemplateCommandFactory;
import com.ryuqq.setof.application.noticetemplate.manager.command.NoticeTemplatePersistenceManager;
import com.ryuqq.setof.application.noticetemplate.manager.query.NoticeTemplateReadManager;
import com.ryuqq.setof.application.noticetemplate.port.in.command.CreateNoticeTemplateUseCase;
import com.ryuqq.setof.domain.productnotice.aggregate.NoticeTemplate;
import org.springframework.stereotype.Service;

/**
 * 상품고시 템플릿 생성 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CreateNoticeTemplateService implements CreateNoticeTemplateUseCase {

    private final NoticeTemplateCommandFactory commandFactory;
    private final NoticeTemplatePersistenceManager persistenceManager;
    private final NoticeTemplateReadManager readManager;

    public CreateNoticeTemplateService(
            NoticeTemplateCommandFactory commandFactory,
            NoticeTemplatePersistenceManager persistenceManager,
            NoticeTemplateReadManager readManager) {
        this.commandFactory = commandFactory;
        this.persistenceManager = persistenceManager;
        this.readManager = readManager;
    }

    /**
     * 상품고시 템플릿 생성
     *
     * @param command 생성 Command
     * @return 생성된 템플릿 ID
     */
    @Override
    public Long create(CreateNoticeTemplateCommand command) {
        // 카테고리 ID 중복 검사
        if (readManager.existsByCategoryId(command.categoryId())) {
            throw new IllegalArgumentException(
                    "해당 카테고리에 이미 템플릿이 존재합니다: " + command.categoryId().value());
        }

        // Domain 생성
        NoticeTemplate template = commandFactory.create(command);

        // 저장 및 ID 반환
        return persistenceManager.persist(template).value();
    }
}
