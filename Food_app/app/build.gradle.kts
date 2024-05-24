plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
//    id ('kotlin-kapt')

}

android {
    namespace = "com.example.food_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.food_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-android:1.2.0-rc01")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.airbnb.android:lottie-compose:5.2.0")
    implementation("com.google.accompanist:accompanist-pager:0.12.0")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.compose.material:material")
    implementation("io.coil-kt:coil-compose:2.6.0")

    //tablayout
    implementation ("androidx.compose.material:material-icons-extended-android:1.5.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.3")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
//    implementation("io.coil-kt:coil-gif:2.6.0")

    //shape
    implementation ("androidx.graphics:graphics-shapes:1.0.0-alpha05")

    //toast
    implementation ("com.github.tfaki:ComposableSweetToast:1.0.1")

    //compose dependencies
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    //notification
    implementation ("com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    //google login
//    implementation("androidx.credentials:credentials:1.0.0-alpha02")

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
//    implementation("androidx.credentials:credentials-play-services-auth:1.0.0-alpha02")
//    implementation ("androidx.credentials:credentials:1.1.0")
//    implementation ("androidx.credentials:credentials-play-services-auth:1.1.0")
//    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    //firebase
//    implementation ("com.google.firebase:firebase-auth-ktx:21.1.0")
//    implementation ("com.google.android.gms:play-services-auth:20.4.1")

    //coroutine
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")

    //lifecircle
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.21")

    implementation("androidx.webkit:webkit:1.8.0")

    /////
    val lifecycle_version = "2.6.2"
    implementation("io.socket:socket.io-client:2.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("com.google.code.gson:gson:2.10.1")
}