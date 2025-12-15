package com.ryuqq.setof.application.productstock.factory.command;

import com.ryuqq.setof.application.productstock.assembler.ProductStockAssembler;
import com.ryuqq.setof.application.productstock.dto.command.InitializeStockCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.productstock.aggregate.ProductStock;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ProductStock Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductStockCommandFactory {

    private final ProductStockAssembler productStockAssembler;
    private final ClockHolder clockHolder;

    public ProductStockCommandFactory(
            ProductStockAssembler productStockAssembler, ClockHolder clockHolder) {
        this.productStockAssembler = productStockAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 재고 생성
     *
     * @param command 초기화 커맨드
     * @return 생성된 ProductStock (저장 전)
     */
    public ProductStock createProductStock(InitializeStockCommand command) {
        Instant now = Instant.now(clockHolder.getClock());
        return productStockAssembler.toDomain(command, now);
    }

    /**
     * 현재 시각 반환
     *
     * @return 현재 Instant
     */
    public Instant now() {
        return Instant.now(clockHolder.getClock());
    }
}
