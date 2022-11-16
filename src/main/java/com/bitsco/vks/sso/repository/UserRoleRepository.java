package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: truongnq
 * @date: 26-Dec-18 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RepositoryRestResource
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findFirstByUserIdAndRoleId(long userId, long roleId);

    List<UserRole> findByUserIdAndStatus(long userId, int status);

    Boolean existsByUserIdAndRoleIdAndStatus(long userId, long roleId, int status);

    @Query(value = "SELECT a FROM UserRole a WHERE 1 = 1 "
            + "AND(:roleId IS NULL OR a.roleId = :roleId)"
            + "AND(:userId IS NULL OR a.userId = :userId)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
    )
    List<UserRole> getPage(
            @Param("roleId") Long roleId,
            @Param("userId") Long userId,
            @Param("status") Integer status);

    @Query(value = " SELECT                                   "
            + "     ur.*                                 "
            + " FROM                                     "
            + "     user_role ur                         "
            + " WHERE                                    "
            + "     ur.n_status = 1                      "
            + "         AND ur.n_role_id IN (SELECT      "
            + "             n_id                         "
            + "         FROM                             "
            + "             role                         "
            + "         WHERE                            "
            + "             n_type = 2 AND n_status = 1) "
            , nativeQuery = true
    )
    List<UserRole> getListUserRoleMethod();
}
