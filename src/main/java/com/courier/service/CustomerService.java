package com.courier.service;

import com.courier.model.Customer;
import com.courier.repository.CustomerRepository;
import com.courier.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> search(String query) {
        return customerRepository.findByFullNameContainingIgnoreCaseOrPhoneContaining(query, query);
    }

    public boolean hasShipments(Long customerId) {
        return shipmentRepository.existsByCustomerId(customerId);
    }
}
