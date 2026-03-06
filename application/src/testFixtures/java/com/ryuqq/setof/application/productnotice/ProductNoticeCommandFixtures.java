package com.ryuqq.setof.application.productnotice;

import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand.NoticeEntryCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import java.util.List;

/**
 * ProductNotice Application Command 테스트 Fixtures.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductNoticeCommandFixtures {

    private ProductNoticeCommandFixtures() {}

    public static final long DEFAULT_PRODUCT_GROUP_ID = 100L;

    // ===== RegisterProductNoticeCommand Fixtures =====

    public static RegisterProductNoticeCommand registerCommand() {
        return new RegisterProductNoticeCommand(DEFAULT_PRODUCT_GROUP_ID, defaultEntryCommands());
    }

    public static RegisterProductNoticeCommand registerCommand(long productGroupId) {
        return new RegisterProductNoticeCommand(productGroupId, defaultEntryCommands());
    }

    public static RegisterProductNoticeCommand registerCommandWithEmptyEntries() {
        return new RegisterProductNoticeCommand(DEFAULT_PRODUCT_GROUP_ID, List.of());
    }

    // ===== UpdateProductNoticeCommand Fixtures =====

    public static UpdateProductNoticeCommand updateCommand() {
        return new UpdateProductNoticeCommand(DEFAULT_PRODUCT_GROUP_ID, updatedEntryCommands());
    }

    public static UpdateProductNoticeCommand updateCommand(long productGroupId) {
        return new UpdateProductNoticeCommand(productGroupId, updatedEntryCommands());
    }

    // ===== NoticeEntryCommand Fixtures =====

    public static List<NoticeEntryCommand> defaultEntryCommands() {
        return List.of(
                new NoticeEntryCommand("소재", "면 100%", 1),
                new NoticeEntryCommand("세탁방법", "손세탁", 2),
                new NoticeEntryCommand("제조국", "대한민국", 3),
                new NoticeEntryCommand("제조자", "테스트제조사", 4));
    }

    public static List<UpdateProductNoticeCommand.NoticeEntryCommand> updatedEntryCommands() {
        return List.of(
                new UpdateProductNoticeCommand.NoticeEntryCommand("소재", "폴리에스터 100%", 1),
                new UpdateProductNoticeCommand.NoticeEntryCommand("세탁방법", "드라이클리닝", 2),
                new UpdateProductNoticeCommand.NoticeEntryCommand("제조국", "중국", 3));
    }

    public static NoticeEntryCommand noticeEntryCommand(
            String fieldName, String fieldValue, int sortOrder) {
        return new NoticeEntryCommand(fieldName, fieldValue, sortOrder);
    }
}
