plugins {
    id("com.github.johnrengelman.shadow") version("8.1.1")
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
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
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

    implementation("com.moandjiezana.toml:toml4j:$toml4j_version") {
        exclude("gson")
    }
    implementation("com.github.Treetrain1:Jankson:mod-SNAPSHOT")
    implementation("com.github.Treetrain1:xjs-data:infinity-compat-SNAPSHOT")
    implementation("org.exjson:xjs-compat:$xjs_compat_version")
    implementation("com.personthecat:fresult:$fresult_version")

    "shadowBundle"("com.moandjiezana.toml:toml4j:$toml4j_version") {
        exclude("gson")
    }
    "shadowBundle"("com.github.Treetrain1:Jankson:mod-SNAPSHOT")
    "shadowBundle"("com.github.Treetrain1:xjs-data:infinity-compat-SNAPSHOT")
    "shadowBundle"("org.exjson:xjs-compat:$xjs_compat_version")
    "shadowBundle"("com.personthecat:fresult:$fresult_version")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionNeoForge"))
}

tasks {
    shadowJar {
        exclude("architectury.common.json")

        relocate("com.moandjiezana.toml", "net.frozenblock.lib.shadow.com.moandjiezana.toml")
        relocate("blue.endless.jankson", "net.frozenblock.lib.shadow.blue.endless.jankson")
        relocate("xjs.data", "net.frozenblock.lib.shadow.xjs.data")
        relocate("xjs.compat.serialization", "net.frozenblock.lib.shadow.xjs.compat.serialization")
        relocate("personthecat.fresult", "net.frozenblock.lib.shadow.personthecat.fresult")

        configurations = listOf(project.configurations.getByName("shadowBundle"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        atAccessWideners.add("${mod_id}.accesswidener")
    }
}
