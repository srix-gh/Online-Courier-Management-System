package com.courier.controller;

import com.courier.service.CustomerService;
import com.courier.service.OfficeService;
import com.courier.service.PaymentService;
import com.courier.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCustomers", customerService.getAllCustomers().size());
        model.addAttribute("totalShipments", shipmentService.getAllShipments().size());
        model.addAttribute("totalOffices", officeService.getAllOffices().size());
        model.addAttribute("totalPayments", paymentService.getAllPayments().size());
        model.addAttribute("recentShipments", shipmentService.getRecentShipments());
        return "dashboard";
    }
}
