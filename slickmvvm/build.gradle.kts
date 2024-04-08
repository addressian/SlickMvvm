plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("maven-publish")
}

publishing {
    publications {
        register<MavenPublication>("MyLibrary") {
            groupId = "com.addressian.slickmvvm"
            artifactId = "slickMvvm"
            version = "0.0.04"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

android {
    namespace = "com.addressian.slickmvvm"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")

    implementation("com.tencent:mmkv:1.2.14")

    /*网络相关*/
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.2")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.github.mrmike:ok2curl:0.7.0")

    /*协程以及协程组件*/
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    //ViewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    //LifecycleScope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    //liveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    // KTX for the ViewModel component
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // UtilCode
    implementation("com.blankj:utilcodex:1.31.1")

    // EventBus
    implementation("org.greenrobot:eventbus:3.2.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
}