val mod_id: String by extra
val mod_version: String by extra
val mc_version: String by extra
val registrate_version: String by extra
val create_version: String by extra
val flywheel_version: String by extra
val flightlib_version: String by extra
val curios_version: String by extra
val caelus_version: String by extra
val jei_version: String by extra
val emi_version: String by extra
val night_config_version: String by extra
val trinkets_version: String by extra

plugins {
    id("com.possible-triangle.gradle") version ("0.1.0")
}

withKotlin()

fabric {
    dataGen()
    includesMod("com.possible_triangle:flightlib-fabric:${flightlib_version}")
}

base {
    archivesName.set("$mod_id-fabric-$mod_version")
}

repositories {
    apply(from = "https://raw.githubusercontent.com/PssbleTrngle/GradleHelper/main/repositories/create-fabric.build.kts")

    curseMaven()
    modrinthMaven()

    maven {
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
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

    maven {
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroup("dev.onyxstudios.cardinal-components-api")
        }
    }
}

dependencies {
    modApi("com.simibubi.create:create-fabric-${mc_version}:${create_version}")
    modImplementation("dev.emi:trinkets:${trinkets_version}")

    if (!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-fabric:${jei_version}")
        modRuntimeOnly("dev.emi:emi-fabric:${emi_version}")

        //modRuntimeOnly("com.electronwill.night-config:core:${night_config_version}")
        //modRuntimeOnly("com.electronwill.night-config:toml:${night_config_version}")
    }

    modCompileOnly("com.possible_triangle:flightlib-api:${flightlib_version}")
}

enablePublishing {
    repositories {
        githubPackages(project)
    }
}

uploadToCurseforge {
    dependencies {
        required("create-fabric")
    }
}

uploadToModrinth {
    dependencies {
        required("Xbc0uyRg")
    }
}