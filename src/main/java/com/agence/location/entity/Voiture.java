package com.agence.location.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "voitures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Voiture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'immatriculation est obligatoire ")
    @Column(unique = true)
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire ")
    private String marque;

    @NotBlank(message = "Le segment est obligatoire ")
    private String segment;

    private boolean disponible = true;

    @NotNull(message = "Le prix par jour est obligatoire ")
    @DecimalMin(value = "0.0", inclusive = false, message = " Le prix doit être positif ")
    private Double prixJour;
}
