package com.courier.repository;

import com.courier.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
    boolean existsByCustomerId(Long customerId);
    boolean existsBySourceOfficeId(Long officeId);
    List<Shipment> findTop5ByOrderByIdDesc();
}
