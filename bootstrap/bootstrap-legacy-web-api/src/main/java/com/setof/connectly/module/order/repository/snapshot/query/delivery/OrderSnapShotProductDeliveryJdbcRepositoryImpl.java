package com.setof.connectly.module.order.repository.snapshot.query.delivery;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.delivery.OrderSnapShotProductDelivery;
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
public class OrderSnapShotProductDeliveryJdbcRepositoryImpl
        implements OrderSnapShotProductDeliveryJdbcRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotProductDelivery> orderSnapShotProductDeliveries) {
        String sql =
                "INSERT INTO order_snapshot_product_delivery (order_id, product_group_id,"
                    + " DELIVERY_AREA, DELIVERY_FEE, DELIVERY_PERIOD_AVERAGE,"
                    + " RETURN_METHOD_DOMESTIC, RETURN_COURIER_DOMESTIC, RETURN_CHARGE_DOMESTIC,"
                    + " RETURN_EXCHANGE_AREA_DOMESTIC, delete_yn, insert_operator, update_operator,"
                    + " insert_date, update_date) VALUES (:orderId, :productGroupId, :deliveryArea,"
                    + " :deliveryFee, :deliveryPeriodAverage, :returnMethodDomestic,"
                    + " :returnCourierDomestic, :returnChargeDomestic, :returnExchangeAreaDomestic,"
                    + " :deleteYn, :insertOperator, :updateOperator, :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProductDelivery op : orderSnapShotProductDeliveries) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("productGroupId", op.getSnapShotProductDelivery().getProductGroupId())
                    .addValue(
                            "deliveryArea",
                            op.getSnapShotProductDelivery().getDeliveryNotice().getDeliveryArea())
                    .addValue(
                            "deliveryFee",
                            op.getSnapShotProductDelivery().getDeliveryNotice().getDeliveryFee())
                    .addValue(
                            "deliveryPeriodAverage",
                            op.getSnapShotProductDelivery()
                                    .getDeliveryNotice()
                                    .getDeliveryPeriodAverage())
                    .addValue(
                            "returnMethodDomestic",
                            op.getSnapShotProductDelivery()
                                    .getRefundNotice()
                                    .getReturnMethodDomestic()
                                    .getName())
                    .addValue(
                            "returnCourierDomestic",
                            op.getSnapShotProductDelivery()
                                    .getRefundNotice()
                                    .getReturnCourierDomestic()
                                    .getName())
                    .addValue(
                            "returnChargeDomestic",
                            op.getSnapShotProductDelivery()
                                    .getRefundNotice()
                                    .getReturnChargeDomestic())
                    .addValue(
                            "returnExchangeAreaDomestic",
                            op.getSnapShotProductDelivery()
                                    .getRefundNotice()
                                    .getReturnExchangeAreaDomestic())
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

        SqlParameterSource[] batch = parameters.toArray(new SqlParameterSource[parameters.size()]);
        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }
}
