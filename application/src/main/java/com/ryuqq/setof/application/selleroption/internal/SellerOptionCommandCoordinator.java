package com.ryuqq.setof.application.selleroption.internal;

import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.result.SellerOptionUpdateResult;
import com.ryuqq.setof.application.selleroption.factory.SellerOptionGroupFactory;
import com.ryuqq.setof.application.selleroption.manager.SellerOptionGroupReadManager;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
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

    /**
     * 신규 옵션 그룹을 등록하고 생성된 SellerOptionValueId 목록을 반환합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @param optionGroupCommands 등록할 옵션 그룹 커맨드 목록
     * @return 생성된 SellerOptionValueId 목록
     */
    @Transactional
    public List<SellerOptionValueId> register(
            ProductGroupId productGroupId,
            List<RegisterProductGroupCommand.OptionGroupCommand> optionGroupCommands) {
        if (optionGroupCommands == null || optionGroupCommands.isEmpty()) {
            return List.of();
        }
        List<SellerOptionGroup> groups =
                optionGroupFactory.createNewGroups(productGroupId, optionGroupCommands);
        return persistFacade.persistAll(groups);
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
