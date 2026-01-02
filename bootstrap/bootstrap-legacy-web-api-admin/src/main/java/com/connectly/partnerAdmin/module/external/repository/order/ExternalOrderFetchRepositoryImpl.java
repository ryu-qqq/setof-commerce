package com.connectly.partnerAdmin.module.external.repository.order;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.connectly.partnerAdmin.module.external.dto.order.ExternalOrderMappingDto;
import com.connectly.partnerAdmin.module.external.dto.order.QExternalOrderMappingDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.connectly.partnerAdmin.module.external.entity.QExternalOrder.externalOrder;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class ExternalOrderFetchRepositoryImpl implements ExternalOrderFetchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExternalOrderMappingDto> doesHasInterlockingOrders(List<Long> orderIds){
        return
                queryFactory.select(
                                new QExternalOrderMappingDto(
                                        externalOrder.orderId,
                                        externalOrder.siteId,
                                        externalOrder.externalIdx,
                                        externalOrder.externalOrderPkId
                                )
                        )
                        .from(externalOrder)
                        .where(orderIdIn(orderIds))
                        .fetch();
    }

    @Override
    public Optional<ExternalOrderMappingDto> fetchByOrderId(long orderId) {
        return Optional.ofNullable(
            queryFactory.select(
                    new QExternalOrderMappingDto(
                        externalOrder.orderId,
                        externalOrder.siteId,
                        externalOrder.externalIdx,
                        externalOrder.externalOrderPkId
                    )
                )
                .from(externalOrder)
                .where(externalOrder.orderId.eq(orderId))
                .fetchOne()
        );
    }

    @Override
    public boolean doesHasSyncOrder(long siteId, long externalIdx){

        Long orderIdOpt = queryFactory.select(
                        externalOrder.orderId
                )
                .from(externalOrder)
                .where(externalOrderIdEq(externalIdx), siteIdEq(siteId))
                .fetchOne();

        return orderIdOpt != null;
    }

    @Override
    public Optional<Long> fetchOrderId(long siteId, long externalIdx) {
        return Optional.ofNullable(
                queryFactory.select(
                                externalOrder.orderId
                        )
                        .from(externalOrder)
                        .where(externalOrderIdEq(externalIdx), siteIdEq(siteId))
                        .fetchOne()
        );
    }


    private BooleanExpression orderIdIn(List<Long> orderIds){
        return externalOrder.orderId.in(orderIds);
    }


    private BooleanExpression externalOrderIdEq(long externalOrderId){
        return externalOrder.externalIdx.eq(externalOrderId);
    }

    private BooleanExpression siteIdEq(long siteId){
        return externalOrder.siteId.eq(siteId);
    }

}

