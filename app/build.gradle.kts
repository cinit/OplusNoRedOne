plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "dev.tmpfs.oplus.redonekiller"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.tmpfs.oplus.redonekiller"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions.dex.useLegacyPackaging = false
}

dependencies {
    compileOnly(libs.androidx.annotation)
    compileOnly(libs.xposed.api)
}
