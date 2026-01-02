package com.connectly.partnerAdmin.module.seller.service;

import com.connectly.partnerAdmin.auth.repository.AdministratorsRepository;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.external.entity.SellerSiteRelation;
import com.connectly.partnerAdmin.module.external.entity.Site;
import com.connectly.partnerAdmin.module.external.repository.site.SellerSiteRelationRepository;
import com.connectly.partnerAdmin.module.external.repository.site.SiteRepository;
import com.connectly.partnerAdmin.module.seller.controller.request.SellerApprovalStatusRequestDto;
import com.connectly.partnerAdmin.module.seller.controller.request.SellerInfoContextRequestDto;
import com.connectly.partnerAdmin.module.seller.controller.request.SellerUpdateDetailRequestDto;
import com.connectly.partnerAdmin.module.seller.entity.Seller;
import com.connectly.partnerAdmin.module.seller.entity.SellerBusinessInfo;
import com.connectly.partnerAdmin.module.seller.entity.SellerShippingInfo;
import com.connectly.partnerAdmin.module.seller.exception.SellerNotFoundException;
import com.connectly.partnerAdmin.module.seller.repository.SellerBusinessInfoRepository;
import com.connectly.partnerAdmin.module.seller.repository.SellerRepository;
import com.connectly.partnerAdmin.module.seller.repository.SellerShippingInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerCommandService {
    private final AdministratorsRepository administratorsRepository;
    private final SellerRepository sellerRepository;
    private final SellerShippingInfoRepository sellerShippingInfoRepository;
    private final SellerBusinessInfoRepository sellerBusinessInfoRepository;
    private final SiteRepository siteRepository;
    private final SellerSiteRelationRepository sellerSiteRelationRepository;

    @Transactional
    public long insert(SellerInfoContextRequestDto sellerInfoContextRequestDto){
        Seller sellerEntity = sellerInfoContextRequestDto.getSellerInfo().toSellerEntity();
        Seller savedSeller = sellerRepository.save(sellerEntity);

        SellerShippingInfo sellerShippingInfoEntity = sellerInfoContextRequestDto.getSellerShippingInfo().toSellerShippingInfoEntity(savedSeller.getId());
        sellerShippingInfoRepository.save(sellerShippingInfoEntity);

        SellerBusinessInfo sellerBusinessInfoEntity = sellerInfoContextRequestDto.getSellerBusinessInfo().toSellerBusinessInfoEntity(savedSeller.getId());
        sellerBusinessInfoRepository.save(sellerBusinessInfoEntity);

        return savedSeller.getId();
    }

    @Transactional
    public long update(long sellerId, SellerUpdateDetailRequestDto sellerUpdateDetailRequestDto){
        Seller sellerEntity = sellerRepository.findById(sellerId).orElseThrow(SellerNotFoundException::new);
        SellerShippingInfo sellerShippingInfo = sellerShippingInfoRepository.findById(sellerId).orElseThrow(SellerNotFoundException::new);
        SellerBusinessInfo sellerBusinessInfo = sellerBusinessInfoRepository.findById(sellerId).orElseThrow(SellerNotFoundException::new);

        sellerEntity.updateDetail(sellerUpdateDetailRequestDto.getSellerName(), sellerUpdateDetailRequestDto.getCommissionRate());
        sellerShippingInfo.update(sellerUpdateDetailRequestDto.toSellerShippingInfoEntity(sellerId));
        sellerBusinessInfo.update(sellerUpdateDetailRequestDto.toSellerBusinessInfoEntity(sellerBusinessInfo));
        findAndUpdateSellerSiteRelations(sellerId, sellerUpdateDetailRequestDto);

        return sellerId;
    }

    @Transactional
    public List<Long> updateApprovalStatus(SellerApprovalStatusRequestDto sellerApprovalStatusRequestDto){
        List<Seller> sellers = sellerRepository.findAllById(sellerApprovalStatusRequestDto.sellerIds());
        sellers.forEach(seller -> seller.updateStatus(sellerApprovalStatusRequestDto.approvalStatus()));
        return sellerApprovalStatusRequestDto.sellerIds();
    }

    private void findAndUpdateSellerSiteRelations(long sellerId, SellerUpdateDetailRequestDto sellerUpdateDetailRequestDto) {
        List<SellerSiteRelation> sellerSiteRelations = sellerSiteRelationRepository.findBySellerId(sellerId);

        List<Long> existingSiteIds = sellerSiteRelations.stream()
                .map(SellerSiteRelation::getSiteId)
                .toList();

        sellerSiteRelations.forEach(relation -> {
            if (sellerUpdateDetailRequestDto.getSiteIds().contains(relation.getSiteId())) {
                relation.updateActiveYn(Yn.Y);
            } else {
                relation.updateActiveYn(Yn.N);
            }
            sellerSiteRelationRepository.save(relation);
        });

        sellerUpdateDetailRequestDto.getSiteIds().stream()
                .filter(siteId -> !existingSiteIds.contains(siteId))
                .forEach(siteId -> {
                    Site site = siteRepository.findById(siteId).orElseThrow(SellerNotFoundException::new);
                    sellerSiteRelationRepository.save(site.toSellerSiteRelation(sellerId));
                });
    }

}
