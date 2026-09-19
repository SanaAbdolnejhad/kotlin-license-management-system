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

## Android Emulator

When the backend is running locally on the development machine, the Android Emulator accesses the host using:

```text
http://10.0.2.2:8080/
```

Inside the Android Emulator, `10.0.2.2` maps to the host machine's loopback interface.

## Database

The PostgreSQL database uses these main tables:

- `customers`
- `products`
- `licenses`
- `devices`

## Security Notes

Do not commit database passwords, API keys, tokens, keystores, or machine-specific configuration files.

Use environment variables or local configuration for secrets.

## Status

Educational project completed and tested with an Android Emulator, a Ktor backend, and PostgreSQL.
