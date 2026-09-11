# Praxisaufgabe: Logging-Vorgaben als Skill übergeben

**Ziel:** Eine Vorgabe so festhalten, dass ein Agent sie anwenden kann – und sehen, was das gegenüber einem „unbegleiteten“ Agenten ausmacht.
**Zeit:** 20 Minuten. **Gruppen:** 3–4 Personen, mindestens ein Laptop mit Claude Code pro Gruppe.

## Ausgangslage

Im Ordner `beispielprojekt/` liegt ein kleiner Java-Service `UeberweisungService.java`. Fachlich funktioniert er. Das Logging ist so, wie es in vielen Systemen aussieht: historisch gewachsen, von mehreren Personen, ohne Vorgabe.

Im Ordner `.claude/skills/logging-vorgaben/` liegt eine **Rohfassung** eines Skills. Die Struktur steht, der Inhalt fehlt (`TODO`).

## Schritt 0 – Vergleichslauf ohne Skill (3 Min., Trainer führt vor)

Damit der Lauf wirklich ohne Vorgabe stattfindet, muss die Rohfassung vorher aus dem Weg:

```
mv .claude/skills/logging-vorgaben /tmp/
```

Sonst zieht der Agent den Skill von selbst heran – seine Beschreibung passt auf den Prompt – und arbeitet dann mit einer Vorgabe, die nur aus `TODO` besteht.

Claude Code im Ordner `beispielprojekt/` starten und eingeben:

```
Verbessere das Logging in UeberweisungService.java.
```

Dann die Datei zurücksetzen (`git checkout -- .`) und **denselben Prompt ein zweites Mal** laufen lassen.

Nicht auf den Diff schauen, sondern auf die **Zusammenfassung** des Agenten: Er benennt darin, welche Log-Level er gewählt hat, wie er IBANs maskiert und was er für schützenswert hält. Vergleicht die beiden Läufe miteinander.

Merkt euch: Der Agent räumt viel auf – auch Kundenname und Token verschwinden meist. Aber er trifft dabei Entscheidungen, nach denen ihn niemand gefragt hat. Und die beiden Läufe treffen sie nicht zwangsläufig gleich.

Danach die Rohfassung zurücklegen:

```
mv /tmp/logging-vorgaben .claude/skills/
```

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

**Ihr dürft den Agenten fragen.** Wenn ihr bei einem Abschnitt unsicher seid, lasst euch Vorschläge geben, etwa:

```
Welche Log-Level-Regeln sind für einen Zahlungsverkehr-Service üblich? Nenne je Level einen Satz.
Welche Felder gehören in jeden Log-Eintrag, damit ein 2nd-Level-Team einen Vorgang nachts wiederfindet?
```

Der Agent kennt die gängige Praxis; er kennt nur **eure** Praxis nicht. Nehmt die Vorschläge als Entwurf und entscheidet dann bewusst, was davon bei euch gilt – genau diese Entscheidung ist der Inhalt der Übung. Übernehmt nichts ungeprüft: Was ihr nicht erklären könnt, könnt ihr später auch nicht gegenüber einem Team vertreten.

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

1. Welche Entscheidungen hat der Agent in Schritt 0 für euch getroffen, ohne zu fragen – und hättet ihr sie genauso getroffen?
2. Wo waren eure Formulierungen so unscharf, dass der Agent geraten hat?
3. Was war der aufwendigste Teil: der Skill schreiben, oder sich einigen, was gelten soll?

## Bonus (wenn Zeit bleibt): dieselbe Vorgabe als Review

Bis hierhin hat der Skill **Code geändert**. Dieselbe Vorgabe lässt sich auch einsetzen, ohne etwas zu ändern: als Review.

1. Review-Skill ins Beispielprojekt kopieren:

   ```
   cp -r ../loesung/.claude/skills/logging-review .claude/skills/
   ```

2. Ausgangsdatei zurücksetzen (`git checkout -- .`) und laufen lassen:

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
