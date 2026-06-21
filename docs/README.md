# StreetArt Live

StreetArt Live is a client-server application designed to popularize street art by connecting artists and audiences through a centralized digital platform.

The application allows users to discover cultural events, explore artist profiles, subscribe to performers, and view approved events on an interactive map. Artists can manage their profiles and submit event requests, while administrators moderate content and manage platform activity.

---

## Features

### User Features

* User registration and authentication
* Artist discovery and profile browsing
* Search and filtering of artists
* Search and filtering of events
* Interactive event map
* Artist subscriptions
* Email notifications about approved events

### Artist Features

* Artist profile management
* Avatar upload and management
* Event request submission
* Viewing personal event requests and statuses
* Subscriber engagement through notifications

### Administrator Features

* Event moderation system
* Approval and rejection of event requests
* Moderation comments
* Platform content management

---

## Technology Stack

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* REST API

### Database

* PostgreSQL

### Frontend

* HTML5
* CSS3
* JavaScript

### Additional Technologies

* Leaflet.js
* JavaFX WebView
* Cloudinary
* JavaMailSender

---

## Architecture

StreetArt Live follows a client-server architecture.

```text
Frontend (HTML/CSS/JavaScript)
            │
            ▼
        REST API
            │
            ▼
 Backend (Spring Boot)
            │
            ▼
 PostgreSQL Database
```

Additional integrations:

* Leaflet.js for interactive map visualization
* Cloudinary for media storage
* SMTP email notifications
* JavaFX WebView for desktop integration

Detailed documentation:

* [Architecture](docs/architecture.md)
* [Database](docs/database.md)
* [API Documentation](docs/api.md)
* [Setup Guide](docs/setup.md)

---

## User Roles

### Viewer

* Browse artists
* Browse events
* Subscribe to artists
* Receive notifications

### Artist

* Manage personal profile
* Submit event requests
* View moderation status

### Administrator

* Review event requests
* Approve or reject events
* Manage platform content

---

## Screenshots

### Home Page

![Home](docs/screenshots/main.png)

### Interactive Map

![Map](docs/screenshots/map.png)

### Events Page

![Events](docs/screenshots/events.png)

### Artists Directory

![Artists](docs/screenshots/artists.png)

### Artist Profile

![Artist Profile](docs/screenshots/artist-profile.png)

### Event Moderation

![Moderation](docs/screenshots/moderation.png)

---

## Project Structure

```text
streetart-live
│
├── backend
├── desktop
├── shared
├── docs
│   ├── architecture.md
│   ├── database.md
│   ├── api.md
│   ├── setup.md
│   └── screenshots
│
├── pom.xml
└── README.md
```

---

## Local Setup

Detailed installation instructions are available in:

[Setup Guide](docs/setup.md)

---

## Author

Sofiia Kryvets

Bachelor's Degree in Computer Science

Sumy State University
 
