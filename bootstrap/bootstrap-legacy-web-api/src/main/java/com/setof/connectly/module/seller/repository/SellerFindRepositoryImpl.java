package com.setof.connectly.module.seller.repository;

import static com.setof.connectly.module.seller.entity.QSeller.seller;
import static com.setof.connectly.module.seller.entity.QSellerBusinessInfo.sellerBusinessInfo;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.seller.dto.QSellerInfo;
import com.setof.connectly.module.seller.dto.QSenderDto;
import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.seller.dto.SenderDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SellerFindRepositoryImpl implements SellerFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SellerInfo> fetchSeller(long sellerId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QSellerInfo(
                                        seller.id,
                                        sellerBusinessInfo.companyName,
                                        seller.sellerLogoUrl,
                                        seller.sellerDescription.coalesce(""),
                                        sellerBusinessInfo
                                                .businessAddressLine1
                                                .concat(" ")
                                                .append(sellerBusinessInfo.businessAddressLine2)
                                                .concat(" ")
                                                .append(sellerBusinessInfo.businessAddressZipCode),
                                        sellerBusinessInfo.csNumber.coalesce(
                                                sellerBusinessInfo.csPhoneNumber),
                                        sellerBusinessInfo.csPhoneNumber,
                                        sellerBusinessInfo.registrationNumber,
                                        sellerBusinessInfo.saleReportNumber,
                                        sellerBusinessInfo.representative,
                                        sellerBusinessInfo.csEmail))
                        .from(seller)
                        .innerJoin(sellerBusinessInfo)
                        .on(sellerBusinessInfo.id.eq(seller.id))
                        .where(sellerIdEq(sellerId))
                        .fetchFirst());
    }

    @Override
    public List<SenderDto> fetchSenders(List<Long> sellerIds) {

        return queryFactory
                .select(
                        new QSenderDto(
                                sellerBusinessInfo.id,
                                sellerBusinessInfo.companyName,
                                sellerBusinessInfo.csEmail,
                                sellerBusinessInfo.csPhoneNumber))
                .from(sellerBusinessInfo)
                .where(sellerIdIn(sellerIds))
                .fetch();
    }

    private BooleanExpression sellerIdEq(long sellerId) {
        return seller.id.eq(sellerId);
    }

    private BooleanExpression sellerIdIn(List<Long> sellerIds) {
        return sellerBusinessInfo.id.in(sellerIds);
    }
}
