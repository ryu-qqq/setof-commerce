package com.setof.connectly.module.user.service.shipping.query;

import com.setof.connectly.module.exception.user.ExcessShippingAddressException;
import com.setof.connectly.module.user.dto.shipping.UserShippingInfo;
import com.setof.connectly.module.user.entity.ShippingAddress;
import com.setof.connectly.module.user.mapper.shipping.ShippingAddressMapper;
import com.setof.connectly.module.user.repository.shipping.ShippingAddressRepository;
import com.setof.connectly.module.user.service.shipping.fetch.ShippingAddressFindService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShippingAddressQueryServiceImpl implements ShippingAddressQueryService {

    private final ShippingAddressFindService shippingAddressFindService;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;

    @Override
    public UserShippingInfo toCreateUserShippingInfo(UserShippingInfo userShippingInfo) {
        List<UserShippingInfo> userShippingInfos = shippingAddressFindService.fetchShippingInfo();
        if (userShippingInfos.size() >= 5) throw new ExcessShippingAddressException();
        if (userShippingInfo.isDefaultShipping()) doseHasDefaultShippingInfo();
        ShippingAddress shippingAddress = shippingAddressMapper.toEntity(userShippingInfo);
        shippingAddressRepository.save(shippingAddress);
        return userShippingInfo;
    }

    @Override
    public UserShippingInfo updateUserShippingInfo(
            long shippingAddressId, UserShippingInfo userShippingInfo) {
        if (userShippingInfo.isDefaultShipping()) doseHasDefaultShippingInfo();
        ShippingAddress shippingAddress =
                shippingAddressFindService.fetchShippingAddressEntity(shippingAddressId);
        shippingAddress.update(userShippingInfo);
        return userShippingInfo;
    }

    @Override
    public ShippingAddress deleteUserShippingInfo(long shippingAddressId) {
        ShippingAddress shippingAddress =
                shippingAddressFindService.fetchShippingAddressEntity(shippingAddressId);
        shippingAddressRepository.delete(shippingAddress);
        return shippingAddress;
    }

    public void doseHasDefaultShippingInfo() {
        Optional<ShippingAddress> shippingAddress =
                shippingAddressFindService.fetchDefaultShippingAddressEntity();
        shippingAddress.ifPresent(ShippingAddress::deactivation);
    }
}
