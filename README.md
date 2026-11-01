# Explored

Personal offline-first Android exploration map.

Mark places as **To explore**, **Worth revisiting**, or **Not worth revisiting**, rate visited locations, and export data as JSON.

## Build

GitHub Actions builds the debug APK automatically from `main`. Download from **Actions → Build Android APK → Artifacts → explored-debug-apk**.

Local build (JDK 17, Android SDK): `gradle assembleDebug`.

## Notes

Saved places are stored locally in Android WebView local storage. Back them up using **Export JSON**. Map tiles and Leaflet require internet connectivity. OpenStreetMap attribution is shown on the map. No account needed.
