package com.setof.connectly.module.cart.repository.fetch;

import static com.setof.connectly.module.brand.entity.QBrand.brand;
import static com.setof.connectly.module.cart.entity.QCart.cart;
import static com.setof.connectly.module.category.entity.QCategory.category;
import static com.setof.connectly.module.product.entity.group.QProduct.product;
import static com.setof.connectly.module.product.entity.group.QProductGroup.productGroup;
import static com.setof.connectly.module.product.entity.image.QProductGroupImage.productGroupImage;
import static com.setof.connectly.module.product.entity.option.QOptionDetail.optionDetail;
import static com.setof.connectly.module.product.entity.option.QOptionGroup.optionGroup;
import static com.setof.connectly.module.product.entity.option.QProductOption.productOption;
import static com.setof.connectly.module.product.entity.stock.QProductStock.productStock;
import static com.setof.connectly.module.seller.entity.QSeller.seller;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.cart.dto.CartFilter;
import com.setof.connectly.module.cart.dto.CartResponse;
import com.setof.connectly.module.cart.dto.QCartResponse;
import com.setof.connectly.module.cart.entity.Cart;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.product.dto.option.QOptionDto;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartFindRepositoryImpl implements CartFindRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Cart> fetchCartEntity(long cartId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(cart)
                        .where(cartIdEq(cartId), userIdEq(userId), deleteYn())
                        .fetchOne());
    }

    @Override
    public Optional<Cart> fetchExistingCartEntityByProductId(long productId, long userId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(cart)
                        .where(productIdEq(productId), userIdEq(userId), deleteYn())
                        .fetchOne());
    }

    @Override
    public List<Cart> fetchExistingCartEntityByProductIds(List<Long> productIds, long userId) {
        return queryFactory
                .selectFrom(cart)
                .where(productIdIn(productIds), userIdEq(userId), deleteYn())
                .fetch();
    }

    @Override
    public List<Cart> fetchCartEntities(List<Long> cartIdList, long userId) {
        return queryFactory
                .selectFrom(cart)
                .where(cartIdIn(cartIdList), userIdEq(userId), deleteYn())
                .fetch();
    }

    private List<Long> fetchCartIdListByUserId(long userId, CartFilter filter, Pageable pageable) {
        return queryFactory
                .select(cart.id)
                .from(cart)
                .where(userIdEq(userId), deleteYn(), isCursorRead(filter))
                .orderBy(cart.id.desc())
                .limit(pageable.getPageSize() + 1)
                .distinct()
                .fetch();
    }

    @Override
    public List<CartResponse> fetchCartList(long userId, CartFilter filter, Pageable pageable) {

        return queryFactory
                .from(cart)
                .innerJoin(productGroup)
                .on(productGroup.id.eq(cart.cartDetails.productGroupId))
                .innerJoin(product)
                .on(product.id.eq(cart.cartDetails.productId))
                .innerJoin(seller)
                .on(seller.id.eq(productGroup.productGroupDetails.sellerId))
                .innerJoin(productGroupImage)
                .on(productGroup.id.eq(productGroupImage.productGroup.id))
                .on(
                        productGroupImage.imageDetail.productGroupImageType.eq(
                                ProductGroupImageType.MAIN))
                .on(productGroupImage.deleteYn.eq(Yn.N))
                .innerJoin(productStock)
                .on(productStock.product.id.eq(cart.cartDetails.productId))
                .innerJoin(brand)
                .on(brand.id.eq(productGroup.productGroupDetails.brandId))
                .innerJoin(category)
                .on(category.id.eq(productGroup.productGroupDetails.categoryId))
                .leftJoin(productOption)
                .on(productOption.product.id.eq(cart.cartDetails.productId))
                .leftJoin(optionGroup)
                .on(optionGroup.id.eq(productOption.optionGroup.id))
                .leftJoin(optionDetail)
                .on(optionDetail.id.eq(productOption.optionDetail.id))
                .where(userIdEq(userId), deleteYn(), isCursorRead(filter))
                .distinct()
                .limit(pageable.getPageSize() + 1)
                .orderBy(cart.id.desc())
                .transform(
                        GroupBy.groupBy(cart.id)
                                .list(
                                        new QCartResponse(
                                                productGroup.productGroupDetails.brandId,
                                                brand.brandName,
                                                productGroup.productGroupDetails.productGroupName,
                                                seller.id,
                                                seller.sellerName,
                                                productStock.product.id,
                                                productGroup.productGroupDetails.price,
                                                cart.cartDetails.quantity,
                                                productStock.stockQuantity,
                                                GroupBy.set(
                                                        new QOptionDto(
                                                                optionGroup.id,
                                                                optionDetail.id,
                                                                optionGroup.optionName,
                                                                optionDetail.optionValue.coalesce(
                                                                        ""))),
                                                cart.id,
                                                productGroupImage.imageDetail.imageUrl,
                                                productGroup.id,
                                                product.productStatus,
                                                category.path)));
    }

    @Override
    public JPAQuery<Long> fetchCartCountQuery(long userId) {
        return queryFactory
                .select(cart.count())
                .from(cart)
                .where(userIdEq(userId), deleteYn())
                .distinct();
    }

    protected BooleanExpression cartIdIn(List<Long> cartIdList) {
        return cart.id.in(cartIdList);
    }

    protected BooleanExpression cartIdEq(long cartId) {
        return cart.id.eq(cartId);
    }

    protected BooleanExpression userIdEq(long userId) {
        return cart.userId.eq(userId);
    }

    protected BooleanExpression productIdEq(long productId) {
        return cart.cartDetails.productId.eq(productId);
    }

    protected BooleanExpression productIdIn(List<Long> productIds) {
        return cart.cartDetails.productId.in(productIds);
    }

    private BooleanExpression cartIdLt(Long cartId) {
        return cartId != null ? cart.id.lt(cartId) : null;
    }

    private BooleanExpression deleteYn() {
        return cart.deleteYn.eq(Yn.N);
    }

    private BooleanExpression isCursorRead(CartFilter filter) {
        Long lastDomainId = filter.getLastDomainId();
        if (lastDomainId != null) return cartIdLt(filter.getLastDomainId());
        return null;
    }
}
