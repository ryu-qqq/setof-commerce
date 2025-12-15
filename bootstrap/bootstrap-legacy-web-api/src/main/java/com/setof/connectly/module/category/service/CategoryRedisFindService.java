package com.setof.connectly.module.category.service;

import com.setof.connectly.module.category.dto.CategoryDisplayDto;
import java.util.List;

public interface CategoryRedisFindService {

    List<CategoryDisplayDto> fetchAllCategoriesAsTree();
}
