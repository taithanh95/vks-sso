package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.GroupUserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface GroupUserGroupRoleRepository extends JpaRepository<GroupUserGroupRole, Long> {
    List<GroupUserGroupRole> findByGroupUserIdAndStatus(long groupUserId, int status);

    GroupUserGroupRole findFirstByGroupUserIdAndGroupRoleId(long groupUserId, long groupRoleId);

    Boolean existsByGroupUserIdAndGroupRoleIdAndStatus(long groupUserId, long groupRoleId, int status);
}
