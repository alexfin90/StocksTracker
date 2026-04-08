import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.alex90fin.build_logic.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("hilt") {
            id = libs.plugins.stockstracker.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("composeNavigation") {
            id = libs.plugins.stockstracker.compose.navigation.get().pluginId
            implementationClass = "ComposeNavigationConventionPlugin"
        }
        register("flavors") {
            id = libs.plugins.stockstracker.flavors.get().pluginId
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.stockstracker.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("featureModule"){
            id = libs.plugins.stockstracker.feature.get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("application") {
            id = libs.plugins.stockstracker.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.stockstracker.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}


