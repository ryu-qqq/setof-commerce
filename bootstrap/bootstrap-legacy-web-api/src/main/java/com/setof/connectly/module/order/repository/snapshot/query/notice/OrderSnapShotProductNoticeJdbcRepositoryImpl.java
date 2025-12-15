package com.setof.connectly.module.order.repository.snapshot.query.notice;

import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.order.entity.snapshot.notice.OrderSnapShotProductNotice;
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
public class OrderSnapShotProductNoticeJdbcRepositoryImpl
        implements OrderSnapShotProductNoticeJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<OrderSnapShotProductNotice> orderSnapShotProductNotices) {
        String sql =
                "INSERT INTO ORDER_SNAPSHOT_PRODUCT_NOTICE (ORDER_ID, PRODUCT_GROUP_ID, MATERIAL,"
                    + " COLOR, SIZE, MAKER, ORIGIN, WASHING_METHOD, YEAR_MONTH_DAY,"
                    + " ASSURANCE_STANDARD, AS_PHONE, DELETE_YN, INSERT_OPERATOR, UPDATE_OPERATOR,"
                    + " INSERT_DATE, UPDATE_DATE) VALUES (:orderId, :productGroupId, :material,"
                    + " :color, :size, :maker, :origin, :washingMethod, :yearMonthDay,"
                    + " :assuranceStandard, :asPhone, :deleteYn, :insertOperator, :updateOperator,"
                    + " :insertDate, :updateDate)";

        List<SqlParameterSource> parameters = new ArrayList<>();

        for (OrderSnapShotProductNotice op : orderSnapShotProductNotices) {
            MapSqlParameterSource paramSource = new MapSqlParameterSource();
            paramSource
                    .addValue("orderId", op.getOrderId())
                    .addValue("productGroupId", op.getSnapShotNotice().getProductGroupId())
                    .addValue("material", op.getSnapShotNotice().getNoticeDetail().getMaterial())
                    .addValue("color", op.getSnapShotNotice().getNoticeDetail().getColor())
                    .addValue("size", op.getSnapShotNotice().getNoticeDetail().getSize())
                    .addValue("maker", op.getSnapShotNotice().getNoticeDetail().getMaker())
                    .addValue(
                            "origin",
                            op.getSnapShotNotice().getNoticeDetail().getOrigin().getName())
                    .addValue(
                            "washingMethod",
                            op.getSnapShotNotice().getNoticeDetail().getWashingMethod())
                    .addValue(
                            "yearMonthDay", op.getSnapShotNotice().getNoticeDetail().getYearMonth())
                    .addValue(
                            "assuranceStandard",
                            op.getSnapShotNotice().getNoticeDetail().getAssuranceStandard())
                    .addValue("asPhone", op.getSnapShotNotice().getNoticeDetail().getAsPhone())
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
