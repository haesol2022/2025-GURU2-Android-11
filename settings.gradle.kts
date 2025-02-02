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
=======
<<<<<<< HEAD
>>>>>>> 676e1a47ab1fdc1f313756a590bdcccf0b4f37fb
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "guru2"
<<<<<<< HEAD
=======
=======
    }
}

rootProject.name = "Polling"
>>>>>>> fb57e30344c62b80b669c8366a4fc1310ef84664
>>>>>>> 676e1a47ab1fdc1f313756a590bdcccf0b4f37fb
include(":app")
 