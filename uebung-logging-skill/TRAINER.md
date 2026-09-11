# Übungskonzept „Vorgaben übergeben“ (Folie 46/47) – Trainerunterlage

## Didaktische Idee

Die Übung macht die Kernaussage von Folie 46 erfahrbar: *Schritt 1 (Festlegen) ist der eigentliche Aufwand – der Skill ist nur das Transportmittel.*

Dafür braucht es einen sichtbaren Kontrast. Der entsteht durch einen **Vergleichslauf ohne Skill** (Schritt 0) und einen **Lauf mit dem selbst geschriebenen Skill** (Schritt 2) auf derselben Datei. Ohne Skill räumt der Agent kosmetisch auf (String-Verkettung, `System.out`, Level). Mit Skill entfernt er zusätzlich Kundendaten, IBANs, Tokens und ergänzt Korrelations-IDs – lauter Dinge, die er ohne Vorgabe nicht wissen *kann*, weil sie hausspezifische Entscheidungen sind.

Die Zielgruppe (Anwendungsmanagement, 2nd-Level, Compliance) muss dafür kein Java schreiben. Ihre Aufgabe ist die fachliche: entscheiden, was gelten soll, und es prüfbar formulieren. Das ist genau ihre Rolle gegenüber den umsetzenden Teams (Fokusthema A).

Logging wurde als Bereich gewählt, weil jedes Team es hat, die Regeln meist halb dokumentiert sind und Verstöße für Compliance-nahe Teilnehmende sofort erkennbar sind (Bankgeheimnis, DSGVO, Secrets).

## Ablauf (20–25 Min.)

| Zeit | Schritt | Wer | Hinweise |
|---|---|---|---|
| 2 Min. | 0 Vergleichslauf ohne Skill | Trainer, Beamer | Prompt: „Verbessere das Logging in UeberweisungService.java.“ Ergebnis stehen lassen, nicht kommentieren. |
| 10 Min. | 1 Vorgaben festlegen | Gruppen | Rohfassung ausfüllen. Trainer geht rum und fragt bei jeder unscharfen Formulierung: „Woran erkennt der Agent, ob das erfüllt ist?“ |
| 5 Min. | 2 Skill anwenden | Gruppen | Datei vorher zurücksetzen. Falls eine Gruppe nicht fertig wird: Lösungs-Skill aus `loesung/` kopieren, damit trotzdem alle den Effekt sehen. |
| 3–5 Min. | 3 Auswertung | Plenum | Drei Fragen aus dem Handout. Brücke zu Folie 47 (Review-Variante) und Folie 51 (Pflege). |

## Erwartete Beobachtungen

- Ohne Skill: erwartungsgemäß nur die technischen Verstöße (siehe `loesung/ERWARTETES-ERGEBNIS.md`; vor dem Workshop mit dem eingesetzten Modell einmal probelaufen, Modellverhalten variiert). Datenschutzverstöße bleiben meist stehen, weil der Agent nicht weiß, dass Kundenname und IBAN nicht ins Log dürfen – aus seiner Sicht sind das nützliche Informationen.
- Mit Skill: Deckungsgrad hängt direkt an der Schärfe der Formulierung. Typischer Aha-Moment: Eine Gruppe schreibt „keine sensiblen Daten“ – der Agent lässt IBAN stehen, weil unklar ist, ob IBAN „sensibel“ ist. Die Nachbargruppe schreibt „IBAN nur maskiert, letzte 4 Stellen“ – funktioniert.
- Häufiger Fehler: Gruppen schreiben Regeltext, aber keine Beispiele. Darauf hinweisen (Speaker Note Folie 47: „Ein Gegenbeispiel wirkt stärker als drei Absätze Regeltext“).

## Inhalt des Pakets

```
uebung-logging-skill/
├── README.md                    diese Datei
├── AUFGABE.md                   Teilnehmer-Handout
├── beispielprojekt/             an die Gruppen verteilen
│   ├── CLAUDE.md
│   ├── pom.xml                  nur damit es kompiliert; wird in der Übung nicht gebaut
│   ├── .claude/skills/logging-vorgaben/SKILL.md     Rohfassung mit TODOs
│   └── src/.../UeberweisungService.java             Ausgangsdatei mit 14 Verstößen
│       └── Modell.java                              Hilfstypen, nicht Gegenstand der Übung
└── loesung/                     nur Trainer; nach der Übung freigeben
    ├── ERWARTETES-ERGEBNIS.md   Verstoßliste mit Zeilennummern und Zuordnung zu Vorgaben
    ├── .claude/skills/logging-vorgaben/SKILL.md     ausgearbeiteter Skill
    ├── .claude/skills/logging-review/SKILL.md       Review-Variante (Folie 47, Schritt 3)
    └── src/.../UeberweisungService.java             Referenzergebnis
```

## Technische Vorbereitung

- Pro Gruppe ein Laptop mit Claude Code und Zugang zum Modell. `beispielprojekt/` als Git-Repository initialisieren (`git init && git add . && git commit -m init`), damit Gruppen die Datei mit `git checkout -- .` zurücksetzen können.
- Skill-Ablage und Aufruf: Projekt-Skills liegen unter `.claude/skills/<name>/SKILL.md` und werden per `/<name>` aufgerufen; der Name ergibt sich aus dem Ordnernamen. Quelle: https://code.claude.com/docs/en/skills
- Fallback bei Technikproblemen: Schritt 0 und Schritt 2 vorab aufzeichnen (Speaker Note Folie 42).
- Der Beispielcode ist fiktiv (Bank „Beispielbank“, keine echten Daten). Er darf ohne Bedenken an das Modell übergeben werden – das ist Weg 3 aus Folie 39 (Fehlerbild nachbauen statt Produktionsdaten übergeben) und kann als Nebenbemerkung genutzt werden.

## Varianten

- **Kürzer (12–15 Min.):** Schritt 1 auf die Abschnitte 3 (Level) und 4 (niemals loggen) beschränken, Rest aus der Lösung vorgeben.
- **Länger (30–40 Min.):** Bonusteil einbauen. Reihenfolge bleibt: erst der eigene Implementierungs-Skill (Schritt 2), danach der Review-Skill aus `loesung/` – erst laufen lassen, dann verbessern. Die Schweregrade und das Ausgabeformat dort sind bewusst diskutabel; das ist der Aufhänger. Abschlussfrage: Finden Implementierungs- und Review-Sicht dieselben Stellen?
- **Näher am Team:** Statt Logging einen Bereich aus Folie 46 nehmen, den das Team wirklich verantwortet (Fehlerbehandlung, Security-Header). Struktur der Rohfassung bleibt gleich; nur Beispieldatei tauschen.