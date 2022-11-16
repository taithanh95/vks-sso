package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT a FROM User a WHERE upper(a.username) = upper(:username)")
    User findFirstByUsername(String username);

    User findFirstByUsernameAndEmail(String username, String email);

    @Query(value = "SELECT a FROM User a WHERE 1 = 1 "
            + "AND(:username IS NULL OR a.username LIKE :username)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
    )
    List<User> getPage(@Param("username") String username,
                       @Param("status") Integer status);

    @Query(value = "SELECT a FROM User a WHERE 1 = 1 "
            + "AND(:username IS NULL OR a.username LIKE (:username) )"
            + "AND(:name IS NULL OR lower(a.name) LIKE (:name) )"
            + "AND(:address IS NULL OR lower(a.address) LIKE (:address) )"
            + "AND(:sppid IS NULL OR lower(a.sppid) LIKE (:sppid) )"
            + "AND(:inspcode IS NULL OR lower(a.inspcode) LIKE (:inspcode) )"
            + "AND(:groupid IS NULL OR a.groupid = :groupid)"
            + "AND(:departid IS NULL OR a.departid = :departid)"
            + "AND(:status = -1 OR :type IS NULL OR a.type = :type)"
            + "AND(:status = -1 OR :status IS NULL OR a.status = :status)"
            + "AND(:groupUserId IS NULL OR a.groupUserId = :groupUserId)"
            + "ORDER BY a.createdAt DESC"
    )
    List<User> getList(
            @Param("username") String username,
            @Param("name") String name,
            @Param("address") String address,
            @Param("sppid") String sppid,
            @Param("inspcode") String inspcode,
            @Param("groupid") String groupid,
            @Param("departid") String departid,
            @Param("type") Integer type,
            @Param("status") Integer status,
            @Param("groupUserId") Long groupUserId
    );
}
