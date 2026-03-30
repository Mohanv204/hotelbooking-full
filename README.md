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
