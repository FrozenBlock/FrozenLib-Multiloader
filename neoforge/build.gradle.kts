plugins {
    id("com.gradleup.shadow")
    id("java")
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
        programArgs("--all", "--mod", "examplemod")
        programArgs("--output", project(":common").file("src/main/generated/resources").absolutePath)
        programArgs("--existing", project(":common").file("src/main/resources").absolutePath)
    }
}

val relocModApi: Configuration by configurations.creating {
    configurations.modApi.get().extendsFrom(this)
}

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
    neoForge("net.neoforged:neoforge:${project.properties["neoforge_version"]}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionNeoForge"))

    implementation("com.moandjiezana.toml:toml4j:$toml4j_version")

    relocModApi("org.exjson:xjs-data:$xjs_data_version")
    relocModApi("org.exjson:xjs-compat:$xjs_compat_version")
    relocModApi("com.personthecat:fresult:$fresult_version")
}

tasks {
    shadowJar {
        exclude("architectury.common.json")
        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set("dev-shadow")
    }

    shadowJar {
        configurations = listOf(relocModApi)
        isEnableRelocation = true
        relocationPrefix = "net.frozenblock.lib.shadow"
        dependencies {
            exclude {
                it.moduleGroup.contains("fabric")
            }
        }
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        atAccessWideners.add("${mod_id}.accesswidener")
    }
}