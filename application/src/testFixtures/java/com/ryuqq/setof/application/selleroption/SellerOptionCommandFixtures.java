package com.ryuqq.setof.application.selleroption;

import com.ryuqq.setof.application.selleroption.dto.command.RegisterSellerOptionGroupsCommand;
import com.ryuqq.setof.application.selleroption.dto.command.RegisterSellerOptionGroupsCommand.OptionGroupCommand;
import com.ryuqq.setof.application.selleroption.dto.command.RegisterSellerOptionGroupsCommand.OptionValueCommand;
import com.ryuqq.setof.application.selleroption.dto.command.UpdateSellerOptionGroupsCommand;
import java.util.List;

/**
 * SellerOption Application Command 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class SellerOptionCommandFixtures {

    private SellerOptionCommandFixtures() {}

    // ===== RegisterSellerOptionGroupsCommand Fixtures =====

    public static RegisterSellerOptionGroupsCommand registerCommand() {
        return new RegisterSellerOptionGroupsCommand(1L, "SINGLE", List.of(optionGroupCommand()));
    }

    public static RegisterSellerOptionGroupsCommand registerCommand(long productGroupId) {
        return new RegisterSellerOptionGroupsCommand(
                productGroupId, "SINGLE", List.of(optionGroupCommand()));
    }

    public static RegisterSellerOptionGroupsCommand registerCombinationCommand() {
        return new RegisterSellerOptionGroupsCommand(
                1L, "COMBINATION", List.of(optionGroupCommand(), optionGroupCommand2()));
    }

    public static OptionGroupCommand optionGroupCommand() {
        return new OptionGroupCommand(
                "색상", List.of(new OptionValueCommand("검정", 1), new OptionValueCommand("흰색", 2)));
    }

    public static OptionGroupCommand optionGroupCommand2() {
        return new OptionGroupCommand(
                "사이즈",
                List.of(
                        new OptionValueCommand("S", 1),
                        new OptionValueCommand("M", 2),
                        new OptionValueCommand("L", 3)));
    }

    // ===== UpdateSellerOptionGroupsCommand Fixtures =====

    public static UpdateSellerOptionGroupsCommand updateCommand() {
        return new UpdateSellerOptionGroupsCommand(1L, List.of(updateOptionGroupCommand(10L)));
    }

    public static UpdateSellerOptionGroupsCommand updateCommand(long productGroupId) {
        return new UpdateSellerOptionGroupsCommand(
                productGroupId, List.of(updateOptionGroupCommand(10L)));
    }

    public static UpdateSellerOptionGroupsCommand updateCommandWithNewGroup() {
        return new UpdateSellerOptionGroupsCommand(1L, List.of(updateOptionGroupCommand(null)));
    }

    public static UpdateSellerOptionGroupsCommand.OptionGroupCommand updateOptionGroupCommand(
            Long sellerOptionGroupId) {
        return new UpdateSellerOptionGroupsCommand.OptionGroupCommand(
                sellerOptionGroupId,
                "색상",
                null,
                "TEXT",
                List.of(
                        new UpdateSellerOptionGroupsCommand.OptionValueCommand(100L, "검정", null, 1),
                        new UpdateSellerOptionGroupsCommand.OptionValueCommand(
                                null, "빨강", null, 2)));
    }
}
