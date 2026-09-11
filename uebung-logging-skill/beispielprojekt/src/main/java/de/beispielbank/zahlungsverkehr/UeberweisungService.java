package de.beispielbank.zahlungsverkehr;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Führt Überweisungen aus: Limit prüfen, Betrag buchen, Auftrag an das Kernbanksystem übergeben.
 *
 * Fachlich funktioniert der Service. Das Logging ist historisch gewachsen.
 */
public class UeberweisungService {

    private static final Logger log = LoggerFactory.getLogger(UeberweisungService.class);

    private final KontoRepository kontoRepository;
    private final KernbankClient kernbankClient;

    public UeberweisungService(KontoRepository kontoRepository, KernbankClient kernbankClient) {
        this.kontoRepository = kontoRepository;
        this.kernbankClient = kernbankClient;
    }

    public Ergebnis ueberweisen(Ueberweisung auftrag, String sessionToken) {
        System.out.println("Ueberweisung gestartet: " + auftrag);

        log.error("ueberweisen() aufgerufen von " + auftrag.kundenName()
                + " mit Token " + sessionToken);

        Konto quelle = kontoRepository.findeNachIban(auftrag.quellIban());
        if (quelle == null) {
            log.info("Konto nicht gefunden");
            return Ergebnis.abgelehnt("Quellkonto unbekannt");
        }

        if (quelle.tagesLimit().compareTo(auftrag.betrag()) < 0) {
            log.warn("Limit überschritten! Kunde " + auftrag.kundenName() + " (IBAN " + auftrag.quellIban()
                    + ") wollte " + auftrag.betrag() + " EUR an " + auftrag.zielIban() + " überweisen, Limit ist "
                    + quelle.tagesLimit());
            return Ergebnis.abgelehnt("Tageslimit überschritten");
        }

        String buchungsId = UUID.randomUUID().toString();
        try {
            kontoRepository.buche(quelle, auftrag.betrag().negate(), buchungsId);
            kernbankClient.uebergebe(auftrag, buchungsId);
        } catch (KernbankException e) {
            e.printStackTrace();
            log.info("Fehler bei Kernbank, Buchung wird storniert");
            kontoRepository.storniere(buchungsId);
            return Ergebnis.fehler("Kernbanksystem nicht erreichbar");
        } catch (Exception e) {
            log.debug("irgendwas ist schiefgelaufen: " + e.getMessage());
            return Ergebnis.fehler("Unbekannter Fehler");
        }

        log.info("done");
        log.debug("DEBUG betrag=" + auftrag.betrag() + " ziel=" + auftrag.zielIban()
                + " verwendungszweck=" + auftrag.verwendungszweck());
        return Ergebnis.ok(buchungsId);
    }

    public void tagesabschluss() {
        log.info("========== TAGESABSCHLUSS ==========");
        int anzahl = kontoRepository.zaehleOffeneBuchungen();
        if (anzahl > 0) {
            log.error(anzahl + " offene Buchungen");
        } else {
            log.error("keine offenen Buchungen");
        }
        log.info("========== ENDE ==========");
    }
}
