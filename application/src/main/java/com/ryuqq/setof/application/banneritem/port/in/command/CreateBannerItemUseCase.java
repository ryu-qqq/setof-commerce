package com.ryuqq.setof.application.banneritem.port.in.command;

import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import java.util.List;

/**
 * BannerItem 생성 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface CreateBannerItemUseCase {

    /**
     * BannerItem 생성
     *
     * @param command 생성 명령
     * @return 생성된 BannerItem ID
     */
    Long create(CreateBannerItemCommand command);

    /**
     * BannerItem 목록 일괄 생성
     *
     * @param commands 생성 명령 목록
     * @return 생성된 BannerItem ID 목록
     */
    List<Long> createAll(List<CreateBannerItemCommand> commands);
}
