package com.ryuqq.setof.application.gnb.service.query;

import com.ryuqq.setof.application.gnb.assembler.GnbAssembler;
import com.ryuqq.setof.application.gnb.dto.response.GnbResponse;
import com.ryuqq.setof.application.gnb.manager.query.GnbReadManager;
import com.ryuqq.setof.application.gnb.port.in.query.GetAllGnbUseCase;
import com.ryuqq.setof.application.gnb.port.in.query.GetGnbUseCase;
import com.ryuqq.setof.domain.cms.aggregate.gnb.Gnb;
import com.ryuqq.setof.domain.cms.vo.GnbId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Gnb 조회 Service
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class GnbQueryService implements GetGnbUseCase, GetAllGnbUseCase {

    private final GnbReadManager gnbReadManager;
    private final GnbAssembler gnbAssembler;

    public GnbQueryService(GnbReadManager gnbReadManager, GnbAssembler gnbAssembler) {
        this.gnbReadManager = gnbReadManager;
        this.gnbAssembler = gnbAssembler;
    }

    @Override
    public GnbResponse execute(GnbId gnbId) {
        Gnb gnb = gnbReadManager.findById(gnbId);
        return gnbAssembler.toResponse(gnb);
    }

    @Override
    public List<GnbResponse> execute() {
        List<Gnb> gnbs = gnbReadManager.findAllActive();
        return gnbAssembler.toResponses(gnbs);
    }
}
