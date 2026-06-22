# REST API Documentation

## Overview

StreetArt Live uses a REST API for communication between the frontend interface and the Spring Boot backend.

Base URL for local development:

```text
http://localhost:8080
```

The API supports:

* User registration and authentication
* User profile editing
* Artist search and profile management
* Event creation, filtering and moderation
* Interactive map data loading
* Subscriptions to artists
* Media upload through Cloudinary

---

## Authentication

### Register User

```http
POST /api/auth/register
```

Creates a new user account.

Request body example:

```json
{
  "username": "viewer01",
  "email": "viewer@example.com",
  "password": "password123",
  "role": "user",
  "name": "Sofiia",
  "surname": "Kryvets",
  "phone": "+380000000000",
  "birthDate": "01.01.2000"
}
```

For artist registration, the request may also include artist-specific fields such as nickname, genre, city and description.

---

### Login User

```http
POST /api/auth/login
```

Authenticates a user.

Request body example:

```json
{
  "email": "viewer@example.com",
  "password": "password123"
}
```

---

## Users

### Update User Profile

```http
PUT /api/users/{id}
```

Updates user profile data.

Request body example:

```json
{
  "name": "Sofiia",
  "surname": "Kryvets",
  "phone": "+380000000000",
  "username": "sofiia",
  "email": "sofiia@example.com",
  "birthDate": "01.01.2000"
}
```

---

## Artists

### Get Artists

```http
GET /api/artists
```

Returns a list of artists.

Supported query parameters:

| Parameter | Description                  |
| --------- | ---------------------------- |
| search    | Search by artist information |
| genre     | Filter by artist genre       |
| city      | Filter by city               |

Example:

```http
GET /api/artists?search=music&city=Sumy
```

---

### Create Artist

```http
POST /api/artists
```

Creates an artist profile.

---

## Artist Account

### Get Artist Account By User ID

```http
GET /api/artist-account/{userId}
```

Returns artist profile data connected to a specific user account.

---

### Upload Artist Avatar

```http
POST /api/artist-account/{userId}/avatar
```

Uploads or replaces an artist avatar.

Request type:

```text
multipart/form-data
```

Form field:

| Field | Type | Description |
| ----- | ---- | ----------- |
| file  | File | Image file  |

---

### Get Artist Media

```http
GET /api/artist-account/{userId}/media
```

Returns media files uploaded by the artist.

---

### Upload Artist Media

```http
POST /api/artist-account/{userId}/media
```

Uploads an image or video file for an artist profile.

Request type:

```text
multipart/form-data
```

Form field:

| Field | Type | Description         |
| ----- | ---- | ------------------- |
| file  | File | Image or video file |

---

### Delete Artist Media

```http
DELETE /api/artist-account/media/{mediaId}
```

Deletes an artist media file.

---

## Events

### Get Events

```http
GET /api/events
```

Returns a list of events.

Supported query parameters:

| Parameter | Description                           |
| --------- | ------------------------------------- |
| search    | Search by event or artist information |
| date      | Filter by event date                  |
| time      | Filter by event time                  |

Example:

```http
GET /api/events?search=center&date=2026-06-20
```

---

### Create Event Request

```http
POST /api/events
```

Creates a new event request.

Request body example:

```json
{
  "userId": 1,
  "place": "Soborna Street 23, Sumy",
  "eventDate": "2026-06-20",
  "time": "18:00",
  "comments": "Acoustic street performance"
}
```

---

### Get Events For Map

```http
GET /api/events/map
```

Returns approved events with coordinates for the interactive map.

---

### Get Pending Events

```http
GET /api/events/pending
```

Returns event requests waiting for administrator moderation.

---

### Approve Event

```http
PUT /api/events/{eventId}/approve
```

Approves an event request.

After approval, the event becomes available in the event list and on the interactive map.

---

### Reject Event

```http
PUT /api/events/{eventId}/reject
```

Rejects an event request.

Request body example:

```json
{
  "comment": "Incorrect event location"
}
```

---

### Get Events By User ID

```http
GET /api/events/user/{userId}
```

Returns events created by a specific user.

---

## Subscriptions

### Subscribe To Artist

```http
POST /api/subscriptions
```

Creates a subscription between a viewer and an artist.

Request body example:

```json
{
  "viewerId": "1",
  "artistId": "2"
}
```

---

### Unsubscribe From Artist

```http
DELETE /api/subscriptions
```

Removes a subscription.

Request body example:

```json
{
  "viewerId": "1",
  "artistId": "2"
}
```

---

### Check Subscription Status

```http
GET /api/subscriptions/check?viewerId={viewerId}&artistId={artistId}
```

Checks whether a viewer is subscribed to an artist.

Example:

```http
GET /api/subscriptions/check?viewerId=1&artistId=2
```

---

## Health Check

### Check Backend Status

```http
GET /api/health
```

Returns backend health status.

Response example:

```json
{
  "ok": true
}
```

---

## Internal / Debug Endpoint

### Get First Event ID

```http
GET /api/first-match-id
```

Returns the first event ID from the database.

This endpoint is used for development/debugging purposes and is not part of the main user workflow.

---

## Common Response Format

Most successful responses return JSON.

Example:

```json
{
  "id": 1,
  "status": "approved",
  "message": "Operation completed successfully"
}
```

---

## Error Handling

The API may return error responses in JSON format.

Example:

```json
{
  "message": "Validation error message"
}
```

Common HTTP status codes:

| Code | Meaning                         |
| ---- | ------------------------------- |
| 200  | Successful request              |
| 400  | Bad request or validation error |
| 404  | Resource not found              |
| 500  | Server error                    |

---

## Notes

* The frontend communicates with these endpoints through HTTP requests.
* Event moderation is performed by the administrator.
* Approved events are displayed in the event list and on the Leaflet interactive map.
* Artist media and avatars are stored using Cloudinary.
* Email notifications are sent to subscribers after event approval.
