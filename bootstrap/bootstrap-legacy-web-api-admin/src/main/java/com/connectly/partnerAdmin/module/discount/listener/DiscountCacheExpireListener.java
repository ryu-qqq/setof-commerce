package com.connectly.partnerAdmin.module.discount.listener;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.discount.dto.query.UpdateUseDiscount;
import com.connectly.partnerAdmin.module.discount.service.DiscountQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Slf4j
@Component
@RequiredArgsConstructor
public class DiscountCacheExpireListener implements MessageListener {

    private final DiscountQueryService discountQueryService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);
        try{
            long discountPolicyId = extractDiscountPolicyIdFromKey(expiredKey);

            UpdateUseDiscount updateUseDiscount = UpdateUseDiscount.builder()
                    .discountPolicyIds(Collections.singletonList(discountPolicyId))
                    .activeYn(Yn.N)
                    .build();

            discountQueryService.updateDiscountUseYn(updateUseDiscount);
        }catch (Exception e){
            log.error(e.getMessage());
            log.error(expiredKey);
        }

    }

    private long extractDiscountPolicyIdFromKey(String key) {
        String[] parts = key.split(":");
        return Long.parseLong(parts[2]);
    }
}
