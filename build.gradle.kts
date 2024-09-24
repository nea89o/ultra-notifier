import com.replaymod.gradle.preprocess.PreprocessExtension
import moe.nea.sharedbuild.MinecraftVersion
import moe.nea.sharedbuild.Versions
import moe.nea.sharedbuild.parseEnvFile
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RunGameTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("java")
	kotlin("jvm")
	id("com.github.johnrengelman.shadow")
}

val version = Versions.values().find { it.projectPath == project.path }!!
if (version.forgeDep != null)
	extra.set("loom.platform", "forge")
apply(plugin = "gg.essential.loom")
apply(plugin = "dev.deftu.gradle.preprocess")

val loom = the<LoomGradleExtensionAPI>()
val preprocess = the<PreprocessExtension>()

if (version.needsPack200) {
	loom.forge.pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
}
if (version.forgeDep != null) {
	loom.forge.mixinConfig("mixins.ultranotifier.json")
}
val mcJavaVersion = version.minecraftVersion.javaLanguageVersion
//loom.mixin.defaultRefmapName.set("mixins.ultranotifier.refmap.json")
java.toolchain.languageVersion.set(mcJavaVersion)
preprocess.run {
	vars.put("MC", version.minecraftVersion.versionNumber)
	vars.put("FORGE", if ((version.forgeDep != null)) 1 else 0)
	vars.put("FABRIC", if ((version.forgeDep != null)) 0 else 1)
	vars.put("JAVA", mcJavaVersion.asInt())
}
loom.run {
	this.runs {
		this.removeIf { it.name != "client" }
		this.named("client") {
			parseEnvFile(file(".env")).forEach { (t, u) ->
				this.environmentVariable(t, u)
			}
			parseEnvFile(file(".properties")).forEach { (t, u) ->
				this.property(t, u)
			}
			this.property("mixin.debug", "true")
			if (version == Versions.MC189) {
				this.programArgs("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
			}
		}
	}
}
val modImplementation by configurations.getting
val shadowImpl by configurations.creating {
	configurations.implementation.get().extendsFrom(this)
}
val shadowModImpl by configurations.creating {
	modImplementation.extendsFrom(this)
}
val include = if (version.forgeDep != null) configurations.create("includeModImpl") {
	configurations.getByName("include").extendsFrom(this)
	modImplementation.extendsFrom(this)
} else shadowModImpl
val devauthVersion = "1.1.2"
dependencies {
	"minecraft"("com.mojang:minecraft:" + version.minecraftVersion.versionName)
	"mappings"(if (version.mappingDependency == "official") loom.officialMojangMappings() else version.mappingDependency)
	if (version.forgeDep != null) {
		"forge"(version.forgeDep!!)
		runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:$devauthVersion")
	} else {
		modImplementation("net.fabricmc:fabric-loader:0.15.10")
		modImplementation("net.fabricmc.fabric-api:fabric-api:${version.fabricVersion!!}")
		runtimeOnly("me.djtheredstoner:DevAuth-fabric:$devauthVersion")
	}
	shadowImpl("com.github.therealbush:eventbus:1.0.2")
	include(version.universalCraft)
	if (version.minecraftVersion.versionNumber < 11300) {
		shadowImpl("com.mojang:brigadier:1.0.18")
	}
	if (version.forgeDep != null) {
		shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
			isTransitive = false
		}
		annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")
		annotationProcessor("com.google.code.gson:gson:2.10.1")
		annotationProcessor("com.google.guava:guava:17.0")
	}
}

tasks.withType<Jar> {
	this.destinationDirectory.set(layout.buildDirectory.dir("badjars"))
	if (version == Versions.MC189) {
		manifest.attributes(
			"FMLCorePluginContainsFMLMod" to "true",
			"ForceLoadAsMod" to "true",
			"TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
			"MixinConfigs" to "mixins.ultranotifier.json"
		)
	}
}

tasks.jar {
	archiveClassifier.set("without-dep")
}
sourceSets.main {
	val resourceFile = project.file("src/main/resources")
	println("All resources: ${resources.srcDirs}")
	if (!resources.srcDirs.contains(resourceFile)) {
		println("Added resource dir")
		resources.srcDir(resourceFile)
	}
}
tasks.shadowJar {
	archiveClassifier.set("all-dev")
	configurations = listOf(shadowImpl, shadowModImpl)
}

tasks.processResources {
	inputs.property("java", mcJavaVersion.asInt().toString())
	inputs.property("mcVersion", version.minecraftVersion.versionName)
	inputs.property("version", project.version.toString())
	inputs.property("modName", "Ultra Notifier")
	inputs.property("description", "Ultra Notifications")
	filesMatching(listOf("fabric.mod.json", "mixins.*.json", "mcmod.info", "META-INF/mods.toml")) {
		expand(inputs.properties)
	}
	if (version.forgeDep != null) {
		exclude("fabric.mod.json")
		if (version.minecraftVersion < MinecraftVersion.MC1144) {
			exclude("META-INF/mods.toml")
		} else {
			exclude("mcmod.info")
		}
	} else {
		exclude("mcmod.info")
		exclude("META-INF/mods.toml")
	}
}

tasks.named("remapJar", RemapJarTask::class) {
	this.destinationDirectory.set(layout.buildDirectory.dir("libs"))
	dependsOn(tasks.shadowJar)
	archiveClassifier.set("")
	inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
}
tasks.named("runClient", RunGameTask::class) {
	this.javaLauncher.set(javaToolchains.launcherFor {
		this.languageVersion.set(mcJavaVersion)
	})
}

if (version == Versions.MC116Forge) {
	tasks.withType<JavaCompile> {
		onlyIf { false }
	}
	tasks.withType<KotlinCompile> {
		onlyIf { false }
	}
}