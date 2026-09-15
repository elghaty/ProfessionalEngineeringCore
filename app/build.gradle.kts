plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.electrical.calculationspro.core"

    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = false
        buildConfig = false
    }

    publishing {
        singleVariant("release")
    }
}

dependencies {
    implementation(
        "androidx.core:core-ktx:1.15.0"
    )

    testImplementation(
        "junit:junit:4.13.2"
    )
}
