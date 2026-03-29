package com.agence.location.controller;

import com.agence.location.entity.Location;
import com.agence.location.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    private final VoitureService voitureService;
    private final ClientService clientService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String statut,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        if (debut != null && fin != null) {
            model.addAttribute("locations", locationService.findByPeriode(debut, fin));
        } else if (statut != null && !statut.isEmpty()) {
            model.addAttribute("locations", locationService.findByStatut(statut));
        } else {
            model.addAttribute("locations", locationService.findAll());
        }
        model.addAttribute("statutFiltre", statut);
        model.addAttribute("debut", debut);
        model.addAttribute("fin", fin);
        return "location/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Location location = new Location();
        location.setDateDebut(LocalDate.now());
        location.setDateFin(LocalDate.now().plusDays(1));
        location.setStatut("EN_COURS");
        model.addAttribute("location", location);
        model.addAttribute("voitures", voitureService.findDisponibles());
        model.addAttribute("clients", clientService.findAll());
        return "location/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Location location, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("voitures", voitureService.findDisponibles());
            model.addAttribute("clients", clientService.findAll());
            return "location/form";
        }
        if (location.getDateFin().isBefore(location.getDateDebut())) {
            result.rejectValue("dateFin", "error.location", "La date de fin doit être après la date de début");
            model.addAttribute("voitures", voitureService.findDisponibles());
            model.addAttribute("clients", clientService.findAll());
            return "location/form";
        }
        locationService.save(location);
        ra.addFlashAttribute("success", "Location enregistrée avec succès !");
        return "redirect:/locations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Location location = locationService.findById(id);
        model.addAttribute("location", location);
        model.addAttribute("voitures", voitureService.findAll());
        model.addAttribute("clients", clientService.findAll());
        return "location/form";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute Location location, BindingResult result,
                         Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("voitures", voitureService.findAll());
            model.addAttribute("clients", clientService.findAll());
            return "location/form";
        }
        locationService.update(location);
        ra.addFlashAttribute("success", "Location mise à jour !");
        return "redirect:/locations";
    }

    @GetMapping("/statut/{id}/{statut}")
    public String updateStatut(@PathVariable Long id, @PathVariable String statut, RedirectAttributes ra) {
        locationService.updateStatut(id, statut);
        ra.addFlashAttribute("success", "Statut mis à jour : " + statut);
        return "redirect:/locations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            locationService.deleteById(id);
            ra.addFlashAttribute("success", "Location supprimée !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erreur lors de la suppression.");
        }
        return "redirect:/locations";
    }
}
