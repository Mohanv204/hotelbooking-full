<<<<<<< HEAD
# 🏨 Hotel Booking System

## 📌 Project Overview

The **Hotel Booking System** is a full-stack web application designed to manage hotel operations such as room booking, user authentication, and hotel management.
It provides a seamless experience for users to browse hotels, book rooms, and manage reservations.

---

## 🚀 Features

* 🔐 User Authentication (Admin & User)
* 🏨 Hotel Management
* 📅 Room Booking System
* 👨‍💼 Admin Dashboard
* 📊 Database Integration
* 🔄 Real-time Data Handling

---

## 🛠️ Tech Stack

### 💻 Frontend

* React.js
* HTML, CSS, JavaScript

### ⚙️ Backend

* Spring Boot
* REST APIs
* Spring Security

### 🗄️ Database

* MySQL / H2 (based on your setup)

---

## 📂 Project Structure

```
hotel-app/
│
├── backend/        # Spring Boot backend
├── frontend/       # React frontend
└── README.md
```

---

## ⚙️ Installation & Setup

### 🔹 Backend Setup

```bash
cd backend
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:9091
```

---

### 🔹 Frontend Setup

```bash
cd frontend
npm install
npm start
```

Frontend runs on:

```
http://localhost:3000
```

---

## 🔐 Default Login Credentials

### 👑 Admin

* Email: [admin@hotel.com](mailto:admin@hotel.com)
* Password: admin123

### 👤 User

* Email: [user@hotel.com](mailto:user@hotel.com)
* Password: user123

---

## 🔗 API Endpoints (Sample)

* `/auth/login` → Login
* `/hotels` → Get hotel details
* `/bookings` → Manage bookings

---

## 📸 Screenshots

*(Add screenshots here for better presentation)*

---

## 🎯 Future Enhancements

* Payment Integration
* Email Notifications
* Advanced Search Filters
* Deployment (AWS / Cloud)

---

## 👨‍💻 Author

**Mohan V**
B.Tech AI & Data Science

---

## ⭐ Acknowledgment

This project was developed as part of a hackathon to demonstrate full-stack development skills using modern technologies.

---

## 📌 Note

Make sure backend is running before starting frontend for proper API communication.
=======
# Hotel Booking Backend System

## Overview
The Hotel Booking Backend System is a RESTful application designed to enable customers to browse, select, and book hotel rooms efficiently. The system ensures secure operations, real-time availability updates, and a seamless booking experience.

This project is built using Spring Boot and follows clean architecture principles, making it scalable, maintainable, and production-ready.

---

## Architecture: 3-Tier Layered Design

This backend system follows a strict 3-tier architecture to enforce the separation of concerns. This ensures the codebase remains modular, testable, and easy to maintain as the application scales.

### 1. Presentation Layer (Controllers)
- **Location:** `controller/`
- **Role:** The entry point of the backend. It intercepts incoming HTTP requests from the client (React), validates the incoming data, and delegates the processing to the Service layer. It never interacts with the database directly. It returns structured HTTP responses (typically JSON via DTOs).

### 2. Business Logic Layer (Services)
- **Location:** `service/`
- **Role:** The brain of the application. It contains all the core business rules—such as verifying if a room is actually available for the requested dates, calculating total costs, and securely processing the booking. It acts as a bridge, orchestrating data between the Controller and the Repository.

### 3. Data Access Layer (Repositories)
- **Location:** `repository/`
- **Role:** Handles all direct communication with the underlying database. Utilizing Spring Data JPA, this layer performs necessary CRUD (Create, Read, Update, Delete) operations on the database entities (like `Hotel`, `Room`, and `Booking`) without the need for boilerplate SQL queries.

## Features

### Customer Enablement
- Browse hotels, room categories, and amenities
- View pricing and availability in real-time
- Seamless and secure booking process

---

## Core Functionalities

### Centralized Management
- Manage hotels, rooms, pricing, and facilities
- Structured backend system with modular components

### Search and Filtering
- Filter hotels based on:
  - Location
  - Dates
  - Price range
  - Amenities

### Booking System
- Real-time room availability updates
- Booking confirmation handling
- Prevention of double booking

### Security
- Authentication and Authorization using JWT
- Secure REST APIs
- Role-based access control
- Rate limiting support (if implemented)

### API Validation
- REST endpoints tested using Postman
- API documentation supported via Swagger/OpenAPI

### Version Control
- Source code managed using Git
- Repository maintained on GitHub

---

## Stretch Features

- Email confirmation with booking details and reservation number
- Booking history for authenticated users
- Quick rebooking functionality
- Promotions:
  - Discount codes
  - Loyalty rewards
  - Seasonal offers

---

## Tech Stack

- Backend: Spring Boot
- Language: Java
- Database: (Specify your DB, e.g., MySQL / PostgreSQL)
- Security: Spring Security with JWT
- Build Tool: Maven
- API Testing: Postman / Swagger
- Version Control: Git & GitHub
- Containerization: Docker (if used)

---

## Project Structure

```text
hotelbooking-full/
├── src/
│   ├── main/
│   │   ├── java/com/hotelbooking/app/hotelbooking/
│   │   │   ├── HotelBookingApplication.java      # Main application entry point
│   │   │   ├── config/                           # App configurations (CORS, Swagger, etc.)
│   │   │   ├── controller/                       # REST API controllers (e.g., HotelController, BookingController)
│   │   │   ├── dto/                              # Data Transfer Objects for API requests and responses
│   │   │   ├── exception/                        # Global error handling and custom exceptions
│   │   │   ├── model/                            # JPA Entities (e.g., User, Hotel, Room, Booking)
│   │   │   ├── repository/                       # Spring Data JPA repositories for database access
│   │   │   ├── security/                         # JWT configuration, filters, and authentication logic
│   │   │   └── service/                          # Business logic implementation
│   │   └── resources/
│   │       ├── application.properties            # Database connections, server port, and JWT secrets
│   │       ├── static/                           # Static assets (can hold compiled React frontend files)
│   │       └── templates/                        # Email templates or server-side rendered pages
│   └── test/
│       └── java/com/hotelbooking/app/hotelbooking/
│           ├── controller/                       # API integration and unit tests
│           └── service/                          # Business logic unit tests
├── .gitignore                                    # Specifies intentionally untracked files to ignore
├── pom.xml                                       # Maven configuration and project dependencies
└── README.md                                     # Project documentation
>>>>>>> 870e18cf2061eff3c70598dc377fa1598d8bbd63
