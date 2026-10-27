package com.courier.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Manager name is required")
    @Column(nullable = false)
    private String managerName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    @Column(nullable = false)
    private String contactNumber;

    @Email(message = "Please enter a valid email")
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "office_id", nullable = false)
    private Office assignedOffice;

    public Manager() {}

    public Manager(String managerName, String contactNumber, String email, Office assignedOffice) {
        this.managerName = managerName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.assignedOffice = assignedOffice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getManagerName() { return managerName; }
    public void setManagerName(String managerName) { this.managerName = managerName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Office getAssignedOffice() { return assignedOffice; }
    public void setAssignedOffice(Office assignedOffice) { this.assignedOffice = assignedOffice; }
}
