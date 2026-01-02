package com.connectly.partnerAdmin.module.external.service.qna;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.core.ExMallQna;
import com.connectly.partnerAdmin.module.external.entity.ExternalQna;
import com.connectly.partnerAdmin.module.external.repository.qna.ExternalQnaRepository;
import com.connectly.partnerAdmin.module.product.dto.ProductGroupFetchResponse;
import com.connectly.partnerAdmin.module.product.service.group.ProductGroupFetchService;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateProductQna;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaContents;
import com.connectly.partnerAdmin.module.qna.entity.Qna;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import com.connectly.partnerAdmin.module.qna.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public abstract class AbstractExternalQnaService <T extends ExMallQna> implements ExternalQnaService<T> {

    private final QnaMapper qnaMapper;
    private final ExternalQnaRepository externalQnaRepository;
    private final ProductGroupFetchService productGroupFetchService;


    protected ExternalQna saveExternalQna(T t, Qna qna){
        ExternalQna externalQna = toInterlockingQna(t, qna);
        return externalQnaRepository.save(externalQna);
    }

    protected QnaMapper getQnaMapper(){
        return qnaMapper;
    }

    private ExternalQna toInterlockingQna(T t, Qna qna){
        return ExternalQna.builder()
                .siteId(t.getSiteName().getSiteId())
                .qnaId(qna.getId())
                .externalIdx(t.getExternalIdx())
                .build();
    }

    protected CreateProductQna toCreateProductQna(T t){
        long productGroupId = t.getTargetId();
        ProductGroupFetchResponse productGroupFetchResponse = productGroupFetchService.fetchProductGroup(productGroupId);
        return CreateProductQna.builder()
                .qnaType(QnaType.PRODUCT)
                .privateYn(Yn.Y)
                .qnaDetailType(QnaDetailType.ETC)
                .sellerId(productGroupFetchResponse.getSellerId())
                .productGroupId(productGroupFetchResponse.getProductGroupId())
                .qnaContents(new CreateQnaContents(t.getTitle(), t.getContent()))
                .build();
    }

    abstract Qna toQna(T t);

}
