package com.connectly.partnerAdmin.module.external.repository.site;

import com.connectly.partnerAdmin.module.external.entity.SellerSiteRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerSiteRelationRepository extends JpaRepository<SellerSiteRelation, Long> {
    List<SellerSiteRelation> findBySellerId(Long sellerId);
}
