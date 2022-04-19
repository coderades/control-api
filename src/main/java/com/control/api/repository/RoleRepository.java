package com.control.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.control.api.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

	Role findByRoleTitle(String roleTitle);

	List<?> findByRoleTitleIgnoreCaseContaining(String roleTitle);

	@Query("SELECT resource.resourceIsPublic, permission.permissionMod, resource.resourceUrl, role.roleTitle FROM Resource resource INNER JOIN Permission permission ON resource.resourceId = permission.resourceId INNER JOIN RolePermissions role_permissions ON permission.permissionId = role_permissions.permissionId INNER JOIN Role role ON role_permissions.roleId = role.roleId WHERE (resource.applicationId = ?1) AND (resource.resourceIsEnabled = 1) AND (role.roleIsEnabled = 1)")
	List<Object[]> findByHasAnyRole(String applicationId);

}
