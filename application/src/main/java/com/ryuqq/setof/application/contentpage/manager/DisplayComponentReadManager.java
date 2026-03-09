package com.ryuqq.setof.application.contentpage.manager;

import com.ryuqq.setof.application.contentpage.port.out.DisplayComponentQueryPort;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.query.ContentPageSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DisplayComponentReadManager - 디스플레이 컴포넌트 조회 매니저.
 *
 * <p>DisplayComponentQueryPort를 통해 컴포넌트 + ComponentSpec을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DisplayComponentReadManager {

    private final DisplayComponentQueryPort queryPort;

    public DisplayComponentReadManager(DisplayComponentQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public List<DisplayComponent> fetchDisplayComponents(ContentPageSearchCriteria criteria) {
        return queryPort.fetchDisplayComponents(criteria);
    }
}
