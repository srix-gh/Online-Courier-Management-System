package com.courier.controller;

import com.courier.model.Customer;
import com.courier.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        List<Customer> customers;
        if (search != null && !search.isBlank()) {
            customers = customerService.search(search);
            model.addAttribute("search", search);
        } else {
            customers = customerService.getAllCustomers();
        }
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/form";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("customer") Customer customer,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "customer/form";
        }
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("success", "Customer registered successfully!");
        return "redirect:/customer";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return customerService.findById(id).map(customer -> {
            model.addAttribute("customer", customer);
            model.addAttribute("editMode", true);
            return "customer/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Customer not found.");
            return "redirect:/customer";
        });
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("customer") Customer customer,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "customer/form";
        }
        customer.setId(id);
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("success", "Customer updated successfully!");
        return "redirect:/customer";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (customerService.hasShipments(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete this customer. They have associated shipments.");
        } else {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Customer deleted successfully.");
        }
        return "redirect:/customer";
    }
}
