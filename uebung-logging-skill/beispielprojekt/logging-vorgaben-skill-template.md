---
name: logging-vorgaben
description: Wendet die Logging-Vorgaben des Teams auf Java-Code an. Verwenden, wenn Logging geschrieben, überarbeitet oder ergänzt wird.
---

# Logging-Vorgaben

<!--
ROHFASSUNG – bitte ausfüllen.
Jede Vorgabe muss so formuliert sein, dass man an einer Codezeile
entscheiden kann: erfüllt oder nicht erfüllt.
"Sauber loggen" ist keine Vorgabe. "Jeder Eintrag enthält das Feld X" schon.
-->

## Wann dieser Skill greift

Immer wenn in Java-Code Log-Ausgaben geschrieben oder geändert werden.

## Vorgaben

### 1. Format
<!-- Freitext oder strukturiert (JSON)? Welche Bibliothek/Fassade? Platzhalter oder String-Verkettung? -->
TODO

### 2. Pflichtfelder
<!-- Was muss in JEDEM Eintrag stehen, damit man ihn im Störungsfall wiederfindet? -->
TODO

### 3. Log-Level
<!-- Wann ERROR, WARN, INFO, DEBUG? Je Level ein Satz. -->
TODO

### 4. Was niemals geloggt wird
<!-- Denkt an Datenschutz, Bankgeheimnis, Secrets. -->
TODO

### 5. Fehlerbehandlung
<!-- Wie wird eine Exception geloggt? Was passiert mit dem Stacktrace? -->
TODO

## Beispiele

### Gut
```java
TODO
```

### Schlecht – und warum
```java
TODO
```

## Arbeitsschritte für den Agenten

1. Alle Log-Aufrufe und `System.out`/`System.err`-Ausgaben in den betroffenen Dateien finden.
2. Jeden Aufruf gegen die Vorgaben 1–5 prüfen.
3. Verstöße beheben, fachliche Logik nicht ändern.
4. Am Ende eine Tabelle ausgeben: Zeile | Verstoß | Änderung.
