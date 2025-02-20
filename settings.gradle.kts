rootProject.name = "demo-msa"

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

    plugins {
        java
        kotlin("jvm") version kotlinVersion
    }
}