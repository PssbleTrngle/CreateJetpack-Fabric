import net.darkhax.curseforgegradle.TaskPublishCurseForge
import java.time.LocalDateTime

val mod_id: String by extra
val mappings_channel: String by extra
val mc_version: String by extra
val registrate_version: String by extra
val create_version: String by extra
val flywheel_version: String by extra
val flightlib_version: String by extra
val repository: String by extra
val mod_version: String by extra
val mod_name: String by extra
val mod_author: String by extra
val release_type: String by extra
val modrinth_project_id: String by extra
val curseforge_project_id: String by extra
val curios_version: String by extra
val caelus_version: String by extra
val jei_version: String by extra
val emi_version: String by extra
val night_config_version: String by extra

val localEnv = file(".env").takeIf { it.exists() }?.readLines()?.associate {
    val (key, value) = it.split("=")
    key to value
} ?: emptyMap()

val env = System.getenv() + localEnv
val isCI = env["CI"] == "true"

plugins {
    id("fabric-loom") version ("1.0-SNAPSHOT")
    id("idea")
    id("maven-publish")
    id("net.darkhax.curseforgegradle") version ("1.0.8")
    id("com.modrinth.minotaur") version ("2.+")
    id("org.jetbrains.kotlin.jvm") version("1.8.21")
}

apply(plugin = "kotlin")

val artifactGroup = "com.possible_triangle"
base {
    archivesName.set("$mod_id-fabric-$mod_version")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

loom {
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run/server")
        }
    }
}

// Include assets and data from data generators
sourceSets["main"].resources.srcDir("src/generated/resources/")

repositories {
    maven {
        url = uri("https://repo.spongepowered.org/repository/maven-public/")
    }
    maven {
        url = uri("https://www.cursemaven.com")
        content {
            includeGroup ("curse.maven")
        }
    }
    maven {
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven {
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
        }
    }
    maven {
        url = uri("https://maven.architectury.dev/")
        content {
            includeGroup("dev.architectury")
        }
    }
    maven {
        url = uri("https://maven.saps.dev/minecraft")
        content {
            includeGroup("dev.latvian.mods")
        }
    }
    maven {
        url = uri("https://mvn.devos.one/snapshots/")
        content {
            includeGroup("com.simibubi.create")
            includeGroup("io.github.fabricators_of_create.Porting-Lib")
            includeGroup("io.github.tropheusj")
            includeGroup("com.tterrag.registrate_fabric")
        }
    }
    maven {
        url = uri("https://maven.tterrag.com/")
        content {
            includeGroup("com.jozufozu.flywheel")
        }
    }
    maven {
        url = uri("https://maven.jamieswhiteshirt.com/libs-release")
        content {
            includeGroup("com.jamieswhiteshirt")
        }
    }
    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroup("com.github.LlamaLad7")
            includeGroup("com.github.Chocohead")
            includeGroup("com.github.llamalad7.mixinextras")
        }
    }
    maven {
        url = uri("https://maven.blamejared.com")
        content {
            includeGroup("com.faux.ingredientextension")
        }
    }
    maven {
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
        content {
            includeGroup("net.minecraftforge")
        }
    }
    maven {
        url = uri("https://maven.terraformersmc.com/")
        content {
            includeGroup("dev.emi")
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/PssbleTrngle/FlightLib")
        credentials {
            username = env["GITHUB_ACTOR"]
            password = env["GITHUB_TOKEN"]
        }
        content {
            includeGroup("com.possible_triangle")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${mc_version}")
    mappings(loom.officialMojangMappings())

    modApi("com.simibubi.create:create-fabric-${mc_version}:${create_version}")

    if (!isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-fabric:${jei_version}")
        modRuntimeOnly("dev.emi:emi-fabric:${emi_version}")

        //modRuntimeOnly("com.electronwill.night-config:core:${night_config_version}")
        //modRuntimeOnly("com.electronwill.night-config:toml:${night_config_version}")
    }

    modCompileOnly("com.possible_triangle:flightlib-api:${flightlib_version}")
    modRuntimeOnly("com.possible_triangle:flightlib-fabric:${flightlib_version}")
    include("com.possible_triangle:flightlib-fabric:${flightlib_version}")

    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.1+kotlin.1.8.10")
}

tasks.withType<Jar> {
    from(rootProject.file("LICENSE")) {
        rename { "${it}_${mod_id}" }
    }

    manifest {
        attributes(
            mapOf(
                "Specification-Title" to mod_id,
                "Specification-Vendor" to "examplemodsareus",
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to mod_version,
                "Implementation-Vendor" to "examplemodsareus",
                "Implementation-Timestamp" to LocalDateTime.now().toString(),
            )
        )
    }
}

tasks.withType<ProcessResources> {
    // this will ensure that this task is redone when the versions change.
    inputs.property("version", version)

    filesMatching(listOf("fabric.mod.json", "pack.mcmeta", "${mod_id}.mixins.json")) {
        expand(
            mapOf(
                "mod_version" to mod_version,
                "mod_name" to mod_name,
                "mod_id" to mod_id,
                "mod_author" to mod_author,
                "repository" to repository,
            )
        )
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/${repository}")
            version = version
            credentials {
                username = env["GITHUB_ACTOR"]
                password = env["GITHUB_TOKEN"]
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            groupId = artifactGroup
            artifactId = "${mod_id}-fabric"
            version = mod_version
            from(components["java"])
        }
    }
}

env["CURSEFORGE_TOKEN"]?.let { token ->
    tasks.register<TaskPublishCurseForge>("curseforge") {
        dependsOn(tasks.jar)

        apiToken = token

        upload(curseforge_project_id, tasks.remapJar.get().archiveFile).apply {
            changelogType = "html"
            changelog = env["CHANGELOG"]
            releaseType = release_type
            addModLoader("Fabric")
            addGameVersion(mc_version)
            displayName = "Fabric $mod_version"

            addRelation("create-fabric", "requiredDependency")
            addRelation("fabric-language-kotlin", "requiredDependency")
        }
    }
}

env["MODRINTH_TOKEN"]?.let { modrinthToken ->
    modrinth {
        token.set(modrinthToken)
        projectId.set(modrinth_project_id)
        versionNumber.set(mod_version)
        versionName.set("Fabric $mod_version")
        changelog.set(env["CHANGELOG"])
        gameVersions.set(listOf(mc_version))
        loaders.set(listOf("fabric"))
        versionType.set(release_type)
        uploadFile.set(tasks.remapJar.get())
        dependencies {
            required.project("Xbc0uyRg")
            required.project("Ha28R6CL")
        }
    }
}