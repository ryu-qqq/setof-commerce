package com.setof.connectly.module.notification.repository;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageQueueJdbcRepositoryImpl implements MessageQueueJdbcRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void saveAll(Set<MessageQueueContext> messageQueues) {
        String sql =
                "INSERT INTO MESSAGE_QUEUE (TEMPLATE_CODE, PARAMETERS, STATUS, INSERT_OPERATOR,"
                        + " UPDATE_OPERATOR) VALUES (:templateCode, :parameters, :status,"
                        + " :insertOperator, :updateOperator)";

        List<MapSqlParameterSource> batchValues = new ArrayList<>(messageQueues.size());
        for (MessageQueueContext messageQueue : messageQueues) {
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("templateCode", messageQueue.getAlimTalkTemplateCode().name());
            parameterSource.addValue("parameters", messageQueue.getParameters());
            parameterSource.addValue("status", messageQueue.getMessageStatus().name());
            parameterSource.addValue(
                    "insertOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            parameterSource.addValue(
                    "updateOperator", MDC.get("user") == null ? "Anonymous" : MDC.get("user"));
            batchValues.add(parameterSource);
        }

        namedParameterJdbcTemplate.batchUpdate(
                sql, batchValues.toArray(new MapSqlParameterSource[messageQueues.size()]));
    }
}
