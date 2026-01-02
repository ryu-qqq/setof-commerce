package com.ryuqq.setof.application.banneritem.factory.command;

import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerId;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.DisplayPeriod;
import com.ryuqq.setof.domain.cms.vo.ImageSize;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * BannerItemCommandFactory - Command → Domain 변환
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BannerItemCommandFactory {

    private final ClockHolder clockHolder;

    public BannerItemCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateCommand → Domain 변환
     *
     * @param command 생성 명령
     * @return BannerItem Domain
     */
    public BannerItem toDomain(CreateBannerItemCommand command) {
        DisplayPeriod displayPeriod = null;
        if (command.displayStartDate() != null && command.displayEndDate() != null) {
            displayPeriod = DisplayPeriod.of(command.displayStartDate(), command.displayEndDate());
        }

        ImageSize imageSize = null;
        if (command.imageWidth() != null && command.imageHeight() != null) {
            imageSize =
                    ImageSize.of(
                            command.imageWidth().doubleValue(),
                            command.imageHeight().doubleValue());
        }

        return BannerItem.forNew(
                BannerId.of(command.bannerId()),
                command.title() != null ? ContentTitle.of(command.title()) : null,
                ImageUrl.of(command.imageUrl()),
                command.linkUrl() != null ? ImageUrl.of(command.linkUrl()) : null,
                imageSize,
                command.displayOrder() != null
                        ? DisplayOrder.of(command.displayOrder())
                        : DisplayOrder.DEFAULT,
                displayPeriod,
                clockHolder.getClock());
    }

    /**
     * CreateCommand 목록 → Domain 목록 변환
     *
     * @param commands 생성 명령 목록
     * @return BannerItem Domain 목록
     */
    public List<BannerItem> toDomainList(List<CreateBannerItemCommand> commands) {
        return commands.stream().map(this::toDomain).toList();
    }
}
