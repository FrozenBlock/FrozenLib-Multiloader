plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    create("common")
    "common" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    create("shadowBundle")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)

    // NeoForge Datagen Gradle config.  Remove if not using NeoForge datagen
    runs.create("datagen") {
        data()
        programArgs("--all", "--mod", "frozenlib")
        programArgs("--output", project(":common").file("src/main/generated/resources").absolutePath)
        programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
    }
}

val relocModApi: Configuration by configurations.creating {
    configurations.modApi.get().extendsFrom(this)
}
val relocImplementation: Configuration by configurations

val mod_id: String by project
val mod_version: String by project
val mod_name: String by project
val mod_author: String by project
val mod_description: String by project
val credits: String by project
val mod_license: String by project
val java_version: String by project
val minecraft_version: String by project
val parchment_version: String by project
val fabric_loader_version: String by project
val fabric_api_version: String by project
val minecraft_version_range_fabric: String by project
val neoforge_version: String by project
val minecraft_version_range_neoforge: String by project

val toml4j_version: String by project
val jankson_version: String by project
val xjs_data_version: String by project
val xjs_compat_version: String by project
val fresult_version: String by project

val cloth_config_version: String by project
val modmenu_version: String by project
val terrablender_version: String by project

dependencies {
    neoForge("net.neoforged:neoforge:${neoforge_version}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionNeoForge"))
}

tasks {
    shadowJar {
        exclude("architectury.common.json")
        configurations = listOf(project.configurations.getByName("shadowBundle"))
        isEnableRelocation = false
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        atAccessWideners.add("${mod_id}.accesswidener")
    }
}
