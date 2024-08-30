<h1 align="center"> Movies DB </h1> <br>

<p align="center">
  Android app to browse and search movie data using the OMDB API. Built with Kotlin.
</p>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Feedback](#feedback)
- [Build Process](#build-process)

## Introduction

Search for any movie by title and view detailed information about it. The app is built using Kotlin, following the MVVM architecture, and uses Dagger-Hilt for dependency injection. Asynchronous API operations are handled with Coroutines and Retrofit.

**Upcoming Features:** Users will be able to add movies to their wishlist and mark the ones they've watched.

## Features

A few of the things you can do with Movies DB Application:
* Easily search for any movie by title and access detailed information.
* Get in-depth information about selected movies, including genre, description, release date, and IMDb rating.

<p align="center">
  <img src = "https://i.imgur.com/W0wU6ZI.png" width=700>
</p>

## Feedback

Feel free to send me feedback on [file an issue](https://github.com/neslihanaydin/MoviesDB-Kotlin/issues/new).

## Build Process

To build and run the Movies DB Application, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/neslihanaydin/MoviesDB-Kotlin.git
   ```

2. **Open the Project in Android Studio:**
   - Open Android Studio.
   - Select **Open an existing project**, and navigate to the cloned folder.

3. **Sync the Project:**
   - Android Studio will automatically sync the project. If not, click on **File > Sync Project with Gradle Files**.

4. **Configure the API Key:**
   - Navigate to `com/nt/moviesapp/util/Constants.kt`.
   - Replace the `API_KEY` constant with your own API key:
     ```kotlin
     const val API_KEY = "your_api_key"
     ```
   - Ensure you have registered for an API key from [OMDb API](http://www.omdbapi.com/apikey.aspx).

5. **Build the Project:**
   - Click on **Build > Make Project**.

6. **Run the App:**
   - To run the app on an emulator or connected device, click on **Run > Run 'app'**.

7. **Dependencies:**
   - Ensure that all required dependencies are correctly installed. You can view and manage dependencies in the `build.gradle` file.
