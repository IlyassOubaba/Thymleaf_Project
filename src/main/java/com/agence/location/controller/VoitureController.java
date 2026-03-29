package com.agence.location.controller;

import com.agence.location.entity.Voiture;
import com.agence.location.service.VoitureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/voitures")
@RequiredArgsConstructor
public class VoitureController {

    private final VoitureService voitureService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String segment,
                       @RequestParam(required = false) Boolean disponible) {
        if (disponible != null && disponible) {
            model.addAttribute("voitures", voitureService.findDisponiblesBySegment(segment));
        } else if (segment != null && !segment.isEmpty()) {
            model.addAttribute("voitures", voitureService.findBySegment(segment));
        } else {
            model.addAttribute("voitures", voitureService.findAll());
        }
        model.addAttribute("segments", voitureService.findAllSegments());
        model.addAttribute("segmentFiltre", segment);
        model.addAttribute("disponibleFiltre", disponible);
        return "voiture/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("voiture", new Voiture());
        model.addAttribute("segments", List.of("Citadine", "Berline", "SUV", "Utilitaire", "Luxe", "Sport"));
        return "voiture/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Voiture voiture, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("segments", List.of("Citadine", "Berline", "SUV", "Utilitaire", "Luxe", "Sport"));
            return "voiture/form";
        }
        voitureService.save(voiture);
        ra.addFlashAttribute("success", "Voiture enregistrée avec succès !");
        return "redirect:/voitures";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("voiture", voitureService.findById(id));
        model.addAttribute("segments", List.of("Citadine", "Berline", "SUV", "Utilitaire", "Luxe", "Sport"));
        return "voiture/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            voitureService.deleteById(id);
            ra.addFlashAttribute("success", "Voiture supprimée avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Impossible de supprimer cette voiture.");
        }
        return "redirect:/voitures";
    }

    @GetMapping("/toggle/{id}")
    public String toggleDisponibilite(@PathVariable Long id, RedirectAttributes ra) {
        voitureService.toggleDisponibilite(id);
        ra.addFlashAttribute("success", "Disponibilité mise à jour !");
        return "redirect:/voitures";
    }
}
