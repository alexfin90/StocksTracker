
plugins {
    alias(libs.plugins.stockstracker.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "$applicationPackage.$coreModuleName.$coreDataModuleName"
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

dependencies{
    implementation(project(":$coreModuleName:$commonModuleName"))
    implementation(project(":$coreModuleName:$coreDomainModuleName"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
}

