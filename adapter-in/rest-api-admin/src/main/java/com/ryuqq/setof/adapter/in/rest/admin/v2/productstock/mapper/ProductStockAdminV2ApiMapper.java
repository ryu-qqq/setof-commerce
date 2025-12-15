package com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.BatchSetStockV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productstock.dto.command.SetStockV2ApiRequest;
import com.ryuqq.setof.application.productstock.dto.command.SetStockCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductStock Admin V2 API Mapper
 *
 * <p>재고 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ProductStockAdminV2ApiMapper {

    /** 단일 재고 설정 요청 → 설정 커맨드 변환 */
    public SetStockCommand toSetStockCommand(Long productId, SetStockV2ApiRequest request) {
        return new SetStockCommand(productId, request.quantity());
    }

    /** 일괄 재고 설정 요청 → 설정 커맨드 목록 변환 */
    public List<SetStockCommand> toSetStockCommands(BatchSetStockV2ApiRequest request) {
        return request.stocks().stream()
                .map(item -> new SetStockCommand(item.productId(), item.quantity()))
                .toList();
    }
}
