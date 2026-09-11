# Praxisaufgabe: Logging-Vorgaben als Skill übergeben

**Ziel:** Eine Vorgabe so festhalten, dass ein Agent sie anwenden kann – und sehen, was das gegenüber einem „unbegleiteten“ Agenten ausmacht.
**Zeit:** 20 Minuten. **Gruppen:** 3–4 Personen, mindestens ein Laptop mit Claude Code pro Gruppe.

## Ausgangslage

Im Ordner `beispielprojekt/` liegt ein kleiner Java-Service `UeberweisungService.java`. Fachlich funktioniert er. Das Logging ist so, wie es in vielen Systemen aussieht: historisch gewachsen, von mehreren Personen, ohne Vorgabe.

Im Ordner `.claude/skills/logging-vorgaben/` liegt eine **Rohfassung** eines Skills. Die Struktur steht, der Inhalt fehlt (`TODO`).

## Schritt 0 – Vergleichslauf ohne Skill (2 Min., Trainer führt vor)

Claude Code im Ordner `beispielprojekt/` starten und eingeben:

```
Verbessere das Logging in UeberweisungService.java.
```

Ergebnis kurz anschauen. Merkt euch: Was hat der Agent geändert, was hat er gelassen?

## Schritt 1 – Vorgaben festlegen (10 Min., Gruppenarbeit)

Öffnet `.claude/skills/logging-vorgaben/SKILL.md` und füllt die fünf `TODO`-Abschnitte. Ihr braucht dafür **kein Java**: Ihr entscheidet, was gelten soll, der Agent setzt es um.

Leitfragen:

- **Format:** Freitext oder Schlüssel=Wert? Darf `System.out` vorkommen?
- **Pflichtfelder:** Wenn nachts eine Störung ist – welches Feld muss in jedem Eintrag stehen, damit ihr den Vorgang wiederfindet?
- **Log-Level:** Wann darf ERROR stehen? (Denkt an die Alarmierung, die daran hängt.)
- **Niemals loggen:** Was darf nie in einem Log stehen, das an ein Monitoring-System oder einen Dienstleister geht? Kundendaten? IBAN? Beträge? Tokens?
- **Fehler:** Wo soll der Stacktrace landen?

Und: **je ein gutes und ein schlechtes Beispiel**. Das schlechte Beispiel dürft ihr gern aus `UeberweisungService.java` abschreiben.

Regel: Jede Vorgabe muss an einer Codezeile entscheidbar sein. „Sinnvoll loggen“ ist keine Vorgabe. „Keine IBAN im Klartext, nur die letzten 4 Stellen“ schon.

## Schritt 2 – Skill anwenden (5 Min.)

Ausgangsdatei zurücksetzen (`git checkout -- .` im Ordner `beispielprojekt/`), dann in Claude Code:

```
/logging-vorgaben Überarbeite UeberweisungService.java.
```

Vergleicht mit Schritt 0:

- Welche Stellen wurden jetzt zusätzlich geändert?
- Hat der Agent etwas gemacht, das ihr nicht wolltet? Dann fehlt eine Vorgabe oder sie ist unscharf.
- Steht in der Abschlusstabelle des Agenten etwas, das ihr nicht nachvollziehen könnt?

## Schritt 3 – Auswertung (3 Min., Plenum)

Eine Gruppe zeigt ihren Skill und das Ergebnis. Fragen an alle:

1. Was hat der Agent **ohne** Skill nicht gefunden – und warum konnte er das nicht wissen?
2. Wo waren eure Formulierungen so unscharf, dass der Agent geraten hat?
3. Was war der aufwendigste Teil: der Skill schreiben, oder sich einigen, was gelten soll?

## Optional (wenn Zeit bleibt)

Im Ordner `loesung/.claude/skills/logging-review/` liegt eine Review-Variante des Skills. Kopiert sie ins Beispielprojekt und lasst sie auf die **unveränderte** Ausgangsdatei laufen:

```
/logging-review Prüfe UeberweisungService.java.
```

Ergebnis: Review-Kommentare mit Schweregrad statt Codeänderungen – dieselbe Vorgabe, zweiter Einsatzort (Folie 47, Schritt 3).
