# Kotlin License Management System

A client-server software licensing project built with Kotlin.

The project contains:

- a **Ktor backend server**
- a **PostgreSQL database**
- an **Android client** built with Jetpack Compose
- device fingerprinting with **SHA-256**
- license provisioning and device-limit checks

## Architecture

```text
Android Client
      |
      | HTTP / JSON
      v
Ktor Server
      |
      v
PostgreSQL
```

The Android application never connects directly to PostgreSQL.  
All license rules and database operations are handled by the Ktor server.

## Tech Stack

### Backend

- Kotlin
- Ktor
- PostgreSQL
- Exposed ORM
- kotlinx.serialization
- Gradle

### Android Client

- Kotlin
- Android Studio
- Jetpack Compose
- Retrofit
- Gson
- Kotlin Coroutines

## Main Features

- Customer and product provisioning
- Automatic license creation
- License activation
- Device registration
- SHA-256 hardware/device fingerprinting
- Duplicate-device prevention
- Maximum-device limit
- License status handling
- License expiration support
- PostgreSQL persistence
- Android Emulator support

## Project Structure

```text
kotlin-license-management-system/
├── server/
│   └── Ktor backend
├── android-client/
│   └── Android application
├── .gitignore
└── README.md
```

## License Flow

1. The Android client collects customer information.
2. Android device information is obtained through Android APIs.
3. A SHA-256 fingerprint is generated from device-related values.
4. The client creates a provision request.
5. Retrofit sends the request to the Ktor backend.
6. The server applies the license rules.
7. Customer, product, license, and device data are stored in PostgreSQL.
8. The server returns the license result to the Android client.

## Main API

```http
POST /license/provision
```

Example request:

```json
{
  "customerName": "Example User",
  "customerEmail": "user@example.com",
  "productName": "License App",
  "productVersion": "1.0",
  "hardwareFingerprint": "DEVICE_SHA256_FINGERPRINT"
}
```

## How to Run

### Prerequisites

Install the following before running the project:

- JDK 22 for the Ktor server
- PostgreSQL
- Android Studio
- Android SDK 33
- An Android Emulator

### 1. Create the PostgreSQL database

Connect to PostgreSQL:

```bash
psql -U postgres -d postgres
```

Create the database:

```sql
CREATE DATABASE license_system;
```

Then exit PostgreSQL:

```text
\q
```

The server creates the required tables automatically when it starts.

### 2. Set the database password

The PostgreSQL password is intentionally not stored in the repository.

Set it as an environment variable:

```bash
export DB_PASSWORD='your_postgresql_password'
```

If you run the server from IntelliJ IDEA, add the same variable under:

```text
Run → Edit Configurations → Environment variables
```

Use:

```text
DB_PASSWORD=your_postgresql_password
```

### 3. Run the Ktor server

From the repository root:

```bash
cd server
./gradlew run
```

The server starts on:

```text
http://localhost:8080
```

Opening the root endpoint should return:

```text
License Server Running
```

### 4. Run the Android client

1. Open the `android-client` folder in Android Studio.
2. Let Gradle sync complete.
3. Start an Android Emulator.
4. Keep the Ktor server running on the host machine.
5. Run the Android app.

The Android Emulator accesses the local server through:

```text
http://10.0.2.2:8080/
```

Inside the Android Emulator, `10.0.2.2` maps to the host machine's loopback interface.

### 5. Activate a license

Enter a customer name and email in the Android app and press:

```text
ACTIVATE LICENSE
```

The client generates the device fingerprint, sends the provisioning request to the server, and displays the returned license information.

## Database

The PostgreSQL database uses these main tables:

- `customers`
- `products`
- `licenses`
- `devices`

## Security Notes

Database passwords, API keys, tokens, keystores, machine-specific files, and build output should not be committed.

The server reads the PostgreSQL password from the `DB_PASSWORD` environment variable.

The Android client currently uses plain HTTP for local emulator development. Production deployments should use HTTPS.

## Status

Educational project completed and tested with an Android Emulator, a Ktor backend, and PostgreSQL.
