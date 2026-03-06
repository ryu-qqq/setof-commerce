package com.ryuqq.setof.application.seller.manager;

import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.exception.SellerNotFoundException;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerReadManager {

    private final SellerQueryPort sellerQueryPort;

    public SellerReadManager(SellerQueryPort sellerQueryPort) {
        this.sellerQueryPort = sellerQueryPort;
    }

    public Seller getById(Long id) {
        return sellerQueryPort.findById(id).orElseThrow(() -> new SellerNotFoundException(id));
    }

    public List<Seller> getByIds(List<Long> ids) {
        return sellerQueryPort.findByIds(ids);
    }

    public boolean existsById(Long id) {
        return sellerQueryPort.existsById(id);
    }

    public boolean existsBySellerName(String sellerName) {
        return sellerQueryPort.existsBySellerName(sellerName);
    }

    public boolean existsBySellerNameExcluding(String sellerName, SellerId excludeId) {
        return sellerQueryPort.existsBySellerNameExcluding(sellerName, excludeId);
    }

    public List<Seller> findByCriteria(SellerSearchCriteria criteria) {
        return sellerQueryPort.findByCriteria(criteria);
    }

    public long countByCriteria(SellerSearchCriteria criteria) {
        return sellerQueryPort.countByCriteria(criteria);
    }
}
