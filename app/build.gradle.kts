plugins {
    id("com.android.application")
    kotlin("android") version "1.6.10" // Kotlin version must be explicitly stated
    kotlin("kapt") version "1.6.10"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "com.example.assignments"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.assignments"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.maps.android:android-maps-utils:2.2.5")
    implementation("com.android.volley:volley:1.2.1")

    //Glide dependencies
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.firebase:firebase-crashlytics:18.6.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")  //It simplifies the process of making network requests and managing API calls.


    //Voice Simulation
    implementation("com.melnykov:floatingactionbutton:1.3.0")
    implementation ("com.jpardogo.materialtabstrip:library:1.1.1")
    implementation ("com.jakewharton:butterknife:10.2.3")
    implementation ("com.jakewharton:butterknife-compiler:10.2.3")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")


    //For snackbar
    implementation ("com.google.android.material:material:<version>")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage:20.3.0")


    //For recycle view
    implementation ("androidx.recyclerview:recyclerview:1.2.1")


    //For swipe background
    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}