plugins {
    alias(libs.plugins.stockstracker.feature)
}

android {
    namespace = "$applicationPackage.$featureModuleName.$stocksFeedModuleName"
}

dependencies {
    implementation(project(":$coreModuleName:$coreDomainModuleName"))
}
