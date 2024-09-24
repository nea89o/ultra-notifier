import com.replaymod.gradle.preprocess.Node
import moe.nea.sharedbuild.Versions

plugins {
	id("dev.deftu.gradle.preprocess") version "0.7.1"
//	id("fabric-loom") version "1.6-SNAPSHOT" apply false
	kotlin("jvm") version "1.9.23" apply false
	id("gg.essential.loom") version "1.6.+" apply false
	id("dev.architectury.architectury-pack200") version "0.1.3"
	id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
	version = "1.0.0"
	group = "moe.nea.rxcraft"
	repositories {
		mavenCentral()
		maven("https://maven.minecraftforge.net") {
			metadataSources {
				artifact()
			}
		}
		maven("https://repo.spongepowered.org/maven/")
		maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
		maven("https://jitpack.io") {
			content {
				includeGroupByRegex("(io|com)\\.github\\..+")
			}
		}
		maven("https://repo.essential.gg/repository/maven-public")
	}
}

preprocess {
	strictExtraMappings.set(true)
	val nodes = mutableMapOf<Versions, Node>()
	Versions.values().forEach { version ->
		nodes[version] =
			createNode(version.projectName, version.minecraftVersion.versionNumber, version.mappingStyle.identifier)
	}
	Versions.values().forEach { child ->
		val parent = child.parent ?: return@forEach
		val mappingFile = file("versions/mapping-${parent.projectName}-${child.projectName}.txt")
		if (mappingFile.exists()) {
			println("Using mapping file $mappingFile")
			nodes[parent]!!.link(nodes[child]!!, mappingFile)
		} else {
			nodes[parent]!!.link(nodes[child]!!)
			println("Skipping mapping file $mappingFile")
		}
	}
}