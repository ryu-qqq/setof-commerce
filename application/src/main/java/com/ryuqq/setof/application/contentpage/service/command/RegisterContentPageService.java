package com.ryuqq.setof.application.contentpage.service.command;

import com.ryuqq.setof.application.contentpage.dto.bundle.ContentPageRegistrationBundle;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.facade.ContentPageCommandFacade;
import com.ryuqq.setof.application.contentpage.factory.ContentPageCommandFactory;
import com.ryuqq.setof.application.contentpage.port.in.command.RegisterContentPageUseCase;
import org.springframework.stereotype.Service;

/**
 * RegisterContentPageService - 콘텐츠 페이지 등록 Service.
 *
 * <p>APP-SVC-001: Service는 @Service 어노테이션을 선언하고 UseCase 인터페이스를 구현합니다.
 *
 * <p>처리 흐름:
 *
 * <ol>
 *   <li>Factory를 통해 등록 번들(ContentPage + 컴포넌트 Commands) 생성
 *   <li>Facade를 통해 번들 일괄 저장
 * </ol>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class RegisterContentPageService implements RegisterContentPageUseCase {

    private final ContentPageCommandFactory contentPageCommandFactory;
    private final ContentPageCommandFacade contentPageCommandFacade;

    public RegisterContentPageService(
            ContentPageCommandFactory contentPageCommandFactory,
            ContentPageCommandFacade contentPageCommandFacade) {
        this.contentPageCommandFactory = contentPageCommandFactory;
        this.contentPageCommandFacade = contentPageCommandFacade;
    }

    @Override
    public Long execute(RegisterContentPageCommand command) {
        ContentPageRegistrationBundle bundle =
                contentPageCommandFactory.createRegistrationBundle(command);
        return contentPageCommandFacade.register(bundle);
    }
}
