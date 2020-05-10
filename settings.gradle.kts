pluginManagement {
    repositories {
        jcenter()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "FukkitMC"
            url = uri("https://raw.githubusercontent.com/FukkitMC/fukkit-repo/master")
        }
        gradlePluginPortal()
    }
}
rootProject.name="LegacyFukkit"
