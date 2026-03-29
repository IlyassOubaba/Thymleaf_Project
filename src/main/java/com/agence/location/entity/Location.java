package com.agence.location.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "locations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    private Double montantTotal;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut = "EN_COURS";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voiture_id")
    @NotNull(message = "La voiture est obligatoire")
    private Voiture voiture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @NotNull(message = "Le client est obligatoire")
    private Client client;

    public long getNombreJours() {
        if (dateDebut != null && dateFin != null) {
            long jours = java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin);
            return jours > 0 ? jours : 1;
        }
        return 1;
    }

    public void calculerMontant() {
        if (voiture != null && voiture.getPrixJour() != null) {
            this.montantTotal = getNombreJours() * voiture.getPrixJour();
        }
    }
}
