package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ParamRepository extends JpaRepository<Param, Long> {
    Param findFirstByGroupAndCode(String group, String code);

    boolean existsByGroupAndCode(String group, String code);

    @Query("SELECT p FROM Param p  WHERE " +
            "(:group IS NULL OR p.group LIKE UPPER(TRIM(:group))) AND " +
            "(:code IS NULL OR p.code LIKE UPPER(TRIM(:code))) AND " +
            "(:value IS NULL OR p.value = :value) AND " +
            "(:name IS NULL OR p.name LIKE :name) AND " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:status = -1 OR :status IS NULL OR p.status = :status) ")
    List<Param> getListParam(@org.springframework.data.repository.query.Param("group") String group,
                             @org.springframework.data.repository.query.Param("code") String code,
                             @org.springframework.data.repository.query.Param("value") String value,
                             @org.springframework.data.repository.query.Param("name") String name,
                             @org.springframework.data.repository.query.Param("type") Integer type,
                             @org.springframework.data.repository.query.Param("status") Integer status);

}
