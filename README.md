# MyLingual
MyLingual is a multi-language translation application for android mobile devices. With MyLingual, users can practice and test their knowledge of foreign languages through translations, phrase repetition and review, and word game activities. 

## Features
* **Multi-Language Translations** via
  * Text
  * Speech
  * Camera
* **Textbook Phrases** 
* **Recent Translations**
* **Flashcards**
* **Word Games**

![MyLingual example](https://github.com/RockieKelz/MyLingual/assets/78999710/64d6504c-dce0-47f1-97d5-63fb6bca8fec)

## Technologies
* [Android Studio](https://developer.android.com/studio/)
* [Google-services](https://developers.google.com/android/guides/overview)
* [Mlkit](https://developers.google.com/ml-kit)
* [Firebase Authentication](https://firebase.google.com/docs/auth)
* [Firebase Firestore](https://firebase.google.com/docs/firestore)
* [Firebase Database](https://firebase.google.com/docs/database)
* [Firebase Storage](https://firebase.google.com/docs/storage)
## Installation
Users can currently open this project as an Android Studio project, and run it on a mobile device or a virtual device (AVD). PLEASE NOTE: This application does require access to the internet.

In order to take advantage of all of the application's features, users will need to grant MyLingual access to the device's microphone, camera, and speaker. 


## Development Setup
Developers will need to ensure that your application is configured to be used with [Firebase](https://firebase.google.com/docs/android/setup). 
  * Add the application to a Firebase project on their [Firebase console](https://console.firebase.google.com). 
  * Use the `applicationId` that is specified in the `app/build.gradle` file as the Android package name. 
  * Download the generated `google-services.json` file, and copy it to the `app/` directory.

In your `app/build.gradle` file, add the following dependencies:
```groovy
dependencies {
    // Import the Firebase BoM
    implementation platform('com.google.firebase:firebase-bom:30.4.1') 
    // Firebase Auth
    implementation 'com.google.firebase:firebase-auth'
    // Firebase Realtime Database
    implementation 'com.google.firebase:firebase-database'
    // Cloud Firestore
    implementation 'com.google.firebase:firebase-firestore:24.3.1'
    // Cloud Storage
    implementation 'com.google.firebase:firebase-storage'
    // MLKit for Translation
    implementation 'com.google.mlkit:translate:16.0.0'
    // MLKit for Camera Text recognition
    implementation 'com.google.android.gms:play-services-mlkit-text-recognition:16.0.0'

}
```

## License
MyLingual is [MIT licensed](https://choosealicense.com/licenses/mit/)

## Contributors
Racquel McCall

## Project Status
MyLingual is currently in its active development phase.
