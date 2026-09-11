---
name: logging-review
description: Prüft Java-Code oder einen Diff auf Verstöße gegen die Logging-Vorgaben des Teams und erzeugt Review-Kommentare. Verwenden bei Code-Review, Merge-Request-Prüfung oder der Frage "ist das Logging hier in Ordnung?". Ändert keinen Code.
---

# Logging-Review

Review-Variante der Logging-Vorgaben. Dieser Skill **ändert keinen Code**, er beanstandet.

## Prüfgrundlage

Die Vorgaben 1–5 aus `.claude/skills/logging-vorgaben/SKILL.md`. Diese Datei zuerst lesen.

## Vorgehen

1. Alle Log-Aufrufe, `System.out`/`System.err` und `printStackTrace()` im übergebenen Code oder Diff auflisten.
2. Jeden Aufruf gegen die Vorgaben prüfen.
3. Je Verstoß einen Review-Kommentar erzeugen. Kein Kommentar ohne Verstoß.

## Format je Kommentar

```
Datei:Zeile – [BLOCKER|MAJOR|MINOR] Vorgabe <Nr.> <Kurzname>
Befund: <was steht da>
Begründung: <warum das ein Problem ist, ein Satz>
Vorschlag: <korrigierte Zeile>
```

## Schweregrade

- **BLOCKER**: Verstoß gegen Vorgabe 4 (Daten, die niemals geloggt werden). Merge nicht erlaubt.
- **MAJOR**: Falsches Level auf ERROR/WARN (Vorgabe 3), verschluckte Exception (Vorgabe 5), fehlende Pflichtfelder (Vorgabe 2).
- **MINOR**: Format (Vorgabe 1), Level INFO/DEBUG vertauscht.

## Abschluss

Zusammenfassung: Anzahl je Schweregrad und ein Satz Empfehlung (`Merge blockiert` / `Merge nach Korrektur der MAJOR-Punkte` / `Merge möglich`).
