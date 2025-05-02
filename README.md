                                                     🛒 E-Commerce Backend API
This is a production-ready e-commerce backend built using Spring Boot, Spring Security, PostgreSQL, and containerized using Docker.
The project demonstrates clean architecture principles, RESTful API design, and secure user authentication and authorization flows.

🔍 Project Overview
This backend serves as the foundation for a full-featured e-commerce platform and includes core features like:

🔐 User authentication & authorization (JWT-based, with Spring Security and oauth)

👤 Role-based access control (Admin, Seller, Customer)

📦 Product management (CRUD operations, category tagging, stock handling)

🛒 Shopping cart & order processing

💳 Checkout logic and payment placeholder integration

🧾 Order history and user profile

✅ Robust validation & exception handling

🐳 Dockerized for easy deployment

🧰 Tech Stack

| Layer           | Technology                         |
|----------------|-------------------------------------|
| Backend         | Spring Boot (Java 21)              |
| Security        | Spring Security + JWT              |
| Database        | PostgreSQL                         |
| ORM             | Spring Data JPA                    |
| Testing         | JUnit, Mockito                     |
| Containerization| Docker                             |
| API Style       | RESTful                            |
| Documentation   | Swagger/OpenAPI (optional)         |


🚀 Features Breakdown

🔐 Authentication
Signup & login with secure password hashing

JWT token-based access control

Refresh token support (optional)

Role-based endpoints

📦 Product & Category Management
Add, update, delete products

Categorization and filtering

Pagination support

🛒 Cart & Orders

Add/remove items to/from cart

Place and track orders

Order status updates (admin)

📊 Admin Features
Manage users, products, and orders

View system metrics and reports

⚙️ Setup & Deployment

🔧 Prerequisites
Java 21+
Maven
Docker & Docker Compose

🐳 Run with Docker

```
 docker-compose up --build 
```

🛠 Manual Setup (without Docker)
```
# Clone repo
https://github.com/AmarjeetMahato/E-commerce-backend.git
cd ecommerce-backend

# Build and run
./mvnw spring-boot:run
```

📂 Project Structure

```
src/
├── config          → Security and CORS configuration
├── controller      → API controllers
├── dto             → Request & response DTOs
├── entity          → JPA entities
├── exception       → Custom error handling
├── repository      → Spring Data JPA repositories
├── service         → Business logic layer
└── util            → Utility classes (e.g. token provider)

```

### 🛡 Security

- ✅ Uses **Spring Security** filters for authentication and authorization  
- 🔐 Implements **JWT token parsing** for stateless authentication  
- 👥 Enforces **role-based access control** (e.g., Admin, Seller, Customer)  
- 🔒 Uses **BCrypt** for secure password hashing  
- 🚫 **CSRF protection** disabled (since the API is stateless and uses JWT)  

---

### 🧪 Testing

- 🧪 **Unit tests** using JUnit and Mockito  
- 🔄 **Integration tests** for service and controller layers  
- 📈 **Test coverage** includes services, controllers, utilities, and edge cases  
- 🔍 Mocking external dependencies using Mockito for isolated testing  













