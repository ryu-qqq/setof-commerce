package com.setof.connectly.module.qna.service;

import com.setof.connectly.module.common.provider.AbstractProvider;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.enums.QnaType;
import com.setof.connectly.module.qna.service.query.AskService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AskStrategy extends AbstractProvider<QnaType, AskService<? extends CreateQna>> {

    public AskStrategy(List<AskService<? extends CreateQna>> services) {
        for (AskService<? extends CreateQna> service : services) {
            map.put(service.getQnaType(), service);
        }
    }
}
