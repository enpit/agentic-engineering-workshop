# Praxisaufgabe: Logging-Vorgaben als Skill übergeben

**Ziel:** Eine Vorgabe so festhalten, dass ein Agent sie anwenden kann – und sehen, was das gegenüber einem „unbegleiteten“ Agenten ausmacht.
**Zeit:** 20 Minuten. **Gruppen:** 3–4 Personen, mindestens ein Laptop mit Claude Code pro Gruppe.

## Ausgangslage

Im Ordner `beispielprojekt/` liegt ein kleiner Java-Service `UeberweisungService.java`. Fachlich funktioniert er. Das Logging ist so, wie es in vielen Systemen aussieht: historisch gewachsen, von mehreren Personen, ohne Vorgabe.

Der Ordner `.claude/skills/logging-vorgaben/` ist leer – dort soll euer Skill entstehen. Als Starthilfe liegt im Projektverzeichnis die Datei `logging-vorgaben-skill-template.md`: eine Rohfassung, bei der die Struktur steht und der Inhalt fehlt (`TODO`). Ob ihr sie benutzt, entscheidet ihr in Schritt 1.

## Schritt 0 – Vergleichslauf ohne Skill (3 Min., Trainer führt vor)

Es liegt noch kein Skill im Projekt – der Agent arbeitet also ohne jede Vorgabe.

Claude Code im Ordner `beispielprojekt/` starten und eingeben:

```
Verbessere das Logging in UeberweisungService.java.
```

Dann die Datei zurücksetzen (`git checkout -- src/`) und **denselben Prompt ein zweites Mal** laufen lassen.

Nicht auf den Diff schauen, sondern auf die **Zusammenfassung** des Agenten: Er benennt darin, welche Log-Level er gewählt hat, wie er IBANs maskiert und was er für schützenswert hält. Vergleicht die beiden Läufe miteinander.

Merkt euch: Der Agent räumt viel auf – auch Kundenname und Token verschwinden meist. Aber er trifft dabei Entscheidungen, nach denen ihn niemand gefragt hat. Und die beiden Läufe treffen sie nicht zwangsläufig gleich.

## Schritt 1 – Vorgaben festlegen (10 Min., Gruppenarbeit)

Euer Skill muss am Ende hier liegen:

```
.claude/skills/logging-vorgaben/SKILL.md
```

Der Ordnername bestimmt den Aufruf – aus `logging-vorgaben` wird `/logging-vorgaben`. Die Datei beginnt mit einem Kopf aus `name:` und `description:` zwischen zwei `---`-Zeilen; die `description` entscheidet, ob der Agent den Skill auch von selbst heranzieht. Neu angelegte Skills sind sofort verfügbar, ein Neustart von Claude Code ist nicht nötig.

Wie ihr dorthin kommt, ist euch überlassen:

**Weg A – Rohfassung ausfüllen.** Verschiebt das Template an die richtige Stelle und füllt es aus:

```
mv logging-vorgaben-skill-template.md .claude/skills/logging-vorgaben/SKILL.md
```

Fünf `TODO`-Abschnitte, Kopf und Struktur sind schon da.

**Weg B – selbst schreiben.** Legt `SKILL.md` direkt an, mit eurer eigenen Gliederung. Ihr dürft euch dabei vom Agenten helfen lassen, etwa:

```
Lege .claude/skills/logging-vorgaben/SKILL.md an. Gliederung: Format, Pflichtfelder,
Log-Level, was niemals geloggt wird, Fehlerbehandlung. Lass die Regeln leer -
die fülle ich selbst aus.
```

Ihr braucht für beide Wege **kein Java**: Ihr entscheidet, was gelten soll, der Agent setzt es um.

Leitfragen für die Abschnitte:

- **Format:** Freitext oder Schlüssel=Wert? Darf `System.out` vorkommen?
- **Pflichtfelder:** Wenn nachts eine Störung ist – welches Feld muss in jedem Eintrag stehen, damit ihr den Vorgang wiederfindet?
- **Log-Level:** Wann darf ERROR stehen? (Denkt an die Alarmierung, die daran hängt.)
- **Niemals loggen:** Was darf nie in einem Log stehen, das an ein Monitoring-System oder einen Dienstleister geht? Kundendaten? IBAN? Beträge? Tokens?
- **Fehler:** Wo soll der Stacktrace landen?

