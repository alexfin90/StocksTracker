pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "StocksTracker"
include(":app")
include(":core:designsystem")
include(":core:common")
include(":core:dispatcher")
include(":core:domain")
include(":core:data")
include(":feature:stocksfeed")
include(":feature:stockdetail")
