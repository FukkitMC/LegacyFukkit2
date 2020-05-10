import java.util.function.Function

plugins {
    id("fabric-loom") version "g0.2.7-20200411.095632-3"
}


group = "io.github.fukkitmc"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    maven {
        name = "spigotandbukkit"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/public")
    }

    maven {
        name = "legacy-fabric"
        url = uri("https://dl.bintray.com/legacy-fabric/Legacy-Fabric-Maven")
    }

}

dependencies {
    compile("com.google.guava:guava:23.5-jre")
    minecraft("net.minecraft:minecraft:1.8.9")

    mappings("net.fabricmc:yarn:1.8.9+build.202004290055:v2")
    modImplementation("net.fabricmc:fabric-loader-1.8.9:0.8.2+build.202005040913") {
        exclude(module = "guava")
    }

    implementation("org.bukkit:bukkit:1.8.8-R0.1-SNAPSHOT")
    implementation("org.yaml:snakeyaml:1.26")
    implementation("org.xerial:sqlite-jdbc:3.7.2")
    implementation("mysql:mysql-connector-java:5.1.14")
    implementation("net.sf.trove4j:trove4j:3.0.3")
    implementation("net.sf.jopt-simple:jopt-simple:3.2")
    implementation("jline:jline:2.12")


}

minecraft {
    intermediaryUrl = Function {
        "https://dl.bintray.com/legacy-fabric/Legacy-Fabric-Maven/net/fabricmc/intermediary/1.8.9/intermediary-1.8.9-v2.jar"
    }

    accessWidener = File("src/main/resources/legacyfukkit.aw")

    loadDefinitions("definitions/fields.json")
    loadDefinitions("definitions/redirects.json")
}
