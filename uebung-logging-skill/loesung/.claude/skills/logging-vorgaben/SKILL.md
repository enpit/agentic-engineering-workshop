---
name: logging-vorgaben
description: Wendet die Logging-Vorgaben des Teams auf Java-Code an. Verwenden, wenn Logging geschrieben, überarbeitet oder ergänzt wird – auch wenn nur "Logging verbessern" oder "Logs aufräumen" gesagt wird.
---

# Logging-Vorgaben

## Wann dieser Skill greift

Immer wenn in Java-Code Log-Ausgaben geschrieben oder geändert werden.

## Vorgaben

### 1. Format
- Ausschließlich SLF4J (`org.slf4j.Logger`). Kein `System.out`, kein `System.err`, kein `printStackTrace()`.
- Platzhalter `{}` statt String-Verkettung: `log.info("Buchung ausgeführt buchungsId={}", id)`.
- Nachricht = kurzer Ereignisname im Präsens, danach `schluessel=wert`-Paare. Keine Satzzeichen, keine Dekoration (`=====`, `!!!`).
- Sprache: Deutsch, Schlüssel in camelCase.

### 2. Pflichtfelder
Jeder Eintrag auf INFO oder höher enthält:
- `buchungsId` bzw. `auftragId`, sobald eine vergeben ist (Wiederfindbarkeit im Störungsfall)
- `ergebnis=` bei Abschluss eines fachlichen Vorgangs (`OK`, `ABGELEHNT`, `FEHLER`)
- bei Ablehnungen: `grund=` mit fachlichem Code, nicht mit Freitext

### 3. Log-Level
- **ERROR**: Vorgang abgebrochen, Eingriff durch Betrieb nötig (z. B. Kernbanksystem nicht erreichbar). Immer mit Exception.
- **WARN**: Vorgang fachlich abgelehnt oder mit Rückfall fortgesetzt, kein Eingriff nötig (z. B. Limit überschritten).
- **INFO**: Start und Ende eines fachlichen Vorgangs, jeweils genau ein Eintrag.
- **DEBUG**: technische Zwischenschritte. In Produktion aus, daher keine fachlich relevanten Ergebnisse nur auf DEBUG.

### 4. Was niemals geloggt wird
- Personenbezogene Daten: Kundenname, Anschrift, Geburtsdatum
- IBAN und Kontonummern im Klartext – erlaubt ist nur die maskierte Form `DE12****4711` (Ländercode + Prüfziffer, letzte 4 Stellen)
- Beträge nie im selben Eintrag wie eine IBAN, ein Konto oder eine Kunden-ID – auch nicht maskiert
- Verwendungszweck (Freitext, kann alles enthalten)
- Session-Token, Passwörter, API-Keys, Authorization-Header
- Ganze Domänenobjekte per `toString()` (enthalten zwangsläufig Obiges)

### 5. Fehlerbehandlung
- Exception als letztes Argument übergeben, damit der Stacktrace mitgeloggt wird: `log.error("... buchungsId={}", id, e)`.
- Jede gefangene Exception wird genau einmal geloggt – nicht zusätzlich per `printStackTrace()`, nicht auf mehreren Ebenen.
- `catch (Exception e)` ohne Weiterwerfen: mindestens ERROR mit Exception, nie DEBUG.

## Beispiele

### Gut
```java
log.info("Ueberweisung gestartet quellIban={}", maskiere(auftrag.quellIban()));
log.warn("Ueberweisung abgelehnt grund=TAGESLIMIT quellIban={}", maskiere(auftrag.quellIban()));
log.error("Kernbank nicht erreichbar buchungsId={} ergebnis=FEHLER", buchungsId, e);
log.info("Ueberweisung abgeschlossen buchungsId={} ergebnis=OK", buchungsId);
```

### Schlecht – und warum
```java
// Kundenname, IBAN im Klartext, Betrag mit Kundenbezug, String-Verkettung, falsches Level (WARN wäre richtig, aber der Inhalt darf so nicht raus)
log.warn("Limit überschritten! Kunde " + name + " (IBAN " + iban + ") wollte " + betrag + " EUR überweisen");

// Stacktrace geht an stdout statt ins Log, Fehler wird zusätzlich als INFO ohne Exception geloggt
e.printStackTrace();
log.info("Fehler bei Kernbank");

// ERROR für Normalfall – Alarmierung feuert, obwohl nichts passiert ist
log.error("keine offenen Buchungen");
```

## Arbeitsschritte für den Agenten

1. Alle Log-Aufrufe und `System.out`/`System.err`/`printStackTrace`-Ausgaben in den betroffenen Dateien finden.
2. Jeden Aufruf gegen die Vorgaben 1–5 prüfen.
3. Verstöße beheben. Fachliche Logik, Signaturen und Rückgabewerte nicht ändern. Fehlt eine Maskierungsfunktion, eine private Methode `maskiere(String iban)` in der Klasse ergänzen.
4. Am Ende eine Tabelle ausgeben: `Zeile | Verstoß (Vorgabe Nr.) | Änderung`.
5. Wenn eine Vorgabe im konkreten Fall nicht anwendbar ist, das benennen statt zu raten.
