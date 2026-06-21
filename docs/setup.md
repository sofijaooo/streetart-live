# Local Setup Guide

## Requirements

Before running the project, ensure the following software is installed:

* Java 17+
* Maven
* PostgreSQL
* Git
* IntelliJ IDEA (recommended)

---

## Clone Repository

```bash
git clone https://github.com/sofijaooo/streetart-live.git
cd streetart-live
```

---

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE streetart_live;
```

Configure database connection properties in:

```text
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/streetart_live
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Cloudinary Configuration

Provide Cloudinary credentials:

```properties
cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET
```

---

## Email Configuration

Provide SMTP credentials:

```properties
spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD
```

---

## Build Project

```bash
mvn clean install
```

---

## Run Backend

```bash
mvn spring-boot:run
```

Backend will start on:

```text
http://localhost:8080
```

---

## Run Desktop Client

Launch the JavaFX application from the desktop module.

The JavaFX WebView component loads and renders the web interface inside a desktop window.

---

## Features Available After Launch

* User registration
* Authentication
* Artist profiles
* Event management
* Interactive map
* Event moderation
* Artist subscriptions
* Email notifications

---

## Troubleshooting

### Database Connection Error

Verify:

* PostgreSQL service is running
* Database exists
* Credentials are correct

### Email Notifications Not Working

Verify:

* SMTP settings
* Application password
* Network access

### Media Upload Issues

Verify:

* Cloudinary configuration
* Internet connection

---

## Project Structure

```text
backend/
desktop/
shared/
docs/
```
