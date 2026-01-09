# Proiect BarLoyalty

Aplicație cloud-native pentru gestionarea punctelor de loialitate între mai multe baruri partenere, dezvoltată pentru [Numele Materiei].

**Echipa:**
*   [Crăciun Bianca-Nicoleta]
*   [Danci Bianca Ana-Maria]

---

## Arhitectura

Aplicația folosește o arhitectură bazată pe microservicii, orchestrată cu Docker Compose.

*   **Gateway (Spring Boot & Java):** Serviciul principal care gestionează logica de business, API-urile REST, comunicarea cu baza de date și notificările WebSocket.
*   **Microserviciu QR (Python & FastAPI):** Un serviciu dedicat pentru simularea generării și validării codurilor QR unice. Comunică prin HTTP cu Gateway-ul pentru a confirma tranzacțiile.
*   **Bază de Date (PostgreSQL):** Stochează toate datele persistente: utilizatori, baruri, recompense, tranzacții.
*   **Orchestrare (Docker Compose):** Toate serviciile sunt containerizate și pornesc cu o singură comandă, asigurând un mediu de dezvoltare și deployment consistent.

---

## Cum se rulează proiectul

**Cerințe:**
*   Docker
*   Docker Compose (de obicei vine cu Docker)
*   WSL2 (pentru Windows)

**Pași:**
1.  Clonează acest repository.
2.  Navighează în folderul rădăcină al proiectului într-un terminal (ex: Ubuntu în WSL2).
3.  Rulează comanda:
    ```bash
    docker-compose up --build
    ```
4.  Aplicația va porni. Serviciile vor fi disponibile la următoarele adrese:
    *   **Gateway (Java):** `http://localhost:8080`
    *   **Microserviciu QR (Python):** `http://localhost:8000`
    *   **Documentație API (Swagger):** `http://localhost:8080/swagger-ui.html`

---

## Demonstrație

Pentru a demonstra funcționalitatea, se poate folosi Swagger, Postman sau orice client HTTP.

**Fluxul unei tranzacții:**
1.  **Generare QR:** Se trimite o cerere `POST` la `http://localhost:8080/api/transactions/generate-qr` cu un body de tipul:
    ```json
    { "clientId": 1, "barId": 1, "points": 50 }
    ```
    Se va primi înapoi un ID unic pentru codul QR.
2.  **Validare QR:** Se trimite o cerere `POST` la `http://localhost:8000/qr/validate/{id-ul-primit-mai-sus}`.
3.  **Verificare:** Se poate verifica că tranzacția a fost salvată făcând o cerere `GET` la `http://localhost:8080/api/transactions/user/1`.