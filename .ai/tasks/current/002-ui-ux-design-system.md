# Task: UI/UX Design System & Theme Development

## Status
- Status: Planned
- Priority: Medium
- Assigned to: UI/UX Engineer (AI)

## Description
Establish a "rich-looking", heritage-focused design system for the LoomCraft Admin app using Jetpack Compose and Material 3. The design must align with the existing LoomCraft web identity: warm, tactile, and premium.

## Key Elements
- [ ] **Color Palette (LoomCraft Warm)**:
    - Primary: `BrandStrong` (#2B241C) - Deep Brown/Charcoal.
    - Surface: `SurfaceBase` (#FDF8F0) - Warm Cream.
    - Accent: `BrandAccent` (#B6623A) - Terracotta/Orange for highlights.
    - Avoid: Pure black, pure white, and "tech" blues.
- [ ] **Typography**:
    - Main UI: Use `Instrument Sans` or a clean sans-serif equivalent.
    - Brand/Display: Use `Playfair Display` (serif) sparingly for headers or splash screens.
- [ ] **Shape & Elevation**:
    - High rounding: 20dp to 28dp for cards; pill-shaped buttons.
    - Border-first design: Use `BorderDefault` (#D4B28C) to define sections rather than heavy shadows.
- [ ] **Components**:
    - Order Status Tags: Muted, warm variants (e.g., `AccentSoft` for pending).
    - Modern Card layouts with generous padding and soft corners.
- [ ] **Responsive Layouts**: Use Adaptive Layouts to support both mobile phones and tablets.

## Deliverables
- [ ] `Theme.kt`, `Color.kt`, `Type.kt`, and `Shape.kt` implementation.
- [ ] Custom Compose components for LoomCraft-style Buttons, Cards, and Status Tags.
- [ ] UI documentation reference in `.ai/knowledge/ui-ux-design-system.md`.
