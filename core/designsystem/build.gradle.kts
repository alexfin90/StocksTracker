plugins {
    alias(libs.plugins.stockstracker.library)
    alias(libs.plugins.stockstracker.compose.navigation)
}

android {
    namespace = "$applicationPackage.$coreModuleName.$designSystemModuleName"

    defaultConfig {
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
}

dependencies {
    api(libs.androidx.appcompat)
    api(libs.material)
    //Common Module
    api(project(":$coreModuleName:$commonModuleName"))
}