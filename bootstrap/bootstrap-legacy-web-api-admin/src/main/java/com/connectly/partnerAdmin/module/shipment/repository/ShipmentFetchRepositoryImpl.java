package com.connectly.partnerAdmin.module.shipment.repository;


import com.connectly.partnerAdmin.module.shipment.dto.QShipmentCodeDto;
import com.connectly.partnerAdmin.module.shipment.dto.ShipmentCodeDto;
import com.connectly.partnerAdmin.module.shipment.entity.Shipment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.connectly.partnerAdmin.module.common.entity.QCommonCode.commonCode;
import static com.connectly.partnerAdmin.module.shipment.entity.QShipment.shipment;


@Repository
@RequiredArgsConstructor
public class ShipmentFetchRepositoryImpl implements ShipmentFetchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Shipment> fetchShipmentEntity(long orderId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(shipment)
                        .where(orderIdEq(orderId))
                        .fetchOne()
        );
    }

    @Override
    public List<ShipmentCodeDto> fetchShipmentCode() {
        return queryFactory
                .select(
                        new QShipmentCodeDto(
                                commonCode.codeDetailDisplayName,
                                commonCode.codeDetail
                        )
                )
                .from(commonCode)
                .where(commonCode.codeGroupId.eq(2L))
                .fetch();
    }

    private BooleanExpression orderIdEq(long orderId){
        return shipment.order.id.eq(orderId);
    }

}
