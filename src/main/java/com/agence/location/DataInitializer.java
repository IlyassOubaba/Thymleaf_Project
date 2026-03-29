package com.agence.location;

import com.agence.location.entity.*;
import com.agence.location.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final VoitureRepository voitureRepository;
    private final ClientRepository clientRepository;
    private final LocationRepository locationRepository;

    @Override
    public void run(String... args) {
        // Voitures
        Voiture v1 = voitureRepository.save(Voiture.builder().immatriculation("AB-123-CD").marque("Renault ").segment("Citadine").disponible(true).prixJour(35.0).build());
        Voiture v2 = voitureRepository.save(Voiture.builder().immatriculation("EF-456-GH").marque("Peugeot 308").segment("munshern").disponible(false).prixJour(55.0).build());
        Voiture v3 = voitureRepository.save(Voiture.builder().immatriculation("IJ-789-KL").marque("Toyota RAV4").segment("FDE").disponible(true).prixJour(80.0).build());
        Voiture v4 = voitureRepository.save(Voiture.builder().immatriculation("MN-012-OP").marque("Mercedes Class C").segment("Luxe").disponible(true).prixJour(150.0).build());
        Voiture v5 = voitureRepository.save(Voiture.builder().immatriculation("QR-345-ST").marque("Citroën C3").segment("Citadine").disponible(true).prixJour(30.0).build());
        Voiture v6 = voitureRepository.save(Voiture.builder().immatriculation("UV-678-WX").marque("BMW J5").segment("FDE").disponible(false).prixJour(120.0).build());

        // Clients
        Client c1 = clientRepository.save(Client.builder().nom("ali addichan").cin("EI938437").telephone("0672832467").build());
        Client c2 = clientRepository.save(Client.builder().nom("Fatma zhra").cin("FZ789992").telephone("0693856274").build());
        Client c3 = clientRepository.save(Client.builder().nom("icho tinghiri").cin("YT345678").telephone("0673859375").build());
        Client c4 = clientRepository.save(Client.builder().nom("hmo ali").cin("KA9938534").telephone("0673429384").build());

        // Locations
        Location loc1 = new Location();
        loc1.setVoiture(v2); loc1.setClient(c1);
        loc1.setDateDebut(LocalDate.now().minusDays(3));
        loc1.setDateFin(LocalDate.now().plusDays(2));
        loc1.setStatut("EN_COURS"); loc1.calculerMontant();
        locationRepository.save(loc1);

        Location loc2 = new Location();
        loc2.setVoiture(v6); loc2.setClient(c2);
        loc2.setDateDebut(LocalDate.now().minusDays(10));
        loc2.setDateFin(LocalDate.now().minusDays(3));
        loc2.setStatut("EN_COURS"); loc2.calculerMontant();
        locationRepository.save(loc2);

        Location loc3 = new Location();
        loc3.setVoiture(v1); loc3.setClient(c3);
        loc3.setDateDebut(LocalDate.now().minusDays(30));
        loc3.setDateFin(LocalDate.now().minusDays(20));
        loc3.setStatut("TERMINEE"); loc3.calculerMontant();
        locationRepository.save(loc3);

        Location loc4 = new Location();
        loc4.setVoiture(v3); loc4.setClient(c4);
        loc4.setDateDebut(LocalDate.now().minusDays(60));
        loc4.setDateFin(LocalDate.now().minusDays(55));
        loc4.setStatut("TERMINEE"); loc4.calculerMontant();
        locationRepository.save(loc4);

        Location loc5 = new Location();
        loc5.setVoiture(v4); loc5.setClient(c1);
        loc5.setDateDebut(LocalDate.now().minusDays(15));
        loc5.setDateFin(LocalDate.now().minusDays(12));
        loc5.setStatut("ANNULEE"); loc5.calculerMontant();
        locationRepository.save(loc5);
    }
}
