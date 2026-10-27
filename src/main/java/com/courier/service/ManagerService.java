package com.courier.service;

import com.courier.model.Manager;
import com.courier.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> findById(Long id) {
        return managerRepository.findById(id);
    }

    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    public void delete(Long id) {
        managerRepository.deleteById(id);
    }
}
