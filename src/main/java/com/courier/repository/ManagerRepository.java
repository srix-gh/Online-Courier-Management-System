package com.courier.repository;

import com.courier.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    boolean existsByAssignedOfficeId(Long officeId);
}
