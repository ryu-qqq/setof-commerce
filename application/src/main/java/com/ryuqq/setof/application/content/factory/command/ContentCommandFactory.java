package com.ryuqq.setof.application.content.factory.command;

import com.ryuqq.setof.application.content.dto.command.CreateContentCommand;
import com.ryuqq.setof.application.content.dto.command.UpdateContentCommand;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import com.ryuqq.setof.domain.cms.vo.ContentMemo;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import org.springframework.stereotype.Component;

/**
 * Content Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentCommandFactory {

    private final ClockHolder clockHolder;

    public ContentCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 신규 Content 생성
     *
     * @param command 생성 커맨드
     * @return 생성된 Content (저장 전)
     */
    public Content create(CreateContentCommand command) {
        Clock clock = clockHolder.getClock();

        ContentTitle title = ContentTitle.of(command.title());
        ContentMemo memo =
                command.memo() != null ? ContentMemo.of(command.memo()) : ContentMemo.empty();
        ImageUrl imageUrl =
                command.imageUrl() != null ? ImageUrl.of(command.imageUrl()) : ImageUrl.empty();
        DisplayPeriod displayPeriod =
                DisplayPeriod.of(command.displayStartDate(), command.displayEndDate());

        return Content.forNew(title, memo, imageUrl, displayPeriod, clock);
    }

    /**
     * 기존 Content 업데이트를 위한 정보 추출
     *
     * @param command 수정 커맨드
     * @return 수정할 ContentTitle
     */
    public ContentTitle createTitle(UpdateContentCommand command) {
        return ContentTitle.of(command.title());
    }

    /**
     * 기존 Content 업데이트를 위한 정보 추출
     *
     * @param command 수정 커맨드
     * @return 수정할 ContentMemo
     */
    public ContentMemo createMemo(UpdateContentCommand command) {
        return command.memo() != null ? ContentMemo.of(command.memo()) : ContentMemo.empty();
    }

    /**
     * 기존 Content 업데이트를 위한 정보 추출
     *
     * @param command 수정 커맨드
     * @return 수정할 ImageUrl
     */
    public ImageUrl createImageUrl(UpdateContentCommand command) {
        return command.imageUrl() != null ? ImageUrl.of(command.imageUrl()) : ImageUrl.empty();
    }

    /**
     * 기존 Content 업데이트를 위한 정보 추출
     *
     * @param command 수정 커맨드
     * @return 수정할 DisplayPeriod
     */
    public DisplayPeriod createDisplayPeriod(UpdateContentCommand command) {
        return DisplayPeriod.of(command.displayStartDate(), command.displayEndDate());
    }
}
