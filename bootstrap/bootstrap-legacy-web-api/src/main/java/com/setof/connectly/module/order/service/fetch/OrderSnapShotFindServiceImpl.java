package com.setof.connectly.module.order.service.fetch;

import com.setof.connectly.module.order.dto.snapshot.OrderProductSnapShotQueryDto;
import com.setof.connectly.module.order.repository.snapshot.fetch.OrderSnapShotFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderSnapShotFindServiceImpl implements OrderSnapShotFindService {

    private final OrderSnapShotFindRepository orderSnapShotFindRepository;

    public List<OrderProductSnapShotQueryDto> fetchOrderProductForSnapShot(long paymentId) {
        return orderSnapShotFindRepository.fetchSnapShotAboutProduct(paymentId);
    }
}
