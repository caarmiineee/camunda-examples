# Camunda BPM Examples with Spring Boot

Progetto di esempio che implementa due processi BPMN completi utilizzando Camunda BPM Platform 7 e Spring Boot.

## 📋 Indice

- [Processi Implementati](#processi-implementati)
- [Requisiti](#requisiti)
- [Avvio del Progetto](#avvio-del-progetto)
- [Console Camunda](#console-camunda)
- [API REST](#api-rest)
- [Testing con Postman](#testing-con-postman)
- [Struttura del Progetto](#struttura-del-progetto)

## 🔄 Processi Implementati

### 1. Order Process (`order_process`)
Processo completo di gestione ordini e-commerce con:
- Validazione ordine
- Controllo inventario
- Gateway esclusivo per disponibilità
- Gateway parallelo per operazioni simultanee (prenotazione items + calcolo spedizione)
- Processamento pagamento con gestione errori (Boundary Event)
- User task per spedizione
- Notifiche email

### 2. Loan Approval (`loan_approval`)
Processo di approvazione prestiti bancari con:
- Verifica credit score (External Task)
- Business Rule Task per valutazione rischio (DMN)
- Gateway esclusivo per routing basato su rischio (LOW/MEDIUM/HIGH)
- Script task per approvazione/rifiuto automatico
- User task per revisione manuale
- Timer boundary event per promemoria
- Preparazione documenti e disbursement

## 📦 Requisiti

- Java 17+
- Maven 3.8+
- Porta 8080 disponibile

## 🚀 Avvio del Progetto

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

Oppure:

```bash
java -jar target/camunda-examples-1.0.0.jar
```

L'applicazione sarà disponibile su `http://localhost:8080`

## 🖥️ Console Camunda

Dopo l'avvio, accedi alle console web:

- **Welcome:** http://localhost:8080/camunda/app/welcome/default/
- **Cockpit:** http://localhost:8080/camunda/app/cockpit/default/ (monitoraggio processi)
- **Tasklist:** http://localhost:8080/camunda/app/tasklist/default/ (gestione task utente)
- **Admin:** http://localhost:8080/camunda/app/admin/default/ (gestione utenti)
- **H2 Console:** http://localhost:8080/h2-console (database)

**Credenziali di default:**
- Username: `demo`
- Password: `demo`

**H2 Database:**
- JDBC URL: `jdbc:h2:file:./camunda-h2-database`
- Username: `sa`
- Password: (lasciare vuoto)

## 🔌 API REST

Base URL: `http://localhost:8080/engine-rest`

### Endpoint Principali

```
GET    /process-definition                      # Lista definizioni processi
GET    /process-definition/key/{key}            # Dettagli processo
POST   /process-definition/key/{key}/start      # Avvia processo
GET    /task                                     # Lista task attive
POST   /task/{taskId}/complete                  # Completa task
GET    /process-instance                         # Lista istanze attive
GET    /history/process-instance                # Storia processi
```

## 📮 Testing con Postman

### Order Process

#### 1. Avvia Processo (Inventario Disponibile)

```http
POST http://localhost:8080/engine-rest/process-definition/key/order_process/start
Content-Type: application/json

{
  "variables": {
    "orderId": { "value": "ORD-123" },
    "orderAmount": { "value": 120.0 },
    "customerEmail": { "value": "user@example.com" },
    "shippingCountry": { "value": "USA" },
    "inventoryAvailable": { "value": true }
  }
}
```

#### 2. Ottieni Task "Ship Order"

```http
GET http://localhost:8080/engine-rest/task?name=Ship Order
```

#### 3. Completa Task "Ship Order"

```http
POST http://localhost:8080/engine-rest/task/{taskId}/complete
Content-Type: application/json

{
  "variables": {
    "trackingNumber": { "value": "TRACK-123456" },
    "shipmentDate": { "value": "2025-11-24" }
  }
}
```

#### Scenario: Inventario Non Disponibile

```http
POST http://localhost:8080/engine-rest/process-definition/key/order_process/start
Content-Type: application/json

{
  "variables": {
    "orderId": { "value": "ORD-124" },
    "orderAmount": { "value": 200.0 },
    "customerEmail": { "value": "user@example.com" },
    "shippingCountry": { "value": "USA" },
    "inventoryAvailable": { "value": false }
  }
}
```

### Loan Approval Process

#### Scenario 1: Rischio Basso (Auto Approvazione)

```http
POST http://localhost:8080/engine-rest/process-definition/key/loan_approval/start
Content-Type: application/json

{
  "variables": {
    "applicantName": { "value": "Mario Rossi" },
    "loanAmount": { "value": 30000.0 },
    "creditScore": { "value": 750 },
    "monthlyIncome": { "value": 5000.0 }
  }
}
```

#### Scenario 2: Rischio Medio (Revisione Manuale)

**1. Avvia Processo:**

```http
POST http://localhost:8080/engine-rest/process-definition/key/loan_approval/start
Content-Type: application/json

{
  "variables": {
    "applicantName": { "value": "Luigi Verdi" },
    "loanAmount": { "value": 50000.0 },
    "creditScore": { "value": 650 },
    "monthlyIncome": { "value": 3000.0 }
  }
}
```

**2. Ottieni Task "Manual Review Required":**

```http
GET http://localhost:8080/engine-rest/task?name=Manual Review Required
```

**3. Completa Revisione (Approva):**

```http
POST http://localhost:8080/engine-rest/task/{taskId}/complete
Content-Type: application/json

{
  "variables": {
    "approved": { "value": true },
    "approvalMethod": { "value": "MANUAL" },
    "interestRate": { "value": 5.5 }
  }
}
```

**4. Ottieni Task "Prepare Loan Documents":**

```http
GET http://localhost:8080/engine-rest/task?processDefinitionKey=loan_approval
```

**5. Completa Preparazione Documenti:**

```http
POST http://localhost:8080/engine-rest/task/{taskId}/complete
Content-Type: application/json

{
  "variables": {
    "documentsReady": { "value": true }
  }
}
```

#### Scenario 3: Rischio Alto (Auto Rifiuto)

```http
POST http://localhost:8080/engine-rest/process-definition/key/loan_approval/start
Content-Type: application/json

{
  "variables": {
    "applicantName": { "value": "Giovanni Neri" },
    "loanAmount": { "value": 100000.0 },
    "creditScore": { "value": 500 },
    "monthlyIncome": { "value": 1500.0 }
  }
}
```

### Query Utili

**Tutte le task attive:**
```http
GET http://localhost:8080/engine-rest/task
```

**Task per processo specifico:**
```http
GET http://localhost:8080/engine-rest/task?processDefinitionKey=order_process
```

**Istanze di processo attive:**
```http
GET http://localhost:8080/engine-rest/process-instance
```

**Storia processi:**
```http
GET http://localhost:8080/engine-rest/history/process-instance?processDefinitionKey=loan_approval
```

**Variabili di un processo:**
```http
GET http://localhost:8080/engine-rest/process-instance/{processInstanceId}/variables
```

## 📁 Struttura del Progetto

```
camunda-examples/
├── src/main/
│   ├── java/com/example/workflow/
│   │   ├── Application.java                         # Main Spring Boot
│   │   ├── delegate/                                 # Service Tasks (Java Delegates)
│   │   │   ├── AssessRiskDelegate.java              # Valutazione rischio
│   │   │   ├── EmailNotificationDelegate.java       # Invio email
│   │   │   ├── LoanDisbursementDelegate.java        # Erogazione prestito
│   │   │   ├── OrderValidationDelegate.java         # Validazione ordine
│   │   │   ├── PaymentDelegate.java                 # Pagamento
│   │   │   ├── RejectionNotificationDelegate.java   # Notifica rifiuto
│   │   │   ├── ReminderDelegate.java                # Promemoria
│   │   │   ├── ReserveItemsDelegate.java            # Prenotazione items
│   │   │   └── ShippingCalculationDelegate.java     # Calcolo spedizione
│   │   ├── externaltask/                            # External Task Workers
│   │   │   └── CreditScoreExternalTaskWorker.java   # Worker credit score
│   │   └── service/                                  # Business Logic
│   │       └── InventoryService.java                # Gestione inventario
│   └── resources/
│       ├── application.yaml                          # Configurazione Spring Boot
│       ├── order-process.bpmn                        # BPMN Order Process
│       ├── loan-approval.bpmn                        # BPMN Loan Approval
│       ├── loan-risk-decision.dmn                    # DMN Risk Assessment
│       └── discount-decision.dmn                     # DMN Discount Rules
├── pom.xml                                           # Maven dependencies
├── PROCESSES.md                                      # Documentazione processi
└── README.md                                         # Questo file
```

## 🛠️ Tecnologie Utilizzate

- **Camunda BPM Platform 7.21.0** - Process Engine
- **Spring Boot 3.3.5** - Application Framework
- **H2 Database** - In-memory database
- **Java 17** - Programming Language
- **Maven** - Build Tool

## 📝 Note

- I processi vengono automaticamente deployati all'avvio dell'applicazione
- Il database H2 è configurato per persistere su file (`camunda-h2-database`)
- Le credenziali demo sono configurate per sviluppo locale
- I delegate Java sono registrati automaticamente come Spring Beans

## 🐛 Troubleshooting

### Errore "Cannot resolve identifier"
Se ricevi errori tipo "Cannot resolve identifier 'variableName'", assicurati di passare tutte le variabili richieste quando avvii il processo.

### Porta 8080 già in uso
Modifica la porta nel file `application.yaml`:
```yaml
server:
  port: 8081
```

### Task non visibili
Verifica che il processo sia stato avviato correttamente e controlla la Cockpit per lo stato dell'istanza.

## 📄 Licenza

Progetto di esempio per scopi educativi.
