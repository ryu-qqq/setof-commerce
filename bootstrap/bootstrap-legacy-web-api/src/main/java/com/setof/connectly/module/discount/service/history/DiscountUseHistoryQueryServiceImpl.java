package com.setof.connectly.module.discount.service.history;

import com.setof.connectly.module.common.enums.RedisKey;
import com.setof.connectly.module.discount.dto.DiscountCacheDto;
import com.setof.connectly.module.discount.dto.DiscountUseDto;
import com.setof.connectly.module.discount.entity.history.DiscountUseHistory;
import com.setof.connectly.module.discount.enums.IssueType;
import com.setof.connectly.module.discount.mapper.DiscountMapper;
import com.setof.connectly.module.discount.repository.history.DiscountUseHistoryJdbcRepository;
import com.setof.connectly.module.discount.service.fetch.DiscountFindService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DiscountUseHistoryQueryServiceImpl implements DiscountUseHistoryQueryService {

    private final DiscountFindService discountFindService;
    private final DiscountUseHistoryJdbcRepository discountUseHistoryJdbcRepository;
    private final DiscountMapper discountMapper;

    @Override
    public void saveDiscountUseHistories(long paymentId, List<DiscountUseDto> discountUses) {

        List<DiscountCacheDto> discountInfoInDb =
                discountFindService.fetchDiscountCaches(
                        discountUses.stream()
                                .map(DiscountUseDto::getProductGroupId)
                                .collect(Collectors.toList()),
                        discountUses.stream()
                                .map(DiscountUseDto::getSellerId)
                                .collect(Collectors.toList()));

        Map<String, DiscountCacheDto> keyMap = generateKeyMap(discountInfoInDb);

        if (!discountInfoInDb.isEmpty()) {

            List<DiscountUseHistory> discountUseHistories = new ArrayList<>();

            discountUses.forEach(
                    discountUse -> {
                        String productKey =
                                generateDiscountKey(
                                        IssueType.PRODUCT, discountUse.getProductGroupId());
                        String sellerKey =
                                generateDiscountKey(IssueType.SELLER, discountUse.getSellerId());

                        DiscountCacheDto discount =
                                Optional.ofNullable(keyMap.get(productKey))
                                        .orElse(keyMap.get(sellerKey));

                        if (discount != null) {
                            DiscountUseHistory discountUseHistory =
                                    discountMapper.toDiscountUseHistory(
                                            discount.getDiscountPolicyId(), discountUse);
                            discountUseHistories.add(discountUseHistory);
                        }
                    });

            if (!discountUseHistories.isEmpty())
                discountUseHistoryJdbcRepository.saveAll(discountUseHistories);
        }
    }

    private Map<String, DiscountCacheDto> generateKeyMap(List<DiscountCacheDto> discounts) {
        return discounts.stream()
                .collect(
                        Collectors.toMap(
                                DiscountCacheDto::makeCacheKey,
                                Function.identity(),
                                (existing, replacement) -> existing));
    }

    private String generateDiscountKey(IssueType issueType, long targetId) {
        return RedisKey.DISCOUNT.generateKey(issueType.name() + targetId);
    }
}
