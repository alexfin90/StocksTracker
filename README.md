# StocksTracker

A real-time stock price tracker built for the **[Android] Coding Challenge: Real-Time Price Tracker
App**.

The app streams price updates for 25 stock symbols over a WebSocket connection, renders them in a
fully reactive Jetpack Compose UI, and lets the user drill down into a detail screen for each
symbol.

---

## Highlights

- **100% Jetpack Compose** UI — no XML, no AppCompat views.
- **MVVM + Clean Architecture** — strict separation between `presentation`, `domain` and `data`
  layers.
- **Multi-module Gradle project** with **convention plugins** (`build-logic`) to keep module scripts
  thin and consistent.
- **Kotlin Flow / StateFlow** end-to-end, with immutable UI state.
- **Hilt** for dependency injection.
- **Product flavors (`mock` / `real`)** to switch between a fake in-memory feed and the real
  WebSocket feed.
- **Unit tests** on ViewModels and **Compose UI tests** on the design system components.
- **All bonus requirements implemented** (price flash animation, light/dark theme, deep links,
  tests).

---

## Challenge Requirements

The challenge is to build an Android app using Jetpack Compose that:

- Tracks **25 stock symbols** (AAPL, GOOG, TSLA, AMZN, MSFT, NVDA, …).
- Connects to the Postman Echo WebSocket: `wss://ws.postman-echo.com/raw`.
- Every **2 seconds**, for each symbol: generates a random price update, sends it to the echo
  server, receives it back, and updates the UI.
- Shows a **Feed screen** with a `LazyColumn` sorted by price (highest on top), each row showing
  symbol, current price and an up/down indicator.
- Shows a **Top Bar** with a connection status indicator (🟢 / 🔴) and a **Start / Stop** toggle.
- Opens a **Symbol Details screen** on row tap, displaying the symbol, current price with direction
  indicator and a description.
- Uses **Navigation Compose** with a `NavHost` and two destinations (`feed` as start destination,
  `detail`).
- Uses **ViewModel**, **StateFlow** and **SavedStateHandle** for the selected symbol.
- Manages a **single WebSocket connection** shared across both screens.

### Bonus (all implemented ✅)

- ✅ Price flashes **green for 1s on increase** and **red for 1s on decrease**.
- ✅ **Compose UI tests** and **unit tests**.
- ✅ **Light and dark themes**.
- ✅ **Deep link** to open the details screen.

---

## Architecture

The project follows **Clean Architecture** with a classic layering: `presentation → domain ← data`.

```
┌──────────────────────────── Presentation ────────────────────────────┐
│  :feature:stocksfeed         :feature:stockdetail                    │
│  (Composables + ViewModels + UiState)                                │
└────────────────────────────────┬─────────────────────────────────────┘
                                 │ depends on
                                 ▼
┌────────────────────────────── Domain ────────────────────────────────┐
│  :core:domain                                                        │
│  (Entities, Repository interfaces, Use Cases — pure Kotlin/JVM)      │
└────────────────────────────────▲─────────────────────────────────────┘
                                 │ implements
                                 │
┌──────────────────────────────── Data ────────────────────────────────┐
│  :core:data                                                          │
│  (Repositories impl, WebSocket DataSource, DTOs, Mappers)            │
│  Flavor-specific source sets: `mock` / `real`                        │
└──────────────────────────────────────────────────────────────────────┘
```

- The **domain layer is pure Kotlin/JVM** (no Android dependencies), so it’s trivially
  unit-testable.
- The **data layer** hides the network / DTOs and exposes only domain models through the
  `StockRepository` contract.
- The **feature modules** (presentation) depend only on `:core:domain` and use cases — they never
  see DTOs, Retrofit or OkHttp.
- A **single `StockRepository` instance** (scoped as a singleton) owns the WebSocket connection, so
  both screens observe the same stream without duplicate connections.

---

## Module Structure

```
StocksTracker
├── app                          # Application module: Activity, NavHost, Top Bar, MainViewModel
├── build-logic                  # Convention plugins (Gradle)
├── core
│   ├── common                   # Shared utilities, type-safe navigation Routes
│   ├── data                     # Repositories, DataSources, DTOs, Mappers (mock / real flavors)
│   ├── designsystem             # Theme, atoms, molecules, reusable Compose components + UI tests
│   ├── dispatcher               # Coroutine dispatcher qualifiers (Hilt)
│   └── domain                   # Entities, Repository contracts, Use Cases (pure JVM)
└── feature
    ├── stocksfeed               # Feed screen: ViewModel, UiState, Composables + unit tests
    └── stockdetail              # Detail screen: ViewModel, UiState, Composables + unit tests
```

### Feature modules — `:feature:stocksfeed`, `:feature:stockdetail`

These are the **presentation layer**. Each module contains:

- A `Screen` composable that renders the UI from an immutable `ScreenState`.
- A `ViewModel` that exposes a `StateFlow<ScreenState>` and collects use cases from `:core:domain`.
- Unit tests on the ViewModel (using Robolectric + a `FakeStockRepository`).

