# Widgetfy — Premium Bento Widgets for Every Home Screen

> **Package:** `com.widgetfy.com`  
> **Version:** 2.0.0 (Build 2) • **Skill:** [ui-ux-pro-max v2.15.0](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill)  
> **Min SDK:** 24 (Android 7.0 Nougat) | **Target SDK:** 34  
> **Design:** Bento Box Grid + Material 3 Expressive (Mobile)

Widgetfy ek **real, working Android widget app** hai — ab **20 premium bento widgets** ke saath! Sab widgets home screen par directly add hote hain — koi fake preview nahi, sab `RemoteViews + AppWidgetProvider` se bane hue hain. UI/UX Pro Max skill se premium design system (Bento + MD3 Expressive, Plus Jakarta Sans typography, vibrant violet/teal/rose palette) use kiya gaya hai.

### ✨ Widgets List — 20 Widgets (12 → 20, quantity badhaya)

| # | Widget | Category | Real Function |
|---|--------|----------|---------------|
| 1 | **Digital Clock** | Clock | Live time, date, timezone — har minute update — **Bento Hero** |
| 2 | **Analog Clock** | Clock | Classic analog style with date |
| 3 | **Flip Clock** | Clock | Retro flip typography, bold |
| 4 | **World Clock** | Clock | Delhi • London • New York — **Wide** |
| 5 | **Battery Pro** | System | Real battery % + health & charging animation |
| 6 | **Weather Glass** | System | Live temp, city & 3-day forecast — **Wide** |
| 7 | **Search Bar** | System | Google search + voice — **Wide** |
| 8 | **Step Tracker** | Health | Daily steps, calories & goal progress |
| 9 | **Calendar Bento** | Productivity | Month grid + events timeline |
| 10 | **Tasks & Notes** | Productivity | Quick tasks — **Wide** |
| 11 | **Photo Frame** | Lifestyle | Your favorite pic, live on home |
| 12 | **Daily Zen** | Lifestyle | Quotes that inspire, daily rotation |
| 13 | **Music Player** ✨ NEW | Lifestyle | Playback controls + album art — **Wide** |
| 14 | **Countdown** ✨ NEW | Productivity | Birthdays/events — days left tracker |
| 15 | **Finance Pulse** ✨ NEW | Productivity | Budget, expenses & savings |
| 16 | **Health Ring** ✨ NEW | Health | Sleep, heart & hydration — **Bento Hero** |
| 17 | **News Headlines** ✨ NEW | Lifestyle | Top headlines — **Wide** |
| 18 | **Crypto Ticker** ✨ NEW | Finance | BTC, ETH live price & change |
| 19 | **Habit Streak** ✨ NEW | Productivity | 🔥 12 day streak — popular |
| 20 | **Quick Reminder** ✨ NEW | Productivity | Upcoming tasks & alerts |

Sab **resizeable, reconfigurable**, Bento varied spans (Hero 2-col, Wide 2-col, Normal 1-col), Android 12+ widget features.

### 🎨 Design System (ui-ux-pro-max skill)

- **Pattern:** Product Demo + Features / Bento Box Grid
- **Style:** `bento-box-grid` (modular cards varied 1x1/2x1/2x2, Apple-style, rounded 24px, soft shadows, hover scale 1.02) + `material-you-md3-mobile` (Material 3 Expressive: pill buttons 999px, tonal elevation, violet #6750A4)
- **Colors:**
  - Primary #6750A4 (Violet MD3) / Secondary #E8DEF8 / Tertiary Rose #E11D48 / Accent #EA580C
  - Background #FFFBFE / Surface #F3EDF7 / Outline #79747E
  - `design-system/widgetfy/MASTER.md` me persisted
- **Typography:** Plus Jakarta Sans (Friendly SaaS) — body 16px/1.5, label 12px, display 32px ExtraBold
- **UX Pro Max Rules Applied:**
  - Accessibility: contrast 4.5:1, 44×44 touch target, keyboard nav
  - Bento grid 4→2→1 cols responsive, consistent 16-24px radius
  - Pill-shaped CTAs (999px), state-layer overlays, emphasized easing 0.2,0,0,1

### 📲 How to Add Widget

1. Home screen par **long press** karo
2. **Widgets** pe tap karo
3. **Widgetfy** section dhundo
4. Koi bhi widget ko **drag kar ke home par drop** karo  
   Ya app ke andar **“Add to Home”** button dabao (Android 8+ pinned widget API) — Bento card se instant pin!

### 🏗️ Build & Release — 3 APK

GitHub Actions automatically 3 APK banata hai (ABI splits):

- `arm64-v8a.apk` — 64-bit phones (modern)
- `armeabi-v7a.apk` — 32-bit / older phones
- `universal.apk` — sab devices ke liye (recommended)  
  + `widgetfy-*` aliases bhi released

Workflow: `.github/workflows/build.yml` → `gradle assembleRelease` (JDK 17, Android SDK 34, Gradle 8.5)

**Local build:**
```bash
./gradlew assembleRelease
# outputs in app/build/outputs/apk/release/
```

Latest Release: `v2.0.0` (2.0.0-*) — check **Releases** tab

### 🔧 Tech Stack

- **Kotlin + Jetpack Compose (Material3 Expressive, Bento Grid)** — UI
- **RemoteViews + AppWidgetProvider — 20 real widgets** (increased from 12)
- **WorkManager** — periodic updates (15 min)
- **SharedPreferences** — notes, steps
- **Gradle 8.5 + AGP 8.2.2 + Kotlin 1.9.22**
- **Skill:** `ui-ux-pro-max` installed at `.claude/skills/ui-ux-pro-max/` + `.github/skills/ui-ux-pro-max/` — search via `python .claude/skills/ui-ux-pro-max/scripts/search.py`

### 📦 Package

```xml
applicationId = "com.widgetfy.com"
namespace = "com.widgetfy.com"
versionCode = 2
versionName = "2.0.0"
```

### 🔐 Permissions

Location (weather), Calendar (events), Battery, Activity Recognition (steps), Notifications, Boot Completed.

### 📝 Notes

- Repo `Moviesstream` → `widgetfy` rename **manual required**: GitHub → Settings → General → Repository name → `widgetfy` (API 403)
- `progress.md` local only (gitignore) — developer tracker
- `design-system/widgetfy/MASTER.md` persisted from skill

Made with ❤️ in India — 20 Real Bento Widgets, Premium Design, No Ads, Free Forever.
