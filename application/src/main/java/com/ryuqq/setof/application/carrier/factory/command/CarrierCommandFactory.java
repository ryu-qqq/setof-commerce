package com.ryuqq.setof.application.carrier.factory.command;

import com.ryuqq.setof.application.carrier.dto.command.RegisterCarrierCommand;
import com.ryuqq.setof.application.carrier.dto.command.UpdateCarrierCommand;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierCode;
import com.ryuqq.setof.domain.carrier.vo.CarrierName;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * Carrier Command Factory
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
public class CarrierCommandFactory {

    private final ClockHolder clockHolder;

    public CarrierCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * 택배사 생성
     *
     * @param command 택배사 등록 커맨드
     * @return 생성된 Carrier (저장 전)
     */
    public Carrier create(RegisterCarrierCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        CarrierCode code = CarrierCode.of(command.code());
        CarrierName name = CarrierName.of(command.name());

        return Carrier.forNew(
                code, name, command.trackingUrlTemplate(), command.displayOrder(), now);
    }

    /**
     * 택배사 수정 적용
     *
     * @param carrier 기존 Carrier
     * @param command 수정 커맨드
     * @return 수정된 Carrier
     */
    public Carrier applyUpdate(Carrier carrier, UpdateCarrierCommand command) {
        Instant now = Instant.now(clockHolder.getClock());

        CarrierName newName = CarrierName.of(command.name());

        return carrier.update(newName, command.trackingUrlTemplate(), command.displayOrder(), now);
    }
}
