plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.benchmark)
}

android {
    namespace = "com.example.benchmark"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
    }
    lint {
        targetSdk = 37
    }
    testOptions {
        targetSdk = 37
    }
    testBuildType = "release"
    buildTypes {
        debug {
            // Since isDebuggable can"t be modified by gradle for library modules,
            // it must be done in a manifest - see src/androidTest/AndroidManifest.xml
            isMinifyEnabled = true
        }
        release {
            isDefault = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    androidTestImplementation(libs.androidx.benchmark.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.junit)
    // Add your dependencies here. Note that you cannot benchmark code
    // in an app module this way - you will need to move any code you
    // want to benchmark to a library module:
    // https://developer.android.com/studio/projects/android-library#Convert

}