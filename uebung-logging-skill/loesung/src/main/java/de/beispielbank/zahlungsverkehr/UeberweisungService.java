package de.beispielbank.zahlungsverkehr;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Führt Überweisungen aus: Limit prüfen, Betrag buchen, Auftrag an das Kernbanksystem übergeben.
 *
 * Referenzergebnis nach Anwendung des Skills logging-vorgaben.
 * Fachliche Logik gegenüber der Ausgangsdatei unverändert.
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
        log.info("Ueberweisung gestartet quellIban={}", maskiere(auftrag.quellIban()));

        Konto quelle = kontoRepository.findeNachIban(auftrag.quellIban());
        if (quelle == null) {
            log.warn("Ueberweisung abgelehnt grund=QUELLKONTO_UNBEKANNT quellIban={} ergebnis=ABGELEHNT",
                    maskiere(auftrag.quellIban()));
            return Ergebnis.abgelehnt("Quellkonto unbekannt");
        }

        if (quelle.tagesLimit().compareTo(auftrag.betrag()) < 0) {
            log.warn("Ueberweisung abgelehnt grund=TAGESLIMIT quellIban={} ergebnis=ABGELEHNT",
                    maskiere(auftrag.quellIban()));
            return Ergebnis.abgelehnt("Tageslimit überschritten");
        }

        String buchungsId = UUID.randomUUID().toString();
        log.debug("Buchung angelegt buchungsId={}", buchungsId);
        try {
            kontoRepository.buche(quelle, auftrag.betrag().negate(), buchungsId);
            kernbankClient.uebergebe(auftrag, buchungsId);
        } catch (KernbankException e) {
            log.error("Kernbank nicht erreichbar, Buchung wird storniert buchungsId={} ergebnis=FEHLER",
                    buchungsId, e);
            kontoRepository.storniere(buchungsId);
            return Ergebnis.fehler("Kernbanksystem nicht erreichbar");
        } catch (Exception e) {
            log.error("Unerwarteter Fehler bei Ueberweisung buchungsId={} ergebnis=FEHLER", buchungsId, e);
            return Ergebnis.fehler("Unbekannter Fehler");
        }

        log.info("Ueberweisung abgeschlossen buchungsId={} ergebnis=OK", buchungsId);
        return Ergebnis.ok(buchungsId);
    }

    public void tagesabschluss() {
        log.info("Tagesabschluss gestartet");
        int anzahl = kontoRepository.zaehleOffeneBuchungen();
        if (anzahl > 0) {
            log.warn("Tagesabschluss mit offenen Buchungen offeneBuchungen={}", anzahl);
        } else {
            log.info("Tagesabschluss abgeschlossen offeneBuchungen=0 ergebnis=OK");
        }
    }

    /** Maskiert eine IBAN auf Ländercode + Prüfziffer und die letzten vier Stellen. */
    private static String maskiere(String iban) {
        if (iban == null || iban.length() < 8) {
            return "****";
        }
        return iban.substring(0, 4) + "****" + iban.substring(iban.length() - 4);
    }
}
