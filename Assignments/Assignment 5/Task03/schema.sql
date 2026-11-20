CREATE DATABASE IF NOT EXISTS persistence_assignment;
USE persistence_assignment;
CREATE TABLE IF NOT EXISTS student(
 student_id INT PRIMARY KEY,
 register_no VARCHAR(30) NOT NULL UNIQUE,
 full_name VARCHAR(120) NOT NULL,
 email VARCHAR(160) NOT NULL
);
CREATE TABLE IF NOT EXISTS bus_pass(
 pass_id VARCHAR(30) PRIMARY KEY,
 register_no VARCHAR(30) NOT NULL,
 holder_name VARCHAR(120) NOT NULL,
 route_no INT NOT NULL,
 boarding_stop VARCHAR(120) NOT NULL,
 valid_until DATE NOT NULL,
 amount_paid DECIMAL(10,2) NOT NULL CHECK(amount_paid>=0)
);