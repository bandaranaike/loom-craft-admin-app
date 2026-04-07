# Task: Authentication Integration with Laravel API

## Status
- Status: Completed
- Priority: High
- Assigned to: Mobile Developer

## Description
Implement the login flow and secure the application using Laravel Sanctum as specified in the API documentation.

## Requirements
- [x] **Data Layer**:
    - Add Retrofit or Ktor-client for API communication.
    - Create `AuthApiService` with a `POST /login` endpoint.
    - Implement an `AuthRepository` to handle login and token storage.
- [x] **Secure Storage**:
    - Use `EncryptedSharedPreferences` or `DataStore` to store the Sanctum Bearer token securely.
- [x] **UI Implementation**:
    - Create a `LoginScreen` following the "LoomCraft Warm" design system.
    - Handle loading, error (invalid credentials), and success states.
- [x] **Auth Interceptor**:
    - Add an `OkHttp` Interceptor to automatically attach the `Authorization: Bearer <token>` header to all outgoing requests.
- [x] **Session Management**:
    - Redirect to `LoginScreen` if the token is missing or expired.
    - Implement a "Logout" functionality.
    - Persist remembered sessions and auto-load the dashboard on relaunch when remember-me is enabled.

## Deliverables
- [x] Working Login Screen.
- [x] Secure token storage implementation.
- [x] Authenticated API client.
