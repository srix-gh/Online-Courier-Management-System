package com.courier.config;

import com.courier.model.*;
import com.courier.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Loads sample data only if the database is empty.
 * Safe to run on every restart — will NOT duplicate records.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private OfficeRepository officeRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ManagerRepository managerRepository;
    @Autowired private ShipmentRepository shipmentRepository;
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private PaymentRepository paymentRepository;

    @Override
    public void run(String... args) {
        // Only seed if the database is completely empty
        if (officeRepository.count() > 0) return;

        // --- Offices ---
        Office mumbai = officeRepository.save(new Office(
                "Mumbai Central Office", "123 Marine Drive", "Mumbai", "9876543210"));
        Office delhi = officeRepository.save(new Office(
                "Delhi North Office", "456 Connaught Place", "Delhi", "9123456789"));

        // --- Managers ---
        managerRepository.save(new Manager("Ravi Sharma", "9000011111", "ravi@courier.com", mumbai));
        managerRepository.save(new Manager("Priya Mehta", "9000022222", "priya@courier.com", delhi));

        // --- Customers ---
        Customer c1 = customerRepository.save(new Customer(
                "Amit Kumar", "9111111111", "amit@example.com", "12 Park Street, Kolkata"));
        Customer c2 = customerRepository.save(new Customer(
                "Sonia Patel", "9222222222", "sonia@example.com", "34 MG Road, Bangalore"));
        Customer c3 = customerRepository.save(new Customer(
                "Rahul Verma", "9333333333", "rahul@example.com", "56 Church Street, Chennai"));

        // --- Shipments ---
        Shipment s1 = new Shipment();
        s1.setTrackingNumber("CMS-20241001-DEMO01");
        s1.setCustomer(c1);
        s1.setSourceOffice(mumbai);
        s1.setDestinationAddress("12 Park Street, Kolkata");
        s1.setShipmentDate(LocalDate.now().minusDays(5));
        s1.setWeight(2.5);
        s1.setCourierCharge(350.00);
        s1.setStatus(Shipment.ShipmentStatus.DELIVERED);
        s1 = shipmentRepository.save(s1);

        Shipment s2 = new Shipment();
        s2.setTrackingNumber("CMS-20241002-DEMO02");
        s2.setCustomer(c2);
        s2.setSourceOffice(delhi);
        s2.setDestinationAddress("34 MG Road, Bangalore");
        s2.setShipmentDate(LocalDate.now().minusDays(2));
        s2.setWeight(1.0);
        s2.setCourierCharge(150.00);
        s2.setStatus(Shipment.ShipmentStatus.IN_TRANSIT);
        s2 = shipmentRepository.save(s2);

        Shipment s3 = new Shipment();
        s3.setTrackingNumber("CMS-20241003-DEMO03");
        s3.setCustomer(c3);
        s3.setSourceOffice(mumbai);
        s3.setDestinationAddress("56 Church Street, Chennai");
        s3.setShipmentDate(LocalDate.now());
        s3.setWeight(3.0);
        s3.setCourierCharge(450.00);
        s3.setStatus(Shipment.ShipmentStatus.BOOKED);
        s3 = shipmentRepository.save(s3);

        // --- Deliveries ---
        Delivery d1 = new Delivery();
        d1.setShipment(s1);
        d1.setPersonnelName("Manoj Singh");
        d1.setDeliveryDate(LocalDate.now().minusDays(1));
        d1.setDeliveryStatus(Delivery.DeliveryStatus.COMPLETED);
        d1.setRemarks("Delivered successfully");
        deliveryRepository.save(d1);

        Delivery d2 = new Delivery();
        d2.setShipment(s2);
        d2.setPersonnelName("Suresh Kumar");
        d2.setDeliveryStatus(Delivery.DeliveryStatus.IN_PROGRESS);
        d2.setRemarks("En route to destination");
        deliveryRepository.save(d2);

        // --- Payments ---
        Payment p1 = new Payment();
        p1.setShipment(s1);
        p1.setAmount(350.00);
        p1.setPaymentMethod(Payment.PaymentMethod.UPI);
        p1.setPaymentDate(LocalDate.now().minusDays(4));
        p1.setPaymentStatus(Payment.PaymentStatus.PAID);
        paymentRepository.save(p1);

        Payment p2 = new Payment();
        p2.setShipment(s2);
        p2.setAmount(150.00);
        p2.setPaymentStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(p2);

        Payment p3 = new Payment();
        p3.setShipment(s3);
        p3.setAmount(450.00);
        p3.setPaymentStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(p3);

        System.out.println("✅ Sample data loaded successfully.");
    }
}
