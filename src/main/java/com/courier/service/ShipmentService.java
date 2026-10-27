package com.courier.service;

import com.courier.model.Shipment;
import com.courier.repository.DeliveryRepository;
import com.courier.repository.PaymentRepository;
import com.courier.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public List<Shipment> getRecentShipments() {
        return shipmentRepository.findTop5ByOrderByIdDesc();
    }

    public Optional<Shipment> findById(Long id) {
        return shipmentRepository.findById(id);
    }

    public Optional<Shipment> findByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber);
    }

    public Shipment save(Shipment shipment) {
        if (shipment.getId() == null) {
            shipment.setTrackingNumber(generateTrackingNumber());
        }
        if (shipment.getShipmentDate() == null) {
            shipment.setShipmentDate(LocalDate.now());
        }
        return shipmentRepository.save(shipment);
    }

    public void delete(Long id) {
        shipmentRepository.deleteById(id);
    }

    public boolean hasRelatedRecords(Long shipmentId) {
        return deliveryRepository.existsByShipmentId(shipmentId)
                || paymentRepository.existsByShipmentId(shipmentId);
    }

    private String generateTrackingNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String unique = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "CMS-" + date + "-" + unique;
    }
}
