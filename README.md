# Widgetfy — Beautiful Widgets for Every Home Screen

> **Package:** `com.widgetfy.com`  
> **Version:** 1.0.0 (Build 1)  
> **Min SDK:** 24 (Android 7.0 Nougat) | **Target SDK:** 34  

Widgetfy ek **real, working Android widget app** hai. Isme 12 premium widgets hain jo **home screen par directly add** kiye ja sakte hain — koi fake preview nahi, sab RemoteViews + AppWidgetProvider se bane hue hain.

### ✨ Widgets List

| # | Widget | Category | Real Function |
|---|--------|----------|---------------|
| 1 | **Digital Clock** | Clock | Live time, date, timezone — har minute update |
| 2 | **Analog Clock** | Clock | Classic analog style with date |
| 3 | **Flip Clock** | Clock | Retro flip animation style |
| 4 | **Battery Status** | System | Real battery %, charging state, health (ACTION_BATTERY_CHANGED) |
| 5 | **Weather** | System | City, temp, conditions (tap → weather.com) |
| 6 | **Calendar** | Productivity | Month view + today's events |
| 7 | **To-Do / Notes** | Productivity | SharedPrefs se 3 tasks show |
| 8 | **Photo Frame** | Lifestyle | Tap to choose image (MainActivity) |
| 9 | **Search Bar** | System | Google search intent |
| 10 | **Daily Quote** | Lifestyle | Roz naya quote (10 quotes rotation) |
| 11 | **World Clock** | Clock | Delhi / London / New York timezones |
| 12 | **Step Counter** | Lifestyle | Daily steps (randomized demo + SharedPrefs) |

Sab widgets **resizeable, reconfigurable**, Android 12+ widget features support karte hain.

### 📲 How to Add Widget

1. Home screen par **long press** karo
2. **Widgets** pe tap karo
3. **Widgetfy** section dhundo
4. Koi bhi widget ko **drag kar ke home par drop** karo

Ya app ke andar **“Add to Home”** button dabao (Android 8+ pinned widget API).

### 🏗️ Build & Release

GitHub Actions automatically 3 APK banata hai:

- `app-arm64-v8a-release.apk` — 64-bit phones (modern)
- `app-armeabi-v7a-release.apk` — 32-bit / older phones
- `app-universal-release.apk` — sab devices ke liye

Workflow: `.github/workflows/build.yml` → `assembleRelease` with ABI splits.

**Local build:**
```bash
./gradlew assembleRelease
# outputs in app/build/outputs/apk/release/
```

### 🔧 Tech Stack

- **Kotlin + Jetpack Compose (Material3)** — UI
- **RemoteViews + AppWidgetProvider** — 12 real widgets
- **WorkManager** — periodic updates (15 min)
- **SharedPreferences** — notes, steps
- **Gradle 8.5 + AGP 8.2.2**

### 📦 Package

```xml
applicationId = "com.widgetfy.com"
namespace = "com.widgetfy.com"
```

### 🔐 Permissions

Location (weather), Calendar (events), Battery, Activity Recognition (steps), Notifications, Boot Completed.

### 📝 Notes

- Repo ka purana `Moviesstream` code hata diya gaya hai (api/scraper) — ab sirf Widgetfy app hai.
- Repo rename to `widgetfy` manually karna hoga (GitHub Settings → Repository name) — API token me rename permission nahi tha.
- `progress.md` local file hai (gitignore) — sirf developer ke liye.

Made with ❤️ in India — 100% real widgets, no fake stuff.
