# Übungskonzept „Vorgaben übergeben“ (Folie 46/47) – Trainerunterlage

## Didaktische Idee

Die Übung macht die Kernaussage von Folie 46 erfahrbar: *Schritt 1 (Festlegen) ist der eigentliche Aufwand – der Skill ist nur das Transportmittel.*

Dafür braucht es einen sichtbaren Kontrast. Der entsteht durch einen **Vergleichslauf ohne Skill** (Schritt 0) und einen **Lauf mit dem selbst geschriebenen Skill** (Schritt 2) auf derselben Datei.

**Wichtig für die Moderation:** Der Kontrast liegt *nicht* darin, dass der Agent ohne Vorgabe die Datenschutzverstöße übersieht. Aktuelle Modelle entfernen Kundenname, Session-Token und Verwendungszweck auch unaufgefordert und maskieren IBANs von sich aus. Der Kontrast liegt darin, dass der Agent die **hausspezifischen Entscheidungen selbst trifft** – Maskierungsformat, Log-Level für fachliche Ablehnungen, Ablehnungscodes, Pflichtfelder – und sie in seiner Zusammenfassung so souverän benennt, dass sie wie eine Vorgabe klingen. Zwei Läufe mit demselben Prompt kommen dabei zu unterschiedlichen Ergebnissen. Wer keine Vorgabe macht, bekommt trotzdem eine – nur nicht die eigene, und jedes Mal eine andere.

Die Zielgruppe (Anwendungsmanagement, 2nd-Level, Compliance) muss dafür kein Java schreiben. Ihre Aufgabe ist die fachliche: entscheiden, was gelten soll, und es prüfbar formulieren. Das ist genau ihre Rolle gegenüber den umsetzenden Teams (Fokusthema A).

Logging wurde als Bereich gewählt, weil jedes Team es hat, die Regeln meist halb dokumentiert sind und Verstöße für Compliance-nahe Teilnehmende sofort erkennbar sind (Bankgeheimnis, DSGVO, Secrets).

## Ablauf (20–25 Min.)

| Zeit | Schritt | Wer | Hinweise |
|---|---|---|---|
| 3 Min. | 0 Vergleichslauf ohne Skill | Trainer, Beamer | Es liegt noch kein Skill im Projekt, der Lauf ist damit automatisch vorgabenfrei. Prompt: „Verbessere das Logging in UeberweisungService.java.“ Zweimal laufen lassen, Datei dazwischen zurücksetzen. Die **Zusammenfassungen** zeigen, nicht die Diffs. |
| 10 Min. | 1 Vorgaben festlegen | Gruppen | Zwei Wege: Template `logging-vorgaben-skill-template.md` nach `.claude/skills/logging-vorgaben/SKILL.md` verschieben und ausfüllen, oder die Datei selbst anlegen (gern mit Hilfe des Agenten). Trainer geht rum und fragt bei jeder unscharfen Formulierung: „Woran erkennt der Agent, ob das erfüllt ist?“ |
| 5 Min. | 2 Skill anwenden | Gruppen | Datei vorher zurücksetzen. Falls eine Gruppe nicht fertig wird: Lösungs-Skill aus `loesung/` kopieren, damit trotzdem alle den Effekt sehen. |
| 3–5 Min. | 3 Auswertung | Plenum | Drei Fragen aus dem Handout. Brücke zu Folie 47 (Review-Variante) und Folie 51 (Pflege). |

## Erwartete Beobachtungen

- **Ohne Skill** (mit Opus 5 nachgestellt, Modellverhalten variiert – vor dem Workshop einmal probelaufen): Der Agent behebt die technischen Punkte **und** die Datenschutzpunkte. Er entfernt Session-Token und Kundenname, streicht den Verwendungszweck und schreibt sich eine `maskiere()`-Hilfsmethode. Von den 14 Punkten bleiben verlässlich nur die Pflichtfelder offen (10, 11, 12), Punkt 4 nur teilweise (der Betrag bleibt meist stehen).
- **Die beiden Läufe aus Schritt 0 widersprechen sich.** Im Test stufte der eine Lauf „Tageslimit überschritten“ als WARN ein, der andere als INFO mit der Begründung, das sei ein erwarteter Geschäftsfall. Der eine löschte den ungenutzten `BigDecimal`-Import, der andere ließ ihn bewusst stehen. Das ist der stärkste Moment der Übung – darauf hinarbeiten.
- **Mit Skill**: Der Deckungsgrad bei den Pflichtfeldern hängt direkt an der Schärfe der Formulierung. Typischer Aha-Moment: Eine Gruppe schreibt „IBAN maskieren“ – der Agent liefert `****4711`. Die Nachbargruppe schreibt „Ländercode + Prüfziffer + letzte 4 Stellen“ – und bekommt `DE12****4711`. Beides ist maskiert, nur eines ist die Hausregel.
- **Häufiger Fehler:** Gruppen schreiben Regeltext, aber keine Beispiele. Darauf hinweisen (Speaker Note Folie 47: „Ein Gegenbeispiel wirkt stärker als drei Absätze Regeltext“).
- **Zum Hinweis „fragt den Agenten“ in Schritt 1:** Gruppen, die sich Vorschläge geben lassen, sind schneller fertig und haben oft die besseren Formulierungen. Beim Rundgang trotzdem fragen: „Warum gilt das bei euch so?“ Wer den Vorschlag nur übernommen hat, merkt an dieser Frage selbst, dass Schritt 1 noch nicht erledigt ist.

