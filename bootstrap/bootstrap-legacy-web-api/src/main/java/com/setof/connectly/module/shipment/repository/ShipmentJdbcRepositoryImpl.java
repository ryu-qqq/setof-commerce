package com.setof.connectly.module.shipment.repository;

import com.setof.connectly.module.shipment.entity.Shipment;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShipmentJdbcRepositoryImpl implements ShipmentJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(List<Shipment> shipments) {
        String sql =
                "INSERT INTO shipment (order_id, payment_snapshot_shipping_address_id, sender_name,"
                        + " SENDER_EMAIL, SENDER_PHONE_NUMBER, DELIVERY_status, COMPANY_CODE,"
                        + " insert_operator, update_operator) VALUES (:orderId,"
                        + " :paymentSnapShotShippingAddressId, :senderName, :senderEmail,"
                        + " :senderPhoneNumner, :deliveryStatus, :companyCode, :insertOperator,"
                        + " :updateOperator)";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(shipments.size());
        for (Shipment shipment : shipments) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("orderId", shipment.getOrderId());
            parameterSource.addValue(
                    "paymentSnapShotShippingAddressId",
                    shipment.getPaymentSnapShotShippingAddressId());
            parameterSource.addValue("senderName", shipment.getSenderName());
            parameterSource.addValue("senderEmail", shipment.getSenderEmail());
            parameterSource.addValue("senderPhoneNumner", shipment.getSenderPhoneNumber());
            parameterSource.addValue("deliveryStatus", shipment.getDeliveryStatus().name());
            parameterSource.addValue("companyCode", shipment.getCompanyCode().name());
            parameterSource.addValue(
                    "insertOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));

            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[shipments.size()]));
    }
}
