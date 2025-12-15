package com.setof.connectly.module.order.repository.snapshot.query.group;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.group.OrderSnapShotProductGroup;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderSnapShotProductGroupJdbcRepositoryImpl
        implements OrderSnapShotProductGroupJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotProductGroup> orderSnapShotProductGroups) {
        String sql =
                "INSERT INTO ORDER_SNAPSHOT_PRODUCT_GROUP (ORDER_ID, PRODUCT_GROUP_ID,"
                    + " PRODUCT_GROUP_NAME, SELLER_ID, BRAND_ID, CATEGORY_ID, OPTION_TYPE,"
                    + " REGULAR_PRICE, CURRENT_PRICE, SALE_PRICE, DIRECT_DISCOUNT_RATE,"
                    + " DIRECT_DISCOUNT_PRICE, DISCOUNT_RATE, SOLD_OUT_YN, DISPLAY_YN,"
                    + " PRODUCT_CONDITION, ORIGIN, MANAGEMENT_TYPE, COMMISSION_RATE, SHARE_RATIO,"
                    + " DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE)"
                    + " VALUES (:orderId, :productGroupId, :productGroupName, :sellerId, :brandId,"
                    + " :categoryId, :optionType, :regularPrice, :currentPrice, :salePrice,"
                    + " :directDiscountRate, :directDiscountPrice, :discountRate, :soldOutYn,"
                    + " :displayYn, :productCondition, :origin, :managementType, :commissionRate,"
                    + " :shareRatio, :deleteYn, :insertOperator, :updateOperator, :insertDate,"
                    + " :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProductGroup op : orderSnapShotProductGroups) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("productGroupId", op.getSnapShotProductGroup().getProductGroupId())
                    .addValue(
                            "productGroupName", op.getSnapShotProductGroup().getProductGroupName())
                    .addValue("sellerId", op.getSnapShotProductGroup().getSellerId())
                    .addValue("brandId", op.getSnapShotProductGroup().getBrandId())
                    .addValue("categoryId", op.getSnapShotProductGroup().getCategoryId())
                    .addValue("optionType", op.getSnapShotProductGroup().getOptionType().name())
                    .addValue(
                            "regularPrice",
                            op.getSnapShotProductGroup().getPrice().getRegularPrice())
                    .addValue(
                            "currentPrice",
                            op.getSnapShotProductGroup().getPrice().getCurrentPrice())
                    .addValue("salePrice", op.getSnapShotProductGroup().getPrice().getSalePrice())
                    .addValue(
                            "directDiscountRate",
                            op.getSnapShotProductGroup().getPrice().getDirectDiscountRate())
                    .addValue(
                            "directDiscountPrice",
                            op.getSnapShotProductGroup().getPrice().getDirectDiscountPrice())
                    .addValue(
                            "discountRate",
                            op.getSnapShotProductGroup().getPrice().getDiscountRate())
                    .addValue(
                            "soldOutYn",
                            op.getSnapShotProductGroup().getProductStatus().getSoldOutYn().name())
                    .addValue(
                            "displayYn",
                            op.getSnapShotProductGroup().getProductStatus().getDisplayYn().name())
                    .addValue(
                            "productCondition",
                            op.getSnapShotProductGroup()
                                    .getClothesDetailInfo()
                                    .getProductCondition()
                                    .name())
                    .addValue(
                            "origin",
                            op.getSnapShotProductGroup().getClothesDetailInfo().getOrigin().name())
                    .addValue(
                            "managementType",
                            op.getSnapShotProductGroup().getManagementType().getName())
                    .addValue("commissionRate", op.getSnapShotProductGroup().getCommissionRate())
                    .addValue("shareRatio", op.getSnapShotProductGroup().getShareRatio())
                    .addValue("deleteYn", Yn.N.name())
                    .addValue(
                            "insertOperator",
                            MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                    .addValue(
                            "updateOperator",
                            MDC.get("user") == null ? "Anonymous" : MDC.get("user"))
                    .addValue("insertDate", LocalDateTime.now())
                    .addValue("updateDate", LocalDateTime.now());

            parameters.add(paramSource);
        }

        // batchUpdate 메소드를 사용하여 일괄 처리합니다.
        SqlParameterSource[] batch = parameters.toArray(new SqlParameterSource[0]);
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }
}
