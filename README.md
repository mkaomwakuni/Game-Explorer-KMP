# KMP-Game Explorer (Compose Multiplatform)

[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.9.0-green)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![ktorClient](https://img.shields.io/badge/ktor_client-3.3.0-pink)](https://ktor.io/docs/welcome.html)
![badge-Android](https://img.shields.io/badge/Platform-Android-brightgreen)
![badge-iOS](https://img.shields.io/badge/Platform-iOS-lightgray)
![badge-desktop](http://img.shields.io/badge/Platform-Desktop-4D76CD.svg?style=flat)
![badge-web](https://img.shields.io/badge/Platform-Web-blueviolet.svg?style=flat)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
<a href="https://github.com/Mkaomwakuni"><img alt="License" src="https://img.shields.io/static/v1?label=GitHub&message=Mkaomwakuni&color=C51162"/></a>

KMP-Game Explorer app built with Compose Multiplatform, supporting Android, iOS, Desktop, and Web.
The app
follows the MVVM architecture to ensure clean, maintainable code and delivers a responsive, modern
UI experience across all platforms, leveraging
the [Rawg Video Game Database API](https://rawg.io/apidocs).

# Platform

- iOS
- Android
- Desktop
- Web

<p float="center">
  <img width="100%" height="50%" src="screenshots/home_screenshot.png"/> </br></br>
  <img width="100%" height="50%" src="screenshots/game_detail_screenshot.png" /></br></br>
  <img width="100%" height="50%" src="screenshots/app_demo.gif" />
</p>

# Main Features

### Games

- Popular, Top Rated & New Releases game sections
- Game Detail Pages with Developers & Publishers
- Similar Games
- Search Games
- Game Achievements

### Genres

- Browse games by different genres
- Genre-specific game collections
- Top games in each genre

### Developers & Publishers

- Popular Developers sections
- Major Publishers showcase
- Developer & Publisher Detail Pages
- Games by Developer/Publisher

### Common Features

- **Bottom Navigation**
- **Navigation Rail**
- **Pagination** supports for all platforms
- **Dark/Light Theme** toggle
- **Favorites** saved locally

## Architecture

- **MVVM Architecture (Model - ComposableView - ViewModel)**

<p float="left">
  <img width="100%" height="60%" src="screenshots/mvvm_architecture.png" />
</p>

## API Key

You will need to provide a developer key to fetch the data from RAWG API.

* Generate a new key from [here](https://rawg.io/apidocs). Copy the key
  and go back to the project.
* Add a new entry in `local.properties` file:

```kotlin
RAWG_API_KEY=yourRawgApiKeyHere
```

## Built With

- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) - Compose
  Multiplatform, a modern UI framework for Kotlin that makes building performant and beautiful user
  interfaces.
- [PreCompose](https://github.com/Tlaster/PreCompose) - Compose Multiplatform Navigation && State
  Management
- [Ktor Client](https://ktor.io/docs/welcome.html) - Ktor includes a multiplatform asynchronous HTTP
  client, which allows you to make requests and handle responses.
- [Koin](https://insert-koin.io/) - A pragmatic lightweight dependency injection framework for
  Kotlin
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform /
  multi-format reflectionless serialization
- [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) - The ViewModel
  class is a business logic or screen level state holder. It exposes state to the UI and
  encapsulates related business logic
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous
  and more.
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) -
  A cold asynchronous data stream that sequentially emits values and completes normally or with an
  exception.
- [Landscapist](https://github.com/skydoves/landscapist) - A pluggable, highly optimized Jetpack
  Compose and Kotlin Multiplatform image loading library that fetches and displays network images
  with Glide, Coil, and Fresco.
- [Android Studio](https://developer.android.com/studio/intro) - Android Studio is the official
  Integrated Development Environment (IDE) for Android app development.
- [XCode](https://developer.apple.com/xcode/) - Xcode 14 includes everything you need to develop,
  test, and distribute apps across all Apple platforms.

## Before running!

- check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- install JDK 11 or higher on your machine
- add `local.properties` file to the project root and set a path to Android SDK there

### Android

To run the application on Android device/emulator:

- open the project in Android Studio and run the imported android run configuration

To build the application bundle:

- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`
  Run android simulator UI tests: `./gradlew :composeApp:pixel5Check`

### iOS

To run the application on an iPhone device/simulator:

- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
- Or
  use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
  for Android Studio
  Run iOS simulator UI tests: `./gradlew :composeApp:iosSimulatorArm64Test`

### Desktop

- Run the desktop application: `./gradlew :composeApp:run`
- Run desktop UI tests: `./gradlew :composeApp:jvmTest`

### Web

- Before running make sure you have `yarn 1.22.22`
- Run the web application: `./gradlew wasmJsBrowserDevelopmentRun`

## Project structure

### [`composeApp`](/composeApp)

This is a Kotlin module that contains the logic common for all platforms.
This shared module is where you write your Compose Multiplatform code. In
`composeApp/src/commonMain/kotlin/App.kt`, you can find the shared root `@Composable` function for
your app.
It uses Gradle as the build system. You can add dependencies and change settings in
`composeApp/build.gradle.kts`. The shared module builds into platform-specific libraries:

- Android library for Android
- Framework for iOS
- JVM library for Desktop
- JavaScript library for Web

#### Complete `composeApp` directory structure

The complete structure of the `composeApp` module showing all platform-specific directories:

```
composeApp/
├── build.gradle.kts
├── src/
│   ├── androidMain/
│   │   ├── kotlin/
│   │   ├── res/
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   ├── commonMain/
│   │   ├── composeResources/
│   │   │   └── ...
│   │   └── kotlin/
│   │       ├── constant/
│   │       │   └── AppConstant.kt
│   │       ├── data/
│   │       │   ├── model/
│   │       │   │   ├── developer/
│   │       │   │   ├── games/
│   │       │   │   ├── genres/
│   │       │   │   ├── publisher/
│   │       │   │   └── BaseModel.kt
│   │       │   ├── remote/
│   │       │   │   ├── ApiClient.kt
│   │       │   │   ├── ApiInterface.kt
│   │       │   │   └── ApiService.kt
│   │       │   └── repository/
│   │       │       └── Repository.kt
│   │       ├── di/
│   │       │   ├── AppModule.kt
│   │       │   ├── KoinApplication.kt
│   │       │   ├── KoinInitializer.kt
│   │       │   └── PlatformModule.kt
│   │       ├── navigation/
│   │       │   ├── NavGraph.kt
│   │       │   └── NavigationScreen.kt
│   │       ├── theme/
│   │       │   ├── Color.kt
│   │       │   ├── Shape.kt
│   │       │   ├── Theme.kt
│   │       │   └── Type.kt
│   │       ├── ui/
│   │       │   ├── component/
│   │       │   ├── screens/
│   │       │   └── App.kt
│   │       └── utils/
│   │           └── ...
│   ├── desktopMain/
│   │   └── kotlin/
│   ├── iosMain/
│   │   └── kotlin/
│   └── wasmJsMain/
│       └── ...
```

### [`androidApp`](/composeApp/src/androidMain/)

This is a Kotlin module that builds into an Android application. It uses Gradle as the build system.
The `androidApp` module depends on and uses the shared module as a regular Android library.

### [`iosApp`](/composeApp/src/iosMain/)

This is an Xcode project that builds into an iOS application. It depends on and uses the shared
module as a regular iOS framework.

## Acknowledgements

- [Kotlin Multiplatform Wizard](https://kmp.jetbrains.com/) For Starter template
- [Rawg API](https://rawg.io/apidocs) For providing the comprehensive video game database

## Developed By

<a href="https://github.com/Mkaomwakuni" target="_blank">
  <img src="https://avatars.githubusercontent.com/Mkaomwakuni" width="90" align="left">
</a>

**Mkaomwakuni**

[![GitHub](https://img.shields.io/badge/-GitHub-181717?logo=github&logoColor=white&style=for-the-badge)](https://github.com/Mkaomwakuni)
[![LinkedIn](https://img.shields.io/badge/-LinkedIn-0077B5?logo=linkedin&logoColor=white&style=for-the-badge)](https://linkedin.com/in/Mkaomwakuni)
[![Twitter](https://img.shields.io/badge/-Twitter-1DA1F2?logo=x&logoColor=white&style=for-the-badge)](https://twitter.com/Mkaomwakuni)

# License

```
Copyright 2025 Mkaomwakuni

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```