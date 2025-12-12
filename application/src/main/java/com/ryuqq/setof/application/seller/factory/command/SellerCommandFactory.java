package com.ryuqq.setof.application.seller.factory.command;

import com.ryuqq.setof.application.seller.assembler.SellerAssembler;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Seller Command Factory
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
public class SellerCommandFactory {

    private final SellerAssembler sellerAssembler;
    private final ClockHolder clockHolder;

    public SellerCommandFactory(SellerAssembler sellerAssembler, ClockHolder clockHolder) {
        this.sellerAssembler = sellerAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 셀러 생성
     *
     * @param command 셀러 등록 커맨드
     * @return 생성된 Seller (저장 전)
     */
    public Seller create(RegisterSellerCommand command) {
        return sellerAssembler.toDomain(command, Instant.now(clockHolder.getClock()));
    }
}
