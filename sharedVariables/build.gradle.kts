plugins {
	`kotlin-dsl`
	kotlin("plugin.serialization") version "1.4.0"
	`java-gradle-plugin`
}

repositories {
	mavenCentral()
	maven("https://maven.fabricmc.net/")
}

sourceSets {
	main.configure {
		this.kotlin.srcDir(file("src"))
	}
}

dependencies {
//	implementation("net.fabricmc:tiny-remapper:0.8.6")
//	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")


}
gradlePlugin {
	plugins {
		create("simplePlugin") {
			id = "moe.nea.shared-variables"
			implementationClass = "moe.nea.sharedbuild.NoOp"
		}
	}
}
