plugins {
    id("com.android.application")
    id("kotlin-android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "chakuy.com.vbo"
    compileSdk = 34

    defaultConfig {
        applicationId = "chakuy.com.vbo"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("emergenciabol.keystore")
            storePassword = "vallegrande2135"
            keyAlias = "tasajos"
            keyPassword = "vallegrande2135"
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
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
        viewBinding = true
    }
}

configurations.all {
    resolutionStrategy {
        force("androidx.datastore:datastore-core:1.1.1")
        force("androidx.datastore:datastore-preferences:1.1.1")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.firebase.storage)
    // <--- AÑADIR ESTA LINEA PARA CORREGIR EL ERROR DE 16 KB
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")
}