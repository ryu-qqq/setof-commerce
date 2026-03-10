package com.ryuqq.setof.application.selleroption.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroupUpdateData;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerOptionGroupFactory {

    private final TimeProvider timeProvider;

    public SellerOptionGroupFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public SellerOptionGroupUpdateData toUpdateData(
            ProductGroupId productGroupId,
            List<UpdateSellerOptionGroupsCommand.OptionGroupCommand> optionGroups) {
        List<SellerOptionGroupUpdateData.GroupEntry> entries = new ArrayList<>();

        for (var group : optionGroups) {
            List<SellerOptionGroupUpdateData.ValueEntry> valueEntries =
                    group.optionValues().stream()
                            .map(
                                    v ->
                                            new SellerOptionGroupUpdateData.ValueEntry(
                                                    v.sellerOptionValueId(),
                                                    v.optionValueName(),
                                                    v.sortOrder()))
                            .toList();

            entries.add(
                    new SellerOptionGroupUpdateData.GroupEntry(
                            group.sellerOptionGroupId(),
                            group.optionGroupName(),
                            group.sortOrder(),
                            valueEntries));
        }

        return SellerOptionGroupUpdateData.of(productGroupId, entries, timeProvider.now());
    }
}
