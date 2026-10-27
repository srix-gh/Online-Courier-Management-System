package com.courier.service;

import com.courier.model.Office;
import com.courier.repository.ManagerRepository;
import com.courier.repository.OfficeRepository;
import com.courier.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public Optional<Office> findById(Long id) {
        return officeRepository.findById(id);
    }

    public Office save(Office office) {
        return officeRepository.save(office);
    }

    public void delete(Long id) {
        officeRepository.deleteById(id);
    }

    public boolean hasRelatedRecords(Long officeId) {
        return shipmentRepository.existsBySourceOfficeId(officeId)
                || managerRepository.existsByAssignedOfficeId(officeId);
    }
}
