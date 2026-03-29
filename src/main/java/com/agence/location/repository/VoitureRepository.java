package com.agence.location.repository;

import com.agence.location.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    List<Voiture> findByDisponible(boolean disponible);
    List<Voiture> findBySegment(String segment);
    List<Voiture> findByDisponibleAndSegment(boolean disponible, String segment);
    List<Voiture> findByMarqueContainingIgnoreCase(String marque);
    long countByDisponible(boolean disponible);
}
