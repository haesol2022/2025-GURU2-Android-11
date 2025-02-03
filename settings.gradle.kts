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
        //캘린더 추가
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "guru2"
include(":app")
=======
    }
}

rootProject.name = "Polling"
include(":app")
 
>>>>>>> origin/sol
