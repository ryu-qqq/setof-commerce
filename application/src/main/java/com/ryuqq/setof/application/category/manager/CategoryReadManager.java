package com.ryuqq.setof.application.category.manager;

import com.ryuqq.setof.application.category.port.out.query.CategoryQueryPort;
import com.ryuqq.setof.domain.category.aggregate.Category;
import com.ryuqq.setof.domain.category.exception.CategoryNotFoundException;
import com.ryuqq.setof.domain.category.id.CategoryId;
import com.ryuqq.setof.domain.category.query.CategorySearchCriteria;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Category Read Manager.
 *
 * <p>카테고리 조회 관련 Manager입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class CategoryReadManager {

    private final CategoryQueryPort queryPort;

    public CategoryReadManager(CategoryQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Transactional(readOnly = true)
    public Category getById(CategoryId id) {
        return queryPort.findById(id).orElseThrow(() -> new CategoryNotFoundException(id.value()));
    }

    @Transactional(readOnly = true)
    public List<Category> getByIds(List<CategoryId> ids) {
        return queryPort.findByIds(ids);
    }

    @Transactional(readOnly = true)
    public boolean existsById(CategoryId id) {
        return queryPort.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Category> findByCriteria(CategorySearchCriteria criteria) {
        return queryPort.findByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(CategorySearchCriteria criteria) {
        return queryPort.countByCriteria(criteria);
    }

    @Transactional(readOnly = true)
    public List<Category> findAllDisplayed() {
        return queryPort.findAllDisplayed();
    }

    @Transactional(readOnly = true)
    public List<Category> findChildrenByParentId(CategoryId parentId) {
        return queryPort.findChildrenByParentId(parentId);
    }

    @Transactional(readOnly = true)
    public List<Category> findParentsByChildId(CategoryId childId) {
        return queryPort.findParentsByChildId(childId);
    }
}
