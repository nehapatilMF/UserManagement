plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.usermanagementapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.usermanagementapp"
        minSdk = 24
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
    viewBinding{
        enable = true
    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.0")

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Adding an google material design dependency for using material components
    implementation ("com.google.android.material:material:1.11.0")

    // Dexter runtime permissions
    // https://github.com/Karumi/Dexter
    implementation ("com.karumi:dexter:6.0.1")

    // https://github.com/hdodenhof/CircleImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // This is for place picker
    implementation ("com.google.android.libraries.places:places:3.3.0")

    // This is for getting the location
    implementation ("com.google.android.gms:play-services-location:21.1.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}