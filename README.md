# MyMovieApplication (Kotlin Multiplatform / KMM)

A sample Kotlin Multiplatform (Android + iOS) application that lists today’s trending movies and supports searching & viewing movie details using **TMDB API**.  

The project follows modern Google-recommended architecture (Clean Architecture + MVVM + Unidirectional Data Flow) and shares as much logic as possible between Android and iOS (networking, caching, domain logic, use cases, DI).

---

## Features

### Movies List (Trending / Search)
- When the search field is empty: fetch **today’s trending movies** (first page) using TMDB Trending endpoint.
- When the user types text: perform **online search** (no caching).
- List header dynamically switches between “Trending movies” and “Search results”.
- Each list item shows: Poster (or backdrop fallback), Title, Year, Vote Average.
- Paging (manual or incremental) support placeholder (extendable).
- Clicking an item opens the Movie Detail screen.

### Offline Behavior
| Data Type | Caching | Notes |
|-----------|---------|-------|
| Trending Movies | ✅ Yes | Cached with timestamp (TTL). Reused if still valid. |
| Search Results | ❌ No | Always online, uncached. |
| Movie Detail | ✅ Yes | Once opened, details are cached & available offline. |

### Movie Detail
- Shows expanded info (overview, images, rating, etc.).
- Opens hyperlinks (homepage / external links) when available.
- Offline after first successful load.

---

## Tech Stack (Shared / Common)

| Concern | Library / Tool |
|---------|----------------|
| Language | Kotlin Multiplatform (Kotlin 1.9+/2.0 depending on setup) |
| Concurrency | Coroutines / Flows |
| Networking | Ktor Client (Android engine / Darwin engine for iOS) |
| Serialization | kotlinx.serialization (JSON) |
| Dependency Injection | Koin |
| Persistence | SQLDelight (Android + Native driver) |
| Logging (basic) | println / extendable |
| Architecture | Clean Architecture (data / domain / presentation separation), MVVM |
| Build | Gradle Kotlin DSL |
| UI (Android) | Jetpack Compose |
| UI (iOS) | SwiftUI entrypoint + (optional) Compose Multiplatform later |

---

## Layered Architecture

```
shared/
  └─ src/commonMain/kotlin
       core/
         di/               (Koin modules, platform-agnostic)
         database/         (SQLDelight driver factory abstractions)
         network/          (HttpClient factory expect/actual if used)
         util/             (KoinHelper, Result, dispatchers, etc.)
       feature/movie/
         data/
           remote/         (DTOs, RemoteDataSource via Ktor)
           local/          (SQLDelight DAO & caching logic)
           repository/     (Repository Impl)
         domain/
           model/          (Movie / MovieDetail entities)
           repository/     (Repository interface)
           usecase/        (GetTrending, SearchMovies, GetMovieDetail ...)
         presentation/ (optional shared ViewModels if added)
```

Android-specific or iOS-specific implementations (drivers, engines) live under:
```
shared/src/androidMain/...
shared/src/iosMain/...
```

---

## Dependency Injection (Koin)

A `KoinHelper` (object) exposes `initKoinIfNeeded()` that:
1. Guards against multiple starts.
2. Registers shared modules.

Example shared modules include:
- `networkModule` – Ktor `HttpClient` configured with JSON serialization
- `databaseModule` – SQLDelight driver + database
- `repositoryModule` – `MovieRepository` binding
- `useCaseModule` – Use cases (Trending, Search, Detail)

---

## Caching Strategy

### Trending Movies
- Stored in `trending_movie` table with `cachedAt`.
- A TTL (e.g., 6 hours) is applied. If data is still “fresh” it is served from local DB before any network call.

### Movie Detail
- Stored as a JSON blob (or decomposed fields) in `movie_detail` table with `cachedAt`.
- On open:
  1. Try local first
  2. If stale / missing → fetch remote → update cache.

### Search
- Never cached (always live API call).

---

## Error Handling & Result Wrapping
A lightweight `Result` (Success / Error) or direct exception strategy is used internally; UI layer observes state (Loading / Data / Error). Extend with sealed classes or MVI if desired.

---

## Networking (TMDB)

Base endpoints:
- Trending: `GET /trending/movie/day`
- Search: `GET /search/movie`
- Detail: `GET /movie/{id}`

Image base URL: `https://image.tmdb.org/t/p/w500/` (posterPath/backdropPath appended)
---

## Build & Run

### Prerequisites
- JDK 17 or 11 (match Gradle config)
- Android Studio Giraffe/Koala or IntelliJ IDEA KMP plugin
- Xcode 15+ (for iOS build)
- Kotlin Multiplatform plugin

### Gradle Tasks
| Task | Description |
|------|-------------|
| `./gradlew :shared:clean :shared:assemble` | Build shared module for all targets |
| `./gradlew :androidApp:installDebug` | Install Android app (if module exists) |
| `./gradlew build` | Full build |


---
## Project Structure (High-Level)

```
root
 ├─ composeApp/          (Compose Multiplatform shared UI layer if used)
 │   └─ src/
 │       ├─ commonMain/  (Common UI code MPP)
 │       ├─ androidMain/
 │       └─ iosMain/
 ├─ shared/              (Core shared business logic & data)
 │   └─ src/
 │       ├─ commonMain/
 │       ├─ androidMain/
 │       └─ iosMain/
 ├─ iosApp/              (iOS entrypoint with SwiftUI)
 ├─ build.gradle.kts
 ├─ settings.gradle.kts
 └─ README.md
```

---

## Coding Conventions

- **Coroutines**: Use `Dispatchers.Default` / `IO` for work; avoid blocking main.
- **Suspend vs Flow**:
  - Trending / Search single-shot loads: prefer `suspend`.
  - Continuous updates or DB observing: wrap with `Flow`.
- **DI**: Keep Koin modules lean; group by layer or feature.
- **Model Mapping**: DTO → Domain → UI ViewModel (or direct Domain if simple).

---

## Contact
For questions / improvements feel free to open an Issue or PR.

---

### Quick Start TL;DR

1. Put TMDB token in DI.
2. Run `./gradlew :shared:assemble`.
3. Android: Run from Android Studio.
4. Trending loads & caches; search is online; detail caches after first view.

---

Happy Coding! 🚀
