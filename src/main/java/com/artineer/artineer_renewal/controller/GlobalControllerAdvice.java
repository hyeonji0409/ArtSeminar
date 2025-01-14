package com.artineer.artineer_renewal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.artineer.artineer_renewal.service.InformationFileDataService;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private InformationFileDataService informationFileDataService;

    @ModelAttribute
    public void addFooterInfo(Model model) {
        model.addAttribute("headOfficerName", informationFileDataService.get("headOfficerName"));
        model.addAttribute("contactNumber", informationFileDataService.get("contactNumber"));
        model.addAttribute("email", informationFileDataService.get("email"));
    }
}
