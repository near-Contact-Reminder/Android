import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.application)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.alarmy.near"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.alarmy.near"
        minSdk = 27
        targetSdk = 35
        versionCode = 4
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("String", "NEAR_URL", getProperty("NEAR_PROD_URL"))
            buildConfigField("String", "TEMP_TOKEN", getProperty("TEMP_TOKEN")) // TODO 추후 삭제 필요
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getProperty("KAKAO_NATIVE_APP_KEY"))
            buildConfigField("String", "SERVICE_AGREED_TERMS_URL", getProperty("SERVICE_AGREED_TERMS_URL"))
            buildConfigField("String", "PERSONAL_INFO_TERMS_URL", getProperty("PERSONAL_INFO_TERMS_URL"))
            buildConfigField("String", "PRIVACY_POLICY_TERMS_URL", getProperty("PRIVACY_POLICY_TERMS_URL"))
            manifestPlaceholders["kakaoAppKey"] = getProperty("KAKAO_NATIVE_APP_KEY").replace("\"", "")
        }
        debug {
            buildConfigField("String", "NEAR_URL", getProperty("NEAR_DEV_URL"))
            buildConfigField("String", "TEMP_TOKEN", getProperty("TEMP_TOKEN")) // TODO 추후 삭제 필요
            buildConfigField("String", "KAKAO_NATIVE_APP_KEY", getProperty("KAKAO_NATIVE_APP_KEY"))
            buildConfigField("String", "SERVICE_AGREED_TERMS_URL", getProperty("SERVICE_AGREED_TERMS_URL"))
            buildConfigField("String", "PERSONAL_INFO_TERMS_URL", getProperty("PERSONAL_INFO_TERMS_URL"))
            buildConfigField("String", "PRIVACY_POLICY_TERMS_URL", getProperty("PRIVACY_POLICY_TERMS_URL"))
            manifestPlaceholders["kakaoAppKey"] = getProperty("KAKAO_NATIVE_APP_KEY").replace("\"", "")
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
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    // Retrofit & OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization.converter)
    implementation(libs.logging.interceptor)
    // Coil
    implementation(libs.coil.compose)
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    implementation(libs.room.paging)
    // Navigation
    implementation(libs.navigation.compose)
    // Serialization
    implementation(libs.kotlin.serialization.json)
    // DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)

    // Kakao Module
    implementation(libs.v2.all)

    // Splash Screen API
    implementation(libs.androidx.core.splashscreen)

    // libphonenumber-android for phone number formatting
    implementation("io.michaelrocks:libphonenumber-android:9.0.15")

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.craslytics)
    implementation(libs.firebase.messaging.sevice)
}

fun getProperty(propertyKey: String): String = gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
