package com.agence.location.controller;

import com.agence.location.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final VoitureService voitureService;
    private final ClientService clientService;
    private final LocationService locationService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalVoitures", voitureService.countTotal());
        model.addAttribute("voituresDisponibles", voitureService.countDisponibles());
        model.addAttribute("totalClients", clientService.countTotal());
        model.addAttribute("totalLocations", locationService.countTotal());
        model.addAttribute("locationsEnCours", locationService.countByStatut("EN_COURS"));
        model.addAttribute("totalRevenus", locationService.getTotalRevenus());
        model.addAttribute("tauxOccupation", locationService.getTauxOccupation());
        model.addAttribute("recentesLocations", locationService.findAll().stream().limit(5).toList());
        return "index";
    }

    @GetMapping("/stats")
    public String stats(Model model) {
        model.addAttribute("tauxOccupation", locationService.getTauxOccupation());
        model.addAttribute("totalVoitures", voitureService.countTotal());
        model.addAttribute("voituresDisponibles", voitureService.countDisponibles());
        model.addAttribute("totalRevenus", locationService.getTotalRevenus());
        model.addAttribute("revenusParMarque", locationService.getRevenusParMarque());
        model.addAttribute("revenusParMois", locationService.getRevenusParMois());
        model.addAttribute("locationsEnCours", locationService.countByStatut("EN_COURS"));
        model.addAttribute("locationsTerminees", locationService.countByStatut("TERMINEE"));
        model.addAttribute("locationsAnnulees", locationService.countByStatut("ANNULEE"));
        return "stats/dashboard";
    }
}
