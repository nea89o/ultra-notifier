import com.replaymod.gradle.preprocess.Node
import moe.nea.sharedbuild.Versions

plugins {
	id("com.replaymod.preprocess") version "b09f534"
//	id("fabric-loom") version "1.6-SNAPSHOT" apply false
	kotlin("jvm") version "1.9.23" apply false
	id("gg.essential.loom") version "1.6.+" apply false
	id("dev.architectury.architectury-pack200") version "0.1.3"
	id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

allprojects {
	version = "1.0.0"
	group = "moe.nea.rxcraft"
}

preprocess {
	val nodes = mutableMapOf<Versions, Node>()
	Versions.values().forEach { version ->
		nodes[version] = createNode(version.projectName, version.numericMinecraftVersion, version.mappingStyle)
	}
	Versions.values().forEach { child ->
		val parent = child.parent ?: return@forEach
		nodes[parent]!!.link(nodes[child]!!, file("versions/mapping-${parent.projectName}-${child.projectName}.txt"))
	}
}