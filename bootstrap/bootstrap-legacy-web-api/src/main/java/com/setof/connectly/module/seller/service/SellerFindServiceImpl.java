package com.setof.connectly.module.seller.service;

import com.setof.connectly.module.exception.seller.SellerNotFoundException;
import com.setof.connectly.module.seller.dto.SellerInfo;
import com.setof.connectly.module.seller.dto.SenderDto;
import com.setof.connectly.module.seller.repository.SellerFindRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class SellerFindServiceImpl implements SellerFindService {

    private final SellerRedisFindService sellerRedisFindService;
    private final SellerFindRepository sellerFindRepository;
    private final SellerRedisQueryService sellerRedisQueryService;

    @Override
    public SellerInfo fetchSeller(long sellerId) {
        Optional<SellerInfo> sellerInfo = sellerRedisFindService.fetchSellerInRedis(sellerId);
        return sellerInfo.orElseGet(() -> fetchSellerInDb(sellerId));
    }

    private SellerInfo fetchSellerInDb(long sellerId) {
        SellerInfo sellerInfo =
                sellerFindRepository
                        .fetchSeller(sellerId)
                        .orElseThrow(() -> new SellerNotFoundException(sellerId));
        sellerRedisQueryService.saveSellerInRedis(sellerInfo);
        return sellerInfo;
    }

    @Override
    public List<SenderDto> fetchSenders(List<Long> sellerIds) {
        return sellerFindRepository.fetchSenders(sellerIds);
    }
}
