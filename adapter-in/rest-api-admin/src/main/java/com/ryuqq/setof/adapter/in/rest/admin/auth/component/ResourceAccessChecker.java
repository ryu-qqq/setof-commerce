package com.ryuqq.setof.adapter.in.rest.admin.auth.component;

import com.ryuqq.setof.adapter.in.rest.admin.auth.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 리소스 접근 권한 검사기
 *
 * <p>@PreAuthorize 어노테이션에서 SpEL 표현식으로 사용됩니다. "access"라는 이름으로 Bean에 등록되어
 * {@code @PreAuthorize("@access.superAdmin()")} 형식으로 사용할 수 있습니다.
 *
 * <p>사용 예시:
 *
 * <pre>{@code
 * @PreAuthorize("@access.superAdmin()")
 * public void adminOnlyMethod() { ... }
 *
 * @PreAuthorize("@access.hasPermission('seller:read')")
 * public void sellerReadMethod() { ... }
 *
 * @PreAuthorize("@access.tenantAdminOrHigher()")
 * public void tenantMethod() { ... }
 * }</pre>
 *
 * <p>권한 계층:
 *
 * <ul>
 *   <li>SUPER_ADMIN: 모든 권한 (시스템 전체 관리)
 *   <li>TENANT_ADMIN: 테넌트 내 모든 권한
 *   <li>ORG_ADMIN: 조직 내 권한
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component("access")
public class ResourceAccessChecker {

    /**
     * SUPER_ADMIN 역할 확인
     *
     * @return SUPER_ADMIN 역할 보유 시 true
     */
    public boolean superAdmin() {
        return SecurityContextHolder.isSuperAdmin();
    }

    /**
     * TENANT_ADMIN 이상 역할 확인 (SUPER_ADMIN 또는 TENANT_ADMIN)
     *
     * @return TENANT_ADMIN 이상 역할 보유 시 true
     */
    public boolean tenantAdminOrHigher() {
        return SecurityContextHolder.isSuperAdmin() || SecurityContextHolder.isTenantAdmin();
    }

    /**
     * ORG_ADMIN 이상 역할 확인 (SUPER_ADMIN, TENANT_ADMIN, 또는 ORG_ADMIN)
     *
     * @return ORG_ADMIN 이상 역할 보유 시 true
     */
    public boolean orgAdminOrHigher() {
        return SecurityContextHolder.isSuperAdmin()
                || SecurityContextHolder.isTenantAdmin()
                || SecurityContextHolder.isOrgAdmin();
    }

    /**
     * 인증된 사용자 확인
     *
     * @return 인증된 사용자이면 true
     */
    public boolean authenticated() {
        return SecurityContextHolder.isAuthenticated();
    }

    /**
     * 특정 역할 보유 확인
     *
     * @param role 확인할 역할 (예: "ROLE_SUPER_ADMIN")
     * @return 역할 보유 시 true
     */
    public boolean hasRole(String role) {
        return SecurityContextHolder.hasRole(role);
    }

    /**
     * 특정 역할들 중 하나라도 보유 확인
     *
     * @param roles 확인할 역할들
     * @return 하나라도 보유 시 true
     */
    public boolean hasAnyRole(String... roles) {
        return SecurityContextHolder.hasAnyRole(roles);
    }

    /**
     * 특정 권한 보유 확인
     *
     * <p>권한 형식: {resource}:{action} (예: seller:read, product:write)
     *
     * @param permission 확인할 권한
     * @return 권한 보유 시 true
     */
    public boolean hasPermission(String permission) {
        return SecurityContextHolder.hasPermission(permission);
    }

    /**
     * 특정 권한들 중 하나라도 보유 확인
     *
     * @param permissions 확인할 권한들
     * @return 하나라도 보유 시 true
     */
    public boolean hasAnyPermission(String... permissions) {
        return SecurityContextHolder.hasAnyPermission(permissions);
    }

    /**
     * 특정 권한들 모두 보유 확인
     *
     * @param permissions 확인할 권한들
     * @return 모두 보유 시 true
     */
    public boolean hasAllPermissions(String... permissions) {
        return SecurityContextHolder.hasAllPermissions(permissions);
    }

    /**
     * 동일 테넌트 확인 (SUPER_ADMIN은 항상 true)
     *
     * <p>현재 사용자의 tenantId와 주어진 tenantId가 일치하는지 확인합니다.
     *
     * @param tenantId 확인할 테넌트 ID
     * @return 동일 테넌트이거나 SUPER_ADMIN이면 true
     */
    public boolean sameTenant(String tenantId) {
        if (SecurityContextHolder.isSuperAdmin()) {
            return true;
        }
        String currentTenantId = SecurityContextHolder.getCurrentTenantId();
        return currentTenantId != null && currentTenantId.equals(tenantId);
    }

    /**
     * 동일 조직 확인 (SUPER_ADMIN, TENANT_ADMIN은 테넌트만 확인)
     *
     * <p>현재 사용자의 조직 ID와 주어진 조직 ID가 일치하는지 확인합니다.
     *
     * @param tenantId 확인할 테넌트 ID
     * @param organizationId 확인할 조직 ID
     * @return 접근 가능하면 true
     */
    public boolean sameOrganization(String tenantId, String organizationId) {
        // SUPER_ADMIN은 모든 조직 접근 가능
        if (SecurityContextHolder.isSuperAdmin()) {
            return true;
        }

        // 테넌트 확인
        String currentTenantId = SecurityContextHolder.getCurrentTenantId();
        if (currentTenantId == null || !currentTenantId.equals(tenantId)) {
            return false;
        }

        // TENANT_ADMIN은 같은 테넌트 내 모든 조직 접근 가능
        if (SecurityContextHolder.isTenantAdmin()) {
            return true;
        }

        // ORG_ADMIN은 조직까지 일치해야 함
        String currentOrgId = SecurityContextHolder.getCurrentOrganizationId();
        return currentOrgId != null && currentOrgId.equals(organizationId);
    }

    /**
     * SUPER_ADMIN이거나 특정 권한 보유 확인
     *
     * @param permission 확인할 권한
     * @return SUPER_ADMIN이거나 권한 보유 시 true
     */
    public boolean superAdminOrPermission(String permission) {
        return SecurityContextHolder.isSuperAdmin()
                || SecurityContextHolder.hasPermission(permission);
    }
}
