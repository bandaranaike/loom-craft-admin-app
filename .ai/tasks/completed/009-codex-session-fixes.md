# Task: Codex Session Fixes - Orders API Alignment and Auth Persistence

## Status
- Status: Completed
- Priority: High
- Assigned to: Mobile Developer (Codex session)

## Description
Capture the fixes completed during the 2026-04-07 Codex session so future agents do not rely on stale API assumptions.

## Completed Work
- [x] Updated mobile API usage to match `.ai/resources/loom-craft-public-api.postman_collection.json`.
- [x] Changed admin and vendor order list/detail endpoints to shared `/api/v1/orders` routes.
- [x] Updated login request payload to include `device_name`.
- [x] Updated notification registration endpoint to `POST /api/v1/notifications/register`.
- [x] Added Moshi field mappings for snake_case order payload fields such as `public_id`, `customer_name`, `items_count`, `created_at`, `product_name`, and `unit_price`.
- [x] Removed customer-data masking from admin order list and detail screens.
- [x] Added admin-safe empty/error fallbacks instead of silently rendering blank screens.
- [x] Added remember-me support with persisted email and auth session auto-load.
- [x] Added logout actions to admin and vendor order list screens.

## Notes
- Current mobile API contract is better represented by the Postman collection than the older task notes.
- Auto-login is only used when `remember_me` is enabled. Logging out clears the stored auth session.
- The next backend shape check, if needed, should use a real `/api/v1/orders/{id}` response sample.
