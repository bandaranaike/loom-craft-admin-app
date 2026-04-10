# Task Inbox

Add your tasks here in your own words. I will periodically check this file and convert them into structured task files in the `.ai/tasks/planned` folder.

## New Tasks

- [x] Initial Project Requirements (Processed into tasks 001-005)
- [x] UI Refinements & Multi-Currency Support (Processed into Task 006)
- [x] Authentication Integration (Processed into Task 007)
- [x] Real API Integration (Processed into Task 008)
- [ ] I logged in to the dashboard using admin email and password
    - The admins dashboard is not showing all the order details which suppose to show.
- [x] Codex session fixes applied on 2026-04-07
    - Aligned mobile app endpoints to Postman collection under `.ai/resources/loom-craft-public-api.postman_collection.json`.
    - Fixed admin order list/detail JSON mapping and removed admin-side customer masking.
    - Added remember-me login option, app auto-login, and logout actions on order list screens.
- [x] I have the following changes to do in the app: (Processed into Task 010)
    - The 'paid' status is not visible in the light theme. text dark while background is dark.
    - The currencies not should use Indian rupee currency code. It should be LKR in everywhere
    - You may have access to the database schema (.ai/resources/db-schema.md) of the API. We need to show product images using 'product_media' table in the
      order view page. It easy to identify the correct product by the vendor or admin. the image can be shown to admin and vendor both. To show the image you
      may need some extra details on the API endpoint. Please update them in .ai/knowledge/api-spec.md file. I will do the changes from the API side. 
    - When try to log in with wrong credentials, It shows code error message as "Use JsonReader.setLenient(true) to accept malformed JSON at path $"
