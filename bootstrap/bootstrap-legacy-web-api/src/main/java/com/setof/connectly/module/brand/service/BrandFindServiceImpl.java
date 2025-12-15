package com.setof.connectly.module.brand.service;

import com.setof.connectly.module.brand.dto.BrandDisplayDto;
import com.setof.connectly.module.brand.dto.BrandFilter;
import com.setof.connectly.module.brand.repository.BrandFindRepository;
import com.setof.connectly.module.exception.brand.BrandNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class BrandFindServiceImpl implements BrandFindService {

    private final BrandFindRepository brandFindRepository;

    public BrandFindServiceImpl(BrandFindRepository brandFindRepository) {
        this.brandFindRepository = brandFindRepository;
    }

    @Override
    public BrandDisplayDto fetchBrand(long brandId) {
        return brandFindRepository
                .fetchBrand(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }

    @Override
    public List<BrandDisplayDto> fetchBrands(BrandFilter filter) {
        if (StringUtils.hasText(filter.getSearchWord())) {
            return brandFindRepository.fetchBrands(filter);
        }
        return brandFindRepository.fetchBrands();
    }
}
