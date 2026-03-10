package com.ryuqq.setof.application.productnotice.factory;

import com.ryuqq.setof.application.common.time.TimeProvider;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand.NoticeEntryCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * ProductNoticeCommandFactory - 상품고시 도메인 객체 생성 Factory.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductNoticeCommandFactory {

    private final TimeProvider timeProvider;

    public ProductNoticeCommandFactory(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    /**
     * 등록 커맨드로부터 새 ProductNotice 도메인 객체를 생성합니다.
     *
     * @param command 등록 커맨드
     * @return 새 ProductNotice 인스턴스
     */
    public ProductNotice createNewNotice(RegisterProductNoticeCommand command) {
        Instant now = timeProvider.now();
        List<ProductNoticeEntry> entries = createNewEntries(command.entries());
        return ProductNotice.forNew(ProductGroupId.of(command.productGroupId()), entries, now);
    }

    /**
     * 수정 커맨드로부터 ProductNoticeUpdateData를 생성합니다.
     *
     * @param command 수정 커맨드
     * @return ProductNoticeUpdateData 인스턴스
     */
    public ProductNoticeUpdateData createUpdateData(UpdateProductNoticeCommand command) {
        Instant now = timeProvider.now();
        List<ProductNoticeEntry> entries = createUpdateEntries(command.entries());
        return ProductNoticeUpdateData.of(entries, now);
    }

    private List<ProductNoticeEntry> createNewEntries(List<NoticeEntryCommand> entryCommands) {
        if (entryCommands == null || entryCommands.isEmpty()) {
            return List.of();
        }
        AtomicInteger sortOrder = new AtomicInteger(0);
        return entryCommands.stream()
                .map(
                        cmd ->
                                ProductNoticeEntry.forNew(
                                        NoticeFieldId.of(cmd.noticeFieldId()),
                                        NoticeFieldValue.of(cmd.fieldName(), cmd.fieldValue()),
                                        sortOrder.getAndIncrement()))
                .toList();
    }

    private List<ProductNoticeEntry> createUpdateEntries(
            List<UpdateProductNoticeCommand.NoticeEntryCommand> entryCommands) {
        if (entryCommands == null || entryCommands.isEmpty()) {
            return List.of();
        }
        AtomicInteger sortOrder = new AtomicInteger(0);
        return entryCommands.stream()
                .map(
                        cmd ->
                                ProductNoticeEntry.forNew(
                                        NoticeFieldId.of(cmd.noticeFieldId()),
                                        NoticeFieldValue.of(cmd.fieldName(), cmd.fieldValue()),
                                        sortOrder.getAndIncrement()))
                .toList();
    }
}
