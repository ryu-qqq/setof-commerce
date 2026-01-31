package com.ryuqq.setof.application.seller.port.out.query;

import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.id.SellerId;
import com.ryuqq.setof.domain.seller.query.SellerSearchCriteria;
import java.util.List;
import java.util.Optional;

/** Seller Query Port. */
public interface SellerQueryPort {

    Optional<Seller> findById(SellerId id);

    List<Seller> findByIds(List<SellerId> ids);

    boolean existsById(SellerId id);

    boolean existsBySellerName(String sellerName);

    boolean existsBySellerNameExcluding(String sellerName, SellerId excludeId);

    List<Seller> findByCriteria(SellerSearchCriteria criteria);

    long countByCriteria(SellerSearchCriteria criteria);
}
