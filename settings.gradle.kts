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
<<<<<<< HEAD
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "guru2"
=======
    }
}

rootProject.name = "Polling"
>>>>>>> fb57e30344c62b80b669c8366a4fc1310ef84664
include(":app")
 