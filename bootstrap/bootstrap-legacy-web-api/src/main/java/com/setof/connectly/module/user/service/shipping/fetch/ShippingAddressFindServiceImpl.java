package com.setof.connectly.module.user.service.shipping.fetch;

import com.setof.connectly.module.exception.user.ShippingAddressNotFoundException;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import com.setof.connectly.module.user.repository.shipping.redis.ShippingAddressFindRepository;
import com.setof.connectly.module.utils.SecurityUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ShippingAddressFindServiceImpl implements ShippingAddressFindService {

    private final ShippingAddressFindRepository shippingAddressFindRepository;

    @Override
    public List<UserShippingInfo> fetchShippingInfo() {
        return shippingAddressFindRepository.fetchShippingAddress(SecurityUtils.currentUserId());
    }

    @Override
    public UserShippingInfo fetchShippingInfo(long shippingAddressId) {
        return fetchShippingInfo().stream()
                .filter(s -> s.getShippingAddressId() == shippingAddressId)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public ShippingAddress fetchShippingAddressEntity(long shippingAddressId) {
        return shippingAddressFindRepository
                .fetchShippingAddressEntity(SecurityUtils.currentUserId(), shippingAddressId)
                .orElseThrow(ShippingAddressNotFoundException::new);
    }

    @Override
    public Optional<ShippingAddress> fetchDefaultShippingAddressEntity() {
        return shippingAddressFindRepository.fetchDefaultShippingAddressEntity(
                SecurityUtils.currentUserId());
    }
}
