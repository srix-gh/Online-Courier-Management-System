package com.courier.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "shipments")
public class Shipment {

    public enum ShipmentStatus {
        BOOKED, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String trackingNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_office_id", nullable = false)
    private Office sourceOffice;

    @NotBlank(message = "Destination address is required")
    @Column(nullable = false)
    private String destinationAddress;

    @Column(nullable = false)
    private LocalDate shipmentDate;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "0.1", message = "Weight must be greater than 0")
    private Double weight;

    @NotNull(message = "Courier charge is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Charge must be greater than 0")
    private Double courierCharge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status = ShipmentStatus.BOOKED;

    public Shipment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Office getSourceOffice() { return sourceOffice; }
    public void setSourceOffice(Office sourceOffice) { this.sourceOffice = sourceOffice; }

    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }

    public LocalDate getShipmentDate() { return shipmentDate; }
    public void setShipmentDate(LocalDate shipmentDate) { this.shipmentDate = shipmentDate; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getCourierCharge() { return courierCharge; }
    public void setCourierCharge(Double courierCharge) { this.courierCharge = courierCharge; }

    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
}
