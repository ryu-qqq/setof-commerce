package com.ryuqq.setof.application.shippingpolicy.factory.command;

import com.ryuqq.setof.application.shippingpolicy.assembler.ShippingPolicyAssembler;
import com.ryuqq.setof.application.shippingpolicy.dto.command.RegisterShippingPolicyCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingpolicy.aggregate.ShippingPolicy;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * ShippingPolicy Command Factory
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
public class ShippingPolicyCommandFactory {

    private final ShippingPolicyAssembler shippingPolicyAssembler;
    private final ClockHolder clockHolder;

    public ShippingPolicyCommandFactory(
            ShippingPolicyAssembler shippingPolicyAssembler, ClockHolder clockHolder) {
        this.shippingPolicyAssembler = shippingPolicyAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 배송 정책 생성
     *
     * @param command 배송 정책 등록 커맨드
     * @return 생성된 ShippingPolicy (저장 전)
     */
    public ShippingPolicy create(RegisterShippingPolicyCommand command) {
        return shippingPolicyAssembler.toDomain(command, Instant.now(clockHolder.getClock()));
    }
}