Und: **je ein gutes und ein schlechtes Beispiel**. Das schlechte Beispiel dürft ihr gern aus `UeberweisungService.java` abschreiben.

Regel: Jede Vorgabe muss an einer Codezeile entscheidbar sein. „Sinnvoll loggen“ ist keine Vorgabe. „Keine IBAN im Klartext, nur die letzten 4 Stellen“ schon.

**Ihr dürft den Agenten fragen.** Wenn ihr bei einem Abschnitt unsicher seid, lasst euch Vorschläge geben, etwa:

```
Welche Log-Level-Regeln sind für einen Zahlungsverkehr-Service üblich? Nenne je Level einen Satz.
Welche Felder gehören in jeden Log-Eintrag, damit ein 2nd-Level-Team einen Vorgang nachts wiederfindet?
```

Der Agent kennt die gängige Praxis; er kennt nur **eure** Praxis nicht. Nehmt die Vorschläge als Entwurf und entscheidet dann bewusst, was davon bei euch gilt – genau diese Entscheidung ist der Inhalt der Übung. Übernehmt nichts ungeprüft: Was ihr nicht erklären könnt, könnt ihr später auch nicht gegenüber einem Team vertreten.

## Schritt 2 – Skill anwenden (5 Min.)

Ausgangsdatei zurücksetzen (`git checkout -- src/` im Ordner `beispielprojekt/` – nur `src/`, damit euer Skill und das verschobene Template unangetastet bleiben), dann in Claude Code:

```
/logging-vorgaben Überarbeite UeberweisungService.java.
```

Vergleicht mit Schritt 0:

- Welche Stellen wurden jetzt zusätzlich geändert?
- Hat der Agent etwas gemacht, das ihr nicht wolltet? Dann fehlt eine Vorgabe oder sie ist unscharf.
- Steht in der Abschlusstabelle des Agenten etwas, das ihr nicht nachvollziehen könnt?

## Schritt 3 – Auswertung (3 Min., Plenum)

Eine Gruppe zeigt ihren Skill und das Ergebnis. Fragen an alle:

1. Welche Entscheidungen hat der Agent in Schritt 0 für euch getroffen, ohne zu fragen – und hättet ihr sie genauso getroffen?
2. Wo waren eure Formulierungen so unscharf, dass der Agent geraten hat?
3. Was war der aufwendigste Teil: der Skill schreiben, oder sich einigen, was gelten soll?

## Bonus (wenn Zeit bleibt): dieselbe Vorgabe als Review

Bis hierhin hat der Skill **Code geändert**. Dieselbe Vorgabe lässt sich auch einsetzen, ohne etwas zu ändern: als Review.

1. Review-Skill ins Beispielprojekt kopieren:

   ```
   cp -r ../loesung/.claude/skills/logging-review .claude/skills/
   ```

2. Ausgangsdatei zurücksetzen (`git checkout -- src/`) und laufen lassen:

   ```
   /logging-review Prüfe UeberweisungService.java.
   ```

   Ergebnis: Review-Kommentare mit Schweregrad statt Codeänderungen – dieselbe Vorgabe, zweiter Einsatzort (Folie 47, Schritt 3).

3. Jetzt den Review-Skill **verbessern**. Er ist bewusst nicht fertig:

   - Die Schweregrade (BLOCKER/MAJOR/MINOR) sind eine Setzung. Passt sie zu eurem Haus? Ist ein fehlendes Pflichtfeld wirklich MAJOR?
   - Der Skill prüft gegen `logging-vorgaben/SKILL.md` – also gegen **eure** Vorgaben aus Schritt 1. Findet er alles, was ihr dort festgelegt habt? Wenn nicht: liegt das am Review-Skill oder an eurer Formulierung?
   - Fehlt im Ausgabeformat etwas, das ihr für ein echtes Review bräuchtet (Zeilennummer, Merge-Empfehlung, Verweis auf die Vorgabe)?

   Ändert den Skill, setzt die Datei zurück und lasst ihn erneut laufen.

**Auswertungsfrage:** Finden Implementierungs- und Review-Sicht dieselben Stellen? Wo nicht – und woran liegt das?
