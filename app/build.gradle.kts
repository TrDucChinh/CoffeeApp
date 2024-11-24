plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("kotlin-android")
    id ("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.proptit.btl_oop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.proptit.btl_oop"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Firebase BOM
    implementation(platform(libs.firebase.bom))
    // Firebase Realtime Database
    implementation(libs.firebase.database)
    // Firebase Authentication
    implementation (libs.firebase.auth)
    // Firebase google auth
    implementation(libs.play.services.auth)
    // Glide for image loading and caching
    implementation (libs.glide)
    // Gson for JSON parsing
    implementation (libs.gson)
    implementation(libs.play.services.location)


    val nav_version = "2.7.2"
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("com.google.android.material:material:1.12.0")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
}