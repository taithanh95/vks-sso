package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.UserGroupRole;
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
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, Long> {
    List<UserGroupRole> findByUserIdAndStatus(long userId, int status);

    List<UserGroupRole> findByUserIdAndStatusOrderByPriority(long userId, int status);

    Boolean existsByUserIdAndGroupRoleIdAndStatus(long userId, long groupRoleId, int status);

    @Query(value = "SELECT MAX(a.priority) FROM UserGroupRole a WHERE 1 = 1 AND a.userId = :userId AND a.status = 1")
    Integer getMaxPriority(Long userId);

    UserGroupRole findFirstByUserIdAndPriorityAndStatus(long userId, int priority, int status);

    UserGroupRole findFirstByUserIdAndGroupRoleId(long userId, long groupRoleId);

    @Query(value = "SELECT a FROM UserGroupRole a WHERE 1 = 1 "
            + "AND(:groupRoleId IS NULL OR a.groupRoleId = :groupRoleId)"
            + "AND(:userId IS NULL OR a.userId = :userId)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
    )
    List<UserGroupRole> getPage(
            @Param("groupRoleId") Long groupRoleId,
            @Param("userId") Long userId,
            @Param("status") Integer status);
}
