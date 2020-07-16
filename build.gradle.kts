plugins {
    id("fabric-loom") version "g0.4.0-SNAPSHOT"
    id("io.github.fukkitmc.crusty") version "1.1.7"
}

group = "io.github.fukkitmc"
version = "1.0.0-SNAPSHOT"

configurations {
    create("ecj")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

minecraft {
    accessWidener = file("src/main/resources/craftbukkit.aw")

    loadDefinitions("definitions/definitions.json")
}

repositories {
    mavenCentral()
    maven {
        name = "SpigotMC"
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven {
        name = "legacy-fabric"
        url = uri("https://dl.bintray.com/legacy-fabric/Legacy-Fabric-Maven")
    }
}

dependencies {
    "ecj"("org.eclipse.jdt:ecj:3.21.0")
    compile("com.google.guava:guava:23.5-jre")
    minecraft("net.minecraft", "minecraft", "1.8.9")
    mappings("net.fabricmc", "yarn", "1.8.9+build.202007100557", classifier = "v2")
    modCompile("net.fabricmc", "fabric-loader-1.8.9", "0.8.2+build.202004131640"){
        exclude("com.google", "guava")
    }
    modImplementation("it.unimi.dsi:fastutil:8.2.2")
    modImplementation("net.sf.trove4j:trove4j:3.0.3")
    compileOnly("com.google.code.findbugs", "jsr305", "3.0.2")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("commons-lang:commons-lang:2.6")
    implementation("jline:jline:2.12.1")
    implementation("mysql:mysql-connector-java:5.1.48")
    implementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("org.xerial:sqlite-jdbc:3.30.1")
    implementation("org.yaml:snakeyaml:1.25")
    implementation("org.apache.logging.log4j:log4j-core:2.8.1")
    include("com.googlecode.json-simple:json-simple:1.1.1")
    include("commons-lang:commons-lang:2.6")
    include("jline:jline:2.12.1")
    include("mysql:mysql-connector-java:5.1.48")
    include("org.bukkit:bukkit:1.15.2-R0.1-SNAPSHOT")
    include("org.xerial:sqlite-jdbc:3.30.1")
    include("org.yaml:snakeyaml:1.25")
    compileOnly("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")
}

tasks.withType<JavaCompile> {
    options.isFork = true

    options.forkOptions.apply {
        executable = "java"
        jvmArgs = listOf("-classpath", project.configurations["ecj"].asPath, "org.eclipse.jdt.internal.compiler.batch.Main", "-nowarn", "-g", "-verbose", "-referenceInfo")
    }
}
