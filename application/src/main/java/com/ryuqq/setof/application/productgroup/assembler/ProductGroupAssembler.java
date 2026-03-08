package com.ryuqq.setof.application.productgroup.assembler;

import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupListBundle;
import com.ryuqq.setof.application.productgroup.dto.composite.ProductGroupThumbnailCompositeResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupSliceResult;
import com.ryuqq.setof.application.productgroup.dto.response.ProductGroupThumbnailResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupAssembler - 상품그룹 Result DTO 조립.
 *
 * <p>ReadFacade에서 조회한 번들 데이터를 조합하여 최종 응답 객체를 생성합니다. 조립 로직은 Service에서 이 Assembler를 통해 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupAssembler {

    /**
     * 커서 기반 목록 번들 → 슬라이스 결과 조립.
     *
     * <p>pageSize + 1개 조회한 결과에서 hasNext를 판별하고, 커서를 결정합니다. cursor는 레거시 방식과 동일하게
     * "lastDomainId,cursorValue" 형식으로 인코딩합니다.
     *
     * @param bundle 썸네일 번들 (thumbnails + totalElements + orderType)
     * @param requestedSize 요청 페이지 크기
     * @return 슬라이스 결과
     */
    public ProductGroupSliceResult toSliceResult(ProductGroupListBundle bundle, int requestedSize) {
        if (bundle.thumbnails().isEmpty()) {
            return ProductGroupSliceResult.empty(requestedSize);
        }

        boolean hasNext = bundle.thumbnails().size() > requestedSize;
        List<ProductGroupThumbnailCompositeResult> rawContent =
                hasNext ? bundle.thumbnails().subList(0, requestedSize) : bundle.thumbnails();

        List<ProductGroupThumbnailResult> content =
                rawContent.stream().map(ProductGroupThumbnailResult::from).toList();

        String nextCursor = resolveNextCursor(rawContent, bundle.orderType());
        SliceMeta sliceMeta =
                SliceMeta.withCursor(nextCursor, requestedSize, hasNext, content.size());

        return ProductGroupSliceResult.of(content, sliceMeta, bundle.totalElements());
    }

    /**
     * 다음 커서 값 결정.
     *
     * <p>레거시 커서 전략: - 마지막 아이템의 productGroupId를 lastDomainId로 사용 - orderType에 따라 cursorValue를 결정 -
     * "lastDomainId,cursorValue" 형식으로 인코딩
     */
    private String resolveNextCursor(
            List<ProductGroupThumbnailCompositeResult> content, String orderType) {
        if (content.isEmpty()) {
            return null;
        }

        ProductGroupThumbnailCompositeResult last = content.get(content.size() - 1);
        long lastDomainId = last.productGroupId();

        String cursorValue = resolveCursorValue(last, orderType);

        if (cursorValue != null) {
            return lastDomainId + "," + cursorValue;
        }
        return String.valueOf(lastDomainId);
    }

    /** orderType에 따른 cursorValue 결정. */
    private String resolveCursorValue(ProductGroupThumbnailCompositeResult item, String orderType) {
        if (orderType == null) {
            return null;
        }
        return switch (orderType.toUpperCase()) {
            case "RECOMMEND" -> String.valueOf(item.score());
            case "REVIEW" -> String.valueOf(item.reviewCount());
            case "HIGH_RATING" -> String.valueOf(item.averageRating());
            case "LOW_PRICE", "HIGH_PRICE" -> String.valueOf(item.salePrice());
            case "LOW_DISCOUNT", "HIGH_DISCOUNT" -> String.valueOf(item.discountRate());
            case "RECENT" -> item.insertDate() != null ? item.insertDate().toString() : null;
            default -> null;
        };
    }
}
