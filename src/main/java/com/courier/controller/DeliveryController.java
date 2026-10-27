package com.courier.controller;

import com.courier.model.Delivery;
import com.courier.service.DeliveryService;
import com.courier.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("deliveries", deliveryService.getAllDeliveries());
        return "delivery/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("delivery", new Delivery());
        model.addAttribute("shipments", shipmentService.getAllShipments());
        model.addAttribute("statuses", Delivery.DeliveryStatus.values());
        return "delivery/form";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("delivery") Delivery delivery,
                            BindingResult result,
                            @RequestParam Long shipmentId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("shipments", shipmentService.getAllShipments());
            model.addAttribute("statuses", Delivery.DeliveryStatus.values());
            return "delivery/form";
        }
        shipmentService.findById(shipmentId).ifPresent(delivery::setShipment);
        deliveryService.save(delivery);
        redirectAttributes.addFlashAttribute("success", "Delivery record created successfully!");
        return "redirect:/delivery";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return deliveryService.findById(id).map(delivery -> {
            model.addAttribute("delivery", delivery);
            model.addAttribute("shipments", shipmentService.getAllShipments());
            model.addAttribute("statuses", Delivery.DeliveryStatus.values());
            model.addAttribute("editMode", true);
            return "delivery/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Delivery not found.");
            return "redirect:/delivery";
        });
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("delivery") Delivery delivery,
                             BindingResult result,
                             @RequestParam Long shipmentId,
                             @RequestParam Delivery.DeliveryStatus deliveryStatus,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("shipments", shipmentService.getAllShipments());
            model.addAttribute("statuses", Delivery.DeliveryStatus.values());
            return "delivery/form";
        }
        // Use the service to sync delivery+shipment status
        deliveryService.updateStatus(id, deliveryStatus, delivery.getRemarks());

        // Update other fields
        Delivery existing = deliveryService.findById(id).orElseThrow();
        existing.setPersonnelName(delivery.getPersonnelName());
        shipmentService.findById(shipmentId).ifPresent(existing::setShipment);
        deliveryService.save(existing);

        redirectAttributes.addFlashAttribute("success", "Delivery updated successfully!");
        return "redirect:/delivery";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        deliveryService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Delivery record deleted.");
        return "redirect:/delivery";
    }
}
