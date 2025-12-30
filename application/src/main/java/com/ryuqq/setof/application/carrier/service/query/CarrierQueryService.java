package com.ryuqq.setof.application.carrier.service.query;

import com.ryuqq.setof.application.carrier.manager.query.CarrierReadManager;
import com.ryuqq.setof.application.carrier.port.in.query.GetCarrierByCodeUseCase;
import com.ryuqq.setof.application.carrier.port.in.query.GetCarrierUseCase;
import com.ryuqq.setof.application.carrier.port.in.query.GetCarriersUseCase;
import com.ryuqq.setof.domain.carrier.aggregate.Carrier;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 택배사 조회 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>CarrierReadManager로 택배사 조회
 *   <li>조회된 Carrier 반환
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class CarrierQueryService
        implements GetCarrierUseCase, GetCarriersUseCase, GetCarrierByCodeUseCase {

    private final CarrierReadManager carrierReadManager;

    public CarrierQueryService(CarrierReadManager carrierReadManager) {
        this.carrierReadManager = carrierReadManager;
    }

    @Override
    public Carrier execute(Long carrierId) {
        return carrierReadManager.findById(carrierId);
    }

    @Override
    public Optional<Carrier> execute(String carrierCode) {
        return carrierReadManager.findByCodeOptional(carrierCode);
    }

    @Override
    public List<Carrier> getActiveCarriers() {
        return carrierReadManager.findAllActive();
    }

    @Override
    public List<Carrier> getAllCarriers() {
        return carrierReadManager.findAll();
    }
}
