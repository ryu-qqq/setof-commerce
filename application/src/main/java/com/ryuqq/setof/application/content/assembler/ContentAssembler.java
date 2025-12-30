package com.ryuqq.setof.application.content.assembler;

import com.ryuqq.setof.application.content.dto.response.ContentResponse;
import com.ryuqq.setof.application.content.dto.response.ContentSummaryResponse;
import com.ryuqq.setof.domain.cms.aggregate.content.Content;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Content Assembler
 *
 * <p>Domain 객체 → Response DTO 변환을 담당
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ContentAssembler {

    /**
     * Content 도메인을 ContentResponse로 변환
     *
     * @param content Content 도메인 객체
     * @return ContentResponse
     */
    public ContentResponse toResponse(Content content) {
        return ContentResponse.of(
                content.getIdValue(),
                content.getTitleValue(),
                content.getMemoValue(),
                content.getImageUrlValue(),
                content.getStatusValue(),
                content.getDisplayStartDate(),
                content.getDisplayEndDate(),
                content.createdAt(),
                content.updatedAt());
    }

    /**
     * Content 도메인을 ContentSummaryResponse로 변환
     *
     * @param content Content 도메인 객체
     * @param componentCount 포함된 컴포넌트 수
     * @return ContentSummaryResponse
     */
    public ContentSummaryResponse toSummaryResponse(Content content, int componentCount) {
        return ContentSummaryResponse.of(
                content.getIdValue(),
                content.getTitleValue(),
                content.getStatusValue(),
                content.getDisplayStartDate(),
                content.getDisplayEndDate(),
                componentCount);
    }

    /**
     * Content 도메인 목록을 ContentSummaryResponse 목록으로 변환
     *
     * @param contents Content 도메인 목록
     * @return ContentSummaryResponse 목록
     */
    public List<ContentSummaryResponse> toSummaryResponses(List<Content> contents) {
        return contents.stream().map(c -> toSummaryResponse(c, 0)).toList();
    }
}
