# Task: UI Refinements & Multi-Currency Support

## Status
- Status: Planned
- Priority: Medium
- Assigned to: Mobile Developer

## Description
Address visibility issues in the UI and ensure the application correctly handles multiple currencies as defined in the database schema.

## Requirements
- [ ] **UI Visibility Fixes**:
    - Identify and fix instances of dark text on dark backgrounds (e.g., in `StatusTag` or `LoomCraftCard` during theme transitions).
    - Ensure contrast ratios meet accessibility standards for both Light and Dark modes.
- [ ] **Multi-Currency Support**:
    - Use the currency information from `OrderDetail` and `Order` models.
    - Implement a formatter that handles different currency symbols (₹, $, etc.) based on the order data.
    - Reference `.ai/resources/db-schema.md` for `exchange_rates` and `payments` table structures if conversion is needed client-side.

## Deliverables
- [ ] Fixed UI components with proper contrast.
- [ ] Currency formatting utility.
- [ ] Updated Order screens displaying correct currency symbols.
