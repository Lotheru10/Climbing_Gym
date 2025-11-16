# Climbing Gym — Reservation System (Full-Stack Project)
Authors: Zuzanna Jedynak and Jakub Prygiel\
Course: Databases 2 AGH WI

# Description
A full-stack application designed to manage reservations for a climbing gym.
The backend is built with Spring Boot and MongoDB, frontend uses a React + Vite.
We made this project to practise work with document-oriented database, REST API design, transactional logic, and concurrency control.

# Core Technology Stack
+ Spring Boot
+ MongoDB
+ Postman (API testing)
+ React + Vite

# Features
+ Three MongoDB collections (users, time_slots, entry_types) structured with nested documents to minimize lookup operations and use document-oriented database to its fully potential.
+ Complete CRUD operations for users, entries, and reservation entities
+ Transactional operations and concurrency control to ensure consistency during concurrent access.
+ Raports generator for quick summarized lookup
+ Frontend to present our application

# How to run
First download full project, then open frontend catalog (climbing-gym-frontend) and use following command: npm run dev

___
For more details check out our documentation - Sprawozdanie.md (written in polish)

