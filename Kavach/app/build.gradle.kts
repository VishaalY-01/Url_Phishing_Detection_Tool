plugins {
    alias(libs.plugins.androidApplication)
    id("com.chaquo.python")
}

android {
    namespace = "com.example.kavach"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kavach"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a","armeabi-v7a","x86", "x86_64")
        }
        flavorDimensions += "pyVersion"
        productFlavors {
//            create("py310") { dimension = "pyVersion" }
            create("py311") { dimension = "pyVersion" }
        }
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
    buildFeatures {
        viewBinding = true
    }
}
chaquopy {
    defaultConfig {
        version="3.11"
        pip {
            // A requirement specifier, with or without a version number:
//                install("scipy")
//                install("requests==2.24.0")


//                install("tensorflow")
            //install("requests==2.8.0")
            // An sdist or wheel filename, relative to the project directory:
//                install("MyPackage-1.2.3-py2.py3-none-any.whl")

            // A directory containing a setup.py, relative to the project
            // directory (must contain at least one slash):
            //install("./MyPackage")

            // "-r"` followed by a requirements filename, relative to the
            // project directory:
            //install("-r", "requirements.txt")
//            install("h5py")
//            install("pandas")
//            install("numpy")
//            install("scikit-learn")
//            install("keras")
        }

        buildPython("C:/Users/yadav/AppData/Local/Programs/Python/Python311/python.exe")
    }
    productFlavors {
//        getByName("py310") { version = "3.10" }
        getByName("py311") { version = "3.11" }
    }
    sourceSets {
        getByName("main") {
            srcDir("src/main/python")
        }
    }
}
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(files("libs\\ojdbc14.jar"))
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

}