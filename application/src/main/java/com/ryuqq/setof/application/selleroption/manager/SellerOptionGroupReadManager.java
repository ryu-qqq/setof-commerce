package com.ryuqq.setof.application.selleroption.manager;

import com.ryuqq.setof.application.selleroption.port.out.query.SellerOptionGroupQueryPort;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.vo.SellerOptionGroups;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SellerOptionGroupReadManager {

    private final SellerOptionGroupQueryPort queryPort;

    public SellerOptionGroupReadManager(SellerOptionGroupQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public List<SellerOptionGroup> findByProductGroupId(ProductGroupId productGroupId) {
        return queryPort.findByProductGroupId(productGroupId);
    }

    @Transactional(readOnly = true)
    public SellerOptionGroups getByProductGroupId(ProductGroupId productGroupId) {
        List<SellerOptionGroup> groups = queryPort.findByProductGroupId(productGroupId);
        return SellerOptionGroups.reconstitute(groups);
    }
}
