package com.agence.location.repository;

import com.agence.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByStatut(String statut);

    List<Location> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    List<Location> findByClientId(Long clientId);

    List<Location> findByVoitureId(Long voitureId);

    @Query("SELECT l FROM Location l WHERE l.dateDebut >= :debut AND l.dateFin <= :fin")
    List<Location> findByPeriode(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query("SELECT l.voiture.marque, SUM(l.montantTotal) FROM Location l WHERE l.statut != 'ANNULEE' GROUP BY l.voiture.marque")
    List<Object[]> findRevenusParMarque();

    @Query("SELECT MONTH(l.dateDebut), SUM(l.montantTotal) FROM Location l WHERE l.statut != 'ANNULEE' GROUP BY MONTH(l.dateDebut) ORDER BY MONTH(l.dateDebut)")
    List<Object[]> findRevenusParMois();

    @Query("SELECT l.voiture.marque, COUNT(l) FROM Location l WHERE l.statut != 'ANNULEE' GROUP BY l.voiture.marque")
    List<Object[]> findLocationsParMarque();

    long countByStatut(String statut);
}
