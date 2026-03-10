package com.ryuqq.setof.application.productdescription.manager;

import com.ryuqq.setof.application.productdescription.port.out.command.DescriptionImageCommandPort;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DescriptionImageCommandManager - 상세설명 이미지 명령 Manager.
 *
 * <p>DescriptionImageCommandPort에 위임만 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@Transactional
public class DescriptionImageCommandManager {

    private final DescriptionImageCommandPort commandPort;

    public DescriptionImageCommandManager(DescriptionImageCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void persistAll(List<DescriptionImage> images, Long productGroupDescriptionId) {
        commandPort.persistAll(images, productGroupDescriptionId);
    }

    public void deleteByProductGroupDescriptionId(Long productGroupDescriptionId) {
        commandPort.deleteByProductGroupDescriptionId(productGroupDescriptionId);
    }
}
