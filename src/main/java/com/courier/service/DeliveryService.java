package com.courier.service;

import com.courier.model.Delivery;
import com.courier.model.Shipment;
import com.courier.repository.DeliveryRepository;
import com.courier.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    public Delivery save(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    /**
     * Updates delivery status and synchronizes shipment status.
     * When a delivery is COMPLETED, shipment becomes DELIVERED.
     * When a delivery is IN_PROGRESS, shipment becomes OUT_FOR_DELIVERY.
     */
    public Delivery updateStatus(Long deliveryId, Delivery.DeliveryStatus newStatus, String remarks) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setDeliveryStatus(newStatus);
        delivery.setRemarks(remarks);

        if (newStatus == Delivery.DeliveryStatus.COMPLETED) {
            delivery.setDeliveryDate(LocalDate.now());
            // Sync shipment status
            Shipment shipment = delivery.getShipment();
            shipment.setStatus(Shipment.ShipmentStatus.DELIVERED);
            shipmentRepository.save(shipment);
        } else if (newStatus == Delivery.DeliveryStatus.IN_PROGRESS) {
            Shipment shipment = delivery.getShipment();
            shipment.setStatus(Shipment.ShipmentStatus.OUT_FOR_DELIVERY);
            shipmentRepository.save(shipment);
        }

        return deliveryRepository.save(delivery);
    }

    public void delete(Long id) {
        deliveryRepository.deleteById(id);
    }
}
