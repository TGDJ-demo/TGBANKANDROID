# TG Bank — TestGrid Demo Banking App

Ultra High Net Worth private-banking demo application built with **Jetpack Compose** for **UI / functional / regression testing** (Appium-friendly unique `testTag` locators throughout).

## Features for Automation

1. **Beneficiary flows** — saved beneficiaries + ACH / IMPS / NEFT / RTGS transfer rails (dropdown).
2. **Real HTTP API calls** — payments POST to `https://httpbin.org/post` so network logs / proxies show real traffic (`TGBankAPI` log tag).
3. **Unique locators** — every interactive element uses `Modifier.testTag("…")` for Appium / scriptless tools.
4. **Form controls** — dropdowns (`ExposedDropdownMenu`), checkboxes, filter chips, switches, text fields.
5. **Slide to Pay** — custom gesture control (`slide_to_pay_track` / `slide_to_pay_thumb`).
6. **OTP scenario** — OTP dialog before payment (`otp_dialog`, `otp_input_field`, demo OTP `123456`).
7. **Biometric / fingerprint login** — mockable via Test Controls (`login_biometric_button`, `toggle_mock_biometric`).
8. **Colorful UI** — indigo / purple / magenta gradients and accent chips.
9. **Sanjay G UHNW profile** — ~$24.5B balance, credit score **850**, banking score **998**, Black Diamond + Invitation-Only Metal cards.
10. **Notifications** — in-app notification center + snackbar toasts.
11. **Push / Seed Database** — Test Controls → “Push / Seed Database to Backend” resets and validates seed data.
12. **Payment auth** — MPIN (`123456`) / OTP / Biometric required before slide-to-pay when enabled.

## Build APKs (Debug + Release)

```bash
./gradlew assembleDebug assembleRelease
```

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

Open the project root in **Android Studio**, let Gradle sync, then Run on emulator/device.

Demo login: **Sanjay G** / PIN **1234**

## Test Controls

Access via the flask icon in the top app bar. Includes:

- Force insufficient balance / failed transaction / network error
- Mock biometric success
- Require payment auth / force OTP
- Push / Seed Database
- Reset demo data

## Network logs

Filter Logcat by tag **`TGBankAPI`** to inspect request/response bodies for every payment.

## Android Studio open instructions (important)

1. **Unzip** `tg-bank-updated.zip` into a folder, e.g.  
   `~/AndroidStudioProjects/TGBank`  
   (the folder must contain `settings.gradle.kts`, `gradlew`, and `app/` at the **top level**).

2. In Android Studio: **File → Open** → select that folder (the one that contains `settings.gradle.kts`), **not** a parent empty folder.

3. Wait for Gradle sync. If prompted about Gradle JDK, pick **JDK 17** (or Embedded JDK).

4. Build → Rebuild Project, then Run.

If you still see “Cannot find IntelliJ IDEA project files”:
- Close the project
- Delete any partial `.idea` folder inside the project
- **File → Open** the project root again (folder with `settings.gradle.kts`)
- Or from terminal: `./gradlew assembleDebug`

Demo login: **Sanjay G** / PIN **1234**
