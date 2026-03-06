package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.List;
import java.util.Optional;

public interface SellerQueryPort {
    Optional<Seller> findById(Long id);

    List<Seller> findByIds(List<Long> ids);

    boolean existsById(Long id);

    boolean existsBySellerName(String sellerName);

    boolean existsBySellerNameExcluding(String sellerName, SellerId excludeId);

    List<Seller> findByCriteria(SellerSearchCriteria criteria);

    long countByCriteria(SellerSearchCriteria criteria);
}
