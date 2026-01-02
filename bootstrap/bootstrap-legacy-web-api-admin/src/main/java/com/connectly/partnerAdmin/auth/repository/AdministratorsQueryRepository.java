package com.connectly.partnerAdmin.auth.repository;

import com.connectly.partnerAdmin.auth.dto.AdminValidation;
import com.connectly.partnerAdmin.auth.dto.AdministratorResponse;
import com.connectly.partnerAdmin.auth.dto.QAdministratorResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.connectly.partnerAdmin.auth.entity.QAdministrators.*;
import static com.connectly.partnerAdmin.module.seller.entity.QSeller.seller;

@Repository
@RequiredArgsConstructor
public class AdministratorsQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<AdministratorResponse> fetchAdministrators(Pageable pageable){
        return queryFactory
                .select(
                        new QAdministratorResponse(
                                administrators.id,
                                administrators.email,
                                administrators.fullName,
                                administrators.phoneNumber,
                                administrators.sellerId,
                                seller.sellerName,
                                administrators.approvalStatus
                        )
                )
                .from(administrators)
                .innerJoin(seller)
                    .on(seller.id.eq(administrators.sellerId))
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber())
                .fetch();
    }

    public List<AdministratorResponse> fetchAdministratorBySellerId(long sellerId, Pageable pageable){
        return queryFactory
                .select(
                        new QAdministratorResponse(
                                administrators.id,
                                administrators.email,
                                administrators.fullName,
                                administrators.phoneNumber,
                                administrators.sellerId,
                                seller.sellerName,
                                administrators.approvalStatus
                        )
                )
                .from(administrators)
                .innerJoin(seller)
                .on(seller.id.eq(administrators.sellerId), seller.id.eq(sellerId))
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber())
                .fetch();
    }

    public long fetchAdminCount(){
        Long l = queryFactory
                .select(
                        administrators.count()
                )
                .from(administrators)
                .fetchOne();

        return l == null ? 0L: l;
    }

    public long fetchAdminBySellerIdCount(long sellerId){
        Long l = queryFactory
                .select(
                        administrators.count()
                )
                .from(administrators)
                .innerJoin(seller)
                .on(seller.id.eq(administrators.sellerId), seller.id.eq(sellerId))
                .fetchOne();

        return l == null ? 0L: l;
    }

    public Optional<AdministratorResponse> fetchAdminValidation(AdminValidation adminValidation) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                new QAdministratorResponse(
                                        administrators.id,
                                        administrators.email,
                                        administrators.fullName,
                                        administrators.phoneNumber,
                                        administrators.sellerId,
                                        seller.sellerName,
                                        administrators.approvalStatus
                                )
                        )
                        .from(administrators)
                        .innerJoin(seller)
                        .on(seller.id.eq(administrators.sellerId))
                        .where(
                                administrators.email.eq(adminValidation.getEmail())
                        )
                        .fetchOne()
        );
    }

}
