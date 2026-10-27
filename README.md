# 🚚 Online Courier Management System

 A complete web-based courier management system built with Spring Boot, Spring MVC, Thymeleaf, and H2 Database.

---

## 📋 Project Objective

Build a functional **Online Courier Management System** demonstrating:
- MVC Architecture with Spring Boot
- Six modules: Office, Shipment, Manager, Delivery, Payment, Customer
- File-based persistent H2 database
- Session-protected admin interface
- Full CRUD operations with validation

---

## 🛠️ Technology Stack

| Technology       | Purpose                              |
|-----------------|--------------------------------------|
| Java 21+        | Programming language                 |
| Spring Boot 3.3 | Application framework                |
| Spring MVC      | MVC architecture (Controller layer)  |
| Thymeleaf       | Server-side HTML template engine     |
| Spring Data JPA | Database access (Repository layer)   |
| Hibernate       | ORM (maps Java classes ↔ DB tables) |
| H2 Database     | Embedded file-based database         |
| Maven           | Build tool and dependency management |
| Custom CSS      | Responsive admin UI styling          |

---

## 🏗️ MVC Architecture Explained

This project strictly follows the **Model-View-Controller** pattern:

### Model (src/main/java/com/courier/model/)
- Contains **entity classes**: `Office`, `Customer`, `Shipment`, `Manager`, `Delivery`, `Payment`
- Each class represents a database table via JPA `@Entity` annotations
- Holds the application's **data structure and business rules**
- Example: `Shipment.java` has a `ShipmentStatus` enum (BOOKED → IN_TRANSIT → DELIVERED)

### View (src/main/resources/templates/)
- **Thymeleaf HTML templates** rendered server-side
- Uses `th:each` to loop over data, `th:field` to bind form inputs, `th:text` to display values
- Completely separate from business logic — only displays what the Controller sends
- Organized by module: `office/`, `shipment/`, `customer/`, `delivery/`, `payment/`, `manager/`

### Controller (src/main/java/com/courier/controller/)
- Spring MVC `@Controller` classes receive HTTP requests via `@GetMapping` / `@PostMapping`
- Controllers call **Services** for business logic, then return a view name
- Example flow: User visits `/shipment` → `ShipmentController` → calls `ShipmentService.getAllShipments()` → returns view `"shipment/list"` with data

### Repository (src/main/java/com/courier/repository/)
- Spring Data JPA interfaces that extend `JpaRepository`
- Auto-generates SQL queries from method names (e.g., `findByTrackingNumber(...)`)
- Support the Model layer — not a replacement for MVC

### Service (src/main/java/com/courier/service/)
- Contains business logic between Controller and Repository
- Example: `ShipmentService.save()` auto-generates tracking numbers; `DeliveryService.updateStatus()` synchronizes shipment status

---

## 📁 Project Folder Structure

```
courier-management/
├── pom.xml                          ← Maven build file
├── README.md
└── src/
    └── main/
        ├── java/com/courier/
        │   ├── CourierApplication.java      ← Main entry point
        │   ├── config/
        │   │   ├── AuthInterceptor.java     ← Session-based auth guard
        │   │   ├── WebConfig.java           ← Registers interceptor
        │   │   └── DataInitializer.java     ← Sample data loader
        │   ├── model/
        │   │   ├── Office.java
        │   │   ├── Customer.java
        │   │   ├── Shipment.java
        │   │   ├── Manager.java
        │   │   ├── Delivery.java
        │   │   └── Payment.java
        │   ├── repository/
        │   │   ├── OfficeRepository.java
        │   │   ├── CustomerRepository.java
        │   │   ├── ShipmentRepository.java
        │   │   ├── ManagerRepository.java
        │   │   ├── DeliveryRepository.java
        │   │   └── PaymentRepository.java
        │   ├── service/
        │   │   ├── OfficeService.java
        │   │   ├── CustomerService.java
        │   │   ├── ShipmentService.java
        │   │   ├── ManagerService.java
        │   │   ├── DeliveryService.java
        │   │   └── PaymentService.java
        │   └── controller/
        │       ├── AuthController.java
        │       ├── DashboardController.java
        │       ├── OfficeController.java
        │       ├── CustomerController.java
        │       ├── ShipmentController.java
        │       ├── ManagerController.java
        │       ├── DeliveryController.java
        │       └── PaymentController.java
        └── resources/
            ├── application.properties       ← DB and app config
            ├── static/
            │   └── css/style.css            ← All UI styles
            └── templates/
                ├── login.html
                ├── dashboard.html
                ├── office/   (list.html, form.html)
                ├── customer/ (list.html, form.html)
                ├── shipment/ (list.html, form.html, view.html, track.html)
                ├── manager/  (list.html, form.html)
                ├── delivery/ (list.html, form.html)
                └── payment/  (list.html, form.html, view.html)
```

