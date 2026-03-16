package com.ryuqq.setof.application.selleroption.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
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

    /**
     * 등록 커맨드로부터 신규 SellerOptionGroup 목록을 생성합니다.
     *
     * @param productGroupId 상품그룹 ID
     * @param commands 옵션 그룹 커맨드 목록
     * @return 신규 SellerOptionGroup 목록
     */
    public List<SellerOptionGroup> createNewGroups(
            ProductGroupId productGroupId,
            List<RegisterProductGroupCommand.OptionGroupCommand> commands) {
        return commands.stream()
                .map(
                        og -> {
                            List<SellerOptionValue> values =
                                    og.optionValues().stream()
                                            .map(
                                                    ov ->
                                                            SellerOptionValue.forNew(
                                                                    SellerOptionGroupId.forNew(),
                                                                    OptionValueName.of(
                                                                            ov.optionValueName()),
                                                                    ov.sortOrder()))
                                            .toList();
                            return SellerOptionGroup.forNew(
                                    productGroupId,
                                    OptionGroupName.of(og.optionGroupName()),
                                    og.sortOrder(),
                                    values);
                        })
                .toList();
    }
}
