package com.ryuqq.setof.application.contentpage.dto.bundle;

import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import java.util.List;

/**
 * ContentPageRegistrationBundle - 콘텐츠 페이지 등록 번들.
 *
 * <p>ContentPage 도메인 객체와 컴포넌트 Command 목록을 함께 묶어 전달합니다. 컴포넌트는 ContentPage 저장 후 ID를 획득한 뒤에야 생성할 수
 * 있으므로, 도메인 객체가 아닌 Command 목록으로 보유합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ContentPageRegistrationBundle {

    private final ContentPage contentPage;
    private final List<RegisterDisplayComponentCommand> componentCommands;

    public ContentPageRegistrationBundle(
            ContentPage contentPage, List<RegisterDisplayComponentCommand> componentCommands) {
        this.contentPage = contentPage;
        this.componentCommands = componentCommands != null ? componentCommands : List.of();
    }

    public ContentPage contentPage() {
        return contentPage;
    }

    public List<RegisterDisplayComponentCommand> componentCommands() {
        return componentCommands;
    }

    public boolean hasComponents() {
        return !componentCommands.isEmpty();
    }
}
