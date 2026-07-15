# Implementation Plan - CEMA v2 UI Update and Icon Customization

This plan addresses the UI update to reflect "v2" and the customization of the app icon to a hawk face with sharp eyes.

## User Review Required

> [!NOTE]
> I will be creating a custom vector drawable for the hawk face. Since I cannot generate a high-fidelity image file, I will provide a clean, aggressive vector representation of a hawk's eyes and beak, which works best for app icons.

## Proposed Changes

### [app] UI Components

#### [MODIFY] [MainActivity.kt](file:///E:/my/kotlin/CemaApp/app/src/main/java/com/qapro/cemaapp/MainActivity.kt)
- Update `DisclaimerScreen` title: Change "企業倫理成熟度評価 (CEMA)" to "企業倫理成熟度評価 (CEMA) v2".
- Update `EvaluationScreen` title: Change "企業倫理成熟度評価 (CEMA)" to "企業倫理成熟度評価 (CEMA) v2".

### [app] Resources (Icon)

#### [MODIFY] [ic_launcher_foreground.xml](file:///E:/my/kotlin/CemaApp/app/src/main/res/drawable/ic_launcher_foreground.xml)
- Replace the default Android mascot with a sharp hawk face vector design.

#### [MODIFY] [ic_launcher_background.xml](file:///E:/my/kotlin/CemaApp/app/src/main/res/drawable/ic_launcher_background.xml)
- Change the background color from Android Green to a dark, professional charcoal gray (#212121) to enhance the visibility of the "sharp eyes".

## Verification Plan

### Automated Tests
- Run `gradle_build(":app:assembleDebug")` to ensure resources and code compile correctly.

### Manual Verification
- Deploy the app to a device/emulator.
- Verify the "v2" text on the first and second screens.
- Check the app icon on the launcher to confirm the hawk face design.
