package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {
    @Query(value = " SELECT a FROM GroupUser a WHERE 1 = 1 "
            + " AND(:name IS NULL OR lower(a.name) LIKE lower(:name))"
            + " AND(:description IS NULL OR lower(a.description) LIKE lower(:description))"
            + " AND(:status IS NULL OR a.status = :status) "
            + " ORDER BY a.id DESC "
    )
    List<GroupUser> getList(
            @Param("name") String name,
            @Param("description") String description,
            @Param("status") Integer status);

    List<GroupUser> findByStatus(int status);
}
