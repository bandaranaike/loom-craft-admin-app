# Task: Order Visuals, Currency Standardization, and Login Error Handling

## Status
- Status: Completed
- Priority: High
- Assigned to: Mobile Developer

## Description
Fix recent app issues affecting order readability, currency consistency, product identification in order views, and invalid-login feedback. This task also includes updating the backend API notes so the API can expose the product media details required by the mobile app.

## Requirements
- [x] **Paid Status Visibility**:
    - Fix the `paid` status chip/tag styling in Light theme so the text remains readable against its background.
    - Review all order status variants in both Light and Dark themes to avoid similar contrast regressions.
- [x] **Currency Standardization**:
    - Replace Indian Rupee usage with Sri Lankan Rupee (`LKR`) everywhere in the app.
    - Update currency code, symbol, and formatting logic in list, detail, summary, and any total/payment-related UI.
- [x] **Product Images in Order View**:
    - Show product images in the order detail view for both admin and vendor users.
    - Use API data derived from the `product_media` table, matched to the correct ordered product so staff can identify items quickly.
    - If the current order endpoints do not expose enough media information, document the required response fields in [`.ai/knowledge/api-spec.md`](../../knowledge/api-spec.md) for backend implementation.
- [x] **Login Error Handling**:
    - When credentials are invalid, show a clear user-facing authentication error instead of the raw JSON parsing message `Use JsonReader.setLenient(true) to accept malformed JSON at path $`.
    - Harden login response parsing so non-success or malformed error bodies do not surface low-level parser messages to the UI.

## Deliverables
- [x] Readable order status tags in Light and Dark themes.
- [x] App-wide `LKR` currency formatting.
- [x] Order detail screens support product images for admin and vendor flows.
- [x] Updated [`.ai/knowledge/api-spec.md`](../../knowledge/api-spec.md) with required product media fields for order endpoints.
- [x] Friendly invalid-login error handling in the authentication flow.

## Notes
- Product image rendering is implemented on mobile and will display as soon as the backend includes `image_url` and/or `product_media` fields on order detail items.
- Verified with `.\gradlew.bat :app:assembleDebug` on 2026-04-10.
