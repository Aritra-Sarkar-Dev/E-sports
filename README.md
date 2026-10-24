# Free Fire Tournament Arena Platform

[![Download Android APK](https://img.shields.io/badge/Download-Android%20APK-brightgreen?style=for-the-badge&logo=android)](https://github.com/Aritra-Sarkar-Dev/E-sports/releases/latest/download/app-debug.apk)
[![Latest Release](https://img.shields.io/github/v/release/Aritra-Sarkar-Dev/E-sports?style=for-the-badge&label=Latest%20Release)](https://github.com/Aritra-Sarkar-Dev/E-sports/releases)

### 📲 Direct APK Download Link
👉 **[Download Latest APK (`app-debug.apk`)](https://github.com/Aritra-Sarkar-Dev/E-sports/releases/latest/download/app-debug.apk)**

> **Note:** When new commits are pushed, GitHub Actions automatically compiles and publishes the latest `app-debug.apk` directly to [GitHub Releases](https://github.com/Aritra-Sarkar-Dev/E-sports/releases).

---

## 🚀 How to Run in VS Code

### Prerequisites
- [Node.js](https://nodejs.org/) (version 18 or higher)
- [VS Code](https://code.visualstudio.com/)
- Recommended VS Code Extensions:
  - **ESLint**
  - **Tailwind CSS IntelliSense**
  - **Kotlin** or **Android iOS Tools** (for viewing Kotlin code)

---

### Option 1: Running the Admin Web Dashboard (Vite + React)

The Admin Web Dashboard lets tournament organizers create matches, publish room credentials, approve player registrations, and export CSV files.

1. **Open the repository in VS Code**:
   ```bash
   code .
   ```

2. **Navigate into the `admin-web` folder**:
   ```bash
   cd admin-web
   ```

3. **Install dependencies**:
   ```bash
   npm install
   ```

4. **Start the local development server**:
   ```bash
   npm run dev
   ```

5. **Open in your browser**:
   Navigate to [http://localhost:3000](http://localhost:3000)

#### Available Admin Web Scripts:
- `npm run dev` — Starts the Vite dev server with Hot Module Replacement (HMR) on port 3000.
- `npm run build` — Compiles TypeScript and builds production bundles to `dist/`.
- `npm run preview` — Previews the production build locally.

---

### Option 2: Running the Android Mobile App

For running the Android client on an emulator or physical device:

1. **Using Android Studio (Recommended for Android)**:
   - Open Android Studio.
   - Select **Open an Existing Project** and choose this project root folder.
   - Wait for Gradle sync to complete.
   - Select a target emulator or connected Android device and click **Run** (`Shift + F10`).

2. **Using the Terminal / VS Code**:
   - Make sure you have the Android SDK and `JAVA_HOME` (Java 17) configured.
   - Build the debug APK:
     ```bash
     ./gradlew assembleDebug
     ```
   - The generated APK will be at:
     `app/build/outputs/apk/debug/app-debug.apk`
   - Install to connected device:
     ```bash
     ./gradlew installDebug
     ```

---

## 📁 Repository Structure

```
├── admin-web/                 # React + Vite Admin Web Console
│   ├── src/
│   │   ├── components/        # Layout, Sidebar, TopBar, Badges, Modals
│   │   ├── pages/
│   │   │   ├── DashboardPage.tsx       # Live analytics & tournament overview
│   │   │   ├── TournamentsPage.tsx     # Create tournaments & publish room IDs
│   │   │   ├── RegistrationsPage.tsx   # Filter, sort, manage & Download CSV
│   │   │   ├── ResultsPage.tsx         # Kill counts, placement & prize payouts
│   │   │   ├── CoinsPage.tsx           # Coin ledger & credit/debit records
│   │   │   ├── NotificationsPage.tsx   # Push room notifications to players
│   │   │   └── SettingsPage.tsx        # System configuration & compliance
│   │   ├── services/          # TournamentService, CoinService, Firestore sync
│   │   ├── types/             # TypeScript definitions
│   │   └── data/              # Mock data & initial seeds
│   ├── package.json
│   ├── vite.config.ts
│   └── tailwind.config.js
│
├── app/                       # Android Client Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/  # Jetpack Compose UI & ViewModels
│   │   │   └── res/               # Android drawables, values, strings
│   │   └── test/                  # Unit and Robolectric tests
│   └── build.gradle.kts
│
├── package.json               # Root npm proxy scripts
└── README.md
```

---

## 🛠 Features Summary

- **Live Tournament Orchestration**: Setup Solo, Duo, Squad tournaments with entry fees and prize distribution.
- **Room ID & Password Vault**: Lock/unlock credentials and publish with push notification alerts to players.
- **Player Registration System**:
  - Duplicate UID prevention (Free Fire MAX 18+ compliant).
  - Status management: `Pending`, `Approved`, `Confirmed`, `Checked In`, `Rejected`, `Disqualified`.
  - Column sorting by Status (Pending First / Rejected First), IGN, UID, Tournament, and Time.
  - **Download CSV**: 1-click export of filtered registrations with UTF-8 BOM encoding for Excel/Sheets.
- **Coin Ledger & Atomic Payouts**: Real-time balance adjustments and automated prize distribution.
- **Indian Gaming Compliance**: 18+ age checks, restricted states policy (Assam, Odisha, Telangana, Andhra Pradesh, Nagaland, Sikkim), and skill-based gaming disclaimer.
