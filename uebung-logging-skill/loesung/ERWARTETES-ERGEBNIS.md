# Erwartetes Ergebnis: Verstöße in `UeberweisungService.java` (Ausgangsdatei)

Zeilenangaben beziehen sich auf `beispielprojekt/src/main/java/de/beispielbank/zahlungsverkehr/UeberweisungService.java`.
Ein Agent mit dem ausgearbeiteten Skill sollte alle 14 Punkte finden.

**Was ein Agent ohne Skill findet** (mit Opus 5 nachgestellt; vor dem Workshop mit dem eingesetzten Modell gegenprüfen, das Verhalten variiert):
Deutlich mehr, als man erwarten würde. Behoben wurden die Punkte 1, 2, 3, 5, 6, 7, 8, 9, 13, 14 – **einschließlich der Datenschutzpunkte**. Der Agent entfernte Session-Token, Kundenname und Verwendungszweck ohne Aufforderung und ergänzte eine eigene `maskiere()`-Methode.

Verlässlich offen bleiben nur:

| Nr. | Warum ohne Vorgabe nicht erreichbar |
|---|---|
| 4 (teilweise) | Der Betrag bleibt stehen, teils im selben Eintrag wie die maskierte IBAN. Dass das bei euch zusammen nicht geloggt werden darf, ist nicht ableitbar. |
| 10, 12 | `grund=`-Codes statt Freitext. Der Agent schreibt „Tageslimit überschritten“ als Satz – auswertbar ist nur ein Code. |
| 11 | `ergebnis=OK/ABGELEHNT/FEHLER` als festes Feld. |

Dazu kommen Abweichungen, die kein „Fund“ sind, aber eben nicht eure Hausregel: Maskierungsformat (`****4711` statt `DE12****4711`), Level für fachliche Ablehnungen (INFO statt WARN), Einstiegs-Log auf DEBUG statt INFO, Nachrichtenform mit Satzzeichen statt reiner `schluessel=wert`-Paare.

**Für die Moderation entscheidend:** Zwei Läufe ohne Skill mit identischem Prompt kamen zu *unterschiedlichen* Entscheidungen (Tageslimit einmal WARN, einmal INFO). Die Aussage der Übung ist deshalb nicht „der Agent findet es nicht“, sondern „der Agent entscheidet es für euch – jedes Mal neu, und er sagt es so selbstbewusst, dass es wie eine Vorgabe klingt“.

| Nr. | Zeile | Befund | Vorgabe |
|---|---|---|---|
| 1 | 27 | `System.out.println` statt Logger; ganzes Domänenobjekt per `toString()` (enthält Name, IBAN, Betrag, Verwendungszweck) | 1, 4 |
| 2 | 29–30 | Kundenname und Session-Token geloggt | 4 |
| 3 | 29 | ERROR für normalen Methodeneinstieg | 3 |
| 4 | 39–41 | Kundenname, zwei IBANs im Klartext, Betrag im selben Eintrag wie die IBAN | 4 |
| 5 | 39–41 | String-Verkettung, Satzzeichen/Ausruf in Nachricht | 1 |
| 6 | 50 | `e.printStackTrace()` – Stacktrace nach stdout | 1, 5 |
| 7 | 51 | Kernbank-Ausfall auf INFO statt ERROR, ohne Exception | 3, 5 |
| 8 | 60–61 | Verwendungszweck und Ziel-IBAN im Klartext (auch auf DEBUG nicht erlaubt) | 4 |
| 9 | 55 | `catch (Exception)` verschluckt, nur DEBUG mit `getMessage()`, kein Stacktrace | 3, 5 |
| 10 | 34 | Ablehnung ohne `grund=` und ohne Kontobezug (nicht wiederfindbar) | 2 |
| 11 | 59 | `"done"` – kein Ereignisname, keine `buchungsId`, kein `ergebnis=` | 1, 2 |
| 12 | 39 | Ablehnung bei Limit ohne `grund=`-Code | 2 |
| 13 | 69, 71 | ERROR für Normalfall („keine offenen Buchungen“) und für Warnfall | 3 |
| 14 | 66, 73 | Dekorationszeilen `==========` | 1 |

## Warum das im Workshop überzeugt

Der Unterschied zwischen dem Lauf ohne Skill und dem Lauf mit Skill liegt fast vollständig in Vorgabe 2 (Pflichtfelder) und in der **Form** der Vorgaben 3 und 4 – nicht mehr darin, *ob* sensible Daten entfernt werden, sondern *nach welcher Regel*. Welche Maskierung, welcher Ablehnungscode, welches Level für eine fachliche Ablehnung: Das sind Entscheidungen, die **das Team treffen muss**, weil sie von Haus zu Haus unterschiedlich sind. Ein Modell kann sie nicht erraten – es trifft sie trotzdem, nur eben unabgestimmt und nicht reproduzierbar.

Genau das ist die Aussage von Folie 46, Schritt 1: Der Aufwand liegt im Festlegen, nicht im Übergeben.
