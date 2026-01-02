package com.connectly.partnerAdmin.module.category.service;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.connectly.partnerAdmin.module.category.core.ExternalCategoryContext;
import com.connectly.partnerAdmin.module.category.core.ProductCategoryContext;
import com.connectly.partnerAdmin.module.category.core.TreeCategoryContext;
import com.connectly.partnerAdmin.module.category.filter.CategoryFilter;
import com.connectly.partnerAdmin.module.category.mapper.CategoryMapper;
import com.connectly.partnerAdmin.module.category.mapper.CategoryPageableMapper;
import com.connectly.partnerAdmin.module.category.repository.CategoryFetchRepository;
import com.connectly.partnerAdmin.module.common.dto.CustomPageable;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryFetchServiceImpl implements CategoryFetchService {

    private final CategoryFetchRepository categoryFetchRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryPageableMapper categoryPageableMapper;

    @Override
    public boolean hasCategoryIdExist(long categoryId){
        return categoryFetchRepository.hasCategoryIdExist(categoryId);
    }

    @Override
    public Optional<TreeCategoryContext> fetchById(long categoryId) {
        return categoryFetchRepository.fetchById(categoryId);
    }

    @Override
    public Set<Long> fetchCategoryChildrenIds(long categoryId){
        return categoryFetchRepository.fetchCategoryChildrenIds(categoryId);
    }
    @Override
    public List<TreeCategoryContext> fetchAllChildCategories(long categoryId) {
        return categoryFetchRepository.fetchAllChildCategories(categoryId);
    }

    @Override
    public List<TreeCategoryContext> fetchAllParentCategories(long categoryId) {
        return categoryFetchRepository.fetchAllParentCategories(categoryId);
    }


    @Override
    public List<TreeCategoryContext> fetchAllCategories() {
        List<TreeCategoryContext> categories = categoryFetchRepository.fetchAllCategories(new HashSet<>());
        return categoryMapper.constructTree(categories);
    }

    @Override
    public List<TreeCategoryContext> fetchAllCategories(Set<Long> categoryIds) {
        return categoryFetchRepository.fetchAllCategories(categoryIds);
    }


    @Override
    public CustomPageable<ProductCategoryContext> fetchCategories(CategoryFilter filter, Pageable pageable) {
        List<ProductCategoryContext> categoryContexts;
        if (filter.isNoOffsetFetch()) {
            categoryContexts = fetchProductCategoryContextsWithNoOffset(filter, pageable);
        }else {
            categoryContexts = fetchProductCategoryContexts(filter, pageable);
        }

        long total = fetchCategoryCountQuery(filter);

        return categoryPageableMapper.toProductCategoryContext(categoryContexts, pageable, total);
    }

    @Override
    public List<ExternalCategoryContext> fetchExternalCategories(long siteId, List<String> mappingCategoryIds) {
        return categoryFetchRepository.fetchExternalCategories(siteId, mappingCategoryIds);
    }

    @Override
    public List<ExternalCategoryContext> fetchExternalCategoriesByOurCategoryIds(long siteId, Set<Long> categoryIds) {
        return categoryFetchRepository.fetchExternalCategoriesByOurCategoryIds(siteId, categoryIds);
    }


    private List<ProductCategoryContext> fetchProductCategoryContexts(CategoryFilter filter, Pageable pageable) {
        return categoryFetchRepository.fetchProductCategoryContexts(filter, pageable);
    }


    private List<ProductCategoryContext> fetchProductCategoryContextsWithNoOffset(CategoryFilter filter, Pageable pageable) {
        return categoryFetchRepository.fetchProductCategoryContextsWithNoOffset(filter, pageable);
    }




    private long fetchCategoryCountQuery(CategoryFilter filter){
        Long total = categoryFetchRepository.fetchCategoryCountQuery(filter).fetchOne();
        if (total == null) return 0L;
        else return total;
    }

}
