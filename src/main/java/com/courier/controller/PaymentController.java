package com.courier.controller;

import com.courier.model.Payment;
import com.courier.service.PaymentService;
import com.courier.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payment/list";
    }

    @GetMapping("/add")
    public String addForm(@RequestParam(required = false) Long shipmentId, Model model) {
        Payment payment = new Payment();
        if (shipmentId != null) {
            shipmentService.findById(shipmentId).ifPresent(s -> {
                payment.setShipment(s);
                payment.setAmount(s.getCourierCharge());
            });
        }
        model.addAttribute("payment", payment);
        model.addAttribute("shipments", shipmentService.getAllShipments());
        model.addAttribute("methods", Payment.PaymentMethod.values());
        model.addAttribute("selectedShipmentId", shipmentId);
        return "payment/form";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("payment") Payment payment,
                            BindingResult result,
                            @RequestParam Long shipmentId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("shipments", shipmentService.getAllShipments());
            model.addAttribute("methods", Payment.PaymentMethod.values());
            return "payment/form";
        }
        shipmentService.findById(shipmentId).ifPresent(payment::setShipment);
        paymentService.save(payment);
        redirectAttributes.addFlashAttribute("success", "Payment record created!");
        return "redirect:/payment";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return paymentService.findById(id).map(payment -> {
            model.addAttribute("payment", payment);
            model.addAttribute("methods", Payment.PaymentMethod.values());
            return "payment/view";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Payment not found.");
            return "redirect:/payment";
        });
    }

    @PostMapping("/pay/{id}")
    public String markAsPaid(@PathVariable Long id,
                             @RequestParam Payment.PaymentMethod paymentMethod,
                             RedirectAttributes redirectAttributes) {
        paymentService.markAsPaid(id, paymentMethod);
        redirectAttributes.addFlashAttribute("success", "Payment marked as PAID.");
        return "redirect:/payment";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        paymentService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Payment record deleted.");
        return "redirect:/payment";
    }
}