### `:core:domain` — Business logic

Pure Kotlin/JVM module. Contains:

- **Entities** — e.g.
  `Stock(symbol, name, description, priceUsd, previousPriceUsd, updatedAtMillis)` with a computed
  `isUp: Boolean?` property.
- **Repository contracts** — `StockRepository` interface exposing `stocks: Flow<List<Stock>>`,
  `connectionActive: Flow<Boolean>`, `connectionError: Flow<String?>`, `start()`, `stop()`,
  `observeStock(symbol)`.
- **Use Cases** (one responsibility each):
    - `ObserveSortedStocksUseCase` — sorted list (desc. by price) for the feed.
    - `ObserveStockUseCase` — single stock for the detail screen.
    - `ObserveConnectionUseCase` — connection status for the top bar.
    - `ObserveConnectionErrorUseCase` — surface errors to the UI.
    - `StartConnectionUseCase` / `StopConnectionUseCase` — toggle the feed.

### `:core:data` — Data layer

- `StockRepositoryImpl` (real flavor) — implements `StockRepository` by orchestrating the WebSocket
  data source.
- `StockRepositoryMockImpl` (mock flavor) — implements `StockRepository` with an in-memory random
  walker.
- `StockWebSocketDataSource` — the OkHttp WebSocket wrapper (see below).
- **DTOs** (e.g. `StockPriceDto`) — network-only models, parsed with **Moshi**.
- **Mappers** — DTO → domain mapping (`StockData.toDomain()`, `StockPriceDto.toStockPriceEvent()`);
  UI code never sees DTOs.

### `:core:designsystem` — Design System

- **Theme**: `StocksTrackerTheme` with Material 3 light/dark color schemes, dynamic colors (Android
  12+), typography and color palette (Indigo / Teal / Sky / Slate + gain-green / loss-red).
- **Atoms** — `ConnectionStatusIndicator`, `StocksTrackerToggleButton`, up/down price icons.
- **Molecules** — `StockRow` (symbol, name, price, direction icon) with the
  `animatedFlashBackground()` modifier.
- **UI tests** (`androidTest/`) covering the atoms and molecules (display, click, enable/disable,
  price formatting).

---

## Convention Plugins

To keep module `build.gradle.kts` files small and consistent, the project uses a **`build-logic`**
included build with a set of custom Gradle convention plugins:

| Plugin ID                          | Purpose                                                                                                  |
|------------------------------------|----------------------------------------------------------------------------------------------------------|
| `stockstracker.application`        | Applies `com.android.application`, Kotlin/Compose, build types and APK naming.                           |
| `stockstracker.library`            | Applies `com.android.library`, Kotlin/Android, common Android config.                                    |
| `stockstracker.jvm.library`        | Applies `kotlin-jvm` for pure JVM modules (used by `:core:domain`).                                      |
| `stockstracker.feature`            | Extends `library`, auto-wires `:core:common` and `:core:designsystem` for feature modules.               |
| `stockstracker.hilt`               | Applies KSP + Hilt plugins and adds Hilt dependencies.                                                   |
| `stockstracker.compose.navigation` | Applies Compose Compiler + Kotlin Serialization, adds Compose / Navigation / Serialization dependencies. |
| `stockstracker.flavors`            | Declares the `mock` / `real` product flavors and wires source sets.                                      |

This makes the individual module files almost declarative (just the plugin ids + module-specific
deps), avoids duplication, and makes it trivial to add a new feature module.

---

## Tech Stack

- **Language** — Kotlin `2.2.0`
- **Build** — AGP `8.13.1`, Gradle Version Catalogs, convention plugins, JDK `17`
- **UI** — Jetpack Compose (BOM `2024.09.00`), Material 3, Navigation Compose `2.9.3`
- **Architecture** — MVVM, Clean Architecture, Use Cases, `StateFlow`, `SavedStateHandle`
- **DI** — Hilt `2.58` (+ Hilt Navigation Compose `1.3.0`), KSP
- **Async** — Kotlin Coroutines `1.10.2`, Flow / SharedFlow / StateFlow
- **Networking** — OkHttp `4.12.0` (WebSocket), Retrofit `3.0.0` (scaffolding), Moshi `1.15.2`
- **Serialization** — Kotlinx Serialization `1.10.0` for type-safe Navigation routes
- **Immutability** — Kotlinx Immutable Collections `0.4.0`
- **Logging** — Timber `5.0.1`
- **Testing** — JUnit 4, Robolectric `4.14.1`, Coroutines Test, Compose UI Test
- **Min SDK** 29 · **Target SDK** 36 · **Compile SDK** 36

---

## Real-Time Feed & WebSocket

The repository owns a **single shared WebSocket connection** so both the feed and the detail screen
observe the same stream without duplicate connections.

- **Endpoint**: `wss://ws.postman-echo.com/raw` (Postman Echo service that echoes back whatever it
  receives).
