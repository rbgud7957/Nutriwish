// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.1" apply false  // 버전 명시
    id("com.google.gms.google-services") version "4.4.2" apply false  // Google 서비스 플러그인 버전 명시
    id("com.google.firebase.crashlytics") version "2.9.4" apply false
}