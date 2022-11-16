package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.GroupRole;
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
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {
    GroupRole findFirstByUrlAndName(String url, String name);

    GroupRole findFirstByIdAndAppCode(Long id, String appCode);

    boolean existsByUrl(String url);

    List<GroupRole> findByStatus(int status);

    List<GroupRole> findByStatusAndUrl(int status, String url);

    @Query(value = "SELECT a FROM GroupRole a WHERE 1 = 1 "
            + "AND(:name IS NULL OR a.name = :name)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
    )
    List<GroupRole> getPage(
            @Param("name") String name,
            @Param("status") Integer status);

    @Query(value = " SELECT a FROM GroupRole a WHERE 1 = 1 "
            + " AND(:name IS NULL OR a.name LIKE :name) "
            + " AND(:url IS NULL OR a.url LIKE :url) "
            + " AND(:status = -1 OR :status IS NULL OR a.status = :status) "
            + " ORDER BY a.url "
    )
    List<GroupRole> getList(
            @Param("name") String name,
            @Param("url") String url,
            @Param("status") Integer status);

    @Query(value = " SELECT gr.*                                                \n" +
            "   FROM group_role gr                                              \n" +
            "        INNER JOIN user_group_role ugr ON gr.n_id = ugr.n_group_role_id  \n" +
            "  WHERE     1 = 1                                                  \n" +
            "        AND (:userId IS NULL OR ugr.n_user_id = :userId )          \n" +
            "        AND (:appCode IS NULL OR gr.s_app_code = :appCode)           \n" +
            "        AND (:status IS NULL OR gr.n_status = :status) ORDER BY ugr.n_priority \n"
            , nativeQuery = true
    )
    List<GroupRole> getListGroupRole(
            @Param("userId") Long userId,
            @Param("appCode") String appCode,
            @Param("status") Integer status
    );

}
