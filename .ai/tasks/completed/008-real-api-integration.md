# Task: Real API Integration

## Status
- Status: Planned
- Priority: High
- Assigned to: Mobile Developer

## Description
Replace all mocked repository data with actual calls to the Laravel 12 API backend.

## Requirements
- [ ] **Infrastructure**:
    - Update Retrofit service to include all endpoints from `api-spec.md`.
    - Handle network errors and generic HTTP status codes (404, 500, etc.).
- [ ] **Vendor Flow**:
    - `getVendorOrders()`: Fetch from `GET /orders`.
    - `getOrderDetail(id)`: Fetch from `GET /orders/{id}`.
    - Ensure vendor privacy (filtered response) is handled by the API or confirmed in the UI.
- [ ] **Admin Flow**:
    - `getAdminOrders()`: Fetch from `GET /orders`.
    - `getAdminOrderDetail(id)`: Fetch from `GET /orders/{id}`.
    - `updateOrderStatus(id, status)`: Call `PATCH /orders/{id}/status`.
- [ ] **Data Mapping**:
    - Update `Order`, `OrderDetail`, and `OrderItem` models to match the API response JSON structure exactly.
    - Use `Moshi` or `Kotlin Serialization` for JSON parsing.

## Deliverables
- [ ] Fully functional app connected to the live backend.
- [ ] Error handling UI for network failures.
- [ ] Repository layer with no more mock delays.
