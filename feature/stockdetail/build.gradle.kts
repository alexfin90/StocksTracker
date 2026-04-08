plugins {
    alias(libs.plugins.stockstracker.feature)
}

android {
    namespace = "$applicationPackage.$featureModuleName.$stockDetailModuleName"
}

dependencies {
    implementation(project(":$coreModuleName:$coreDomainModuleName"))
}
