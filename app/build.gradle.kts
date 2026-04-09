plugins {
    alias(libs.plugins.stockstracker.application)
    alias(libs.plugins.stockstracker.flavors)
    alias(libs.plugins.stockstracker.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = project.applicationPackage
    defaultConfig {
        buildConfigField(
            "String",
            "DEEPLINK_BASE_URL",
            "\"stockstracker://stock.detail\""
        )
    }
}

dependencies{
    //App dependencies
    implementation(project(":$coreModuleName:$designSystemModuleName"))
    implementation(project(":$coreModuleName:$commonModuleName"))
    implementation(project(":$coreModuleName:$coreDomainModuleName"))
    implementation(project(":$coreModuleName:$coreDataModuleName"))
    //Feature module dependencies
    implementation(project(":$featureModuleName:$stocksFeedModuleName"))
    implementation(project(":$featureModuleName:$stockDetailModuleName"))
    //Others dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.google.firebase.analytics)
}