plugins {
    id("com.github.johnrengelman.shadow") version("+")
    id("maven-publish")
}

architectury {
    common("fabric")
    common("neoforge")
    platformSetupLoomIde()
}

val mod_id: String by project
val fabric_loader_version: String by project

loom.accessWidenerPath.set(file("src/main/resources/${mod_id}.accesswidener"))

sourceSets.main.get().resources.srcDir("src/main/generated/resources")

val toml4j_version: String by project
val jankson_version: String by project
val xjs_data_version: String by project
val xjs_compat_version: String by project
val fresult_version: String by project

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${fabric_loader_version}")

    implementation("com.moandjiezana.toml:toml4j:$toml4j_version") {
        exclude("gson")
    }
    implementation("com.github.Treetrain1:Jankson:mod-SNAPSHOT")
    implementation("com.github.Treetrain1:xjs-data:infinity-compat-SNAPSHOT")
    implementation("org.exjson:xjs-compat:$xjs_compat_version")
    implementation("com.personthecat:fresult:$fresult_version")
}
