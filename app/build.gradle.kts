import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android") version "1.9.24"  // 수지님 변경사항
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.polling"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.polling"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Properties 객체 생성하여 local.properties 파일 로드
        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))

        // API 키를 BuildConfig에 설정
        buildConfigField("String", "OPENAI_API_KEY", "\"${localProperties.getProperty("OPENAI_API_KEY")}\"")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    viewBinding { enable = true } // 수지님 변경사항

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Core, Activity, AppCompat
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation("androidx.activity:activity-ktx:1.2.4") // 수지님

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.fragment.ktx)

    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.appcompat:appcompat:1.6.0")
    implementation ("com.google.android.material:material:<version>")


    // 네트워크 통신 라이브러리 추가 (OkHttp, Gson)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.8.8")

    // Retrofit (API 호출을 더 쉽게 관리)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 변환용

    // JSON 파싱용 Gson 추가
    implementation("com.google.code.gson:gson:2.9.1")
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)

    // 캘린더 추가 의존성
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.1.1")

    // 테스트 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
