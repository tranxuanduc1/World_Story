plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("kotlin-parcelize")

}
android {
    namespace = "com.example.myapplication"
    compileSdk = 35
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildToolsVersion = "35.0.0"
}

dependencies {
    implementation("org.mindrot:jbcrypt:0.4")
    implementation ("com.squareup.picasso:picasso:2.8")

    implementation(libs.filament.android)
    val nav_version = "2.8.3"
    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")
    implementation("com.google.android.material:material:1.9.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.flexbox)
    implementation (libs.touchimageview)
    implementation (libs.photoview)
    implementation(libs.androidx.core.ktx.v1150)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.coil)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
}