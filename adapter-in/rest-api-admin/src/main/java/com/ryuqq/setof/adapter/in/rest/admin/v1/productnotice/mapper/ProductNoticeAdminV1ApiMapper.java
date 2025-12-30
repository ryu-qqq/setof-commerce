package com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.productnotice.dto.command.CreateProductNoticeV1ApiRequest;
import com.ryuqq.setof.application.productnotice.dto.command.NoticeItemDto;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Admin V1 Product Notice Mapper
 *
 * <p>V1 API 상품고시 요청을 Application Command로 변환합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticeAdminV1ApiMapper {

    /**
     * V1 상품고시 요청을 수정 Command로 변환
     *
     * <p>V1 API는 의류 카테고리 전용이므로 9개 필드 고정
     *
     * @param productNoticeId 상품고시 ID (상품그룹 ID 아님)
     * @param request V1 상품고시 요청
     * @return 수정 Command
     */
    public UpdateProductNoticeCommand toUpdateCommand(
            long productNoticeId, CreateProductNoticeV1ApiRequest request) {

        List<NoticeItemDto> items = new ArrayList<>();

        items.add(new NoticeItemDto("material", request.material(), 1));
        items.add(new NoticeItemDto("color", request.color(), 2));
        items.add(new NoticeItemDto("size", request.size(), 3));
        items.add(new NoticeItemDto("maker", request.maker(), 4));
        items.add(new NoticeItemDto("origin", request.origin(), 5));
        items.add(new NoticeItemDto("washingMethod", request.washingMethod(), 6));
        items.add(new NoticeItemDto("yearMonth", request.yearMonth(), 7));
        items.add(new NoticeItemDto("assuranceStandard", request.assuranceStandard(), 8));
        items.add(new NoticeItemDto("asPhone", request.asPhone(), 9));

        return new UpdateProductNoticeCommand(ProductNoticeId.of(productNoticeId), items);
    }
}
