package com.ryuqq.setof.application.claim.factory.command;

import com.ryuqq.setof.application.claim.dto.command.RequestClaimCommand;
import com.ryuqq.setof.domain.claim.aggregate.Claim;
import com.ryuqq.setof.domain.claim.vo.ClaimReason;
import com.ryuqq.setof.domain.claim.vo.ClaimType;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import org.springframework.stereotype.Component;

/**
 * ClaimCommandFactory - Claim Command → Domain 변환 Factory
 *
 * <p>Command DTO를 Domain Aggregate로 변환합니다.
 *
 * <p>String → Domain Enum 변환은 이 Factory에서 수행합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class ClaimCommandFactory {

    private final ClockHolder clockHolder;

    public ClaimCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * RequestClaimCommand → Claim 변환
     *
     * @param command 클레임 요청 Command
     * @return 새로 생성된 Claim Aggregate
     */
    public Claim create(RequestClaimCommand command) {
        return Claim.request(
                command.orderId(),
                command.orderItemId(),
                ClaimType.valueOf(command.claimType()),
                ClaimReason.valueOf(command.claimReason()),
                command.claimReasonDetail(),
                command.quantity(),
                command.refundAmount(),
                clockHolder.getClock().instant());
    }
}
