package com.setof.connectly.module.order.repository.snapshot.fetch;

import static com.setof.connectly.module.order.entity.order.QOrder.order;
import static com.setof.connectly.module.product.entity.delivery.QProductDelivery.productDelivery;
import static com.setof.connectly.module.product.entity.group.QProduct.product;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupDetailDescription.productGroupDetailDescription;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.product.entity.notice.QProductNotice.productNotice;
import static com.setof.connectly.module.product.entity.option.QOptionDetail.optionDetail;
import static com.setof.connectly.module.product.entity.option.QOptionGroup.optionGroup;
import static com.setof.connectly.module.product.entity.option.QProductOption.productOption;
import static com.setof.connectly.module.product.entity.stock.QProductStock.productStock;
import static com.setof.connectly.module.seller.entity.QSeller.seller;
import static com.setof.connectly.module.user.entity.QUsers.users;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import com.setof.connectly.module.order.dto.snapshot.QOrderProductSnapShotQueryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSnapShotFindRepositoryImpl implements OrderSnapShotFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderProductSnapShotQueryDto> fetchSnapShotAboutProduct(long paymentId) {
        return queryFactory
                .from(order)
                .innerJoin(users)
                .on(order.userId.eq(users.id))
                .innerJoin(seller)
                .on(seller.id.eq(order.sellerId))
                .innerJoin(product)
                .on(product.id.eq(order.productId))
                .on(product.deleteYn.eq(Yn.N))
                .innerJoin(productGroup)
                .on(product.productGroup.id.eq(productGroup.id))
                .innerJoin(productDelivery)
                .on(productDelivery.productGroup.id.eq(productGroup.id))
                .innerJoin(productNotice)
                .on(productNotice.productGroup.id.eq(productGroup.id))
                .innerJoin(productGroupImage)
                .on(productGroupImage.productGroup.id.eq(productGroup.id))
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(productGroupDetailDescription)
                .on(productGroupDetailDescription.id.eq(productGroup.id))
                .innerJoin(productStock)
                .on(productStock.id.eq(product.id))
                .on(productStock.deleteYn.eq(Yn.N))
                .leftJoin(productOption)
                .on(productOption.product.id.eq(product.id))
                .on(productOption.deleteYn.eq(Yn.N))
                .leftJoin(optionGroup)
                .on(optionGroup.id.eq(productOption.optionGroup.id))
                .leftJoin(optionDetail)
                .on(optionDetail.id.eq(productOption.optionDetail.id))
                .on(optionDetail.deleteYn.eq(Yn.N))
                .where(paymentIdEq(paymentId))
                .transform(
                        GroupBy.groupBy(order.id)
                                .list(
                                        new QOrderProductSnapShotQueryDto(
                                                users.id,
                                                users.name,
                                                order.id,
                                                order.orderAmount,
                                                order.quantity,
                                                order.productId,
                                                productStock.id,
                                                productGroup.id,
                                                seller.commissionRate,
                                                productGroup.productGroupDetails,
                                                productDelivery.deliveryNotice,
                                                productDelivery.refundNotice,
                                                productNotice.noticeDetail,
                                                GroupBy.set(productGroupImage.imageDetail),
                                                productGroupDetailDescription.imageDetail,
                                                GroupBy.set(optionGroup),
                                                GroupBy.set(optionDetail),
                                                GroupBy.set(productOption))));
    }

    private BooleanExpression paymentIdEq(long paymentId) {
        return order.paymentId.eq(paymentId);
    }
}
