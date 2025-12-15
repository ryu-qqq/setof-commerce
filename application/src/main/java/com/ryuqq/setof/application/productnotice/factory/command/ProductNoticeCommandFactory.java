package com.ryuqq.setof.application.productnotice.factory.command;

import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import com.ryuqq.setof.domain.productnotice.vo.NoticeTemplateId;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 상품고시 Command Factory
 *
 * <p>Command DTO → Domain 변환을 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticeCommandFactory {

    private final ClockHolder clockHolder;

    public ProductNoticeCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 현재 시각 반환
     *
     * @return 현재 Instant
     */
    public Instant now() {
        return Instant.now(clockHolder.getClock());
    }

    /**
     * 등록 Command → Domain 변환
     *
     * @param command 등록 Command
     * @return ProductNotice 도메인
     */
    public ProductNotice createProductNotice(RegisterProductNoticeCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        NoticeTemplateId templateId = NoticeTemplateId.of(command.templateId());
        List<NoticeItem> items = toItems(command.items());

        return ProductNotice.create(command.productGroupId(), templateId, items, now);
    }

    /**
     * 항목 DTO 목록 → Domain 목록 변환
     *
     * @param itemDtos 항목 DTO 목록
     * @return 항목 Domain 목록
     */
    public List<NoticeItem> toItems(List<NoticeItemDto> itemDtos) {
        if (itemDtos == null || itemDtos.isEmpty()) {
            return List.of();
        }

        return itemDtos.stream().map(this::toItem).toList();
    }

    /**
     * 항목 DTO → Domain 변환
     *
     * @param dto 항목 DTO
     * @return 항목 Domain
     */
    private NoticeItem toItem(NoticeItemDto dto) {
        return NoticeItem.of(dto.fieldKey(), dto.fieldValue(), dto.displayOrder());
    }
}
