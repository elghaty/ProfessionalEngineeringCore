plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "com.electricalengineeringpro.app.core"

    compileSdk = 35

    defaultConfig {
        applicationId = "com.electricalengineeringpro.app.core"

        minSdk = 24

        targetSdk = 35

        versionCode = 1

        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }

        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")

    implementation("androidx.activity:activity-compose:1.10.0")

    implementation("androidx.compose.ui:ui:1.7.6")

    implementation("androidx.compose.material3:material3:1.3.1")

    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")

    debugImplementation(
        "androidx.compose.ui:ui-tooling:1.7.6"
    )

    testImplementation("junit:junit:4.13.2")
}
