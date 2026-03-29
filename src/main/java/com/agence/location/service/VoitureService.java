package com.agence.location.service;

import com.agence.location.entity.Voiture;
import com.agence.location.repository.VoitureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VoitureService {

    private final VoitureRepository voitureRepository;

    public List<Voiture> findAll() {
        return voitureRepository.findAll();
    }

    public Voiture findById(Long id) {
        return voitureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voiture non trouvée avec l'id: " + id));
    }

    public Voiture save(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    public void deleteById(Long id) {
        voitureRepository.deleteById(id);
    }

    public List<Voiture> findDisponibles() {
        return voitureRepository.findByDisponible(true);
    }

    public List<Voiture> findBySegment(String segment) {
        return voitureRepository.findBySegment(segment);
    }

    public List<Voiture> findDisponiblesBySegment(String segment) {
        if (segment == null || segment.isEmpty()) {
            return voitureRepository.findByDisponible(true);
        }
        return voitureRepository.findByDisponibleAndSegment(true, segment);
    }

    public void toggleDisponibilite(Long id) {
        Voiture voiture = findById(id);
        voiture.setDisponible(!voiture.isDisponible());
        voitureRepository.save(voiture);
    }

    public long countDisponibles() {
        return voitureRepository.countByDisponible(true);
    }

    public long countTotal() {
        return voitureRepository.count();
    }

    public List<String> findAllSegments() {
        return voitureRepository.findAll().stream()
                .map(Voiture::getSegment)
                .distinct()
                .sorted()
                .toList();
    }
}
