package com.ryuqq.setof.application.selleroption.internal;

import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.factory.SellerOptionGroupFactory;
import com.ryuqq.setof.application.selleroption.manager.SellerOptionGroupReadManager;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupDiff;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupUpdateData;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerOptionCommandCoordinator {

    private final SellerOptionGroupFactory optionGroupFactory;
    private final SellerOptionGroupReadManager readManager;
    private final SellerOptionPersistFacade persistFacade;

    public SellerOptionCommandCoordinator(
            SellerOptionGroupFactory optionGroupFactory,
            SellerOptionGroupReadManager readManager,
            SellerOptionPersistFacade persistFacade) {
        this.optionGroupFactory = optionGroupFactory;
        this.readManager = readManager;
        this.persistFacade = persistFacade;
    }

    @Transactional
    public SellerOptionUpdateResult update(UpdateSellerOptionGroupsCommand command) {
        ProductGroupId pgId = ProductGroupId.of(command.productGroupId());

        SellerOptionGroupUpdateData updateData =
                optionGroupFactory.toUpdateData(pgId, command.optionGroups());

        SellerOptionGroups existing = readManager.getByProductGroupId(pgId);
        SellerOptionGroupDiff diff = existing.update(updateData);

        List<SellerOptionValueId> resolvedIds = persistFacade.persistDiff(diff);
        return new SellerOptionUpdateResult(resolvedIds, diff.occurredAt(), !diff.hasNoChanges());
    }
}
