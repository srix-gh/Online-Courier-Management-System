package com.courier.controller;

import com.courier.model.Office;
import com.courier.service.OfficeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("offices", officeService.getAllOffices());
        return "office/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("office", new Office());
        return "office/form";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("office") Office office,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "office/form";
        }
        officeService.save(office);
        redirectAttributes.addFlashAttribute("success", "Office added successfully!");
        return "redirect:/office";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return officeService.findById(id).map(office -> {
            model.addAttribute("office", office);
            model.addAttribute("editMode", true);
            return "office/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Office not found.");
            return "redirect:/office";
        });
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("office") Office office,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "office/form";
        }
        office.setId(id);
        officeService.save(office);
        redirectAttributes.addFlashAttribute("success", "Office updated successfully!");
        return "redirect:/office";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (officeService.hasRelatedRecords(id)) {
            redirectAttributes.addFlashAttribute("error",
                    "Cannot delete this office. It has associated shipments or managers.");
        } else {
            officeService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Office deleted successfully.");
        }
        return "redirect:/office";
    }
}
