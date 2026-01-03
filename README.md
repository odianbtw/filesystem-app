# 📂 Filesystem REST API application
A Spring Boot application that exposes the filesystem structure (directories and files) via a REST API.

The API allows clients (e.g. frontend UI applications) to:

- Browse directory trees

- Inspect files and directories metadata

This project is designed with clean architecture, clear API contracts, and strong testing practices in mind.

# 🧱 Tech Stack

- ☕ Java 21

- 🌱 Spring Boot

- 📘 OpenAPI (OAS / Swagger)

- 🐳 Docker

- 🧪 JUnit 5, Mockito

- 📂 java.nio.file (modern filesystem API)

# ▶️ How to Run

🐳 Run with Docker

Build the image: `docker build -t filesystem-app .`

Run the container: `docker run -p 8080:8080 filesystem-app`

The application will be available at: `http://localhost:8080`
