rootProject.name = "toy-backend"

val buildFiles: FileTree = fileTree(rootDir) {
    include("**/*.gradle", "**/*.gradle.kts")
    exclude(".*", "out", "build.gradle", "build.gradle.kts", "settings.gradle", "settings.gradle.kts")
}

val rootDirPath = rootDir.absolutePath + File.separator
buildFiles.forEach { buildFile: File ->
    val buildFilePath: String = buildFile.parentFile.absolutePath
    val projectPath: String = buildFilePath.replace(rootDirPath, "")
        .replace(File.separator, ":")
    include(projectPath)
}

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        java
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.lombok") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }
}