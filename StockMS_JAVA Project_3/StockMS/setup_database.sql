-- ============================================================
--  Stock Management System — Database Setup
--  Run this in phpMyAdmin or MySQL CLI BEFORE launching the app.
--  The Java app will auto-create all tables on first run.
-- ============================================================

CREATE DATABASE IF NOT EXISTS stockms
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE stockms;

-- Tables are auto-created by DBInit.java on first startup.
-- Default login: admin / admin123
