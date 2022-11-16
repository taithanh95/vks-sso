package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.GroupRoleMap;
import com.bitsco.vks.sso.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 26-Dec-18 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
@RepositoryRestResource
public interface GroupRoleMapRepository extends JpaRepository<GroupRoleMap, Long> {
    GroupRoleMap findFirstByGroupRoleIdAndRoleId(long groupRoleId, long roleId);

    GroupRoleMap findFirstByGroupRoleIdAndPriorityAndStatus(long groupRoleId, int priority, int status);

    Boolean existsByGroupRoleIdAndRoleIdAndStatus(long groupRoleId, long roleId, int status);

    @Query(value = "SELECT new Role (a.roleId, a.priority) FROM GroupRoleMap a WHERE 1 = 1 AND a.groupRoleId = :groupRoleId AND a.roleId = :roleId AND a.status = :status")
    Role findByGroupRoleIdAndRoleIdAndStatus(long groupRoleId, long roleId, int status);

    @Query(value = "SELECT MAX(a.priority) FROM GroupRoleMap a WHERE 1 = 1 AND (:groupRoleId IS NULL OR a.groupRoleId = :groupRoleId)")
    Integer getMaxPriority(Long groupRoleId);

    @Query(value = "SELECT a FROM GroupRoleMap a WHERE 1 = 1 "
            + "AND(:groupRoleId IS NULL OR a.groupRoleId = :groupRoleId)"
            + "AND(:roleId IS NULL OR a.roleId = :roleId)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
    )
    List<GroupRoleMap> getPage(
            @Param("groupRoleId") Long groupRoleId,
            @Param("roleId") Long roleId,
            @Param("status") Integer status);

    List<GroupRoleMap> findByGroupRoleIdAndStatusOrderByPriority(long groupRoleId, int status);

    List<GroupRoleMap> findByGroupRoleIdOrderByPriority(long groupRoleId);
}
