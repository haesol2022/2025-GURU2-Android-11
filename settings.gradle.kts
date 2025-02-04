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
        //캘린더 추가
        maven { url = uri("https://repository.map.naver.com/archive/maven") }  // 네이버 맵 SDK 리포지토리 추가
        maven { url = uri("https://jitpack.io") }

    }
}

rootProject.name = "Polling"
include(":app")
