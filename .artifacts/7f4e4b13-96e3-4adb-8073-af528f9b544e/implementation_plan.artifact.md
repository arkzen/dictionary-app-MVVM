# Project Modernization Plan

This plan outlines the steps to upgrade the dictionary app from a legacy Java-based project to a modern Android environment using Kotlin, Hilt, Coroutines, and the latest SDKs.

## User Review Required

> [!IMPORTANT]
> **JDK Requirement**: Upgrading to Android Gradle Plugin (AGP) 8.x/9.x and Retrofit 3 requires the project to be built with **JDK 17 or higher**. Please ensure your Android Studio is configured to use JDK 17 for this project.

> [!WARNING]
> **Retrofit 3 Migration**: Upgrading to Retrofit 3 might require minor adjustments to the existing network code (e.g., changes in internal dependencies or minimum Java version requirements).

## Proposed Changes

### Build System & Infrastructure
I will update the Gradle version and Android Gradle Plugin to their latest stable versions to ensure compatibility with modern libraries and Android 15.

#### [MODIFY] [gradle-wrapper.properties](file:///C:/Users/hp/StudioProjects/dictionary-app-MVVM/gradle/wrapper/gradle-wrapper.properties)
*   Update Gradle distribution URL to 8.10.2 (required for AGP 8.x+).

#### [MODIFY] [build.gradle (root)](file:///C:/Users/hp/StudioProjects/dictionary-app-MVVM/build.gradle)
*   Update AGP version to 8.7.2.
*   Add Kotlin Gradle plugin.
*   Add Hilt Gradle plugin.
*   Add Navigation Safe Args plugin.

#### [MODIFY] [build.gradle (:app)](file:///C:/Users/hp/StudioProjects/dictionary-app-MVVM/app/build.gradle)
*   Update `compileSdk` and `targetSdk` to 35.
*   Set `sourceCompatibility` and `targetCompatibility` to `JavaVersion.VERSION_17`.
*   Enable `kotlin-android`, `kotlin-kapt`, and `com.google.dagger.hilt.android` plugins.
*   Add dependencies for:
    *   Kotlin Core KTX
    *   Hilt (DI)
    *   Coroutines (Async)
    *   Navigation Component (Fragment & UI)
    *   Lifecycle KTX (ViewModel & LiveData)
    *   Retrofit 3.0.0

### Dependency Management
I will introduce a `libs.versions.toml` file to manage all dependencies in a centralized and modern way.

#### [NEW] [libs.versions.toml](file:///C:/Users/hp/StudioProjects/dictionary-app-MVVM/gradle/libs.versions.toml)
*   Define versions and libraries for all current and new dependencies.

## Verification Plan

### Automated Tests
*   Run `./gradlew assembleDebug` to ensure the project compiles with new versions.
*   Run `./gradlew test` to verify existing unit tests still pass.

### Manual Verification
*   Deploy the app to an emulator/device to verify that the existing Java-based UI and network calls still function correctly.
