package com.courier.repository;

import com.courier.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    boolean existsByShipmentId(Long shipmentId);
}
