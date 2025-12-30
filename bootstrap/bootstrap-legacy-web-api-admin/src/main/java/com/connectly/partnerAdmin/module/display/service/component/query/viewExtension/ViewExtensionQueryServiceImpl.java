package com.connectly.partnerAdmin.module.display.service.component.query.viewExtension;


import com.connectly.partnerAdmin.module.display.entity.component.ViewExtension;
import com.connectly.partnerAdmin.module.display.entity.embedded.ViewExtensionDetails;
import com.connectly.partnerAdmin.module.display.mapper.viewExtension.ViewExtensionMapper;
import com.connectly.partnerAdmin.module.display.repository.component.viewExtension.ViewExtensionJdbcRepository;
import com.connectly.partnerAdmin.module.display.repository.component.viewExtension.ViewExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class ViewExtensionQueryServiceImpl implements ViewExtensionQueryService{

    private final ViewExtensionRepository viewExtensionRepository;
    private final ViewExtensionJdbcRepository viewExtensionJdbcRepository;
    private final ViewExtensionMapper viewExtensionMapper;

    public ViewExtension saveViewExtension(ViewExtensionDetails viewExtensionDetails){
        ViewExtension viewExtension = viewExtensionMapper.toEntity(viewExtensionDetails);
        return viewExtensionRepository.save(viewExtension);
    }

    @Override
    public void deleteAll(List<Long> viewExtensionIds) {
        viewExtensionJdbcRepository.deleteAll(viewExtensionIds);
    }

    @Override
    public void updateAll(List<ViewExtension> viewExtensions) {
        viewExtensionJdbcRepository.updateAll(viewExtensions);
    }
}
