package com.ryuqq.setof.application.selleroption.dto.command;

import java.util.List;

public record UpdateSellerOptionGroupsCommand(
        long productGroupId, List<OptionGroupCommand> optionGroups) {

    public record OptionGroupCommand(
            Long sellerOptionGroupId,
            String optionGroupName,
            int sortOrder,
            List<OptionValueCommand> optionValues) {}

    public record OptionValueCommand(
            Long sellerOptionValueId, String optionValueName, int sortOrder) {}
}
