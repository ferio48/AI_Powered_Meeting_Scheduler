# 🧠 AI-Powered Meeting Scheduler

This project is a full-featured **Meeting Scheduling System** enhanced with **AI assistance (Gemini API)** that extracts meeting details from natural language, stores appointments, and automatically sends **email reminders** 1 hour before meetings.

---

## 🚀 Features

- 🔐 **Authentication & Authorization**
    - JWT-based login & registration
    - Role & permission-based access control (`USER_CREATE`, `MANAGER_CREATE`)

- 💬 **AI-Powered Conversation Flow**
    - Uses Gemini API to extract structured meeting info from user messages
    - Handles missing fields with intelligent follow-up questions

- 📆 **Meeting Scheduling**
    - Schedules landlord–tenant property viewings
    - Stores appointments with date/time validation

- 📧 **Email Reminders**
    - Automatically sends email notifications 1 hour before meetings

- 🌐 **Internationalization**
    - Supports message localization using `MessageSource`

---

## 🛠️ Tech Stack

| Layer         | Technology                        |
|---------------|------------------------------------|
| Language      | Java 17                            |
| Framework     | Spring Boot 3.4.x                  |
| Security      | Spring Security + JWT              |
| AI            | Gemini 2.0 API                     |
| Scheduler     | `@Scheduled` reminders             |
| Email         | Spring Boot Starter Mail           |
| Database      | JPA + Hibernate (H2/MySQL)         |
| Build Tool    | Maven                              |

---

## ⚙️ Configuration

Update the following in your `application.properties`:

```properties
# Gemini
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=
gemini.api.key=YOUR_GEMINI_API_KEY

# JWT
security.secret.key=YOUR_SECRET_KEY
security.accessToken.expiration.time=86400000
security.refreshToken.expiration.time=2592000000
security.resetPasswordToken.expiration.time=3600000

# Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

```
## 🧪 Running the Application

```bash
# Build the project
mvn clean install

# Run the Spring Boot app
mvn spring-boot:run

```

## 📬 API Endpoints

| Method | Endpoint                | Description                         |
|--------|-------------------------|-------------------------------------|
| POST   | `/api/v1/auth/register` | Register a new user                 |
| POST   | `/api/v1/auth/login`    | Authenticate and receive JWT token  |
| POST   | `/meeting/schedule`     | Start or continue AI appointment    |



### 📥 Example Request for `/meeting/schedule`

```
json
  {
    "text": "Example Text"
  }
```

## ⏰ Reminder Scheduler

- Runs every minute using `@Scheduled(cron = "0 * * * * *")`
- Sends an email reminder when a meeting is **1 hour away**
- Skips meetings where `reminderSent = true` to avoid duplicate emails

---

## 🧠 Gemini AI Behavior

The assistant:

- Extracts appointment info:
  - `appointmentDate`
  - `appointmentTime`
  - `propertyAddress`
  - `landlordName`
  - `landlordContact`
  - `tenantName`
  - `tenantContact`
- Understands natural language (e.g., “next Monday at 2 PM”)
- Asks only for missing or unclear information
- Returns a complete valid JSON object once all fields are collected
