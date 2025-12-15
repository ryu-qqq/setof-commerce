package com.setof.connectly.module.common.filter;

import java.util.List;

public interface BrandFilter {

    Long getBrandId();

    List<Long> getBrandIds();

    void setBrandIds(List<Long> brandIds);
}
