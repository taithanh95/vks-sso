package com.bitsco.vks.sso.repository;

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
 * @date: 26-Dec-18 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findFirstByUrl(String url);

    List<Role> findByParentIdAndStatus(Long parentId, Integer status);

    @Query(
            value = "SELECT DISTINCT \n" +
                    "    r.*\n" +
                    "FROM\n" +
                    "    role r\n" +
                    "WHERE\n" +
                    "    n_status = 1\n" +
                    "        AND (n_id IN (SELECT \n" +
                    "            n_role_id\n" +
                    "        FROM\n" +
                    "            user_role\n" +
                    "        WHERE\n" +
                    "            n_status = 1 AND n_user_id = :userId)\n" +
                    "        OR n_id IN (SELECT \n" +
                    "            grm.n_role_id\n" +
                    "        FROM\n" +
                    "            user_group_role ugr,\n" +
                    "            group_role_map grm\n" +
                    "        WHERE\n" +
                    "            ugr.n_group_role_id = grm.n_group_role_id\n" +
                    "                AND ugr.n_status = 1\n" +
                    "                AND grm.n_status = 1\n" +
                    "                AND ugr.n_user_id = :userId))",
            nativeQuery = true)
    List<Role> findByUserId(
            @Param("userId") long userId
    );

    List<Role> findByStatus(int status);

    List<Role> findByStatusAndUrlLike(int status, String url);

    List<Role> findByStatusAndTypeAndUrlLike(int status, int type, String url);

    @Query(
            value = "SELECT \n" +
                    "    r.*\n" +
                    "FROM\n" +
                    "    role r\n" +
                    "        INNER JOIN\n" +
                    "    group_role_map grm ON r.n_id = grm.n_role_id\n" +
                    "WHERE\n" +
                    "    r.n_status = :roleStatus AND r.n_type = :roleType\n" +
                    "        AND grm.n_status = :groupRoleMapStatus\n" +
                    "        AND grm.n_group_role_id = :groupRoleId\n" +
                    "ORDER BY grm.n_priority",
            nativeQuery = true)
    List<Role> findByGroupRoleId(
            @Param("groupRoleId") long groupRoleId,
            @Param("roleStatus") int roleStatus,
            @Param("roleType") int roleType,
            @Param("groupRoleMapStatus") int groupRoleMapStatus
    );

    @Query(
            value = "SELECT \n" +
                    "    r.*\n" +
                    "FROM\n" +
                    "    role r\n" +
                    "        INNER JOIN\n" +
                    "    group_role_map grm ON r.n_id = grm.n_role_id\n" +
                    "WHERE\n" +
                    "    r.n_status = :roleStatus " +
                    "        AND grm.n_group_role_id = :groupRoleId\n" +
                    "ORDER BY grm.n_priority",
            nativeQuery = true)
    List<Role> findByGroupRoleIdAndStatus(
            @Param("groupRoleId") long groupRoleId,
            @Param("roleStatus") int roleStatus
    );

    @Query(value = "SELECT a FROM Role a WHERE 1 = 1 "
            + "AND(:name IS NULL OR a.name LIKE :name)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
    )
    List<Role> getPage(@Param("name") String name,
                       @Param("status") Integer status);

    @Query(value = "SELECT a FROM Role a WHERE 1 = 1 "
            + "AND(:name IS NULL OR a.name LIKE :name)"
            + "AND(:url IS NULL OR a.url LIKE :url)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
            + " ORDER BY a.url, a.name "
    )
    List<Role> getList(
            @Param("name") String name,
            @Param("url") String url,
            @Param("status") Integer status);

    List<Role> findByTypeAndStatus(int type, int status);

}
