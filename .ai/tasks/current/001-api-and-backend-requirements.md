# Task: Define API Contract and Backend Requirements

## Status
- Status: In Progress
- Priority: High
- Assigned to: AI Agent (Mobile) & Backend Agent

## Description
Since the backend API is not yet created, we need to define the exact requirements for the `https://loomcraft.work/api/v1` endpoints. This document will serve as a bridge for the Backend AI agent to implement the necessary logic in the Laravel 12 application.

## Requirements for Backend
- [x] **API Specification Document**: Create a detailed guide for the Backend AI Agent.
- [ ] **Authentication API**: Login endpoint returning a JWT or Bearer token along with user role (`admin` or `vendor`).
- [ ] **Order List (Vendor)**:
    - Endpoint: `GET /vendor/orders`
    - Filter: Only return orders containing at least one product belonging to the authenticated vendor.
- [ ] **Order Status Update (Vendor)**:
    - Endpoint: `PATCH /vendor/orders/{id}/status`
- [ ] **Notifications**: 
    - Integration with FCM (Firebase Cloud Messaging).

## Deliverables
- [x] A detailed Markdown document in [`.ai/knowledge/api-spec.md`](../../knowledge/api-spec.md) for the Backend Agent.
