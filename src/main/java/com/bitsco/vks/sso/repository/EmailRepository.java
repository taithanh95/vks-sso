package com.bitsco.vks.sso.repository;

import com.bitsco.vks.sso.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface EmailRepository extends JpaRepository<Email, Long> {
    @Query("SELECT e FROM Email e WHERE e.status = :status AND e.intendAt <= :currDate ORDER BY e.id")
    List<Email> findByStatusOrderById(@Param("status") int status, @Param("currDate") Date currDate);

    List<Email> findByToAddressOrderById(String toAddress);
}
