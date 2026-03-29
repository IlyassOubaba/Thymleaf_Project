package com.agence.location.controller;

import com.agence.location.entity.Client;
import com.agence.location.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("clients", clientService.searchByNom(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("clients", clientService.findAll());
        }
        return "client/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("client", new Client());
        return "client/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Client client, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "client/form";
        }
        clientService.save(client);
        ra.addFlashAttribute("success", "Client enregistré avec succès !");
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        return "client/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            clientService.deleteById(id);
            ra.addFlashAttribute("success", "Client supprimé avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Impossible de supprimer ce client (locations existantes).");
        }
        return "redirect:/clients";
    }
}
