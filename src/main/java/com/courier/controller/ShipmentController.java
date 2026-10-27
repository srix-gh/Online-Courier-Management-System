package com.courier.controller;

import com.courier.model.Shipment;
import com.courier.service.CustomerService;
import com.courier.service.OfficeService;
import com.courier.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OfficeService officeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("shipments", shipmentService.getAllShipments());
        return "shipment/list";
    }

    @GetMapping("/track")
    public String trackForm(@RequestParam(required = false) String trackingNumber, Model model) {
        if (trackingNumber != null && !trackingNumber.isBlank()) {
            shipmentService.findByTrackingNumber(trackingNumber.trim()).ifPresentOrElse(
                    s -> model.addAttribute("shipment", s),
                    () -> model.addAttribute("trackError", "No shipment found with tracking number: " + trackingNumber)
            );
            model.addAttribute("trackingNumber", trackingNumber);
        }
        return "shipment/track";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("shipment", new Shipment());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("offices", officeService.getAllOffices());
        model.addAttribute("statuses", Shipment.ShipmentStatus.values());
        return "shipment/form";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("shipment") Shipment shipment,
                            BindingResult result,
                            @RequestParam Long customerId,
                            @RequestParam Long sourceOfficeId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("statuses", Shipment.ShipmentStatus.values());
            return "shipment/form";
        }
        customerService.findById(customerId).ifPresent(shipment::setCustomer);
        officeService.findById(sourceOfficeId).ifPresent(shipment::setSourceOffice);
        shipmentService.save(shipment);
        redirectAttributes.addFlashAttribute("success", "Shipment created successfully!");
        return "redirect:/shipment";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return shipmentService.findById(id).map(shipment -> {
            model.addAttribute("shipment", shipment);
            return "shipment/view";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Shipment not found.");
            return "redirect:/shipment";
        });
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return shipmentService.findById(id).map(shipment -> {
            model.addAttribute("shipment", shipment);
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("statuses", Shipment.ShipmentStatus.values());
            model.addAttribute("editMode", true);
            return "shipment/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Shipment not found.");
            return "redirect:/shipment";
        });
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("shipment") Shipment shipment,
                             BindingResult result,
                             @RequestParam Long customerId,
                             @RequestParam Long sourceOfficeId,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("customers", customerService.getAllCustomers());
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("statuses", Shipment.ShipmentStatus.values());
            return "shipment/form";
        }

        Shipment existing = shipmentService.findById(id).orElseThrow();
        existing.setDestinationAddress(shipment.getDestinationAddress());
        existing.setWeight(shipment.getWeight());
        existing.setCourierCharge(shipment.getCourierCharge());
        existing.setStatus(shipment.getStatus());
        existing.setShipmentDate(shipment.getShipmentDate());
        customerService.findById(customerId).ifPresent(existing::setCustomer);
        officeService.findById(sourceOfficeId).ifPresent(existing::setSourceOffice);

        shipmentService.save(existing);
        redirectAttributes.addFlashAttribute("success", "Shipment updated successfully!");
        return "redirect:/shipment";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (shipmentService.hasRelatedRecords(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete this shipment. It has associated delivery or payment records.");
        } else {
            shipmentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Shipment deleted.");
        }
        return "redirect:/shipment";
    }
}
