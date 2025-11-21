val mod_id: String by extra
val mod_version: String by extra
val mc_version: String by extra
val registrate_version: String by extra
val create_version: String by extra
val flightlib_version: String by extra
val curios_version: String by extra
val caelus_version: String by extra
val jei_version: String by extra
val emi_version: String by extra
val night_config_version: String by extra
val trinkets_version: String by extra
val cca_version: String by extra

plugins {
    id("com.possible-triangle.fabric")
}

withKotlin()

mod {
    mods.include("com.possible-triangle:flightlib-fabric:${flightlib_version}")
}

fabric {
    dataGen()
}

base {
    archivesName.set("$mod_id-fabric-$mod_version")
}

repositories {
    maven {
        url = uri("https://maven.createmod.net")
        content {
            includeGroupAndSubgroups("net.createmod")
            includeGroupAndSubgroups("dev.engine-room")
        }
    }

    maven {
        url = uri("https://mvn.devos.one/releases/")
        content {
            includeGroup("io.github.fabricators_of_create.Porting-Lib")
        }
    }

    maven {
        url = uri("https://mvn.devos.one/snapshots/")
        content {
            includeGroup("com.simibubi.create")
            includeGroup("io.github.tropheusj")
            includeGroup("com.tterrag.registrate_fabric")
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
            includeGroupAndSubgroups("com.github.LlamaLad7")
            includeGroup("com.github.Chocohead")
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
            includeGroup("fuzs.forgeconfigapiport")
        }
    }

    maven {
        url = uri("https://maven.terraformersmc.com/")
        content {
            includeGroup("dev.emi")
        }
    }

    maven {
        url = uri("https://maven.blamejared.com/")
        content {
            includeGroup("mezz.jei")
        }
    }

    nexus {
        content {
            includeGroup("com.possible-triangle")
        }
    }

    // TODO remove
    maven {
        url = uri("https://maven.pkg.github.com/PssbleTrngle/FlightLib")
        credentials {
            username = env["GITHUB_ACTOR"]
            password = env["GITHUB_TOKEN"]
        }
        content {
            includeGroup("com.possible-triangle")
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
    modApi("dev.emi:trinkets:${trinkets_version}")

    // Should be included with trinkets but is not available at compile time somehow
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cca_version}")
    modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${cca_version}")

    if (!env.isCI) {
        modRuntimeOnly("mezz.jei:jei-${mc_version}-fabric:${jei_version}")
        modRuntimeOnly("dev.emi:emi-fabric:${emi_version}")

        //modRuntimeOnly("com.electronwill.night-config:core:${night_config_version}")
        //modRuntimeOnly("com.electronwill.night-config:toml:${night_config_version}")
    }

    modCompileOnly("com.possible-triangle:flightlib-api:${flightlib_version}")
}

upload {
    maven {
        githubPackages()
        nexus()
    }

    curseforge {
        dependencies {
            required("create-fabric")
        }
    }

    modrinth {
        dependencies {
            required("Xbc0uyRg")
        }
    }
}

enableSpotless()