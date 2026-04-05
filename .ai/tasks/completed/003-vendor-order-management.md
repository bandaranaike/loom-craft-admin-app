# Task: Vendor Order Management Flow

## Status
- Status: Completed
- Priority: High
- Assigned to: Mobile Developer (Kotlin/Compose)

## Description
Implement the end-to-end flow for Vendors to view and manage their portion of an order.

## Requirements
- [x] **Order List Screen**:
    - Display orders where the vendor has at least one product.
    - List items must show: Product Name, Quantity, Price, and Current Status.
    - **UI Note**: Use a clean, modern list or card layout.
- [x] **Order Detail Screen**:
    - Detailed view of the vendor's items in the order.
    - **Privacy Constraint**: Hide all customer details (Address, Phone, Name).
- [x] **Status Management**:
    - Add UI to update status to: `Accepted`, `Rejected`, `Processing`, or `Hand over to admin`.
- [ ] **Real-time Notifications**:
    - Implement a listener/service to notify the vendor when a new order arrives (Wait for FCM API).

## Deliverables
- [x] Vendor Order List Screen.
- [x] Vendor Order Detail Screen.
- [x] Status Update Logic (Mocked initially until API is ready).
