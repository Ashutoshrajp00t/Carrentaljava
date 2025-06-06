# Car Rental System

A desktop application built using Java Swing that manages a car rental inventory with full CRUD (Create, Read, Update, Delete) functionality. It connects to a MySQL database using JDBC and provides a simple and intuitive user interface for rental management.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Database Setup](#database-setup)
- [Sample Data](#sample-data)
- [How to Run](#how-to-run)
- [Usage Guide](#usage-guide)
- [Exception Handling & Validation](#exception-handling--validation)
- [License](#license)
- [Author](#author)

---

## Features

- **Add Car:** Add a new car to the system with unique ID, make, model, year, and availability status.
- **View Cars:** Display all cars in a sortable and searchable table.
- **Update Car:** Modify existing car details (except the ID).
- **Delete Car:** Remove a car from the system by its ID.
- **Rent Car:** Mark a car as rented (unavailable), preventing it from being rented again until returned.
- **Clear Fields:** Quickly clear all input fields on the form.
- **Refresh List:** Reload the car list from the database to reflect the latest data.
- **Robust Exception Handling:** The app gracefully handles database errors, invalid input, and other runtime exceptions, showing user-friendly messages.
- **Input Validation:** Ensures critical fields are filled and that the year is within a realistic range (1900 - 2100).
- **Responsive UI:** Simple and clean Swing GUI with intuitive controls.

---

## Technologies Used

- Java SE 8+ with Swing GUI toolkit
- JDBC for database connectivity
- MySQL for data storage
- MVC design pattern with DAO for data operations

---

## Project Structure

- `model/Car.java`  
  Java class representing the car entity with attributes and methods.

- `dao/CarDAO.java`  
  Handles all SQL operations (CRUD) against the `cars` table.

- `ui/CarRentalUI.java`  
  Swing-based user interface managing user interactions.

- `util/DBUtil.java`  
  Utility class for managing MySQL connections.

- `Main.java`  
  Entry point that launches the Swing UI.

---

## Database Setup

Make sure MySQL is installed and running on your system. Use the following SQL commands to create the required database and table:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS car_rental;

USE car_rental;

-- Create 'cars' table
CREATE TABLE IF NOT EXISTS cars (
    id VARCHAR(50) PRIMARY KEY,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year INT NOT NULL CHECK (year BETWEEN 1900 AND 2100),
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);
INSERT INTO cars (id, make, model, year, is_available) VALUES
-- Regular Cars
('CAR101', 'Toyota', 'Corolla', 2020, TRUE),
('CAR102', 'Honda', 'Civic', 2019, FALSE),
('CAR103', 'Hyundai', 'Creta', 2022, TRUE),
('CAR104', 'Ford', 'Ecosport', 2021, TRUE),
('CAR105', 'Tata', 'Nexon', 2023, FALSE),
('CAR106', 'Mahindra', 'XUV700', 2022, TRUE),
('CAR107', 'Suzuki', 'Swift', 2020, TRUE),
('CAR108', 'Kia', 'Seltos', 2021, FALSE),
('CAR109', 'Renault', 'Kwid', 2023, TRUE),
('CAR110', 'Volkswagen', 'Polo', 2018, TRUE),

-- Luxury Cars for Tourists
('CAR201', 'Mercedes-Benz', 'E-Class', 2022, TRUE),
('CAR202', 'BMW', '5 Series', 2021, FALSE),
('CAR203', 'Audi', 'A6', 2023, TRUE),
('CAR204', 'Jaguar', 'XF', 2020, TRUE),
('CAR205', 'Lexus', 'ES 300h', 2022, TRUE);
