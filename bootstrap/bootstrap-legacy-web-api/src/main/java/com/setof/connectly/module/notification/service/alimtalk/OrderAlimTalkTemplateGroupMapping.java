package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.enums.OrderAlimTalkTemplateGroup;
import java.util.*;

public class OrderAlimTalkTemplateGroupMapping {

    private static final Map<AlimTalkTemplateCode, OrderAlimTalkTemplateGroup> statusGroupMap =
            new HashMap<>();
    private static final Map<OrderAlimTalkTemplateGroup, List<AlimTalkTemplateCode>>
            groupStatusMap = new EnumMap<>(OrderAlimTalkTemplateGroup.class);

    static {
        statusGroupMap.put(
                AlimTalkTemplateCode.ORDER_ACCEPT, OrderAlimTalkTemplateGroup.ORDER_TRIGGER);
        statusGroupMap.put(
                AlimTalkTemplateCode.ORDER_COMPLETE, OrderAlimTalkTemplateGroup.ORDER_TRIGGER);

        statusGroupMap.put(
                AlimTalkTemplateCode.CANCEL_ORDER_S,
                OrderAlimTalkTemplateGroup.ORDER_CANCEL_TRIGGER);
        statusGroupMap.put(
                AlimTalkTemplateCode.CANCEL_REQUEST,
                OrderAlimTalkTemplateGroup.ORDER_CANCEL_TRIGGER);

        statusGroupMap.put(
                AlimTalkTemplateCode.RETURN_REQUEST,
                OrderAlimTalkTemplateGroup.ORDER_RETURN_TRIGGER);
        statusGroupMap.put(
                AlimTalkTemplateCode.RETURN_REQUEST_S,
                OrderAlimTalkTemplateGroup.ORDER_RETURN_TRIGGER);

        statusGroupMap.put(
                AlimTalkTemplateCode.DELIVERY_START, OrderAlimTalkTemplateGroup.ORDER_DELIVERY);
        statusGroupMap.put(
                AlimTalkTemplateCode.CANCEL_COMPLETE, OrderAlimTalkTemplateGroup.ORDER_CANCELED);
        statusGroupMap.put(
                AlimTalkTemplateCode.RETURN_ACCEPT, OrderAlimTalkTemplateGroup.ORDER_RETURN_ACCEPT);
        statusGroupMap.put(
                AlimTalkTemplateCode.RETURN_REJECT, OrderAlimTalkTemplateGroup.ORDER_RETURN_REJECT);
        statusGroupMap.put(
                AlimTalkTemplateCode.CANCEL_SALE, OrderAlimTalkTemplateGroup.ORDER_SALE_CANCELED);

        statusGroupMap.forEach(
                (code, group) -> {
                    groupStatusMap.computeIfAbsent(group, k -> new ArrayList<>()).add(code);
                });
    }

    public static List<AlimTalkTemplateCode> getCodesForGroup(OrderAlimTalkTemplateGroup group) {
        return groupStatusMap.getOrDefault(group, Collections.emptyList());
    }
}
