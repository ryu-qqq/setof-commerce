package com.ryuqq.setof.application.banneritem.service.command;

import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import com.ryuqq.setof.application.banneritem.factory.command.BannerItemCommandFactory;
import com.ryuqq.setof.application.banneritem.manager.command.BannerItemPersistenceManager;
import com.ryuqq.setof.application.banneritem.port.in.command.CreateBannerItemUseCase;
import com.ryuqq.setof.domain.cms.aggregate.banner.BannerItem;
import com.ryuqq.setof.domain.cms.vo.BannerItemId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CreateBannerItemService - BannerItem 생성 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional
public class CreateBannerItemService implements CreateBannerItemUseCase {

    private final BannerItemCommandFactory commandFactory;
    private final BannerItemPersistenceManager persistenceManager;

    public CreateBannerItemService(
            BannerItemCommandFactory commandFactory,
            BannerItemPersistenceManager persistenceManager) {
        this.commandFactory = commandFactory;
        this.persistenceManager = persistenceManager;
    }

    @Override
    public Long create(CreateBannerItemCommand command) {
        BannerItem bannerItem = commandFactory.toDomain(command);
        BannerItemId bannerItemId = persistenceManager.persist(bannerItem);
        return bannerItemId.value();
    }

    @Override
    public List<Long> createAll(List<CreateBannerItemCommand> commands) {
        List<BannerItem> bannerItems = commandFactory.toDomainList(commands);
        List<BannerItemId> bannerItemIds = persistenceManager.persistAll(bannerItems);
        return bannerItemIds.stream().map(BannerItemId::value).toList();
    }
}
