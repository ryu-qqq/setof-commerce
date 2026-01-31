package com.ryuqq.setof.application.commoncode.port.out.command;

import com.ryuqq.setof.domain.commoncode.aggregate.CommonCode;
import java.util.List;

/** 공통 코드 Command Port. */
public interface CommonCodeCommandPort {

    Long persist(CommonCode commonCode);

    void persistAll(List<CommonCode> commonCodes);
}
