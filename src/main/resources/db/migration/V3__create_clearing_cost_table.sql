   -- This file creates the clearing_cost table
   CREATE TABLE IF NOT EXISTS clearing_cost (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       country_code VARCHAR(10) NOT NULL UNIQUE,
       cost DECIMAL(10, 2) NOT NULL
   );