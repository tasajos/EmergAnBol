plugins {
    id("com.android.application")
    id("kotlin-android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "chakuy.com.vbo"
    compileSdk = 36

    defaultConfig {
        applicationId = "chakuy.com.vbo"
        minSdk = 23
        targetSdk = 35
        versionCode = 72
        versionName = "Orion 3.2"

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


    packaging {
        jniLibs {
            useLegacyPackaging = true

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
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
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation(libs.firebase.storage)
    // <--- AÑADIR ESTA LINEA PARA CORREGIR EL ERROR DE 16 KB
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.datastore:datastore-core:1.1.7")
    implementation("com.google.firebase:firebase-database:22.0.1") // O la versión que te sugiera AS
    implementation("com.github.bumptech.glide:glide:5.0.5") // Para cargar las imágenes
}