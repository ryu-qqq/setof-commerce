package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.external.dto.qna.OcoQna;
import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import com.connectly.partnerAdmin.module.external.repository.qna.ExternalQnaRepository;
import com.connectly.partnerAdmin.module.order.enums.SiteName;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateProductQna;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.mapper.QnaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OcoQnaService extends AbstractExternalQnaService<OcoQna> {

    public OcoQnaService(QnaMapper qnaMapper, ExternalQnaRepository externalQnaRepository, ProductGroupFetchService productGroupFetchService) {
        super(qnaMapper, externalQnaRepository, productGroupFetchService);
    }

    @Override
    public ExternalQna syncQna(OcoQna ocoQna) {
        Qna qna = toQna(ocoQna);
        return saveExternalQna(ocoQna, qna);
    }

    @Override
    public SiteName getSiteName() {
        return SiteName.OCO;
    }

    @Override
    Qna toQna(OcoQna ocoQna) {
        QnaMapper qnaMapper = getQnaMapper();
        CreateProductQna createProductQna = toCreateProductQna(ocoQna);
        return qnaMapper.toQna(createProductQna);
    }
}
