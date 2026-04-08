import com.android.build.gradle.LibraryExtension
import com.alexfin90.stockstracker.libs

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("stockstracker-library").get().get().pluginId)
                apply(libs.findPlugin("stockstracker-compose-navigation").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }
            dependencies {
                add(
                    "implementation",
                    project(":${project.coreModuleName}:${project.commonModuleName}")
                )
                add(
                    "implementation",
                    project(":${project.coreModuleName}:${project.designSystemModuleName}")
                )
            }
        }
    }
}

