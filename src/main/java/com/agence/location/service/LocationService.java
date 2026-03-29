package com.agence.location.service;

import com.agence.location.entity.Location;
import com.agence.location.entity.Voiture;
import com.agence.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;
    private final VoitureService voitureService;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location non trouvée avec l'id: " + id));
    }

    public Location save(Location location) {
        location.calculerMontant();
        Location saved = locationRepository.save(location);
        // Marquer la voiture comme indisponible si location en cours
        if ("EN_COURS".equals(location.getStatut())) {
            Voiture v = location.getVoiture();
            v.setDisponible(false);
            voitureService.save(v);
        }
        return saved;
    }

    public Location update(Location location) {
        location.calculerMontant();
        return locationRepository.save(location);
    }

    public void deleteById(Long id) {
        Location loc = findById(id);
        // Libérer la voiture si la location est supprimée
        if ("EN_COURS".equals(loc.getStatut())) {
            Voiture v = loc.getVoiture();
            v.setDisponible(true);
            voitureService.save(v);
        }
        locationRepository.deleteById(id);
    }

    public void updateStatut(Long id, String statut) {
        Location location = findById(id);
        String ancienStatut = location.getStatut();
        location.setStatut(statut);
        locationRepository.save(location);

        // Gérer la disponibilité de la voiture
        Voiture voiture = location.getVoiture();
        if ("TERMINEE".equals(statut) || "ANNULEE".equals(statut)) {
            voiture.setDisponible(true);
        } else if ("EN_COURS".equals(statut)) {
            voiture.setDisponible(false);
        }
        voitureService.save(voiture);
    }

    public List<Location> findByStatut(String statut) {
        return locationRepository.findByStatut(statut);
    }

    public List<Location> findByPeriode(LocalDate debut, LocalDate fin) {
        return locationRepository.findByPeriode(debut, fin);
    }

    // Statistiques
    public double getTauxOccupation() {
        long total = voitureService.countTotal();
        if (total == 0) return 0;
        long indisponibles = total - voitureService.countDisponibles();
        return (double) indisponibles / total * 100;
    }

    public Map<String, Double> getRevenusParMarque() {
        Map<String, Double> result = new LinkedHashMap<>();
        locationRepository.findRevenusParMarque().forEach(row ->
                result.put((String) row[0], row[1] != null ? ((Number) row[1]).doubleValue() : 0.0));
        return result;
    }

    public Map<Integer, Double> getRevenusParMois() {
        Map<Integer, Double> result = new LinkedHashMap<>();
        // Initialiser tous les mois
        for (int i = 1; i <= 12; i++) result.put(i, 0.0);
        locationRepository.findRevenusParMois().forEach(row ->
                result.put(((Number) row[0]).intValue(), row[1] != null ? ((Number) row[1]).doubleValue() : 0.0));
        return result;
    }

    public double getTotalRevenus() {
        return locationRepository.findAll().stream()
                .filter(l -> !"ANNULEE".equals(l.getStatut()))
                .mapToDouble(l -> l.getMontantTotal() != null ? l.getMontantTotal() : 0)
                .sum();
    }

    public long countByStatut(String statut) {
        return locationRepository.countByStatut(statut);
    }

    public long countTotal() {
        return locationRepository.count();
    }
}
