# Task: Authentication Integration with Laravel API

## Status
- Status: Planned
- Priority: High
- Assigned to: Mobile Developer

## Description
Implement the login flow and secure the application using Laravel Sanctum as specified in the API documentation.

## Requirements
- [ ] **Data Layer**:
    - Add Retrofit or Ktor-client for API communication.
    - Create `AuthApiService` with a `POST /login` endpoint.
    - Implement an `AuthRepository` to handle login and token storage.
- [ ] **Secure Storage**:
    - Use `EncryptedSharedPreferences` or `DataStore` to store the Sanctum Bearer token securely.
- [ ] **UI Implementation**:
    - Create a `LoginScreen` following the "LoomCraft Warm" design system.
    - Handle loading, error (invalid credentials), and success states.
- [ ] **Auth Interceptor**:
    - Add an `OkHttp` Interceptor to automatically attach the `Authorization: Bearer <token>` header to all outgoing requests.
- [ ] **Session Management**:
    - Redirect to `LoginScreen` if the token is missing or expired.
    - Implement a "Logout" functionality.

## Deliverables
- [ ] Working Login Screen.
- [ ] Secure token storage implementation.
- [ ] Authenticated API client.
