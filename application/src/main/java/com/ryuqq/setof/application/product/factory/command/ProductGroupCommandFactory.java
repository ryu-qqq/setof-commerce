package com.ryuqq.setof.application.product.factory.command;

import com.ryuqq.setof.application.product.assembler.ProductGroupAssembler;
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand;
import com.ryuqq.setof.application.product.dto.command.RegisterProductGroupCommand.RegisterProductCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.OptionType;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroup Command Factory
 *
 * <p>Command → Domain 변환 전용 Factory
 *
 * <p>역할:
 *
 * <ul>
 *   <li>Command DTO를 Domain 객체로 변환
 *   <li>도메인 생성 로직 캡슐화
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductGroupCommandFactory {

    private final ProductGroupAssembler productGroupAssembler;
    private final ClockHolder clockHolder;

    public ProductGroupCommandFactory(
            ProductGroupAssembler productGroupAssembler, ClockHolder clockHolder) {
        this.productGroupAssembler = productGroupAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 상품그룹 생성
     *
     * @param command 상품그룹 등록 커맨드
     * @return 생성된 ProductGroup (저장 전)
     */
    public ProductGroup createProductGroup(RegisterProductGroupCommand command) {
        Instant now = Instant.now(clockHolder.getClock());
        return productGroupAssembler.toDomain(command, now);
    }

    /**
     * 상품(SKU) 목록 생성
     *
     * @param productGroupId 상품그룹 ID
     * @param optionType 옵션 타입
     * @param commands 상품 등록 커맨드 목록
     * @return 생성된 Product 목록 (저장 전)
     */
    public List<Product> createProducts(
            Long productGroupId, OptionType optionType, List<RegisterProductCommand> commands) {
        Instant now = Instant.now(clockHolder.getClock());
        return commands.stream()
                .map(
                        cmd ->
                                productGroupAssembler.toProductDomain(
                                        productGroupId, optionType, cmd, now))
                .toList();
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
