plugins {
    id("com.android.application")
    id("com.google.gms.google-services")  // Google 서비스 플러그인
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.nutriwish"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nutriwish"
        minSdk = 30
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
}

dependencies {

    //파이어베이스 의존성 추가
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))  // Firebase BoM
    implementation("com.google.firebase:firebase-analytics")              // Firebase Analytics
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-messaging:24.0.3")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("androidx.work:work-runtime:2.7.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.fragment:fragment:1.8.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}