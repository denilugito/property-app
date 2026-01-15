# 🏠 Property App – Secure Backend API

A Spring Boot REST API for a property listing application, featuring **JWT authentication with refresh token rotation**, role-based access, and clean security architecture.

This project is designed as a **portfolio-ready backend** demonstrating modern authentication best practices.

---

## 🚀 Features

- ✅ User authentication with **JWT (Access Token)**
- 🔁 **Refresh Token rotation** (secure re-issue of access tokens)
- 🔒 Token revocation & expiration handling
- 🧑‍💼 Role-based authorization
- 🏘️ Property CRUD APIs (secured)
- ⚙️ Environment-based configuration using Spring Profiles

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot**
- Spring Security
- JWT (io.jsonwebtoken)
- PostgreSQL
- JPA / Hibernate
- Maven

---

## 🔐 Authentication Flow (JWT + Refresh Token)

1. User logs in with username & password
2. Backend returns:
   - `accessToken` (short-lived JWT)
   - `refreshToken` (long-lived, stored in DB)
3. Frontend uses `accessToken` for API requests
4. When access token expires:
   - Frontend calls `/auth/refresh`
   - Backend validates refresh token
   - Issues a new access token
   - Revokes old refresh token (rotation)
5. User stays logged in without re-authentication

---

## 📁 Project Structure

```text
com.realestate.propertyapp
├── config
│   └── SecurityConfig
├── security
│   ├── JwtUtil
│   ├── JwtAuthenticationFilter
│   ├── entity
│   │   └── RefreshToken
│   ├── repository
│   │   └── RefreshTokenRepository
│   └── service
│       └── RefreshTokenService
├── property
│   ├── controller
│   ├── entity
│   ├── repository
│   └── service
└── user
    ├── entity
    └── repository
