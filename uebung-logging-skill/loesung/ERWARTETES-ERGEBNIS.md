# Erwartetes Ergebnis: Verstöße in `UeberweisungService.java` (Ausgangsdatei)

Zeilenangaben beziehen sich auf `beispielprojekt/src/main/java/de/beispielbank/zahlungsverkehr/UeberweisungService.java`.
Ein Agent mit dem ausgearbeiteten Skill sollte alle 14 Punkte finden. Erwartung (vor dem Workshop mit dem eingesetzten Modell einmal gegenprüfen): Ohne Skill werden eher die technischen Punkte 1, 3, 5, 6, 9, 13, 14 behoben; die Datenschutz-Punkte (2, 4, 8) und die Pflichtfelder (10, 11, 12) hängen von hausspezifischen Entscheidungen ab und bleiben ohne Vorgabe meist unvollständig.

| Nr. | Zeile | Befund | Vorgabe |
|---|---|---|---|
| 1 | 27 | `System.out.println` statt Logger; ganzes Domänenobjekt per `toString()` (enthält Name, IBAN, Betrag, Verwendungszweck) | 1, 4 |
| 2 | 29–30 | Kundenname und Session-Token geloggt | 4 |
| 3 | 29 | ERROR für normalen Methodeneinstieg | 3 |
| 4 | 39–41 | Kundenname, zwei IBANs im Klartext, Betrag mit Kundenbezug | 4 |
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

Der Unterschied zwischen dem Lauf ohne Skill und dem Lauf mit Skill liegt fast vollständig in Vorgabe 4 (was niemals geloggt wird) und Vorgabe 2 (Pflichtfelder). Beides sind Entscheidungen, die **das Team treffen muss** – ein Modell kann sie nicht erraten, weil sie von Haus zu Haus unterschiedlich sind (welche Maskierung, welche Korrelations-ID, welche Ablehnungscodes). Genau das ist die Aussage von Folie 46, Schritt 1: Der Aufwand liegt im Festlegen, nicht im Übergeben.
