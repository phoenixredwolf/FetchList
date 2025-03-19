plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.phoenixredwolf.fetchlist"
    compileSdk = ProjectConfigs.COMPILE_SDK

    defaultConfig {
        applicationId = "com.phoenixredwolf.fetchlist"
        minSdk = ProjectConfigs.MIN_SDK
        targetSdk = ProjectConfigs.TARGET_SDK
        versionCode = ProjectConfigs.VERSION_CODE
        versionName = ProjectConfigs.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["hostName"] = "www.phoenixredwolf.com"
        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://fetch-hiring.s3.amazonaws.com\""
        )

    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("staging") {
            initWith(getByName("debug"))
            manifestPlaceholders["hostName"] = "internal.phoenixredwolf.com"
            applicationIdSuffix = ".debugStaging"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    kotlinOptions {
        jvmTarget = "22"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {

    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Coroutines
    implementation(libs.kotlinx.coroutines)

    // Google Fonts
    implementation(libs.accompanist.flowlayout)
    implementation(libs.androidx.ui.text.google.fonts)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Material Design
    implementation(libs.androidx.material3)

    // Moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)

    // Navigation
    implementation(libs.navigation.compose)

    // OkHTTP
    implementation(libs.okhttp3.base)
    implementation(libs.okhttp3.logging)

    // Retrofit
    implementation(libs.retrofit.base)
    implementation(libs.retrofit.converter)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.junit4)
    testImplementation(libs.koin.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
    testImplementation(libs.okhttp3.mockwebserver)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk.android)
    testImplementation(kotlin("test"))

    // UI Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.koin.test)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}