package de.beispielbank.zahlungsverkehr;

import java.math.BigDecimal;

/*
 * Hilfstypen, damit UeberweisungService kompiliert. Nicht Gegenstand der Übung.
 */

record Ueberweisung(String kundenName, String quellIban, String zielIban, BigDecimal betrag, String verwendungszweck) {
}

record Konto(String iban, BigDecimal tagesLimit) {
}

record Ergebnis(Status status, String buchungsId, String meldung) {
    enum Status { OK, ABGELEHNT, FEHLER }

    static Ergebnis ok(String buchungsId) { return new Ergebnis(Status.OK, buchungsId, null); }
    static Ergebnis abgelehnt(String meldung) { return new Ergebnis(Status.ABGELEHNT, null, meldung); }
    static Ergebnis fehler(String meldung) { return new Ergebnis(Status.FEHLER, null, meldung); }
}

class KernbankException extends RuntimeException {
    KernbankException(String message) { super(message); }
}

interface KontoRepository {
    Konto findeNachIban(String iban);
    void buche(Konto konto, BigDecimal betrag, String buchungsId);
    void storniere(String buchungsId);
    int zaehleOffeneBuchungen();
}

interface KernbankClient {
    void uebergebe(Ueberweisung auftrag, String buchungsId);
}
