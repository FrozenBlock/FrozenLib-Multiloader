plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    fabric()
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
    getByName("developmentFabric").extendsFrom(configurations["common"])
    "shadowBundle" {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

loom.accessWidenerPath.set(project(":common").loom.accessWidenerPath)

// Fabric Datagen Gradle config. Remove if not using Fabric datagen
// fabricApi.configureDataGeneration()

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

val relocModApi: Configuration by configurations.creating {
    configurations.modApi.get().extendsFrom(this)
}

repositories {
    maven("https://maven.terraformersmc.com")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.minecraftforge.net/")
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")
    modApi("net.fabricmc.fabric-api:fabric-api:${fabric_api_version}+$minecraft_version")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${cloth_config_version}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("com.terraformersmc:modmenu:${modmenu_version}")
    compileOnly("com.github.glitchfiend:TerraBlender-fabric:${terrablender_version}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionFabric"))

    implementation("com.github.Treetrain1:Jankson:mod-SNAPSHOT")
    implementation("org.exjson:xjs-data:$xjs_data_version")
    implementation("org.exjson:xjs-compat:$xjs_compat_version")
    implementation("com.personthecat:fresult:$fresult_version")
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
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }
}
