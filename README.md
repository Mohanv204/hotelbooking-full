# Hotel Booking Backend (Spring Boot 3)

Simple backend to **maintain hotel bookings**:
- Register / Sign in (JWT)
- List hotels
- List available rooms for a date range
- Create booking
- Cancel booking

## Where to put it
All backend code is already under:
`hotel-booking-backend/`

Open that folder in your IDE as a Maven project.

## Run (local)
1. Start MySQL 8 and create a database:
   - `hotel_booking`
2. Use JDK **17** (your machine currently has Java 17).
3. Set environment variables (recommended) or edit `application.yml`:
   - `DB_HOST` (default `localhost`)
   - `DB_PORT` (default `3306`)
   - `DB_NAME` (default `hotel_booking`)
   - `DB_USER` (default `root`)
   - `DB_PASSWORD` (default `root`)
   - `JWT_SECRET` (must be at least 32 characters)
4. Run:
   - From inside `hotel-booking-backend/`:
     - `mvn spring-boot:run`

Server starts at:
- `http://localhost:8080`

## Run with Docker (MySQL + backend)
From inside `hotel-booking-backend/`:
1. Start:
   - `docker compose up --build`
2. Backend:
   - `http://localhost:8080`

MySQL will be available on the container network as host `mysql`, and the database name is `hotel_booking`.

## API quick start
### Register
`POST /api/auth/register`
Body:
```json
{ "email": "user@example.com", "password": "your-password" }
```

### Sign in
`POST /api/auth/signin`
Body:
```json
{ "email": "user@example.com", "password": "your-password" }
```
Response:
```json
{ "token": "..." }
```

### List hotels
`GET /api/hotels`
Optional query: `location=...`

### Create hotel (requires JWT)
`POST /api/hotels`
Header: `Authorization: Bearer <token>`
Body:
```json
{ "name": "Grand Plaza", "location": "Delhi", "description": "..." }
```

### List available rooms
`GET /api/hotels/{hotelId}/rooms?checkIn=2026-01-01&checkOut=2026-01-05`

### Create room (requires JWT)
`POST /api/hotels/{hotelId}/rooms`
Header: `Authorization: Bearer <token>`
Body:
```json
{ "roomType": "DELUXE", "capacity": 2, "pricePerNight": 120.00 }
```

### Create booking (requires JWT)
`POST /api/bookings`
Header: `Authorization: Bearer <token>`
Body:
```json
{ "roomId": 1, "checkIn": "2026-01-01", "checkOut": "2026-01-05" }
```

### Cancel booking (requires JWT)
`PATCH /api/bookings/{bookingId}/cancel`
Header: `Authorization: Bearer <token>`

### My bookings
`GET /api/bookings/me`
Header: `Authorization: Bearer <token>`

