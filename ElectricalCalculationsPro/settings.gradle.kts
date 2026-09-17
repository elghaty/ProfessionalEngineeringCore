pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name =
    "ElectricalCalculationsPro"

include(":app")
include(":professional-core")

project(":professional-core").projectDir =
    file("professional-core/app")
