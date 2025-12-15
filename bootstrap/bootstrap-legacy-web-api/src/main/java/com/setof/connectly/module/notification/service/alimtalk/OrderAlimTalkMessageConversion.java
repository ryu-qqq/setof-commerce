package com.setof.connectly.module.notification.service.alimtalk;

import com.setof.connectly.module.notification.core.MessageQueueContext;
import com.setof.connectly.module.notification.dto.order.ProductOrderSheet;
import com.setof.connectly.module.notification.enums.AlimTalkTemplateCode;
import com.setof.connectly.module.notification.enums.MessageStatus;
import com.setof.connectly.module.notification.enums.OrderAlimTalkTemplateGroup;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageMapper;
import com.setof.connectly.module.notification.mapper.AlimTalkMessageProvider;
import com.setof.connectly.module.order.enums.OrderStatus;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OrderAlimTalkMessageConversion extends AlimTalkMessageConversion<ProductOrderSheet> {

    public OrderAlimTalkMessageConversion(
            AlimTalkMessageProvider<? extends AlimTalkMessage, ProductOrderSheet>
                    alimTalkMessageProvider) {
        super(alimTalkMessageProvider);
    }

    @Override
    public List<MessageQueueContext> convert(ProductOrderSheet productOrderSheet) {
        List<AlimTalkTemplateCode> serviceByOrderStatus =
                getServiceByOrderStatus(productOrderSheet.getOrderStatus());
        return serviceByOrderStatus.stream()
                .map(
                        alimTalkTemplateCode -> {
                            AlimTalkMessageMapper<? extends AlimTalkMessage, ProductOrderSheet>
                                    mapperProvider = getMapperProvider(alimTalkTemplateCode);

                            AlimTalkMessage alimTalkMessage =
                                    mapperProvider.toAlimTalkMessage(productOrderSheet);
                            String parameter = toJson(alimTalkMessage);

                            return new MessageQueueContext(
                                    alimTalkTemplateCode, MessageStatus.PENDING, parameter);
                        })
                .collect(Collectors.toList());
    }

    private List<AlimTalkTemplateCode> getServiceByOrderStatus(OrderStatus orderStatus) {
        OrderAlimTalkTemplateGroup orderAlimTalkTemplateGroup =
                OrderAlimTalkTemplateGroup.of(orderStatus);
        if (orderAlimTalkTemplateGroup != null)
            return OrderAlimTalkTemplateGroupMapping.getCodesForGroup(orderAlimTalkTemplateGroup);
        else return Collections.emptyList();
    }
}
