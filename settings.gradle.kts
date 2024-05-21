import moe.nea.sharedbuild.Versions

pluginManagement {
	includeBuild("sharedVariables")
	repositories {
		maven("https://maven.fabricmc.net")
		maven("https://jitpack.io")
		mavenCentral()
		google()
		mavenCentral()
		gradlePluginPortal()
		maven("https://oss.sonatype.org/content/repositories/snapshots")
		maven("https://maven.architectury.dev/")
		maven("https://maven.minecraftforge.net/")
		maven("https://repo.spongepowered.org/maven/")
		maven("https://repo.sk1er.club/repository/maven-releases/")
	}
	resolutionStrategy {
		eachPlugin {
			when (requested.id.id) {
				"com.replaymod.preprocess" -> useModule("com.github.replaymod:preprocessor:${requested.version}")
				"gg.essential.loom" -> useModule("gg.essential:architectury-loom:${requested.version}")
			}
		}
	}
}
plugins {
	id("moe.nea.shared-variables")
}

rootProject.name = "ultra-notifier"
rootProject.buildFileName = "root.gradle.kts"

Versions.values().forEach { version ->
	include(version.projectPath)
	val p = project(version.projectPath)
	p.projectDir = file("versions/${version.projectName}")
	p.buildFileName = "../../build.gradle.kts"
}
