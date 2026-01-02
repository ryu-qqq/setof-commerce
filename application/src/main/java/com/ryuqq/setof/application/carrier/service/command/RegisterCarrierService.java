package com.ryuqq.setof.application.carrier.service.command;

import com.ryuqq.setof.application.carrier.dto.command.RegisterCarrierCommand;
import com.ryuqq.setof.application.carrier.factory.command.CarrierCommandFactory;
import com.ryuqq.setof.application.carrier.manager.command.CarrierPersistenceManager;
import com.ryuqq.setof.application.carrier.manager.query.CarrierReadManager;
import com.ryuqq.setof.application.carrier.port.in.command.RegisterCarrierUseCase;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import com.ryuqq.setof.domain.carrier.vo.CarrierId;
import org.springframework.stereotype.Service;

/**
 * 택배사 등록 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>택배사 코드 중복 검증
 *   <li>CarrierCommandFactory로 Carrier 도메인 생성 (VO 검증 포함)
 *   <li>CarrierPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterCarrierService implements RegisterCarrierUseCase {

    private final CarrierCommandFactory carrierCommandFactory;
    private final CarrierPersistenceManager carrierPersistenceManager;
    private final CarrierReadManager carrierReadManager;

    public RegisterCarrierService(
            CarrierCommandFactory carrierCommandFactory,
            CarrierPersistenceManager carrierPersistenceManager,
            CarrierReadManager carrierReadManager) {
        this.carrierCommandFactory = carrierCommandFactory;
        this.carrierPersistenceManager = carrierPersistenceManager;
        this.carrierReadManager = carrierReadManager;
    }

    @Override
    public Long execute(RegisterCarrierCommand command) {
        validateCodeNotDuplicated(command.code());

        Carrier carrier = carrierCommandFactory.create(command);
        CarrierId carrierId = carrierPersistenceManager.persist(carrier);
        return carrierId.value();
    }

    private void validateCodeNotDuplicated(String code) {
        if (carrierReadManager.existsByCode(code)) {
            throw new IllegalArgumentException("택배사 코드가 이미 존재합니다: " + code);
        }
    }
}
