plugins {
    id("com.android.application")
}

android {
    namespace = "project.prm392_oss"
    compileSdk = 35

    defaultConfig {
        applicationId = "project.prm392_oss"
        minSdk = 27
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Room Database
    implementation(libs.room.runtime.v252)
    annotationProcessor(libs.room.compiler) // Không dùng kapt
// https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-viewmodel
    runtimeOnly(libs.lifecycle.viewmodel)
// https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-livedata
    runtimeOnly(libs.lifecycle.livedata)
    // https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-extensions
    runtimeOnly(libs.lifecycle.extensions)

    // https://mvnrepository.com/artifact/com.squareup.picasso3/picasso
    implementation(libs.picasso)
// https://mvnrepository.com/artifact/com.squareup.picasso/picasso

    // https://mvnrepository.com/artifact/androidx.appcompat/appcompat
    runtimeOnly(libs.appcompat)

    // https://mvnrepository.com/artifact/com.squareup.picasso/picasso
    implementation(libs.picasso.v252)



    // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
    implementation(libs.glide)
// https://mvnrepository.com/artifact/javax.mail/javax.mail-api
    implementation (libs.android.mail)
    implementation (libs.android.activation)


    // https://mvnrepository.com/artifact/com.squareup.picasso/picasso
    implementation(libs.squareup.picasso)

}
