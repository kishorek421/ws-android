import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.bw.wshost"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bw.wshost"
        minSdk = 26
        targetSdk = 34
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

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/INDEX.LIST",
                "META-INF/io.netty.versions.properties"
            )
        )
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.ktor.server.core) // Use alias from the version catalog
    implementation(libs.ktor.server.netty) // Use alias from the version catalog
    implementation(libs.ktor.server.host.common) // Use alias from the version catalog
    implementation(libs.ktor.server.websockets ) // Use alias from the version catalog
    implementation(libs.ktor.client.core ) // Use alias from the version catalog
    implementation(libs.ktor.client.okhttp ) // Use alias from the version catalog
    implementation(libs.ktor.client.websockets ) // Use alias from the version catalog
//    implementation(libs.logback) // Use alias from the version catalog
    implementation(libs.kmongoCoroutine)
    implementation(libs.kmongoAsync)
}