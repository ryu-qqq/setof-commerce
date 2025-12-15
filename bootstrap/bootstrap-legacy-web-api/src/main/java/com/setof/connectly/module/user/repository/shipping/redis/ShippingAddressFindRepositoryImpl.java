package com.setof.connectly.module.user.repository.shipping.redis;

import static com.setof.connectly.module.user.entity.QShippingAddress.shippingAddress;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.user.dto.shipping.QUserShippingInfo;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShippingAddressFindRepositoryImpl implements ShippingAddressFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserShippingInfo> fetchShippingAddress(long userId) {
        return queryFactory
                .select(
                        new QUserShippingInfo(
                                shippingAddress.id,
                                shippingAddress.shippingDetails,
                                shippingAddress.defaultYn))
                .from(shippingAddress)
                .where(userIdEq(userId))
                .fetch();
    }

    @Override
    public Optional<ShippingAddress> fetchShippingAddressEntity(
            long userId, long shippingAddressId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(shippingAddress)
                        .where(userIdEq(userId), shippingAddressIdEq(shippingAddressId))
                        .fetchOne());
    }

    @Override
    public Optional<ShippingAddress> fetchDefaultShippingAddressEntity(long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(shippingAddress)
                        .where(userIdEq(userId), defaultYn())
                        .fetchOne());
    }

    private BooleanExpression userIdEq(long userId) {
        return shippingAddress.userId.eq(userId);
    }

    private BooleanExpression shippingAddressIdEq(long shippingAddressId) {
        return shippingAddress.id.eq(shippingAddressId);
    }

    private BooleanExpression defaultYn() {
        return shippingAddress.defaultYn.eq(Yn.Y);
    }
}
