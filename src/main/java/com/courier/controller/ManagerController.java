package com.courier.controller;

import com.courier.model.Manager;
import com.courier.service.ManagerService;
import com.courier.service.OfficeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private OfficeService officeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("managers", managerService.getAllManagers());
        return "manager/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("manager", new Manager());
        model.addAttribute("offices", officeService.getAllOffices());
        return "manager/form";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("manager") Manager manager,
                            BindingResult result,
                            @RequestParam Long officeId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("offices", officeService.getAllOffices());
            return "manager/form";
        }
        officeService.findById(officeId).ifPresent(manager::setAssignedOffice);
        managerService.save(manager);
        redirectAttributes.addFlashAttribute("success", "Manager added successfully!");
        return "redirect:/manager";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return managerService.findById(id).map(manager -> {
            model.addAttribute("manager", manager);
            model.addAttribute("offices", officeService.getAllOffices());
            model.addAttribute("editMode", true);
            return "manager/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Manager not found.");
            return "redirect:/manager";
        });
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("manager") Manager manager,
                             BindingResult result,
                             @RequestParam Long officeId,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("offices", officeService.getAllOffices());
            return "manager/form";
        }
        manager.setId(id);
        officeService.findById(officeId).ifPresent(manager::setAssignedOffice);
        managerService.save(manager);
        redirectAttributes.addFlashAttribute("success", "Manager updated successfully!");
        return "redirect:/manager";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        managerService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Manager deleted.");
        return "redirect:/manager";
    }
}