- **Feed loop**: every **2 seconds** the repository generates a random price update (±5% swing) for
  each of the 25 symbols and sends it to the echo server. The echoed JSON is parsed with Moshi into
  a `StockPriceDto`, mapped to a domain event and published to a `Flow<List<Stock>>`.
- **Events**: `StockWebSocketDataSource` exposes a `SharedFlow<StockSocketEvent>` with `Connected`,
  `Disconnected`, `PriceUpdateReceived` and `Failure` variants, which the repository collects to
  build the domain streams.

### Reconnection strategy

In `StockWebSocketDataSource.onFailure()`, a basic **auto-reconnect** mechanism schedules a new
connection attempt after **1 second** (unless the user manually stopped the feed via the Start/Stop
toggle). This keeps the stream alive on transient network errors without blowing up the UI with
failure states.

---

## Product Flavors: `mock` vs `real`

The app ships with **two product flavors** (defined in the `stockstracker.flavors` convention
plugin) that swap the `StockRepository` implementation via Hilt modules in flavor-specific source
sets:

| Flavor | Repository                | Data source                                   | Use it when…                               |
|--------|---------------------------|-----------------------------------------------|--------------------------------------------|
| `mock` | `StockRepositoryMockImpl` | `StockMockDataSource` (in-memory random)      | You want to run the app offline / test UI. |
| `real` | `StockRepositoryImpl`     | `StockWebSocketDataSource` (OkHttp WebSocket) | You want the real WebSocket feed.          |

Each flavor has its own `DataModule` Hilt file under `core/data/src/{mock,real}/java/...` so the
binding is chosen at build time and no runtime branching is needed.

You can switch flavor from Android Studio (**Build Variants** tool window) or from the command line:

```bash
./gradlew :app:installMockDebug   # in-memory fake feed
./gradlew :app:installRealDebug   # real WebSocket feed
```

---

## Navigation & Deep Links

Navigation is built on **Navigation Compose** with **type-safe routes** backed by
`kotlinx.serialization`:

```kotlin
sealed interface Route {
    @Serializable
    data object StocksFeed : Route
    @Serializable
    data class StockDetail(val symbol: String) : Route
}
```

- `StocksFeed` is the start destination.
- `StockDetail` is reached either by tapping a row in the feed or via deep link.
- `StockDetailViewModel` retrieves the selected symbol with
  `savedStateHandle.toRoute<Route.StockDetail>().symbol`, as required by the challenge (no manual
  argument keys).

### Deep link

The detail screen declares a deep link using
`navDeepLink<Route.StockDetail>(basePath = "stockstracker://stock.detail")`.

You can test it from a terminal with ADB:

```bash
adb shell am start -a android.intent.action.VIEW -d "stockstracker://stock.detail/TSLA"
```

Replace `TSLA` with any of the 25 supported symbols (AAPL, GOOG, AMZN, MSFT, NVDA, …).

---

## Bonus Features

All optional bonus features from the challenge are implemented:

- **Price flash animation** — a custom `Modifier.animatedFlashBackground()` extension in the design
  system flashes the row background **green for 1 second on price increase** and **red for 1 second
  on price decrease**. It’s keyed on the price value so it re-runs on every update.
- **Light & dark themes** — `StocksTrackerTheme` provides full Material 3 light and dark color
  schemes and follows the system setting. On Android 12+ dynamic colors are used when available.
- **Deep link** — `stockstracker://stock.detail/{symbol}` opens the detail screen directly (see
  above).
- **Tests** —
    - **Unit tests** on `StocksFeedViewModel` and `StockDetailViewModel` with a
      `FakeStockRepository`, covering initial state, sorting, SavedStateHandle resolution and error
      handling.
    - **Compose UI tests** on the design system atoms (`ConnectionStatusIndicator`,
      `StocksTrackerToggleButton`) and molecules (`StockRow`), covering display, click handling and
      price formatting.

---

## Running the Project

### Requirements

- Android Studio (Ladybug or newer recommended)
- JDK 17
- Android SDK with API 36 installed

### Build

```bash
./gradlew assembleMockDebug    # build the mock flavor (offline fake feed)
./gradlew assembleRealDebug    # build the real flavor (WebSocket feed)
```

### Run tests

```bash
./gradlew test                 # all JVM unit tests
./gradlew connectedAndroidTest # Compose UI tests on a connected device/emulator
```

### Test the deep link

```bash
adb shell am start -a android.intent.action.VIEW -d "stockstracker://stock.detail/TSLA"
```

---

## Project Conventions

- **Immutable UI state** via `data class` + `PersistentList` (kotlinx-immutable-collections).
- **One-way data flow**: UseCase → ViewModel → UiState → Composable; events go the other way through
  typed lambdas.
- **No Android classes in `:core:domain`** — enforced by the `stockstracker.jvm.library` convention
  plugin.
- **Feature modules never depend on `:core:data`** — they depend on `:core:domain` only, keeping the
  dependency graph acyclic and the data layer swappable.

---

## License

This project was developed as a coding challenge submission and is provided as-is for evaluation
purposes.