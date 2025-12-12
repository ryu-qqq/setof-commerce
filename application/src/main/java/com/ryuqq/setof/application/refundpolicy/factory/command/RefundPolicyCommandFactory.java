package com.ryuqq.setof.application.refundpolicy.factory.command;

import com.ryuqq.setof.application.refundpolicy.assembler.RefundPolicyAssembler;
import com.ryuqq.setof.application.refundpolicy.dto.command.RegisterRefundPolicyCommand;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.refundpolicy.aggregate.RefundPolicy;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * RefundPolicy Command Factory
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
public class RefundPolicyCommandFactory {

    private final RefundPolicyAssembler refundPolicyAssembler;
    private final ClockHolder clockHolder;

    public RefundPolicyCommandFactory(
            RefundPolicyAssembler refundPolicyAssembler, ClockHolder clockHolder) {
        this.refundPolicyAssembler = refundPolicyAssembler;
        this.clockHolder = clockHolder;
    }

    /**
     * 환불 정책 생성
     *
     * @param command 환불 정책 등록 커맨드
     * @return 생성된 RefundPolicy (저장 전)
     */
    public RefundPolicy create(RegisterRefundPolicyCommand command) {
        return refundPolicyAssembler.toDomain(command, Instant.now(clockHolder.getClock()));
    }
}
