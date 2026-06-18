# Study Cards 

Eine Desktop-Anwendung zum Erstellen, Verwalten und Lernen von Karteikarten.
Gebaut mit **JavaFX** (UI) und **PostgreSQL** (Datenbank) sowie Java-OOP-Programme (Logik).

---

## Features

- **Lernsets** erstellen, bearbeiten und löschen
- **Karten** (Frage/Antwort) innerhalb eines Sets anlegen, bearbeiten und löschen
- **Lernmodus** mit zufällig gemischten Karten und Fortschrittsanzeige
  ("Karte 3 von 10")
- **Antwort aufdecken** per Klick, danach Bewertung *bekannt* / *nicht bekannt*
- **Statistik**: Zählung bekannter und unbekannter Karten pro Sitzung,
  dauerhafte Speicherung der Trefferzahlen in der Datenbank
- **Sicherheitsabfragen** vor dem Löschen von Sets und Karten

---

## Voraussetzungen

| Software   | Version           |
|------------|-------------------|
| Java (JDK) | 21                |
| JavaFX     | 21 (oder höher)   |
| Maven      | 3.8+              |
| PostgreSQL | 14+ (laufend)     |

---

## Datenbank einrichten

1. Eine Datenbank anlegen:

   ```sql
   CREATE DATABASE studycard;
   ```

2. Die Tabellen erstellen:

   ```sql
   CREATE TABLE studyset (
       id          SERIAL PRIMARY KEY,
       name        VARCHAR(255) NOT NULL,
       description TEXT
   );

   CREATE TABLE card (
       id            SERIAL PRIMARY KEY,
       set_id        INTEGER NOT NULL REFERENCES studyset(id) ON DELETE CASCADE,
       question      TEXT NOT NULL,
       answer        TEXT NOT NULL,
       times_known   INTEGER NOT NULL DEFAULT 0,
       times_unknown INTEGER NOT NULL DEFAULT 0
   );
   ```

   > **Hinweis:** `ON DELETE CASCADE` sorgt dafür, dass beim Löschen eines Sets
   > automatisch alle zugehörigen Karten entfernt werden.

---

## Konfiguration

Die Zugangsdaten zur Datenbank stehen in
`src/main/java/org/example/studycarddemo/dao/DatabaseConnection.java`:

```java
private static final String URL      = "jdbc:postgresql://localhost:5432/studycard";
private static final String USER     = "postgres";
private static final String PASSWORD = "DEIN_PASSWORT";
```


> Zugangsdaten sollten nicht im Quellcode stehen, aber ich habe keine Ahnung wie zu verstecken (vielleicht in gitignores reinwerfen wenn ich git pushen werde)

---

## Starten

```bash
mvn clean javafx:run
```

Beim Start öffnet sich das Fenster mit der Übersicht aller Lernsets.

---

## Bedienung

1. **Neues Set anlegen** → Button *New Set*, Name und Beschreibung eingeben.
2. **Karten hinzufügen** → Set auswählen → *Edit Cards* → Frage und Antwort
   eingeben → *Save*.
3. **Lernen** → Set auswählen → *Start Study Session* → Frage lesen →
   *Reveal* → Antwort bewerten (*Known* / *Not known*).
4. Am Ende der Sitzung erscheint eine Zusammenfassung der Ergebnisse.

---

## Architektur

Die Anwendung ist in klar getrennte Schichten gegliedert. Jede Schicht ruft nur
die darunterliegende auf — so bleibt der Code wartbar und testbar.

### Projektstruktur

```
src/
├── main/
│   ├── java/org/example/studycarddemo/
│   │   ├── entity/        StudySet.java, Card.java
│   │   ├── dao/           DatabaseConnection.java, CardCRUD.java, StudySetCRUD.java
│   │   ├── services/      StudyService.java
│   │   ├── UI/            SetOverviewController.java, CardEditorController.java,
│   │   │                  StudyModeController.java
│   │   └── mainapp/       MainApp.java
│   └── resources/org/example/studycarddemo/
│       ├── fxml/          SetOverview.fxml, CardEditor.fxml, StudyMode.fxml
│       └── css/           styles.css
└── test/
    └── java/              StudyServiceTest.java
```
```

| Paket       | Verantwortung                                                       |
|-------------|---------------------------------------------------------------------|
| `entity`    | Reine Datenobjekte (`Card`, `StudySet`)                             |
| `dao`       | Datenbankzugriff (`DatabaseConnection`, `CardCRUD`, `StudySetCRUD`) |
| `services`  | Lernlogik (`StudyService`: Mischen, Zählen, Fortschritt)            |
| `UI`        | Controller (`SetOverview`, `CardEditor`, `StudyMode`)               |
| `fxml`      | Oberflächen-Layouts                                                 |
| `css`       | Visuelles Styling (`styles.css`)                                    |
| `mainapp`   | Start und Navigation (`MainApp`)                                    |

```
---

## Verwendete JavaFX-APIs

- **Application / Stage / Scene** — Lebenszyklus und Fenster
- **FXMLLoader** und Annotation **`@FXML`** — Trennung von Layout und Logik
- **Controls:** `ListView`, `Button`, `Label`, `TextArea`, `TextField`,
  `Dialog`, `Alert`, `Separator`
- **Layouts:** `BorderPane`, `VBox`, `HBox`
- **`ObservableList` / `FXCollections`** — automatische Aktualisierung der Listen
- **Property-Listener** (`selectedItemProperty().addListener`) — reaktive UI
- **CSS-Styling** über `Scene.getStylesheets()`



---

## Tests

Die Geschäftslogik in `StudyService` ist durch JUnit-5-Tests abgedeckt
(`StudyServiceTest`), z. B. Initialisierung der Sitzung, Aufdecken der Antwort,
Weiterschalten der Karten und korrekte Zählung. Ausführen mit:

```bash
mvn test
```

