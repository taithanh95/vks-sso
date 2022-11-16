package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.GroupUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GroupUserRoleRepository extends JpaRepository<GroupUserRole, Long> {
    List<GroupUserRole> findByGroupUserIdAndStatus(long groupUserId, int status);

    GroupUserRole findFristByGroupUserIdAndRoleId(long groupUserId, long roleId);

    Boolean existsByGroupUserIdAndRoleIdAndStatus(long groupUserId, long roleId, int status);
}
