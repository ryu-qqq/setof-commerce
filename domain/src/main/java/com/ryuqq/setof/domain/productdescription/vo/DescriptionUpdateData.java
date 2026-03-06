package com.ryuqq.setof.domain.productdescription.vo;

import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import java.time.Instant;
import java.util.List;

/**
 * Description 수정 데이터.
 *
 * <p>수정할 컨텐츠, CDN 경로, 새 이미지 목록, 수정 시각을 불변으로 보관합니다.
 */
public class DescriptionUpdateData {

    private final DescriptionHtml content;
    private final String cdnPath;
    private final List<DescriptionImage> newImages;
    private final Instant updatedAt;

    private DescriptionUpdateData(
            DescriptionHtml content,
            String cdnPath,
            List<DescriptionImage> newImages,
            Instant updatedAt) {
        this.content = content;
        this.cdnPath = cdnPath;
        this.newImages = newImages;
        this.updatedAt = updatedAt;
    }

    public static DescriptionUpdateData of(
            DescriptionHtml content,
            String cdnPath,
            List<DescriptionImage> newImages,
            Instant updatedAt) {
        return new DescriptionUpdateData(content, cdnPath, List.copyOf(newImages), updatedAt);
    }

    public DescriptionHtml content() {
        return content;
    }

    public String cdnPath() {
        return cdnPath;
    }

    public List<DescriptionImage> newImages() {
        return newImages;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
