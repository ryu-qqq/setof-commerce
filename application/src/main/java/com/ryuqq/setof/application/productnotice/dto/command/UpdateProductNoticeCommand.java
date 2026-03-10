package com.ryuqq.setof.application.productnotice.dto.command;

import java.util.List;

/**
 * 상품고시 수정 커맨드.
 *
 * @param productGroupId 상품그룹 ID
 * @param entries 고시 항목 목록
 */
public record UpdateProductNoticeCommand(long productGroupId, List<NoticeEntryCommand> entries) {

    /**
     * 고시 항목 커맨드.
     *
     * @param noticeFieldId 고시 필드 ID
     * @param fieldName 필드명
     * @param fieldValue 필드값
     */
    public record NoticeEntryCommand(long noticeFieldId, String fieldName, String fieldValue) {}
}