---

## 📦 Six Modules

### 1. 🏢 Office Module (`/office`)
- Add, view, edit, and delete courier offices
- Fields: Office Name, Address, City, Contact Number
- Safe deletion: prevented if shipments or managers are assigned

### 2. 📦 Shipment Module (`/shipment`)
- Create shipments with customer and source office
- Auto-generates unique tracking numbers (format: `CMS-YYYYMMDD-XXXXXX`)
- Statuses: BOOKED → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED
- Track shipments by tracking number at `/shipment/track`

### 3. 👔 Manager Module (`/manager`)
- Manage manager records assigned to offices
- Fields: Name, Contact, Email, Assigned Office

### 4. 🛵 Delivery Module (`/delivery`)
- Assign deliveries to shipments with personnel name
- Status changes automatically sync with shipment status:
  - IN_PROGRESS → shipment becomes OUT_FOR_DELIVERY
  - COMPLETED → shipment becomes DELIVERED

### 5. 💳 Payment Module (`/payment`)
- Record payment for each shipment
- Methods: Cash, UPI, Card
- One-click "Mark as Paid" with method selection
- Status: PENDING / PAID

### 6. 👥 Customer Module (`/customer`)
- Register and manage customers
- Search by name or phone number
- Safe deletion: prevented if shipments exist

---

## 🗄️ Database Configuration

**H2 File-Based Persistent Database** — data survives restarts.

Configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:h2:file:./data/courierdb;AUTO_SERVER=TRUE
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

The database files are stored in the `./data/` folder (created automatically).

**H2 Console** (for local development/inspection):
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:file:./data/courierdb`
- Username: `sa`
- Password: *(leave empty)*

---

## ✅ Prerequisites

| Requirement | Version   |
|------------|-----------|
| Java JDK   | 17 or 21+ |
| Maven      | 3.6+      |
| Browser    | Any modern browser |

**Check your versions:**
```bash
java -version
mvn -version
```

---

## 🚀 How to Build and Run

### Step 1: Navigate to the project folder
```bash
cd "s:\Study\OOP\Micro Project"
```

### Step 2: Build and run with Maven
```bash
mvn spring-boot:run
```
> If Maven is not in PATH, use the full path:
> `C:\apache-maven\bin\mvn.cmd spring-boot:run`

### Step 3: Open in browser
```
http://localhost:8080
```

You will be redirected to the login page automatically.

---

## 🔐 Login Credentials

| Field    | Value    |
|----------|----------|
| Username | `admin`  |
| Password | `admin123` |

> ⚠️ **Academic use only.** These are demonstration credentials hardcoded for the lab project. Do NOT use this authentication approach in a real production system.

---

## 📊 Sample Data

When the application starts with an **empty database**, it automatically loads:
- **2 Offices**: Mumbai Central Office, Delhi North Office
- **2 Managers**: Ravi Sharma, Priya Mehta
- **3 Customers**: Amit Kumar, Sonia Patel, Rahul Verma
- **3 Shipments** with various statuses
- **2 Delivery records**
- **3 Payment records**

> Sample data is loaded **only once** when the DB is empty. Restarting the app will NOT duplicate records.