## Inhalt des Pakets

```
uebung-logging-skill/
├── README.md                    Teilnehmer-Handout
├── TRAINER.md                   diese Datei
├── beispielprojekt/             an die Gruppen verteilen
│   ├── CLAUDE.md
│   ├── pom.xml                  nur damit es kompiliert; wird in der Übung nicht gebaut
│   ├── .claude/skills/logging-vorgaben/             leer; hier entsteht der Skill der Gruppe
│   ├── logging-vorgaben-skill-template.md          Rohfassung mit TODOs, optionale Starthilfe
│   └── src/.../UeberweisungService.java             Ausgangsdatei mit 14 Verstößen
│       └── Modell.java                              Hilfstypen, nicht Gegenstand der Übung
└── loesung/                     nur Trainer; nach der Übung freigeben
    ├── ERWARTETES-ERGEBNIS.md   Verstoßliste mit Zeilennummern und Zuordnung zu Vorgaben
    ├── .claude/skills/logging-vorgaben/SKILL.md     ausgearbeiteter Skill
    ├── .claude/skills/logging-review/SKILL.md       Review-Variante (Folie 47, Schritt 3)
    └── src/.../UeberweisungService.java             Referenzergebnis
```

## Technische Vorbereitung

- Pro Gruppe ein Laptop mit Claude Code und Zugang zum Modell. `beispielprojekt/` als Git-Repository initialisieren (`git init && git add . && git commit -m init`), damit Gruppen die Ausgangsdatei mit `git checkout -- src/` zurücksetzen können. Ohne diesen Schritt schlägt das Zurücksetzen in Schritt 0 und 2 fehl – im Zip-Paket ist kein `.git` enthalten. Bewusst nur `src/` und nicht `.`: Sonst holt der Befehl das verschobene Template zurück.
- Skill-Ablage und Aufruf: Projekt-Skills liegen unter `.claude/skills/<name>/SKILL.md` und werden per `/<name>` aufgerufen; der Name ergibt sich aus dem Ordnernamen. Neu angelegte Skills greifen sofort, ohne Neustart. Quelle: https://code.claude.com/docs/en/skills
- Das Template liegt bewusst **außerhalb** von `.claude/skills/`. Läge es drin, würde der Agent es in Schritt 0 von selbst heranziehen – seine `description` passt auf den Prompt – und mit einer Vorgabe arbeiten, die nur aus `TODO` besteht. Der Vergleichslauf wäre damit wertlos.
- Gruppen, die Weg B wählen (Skill selbst anlegen), brauchen den Hinweis auf den Frontmatter-Kopf: ohne `name:` und `description:` taucht der Skill nicht als `/`-Befehl auf. Steht im Handout, wird aber gern überlesen.
- Fallback bei Technikproblemen: Schritt 0 und Schritt 2 vorab aufzeichnen (Speaker Note Folie 42).
- Der Beispielcode ist fiktiv (Bank „Beispielbank“, keine echten Daten). Er darf ohne Bedenken an das Modell übergeben werden – das ist Weg 3 aus Folie 39 (Fehlerbild nachbauen statt Produktionsdaten übergeben) und kann als Nebenbemerkung genutzt werden.

## Varianten

- **Kürzer (12–15 Min.):** Alle Gruppen auf Weg A festlegen (Template verschieben) und Schritt 1 auf die Abschnitte 3 (Level) und 4 (niemals loggen) beschränken, Rest aus der Lösung vorgeben.
- **Länger (30–40 Min.):** Bonusteil einbauen. Reihenfolge bleibt: erst der eigene Implementierungs-Skill (Schritt 2), danach der Review-Skill aus `loesung/` – erst laufen lassen, dann verbessern. Die Schweregrade und das Ausgabeformat dort sind bewusst diskutabel; das ist der Aufhänger. Abschlussfrage: Finden Implementierungs- und Review-Sicht dieselben Stellen?
- **Näher am Team:** Statt Logging einen Bereich aus Folie 46 nehmen, den das Team wirklich verantwortet (Fehlerbehandlung, Security-Header). Struktur der Rohfassung bleibt gleich; nur Beispieldatei tauschen.