# Student Wellness Tracker App

## Overview
This Android application is designed to help students track and improve their wellness habits. It includes features for monitoring mood, sleep patterns, exercise, and stress levels.

## Key Components
- **Login/Register System**: User authentication with Firebase Auth.
- **Dashboard**: Overview of wellness metrics.
- **Data Entry**: Forms for logging daily wellness data.
- **Reports**: Visual representations of wellness data over time.
- **Account Management**: User profile and settings.

## Advanced Features
1. **Firebase Integration**: 
   - Authentication for secure user login
   - Firestore database for data storage

2. **WorkManager Background Tasks**: 
   - Automatic data synchronization
   - Daily notifications and reminders

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.8.0 or later
- Google Firebase account

### Firebase Setup
1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add an Android app to your Firebase project
   - Use package name: `com.example.a5046a3`
   - Download the `google-services.json` file
4. Place the `google-services.json` file in the app directory of your project
5. Enable Authentication in Firebase:
   - Go to Authentication > Sign-in method
   - Enable Email/Password authentication
6. Create Firestore Database:
   - Go to Firestore Database > Create database
   - Start in test mode

### Project Setup
1. Clone this repository
2. Open the project in Android Studio
3. Add the `google-services.json` file to the app directory
4. Sync the project with Gradle files
5. Build and run the application

## Architecture
- **MVVM Architecture**: Separates UI from business logic
- **Repository Pattern**: Single source of truth for data
- **Room Database**: Local data persistence
- **Jetpack Compose**: Modern UI toolkit
- **Firebase Authentication**: User management
- **Firestore**: Cloud database for data synchronization
- **WorkManager**: Background tasks and synchronization

## UI Components
- Navigation Drawer or Bottom Navigation
- Form components with validation
- Charts and graphs for data visualization
- Material Design 3 components

## Data Model
- User profiles
- Wellness entries (mood, sleep, stress)
- Exercise records
- Journal entries

## Testing
The app includes:
- Unit tests for ViewModels and Repositories
- Instrumentation tests for Room database
- UI tests for critical user flows

## License
This project is created for educational purposes as part of assignment requirements. 