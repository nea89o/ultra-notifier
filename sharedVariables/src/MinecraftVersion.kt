package moe.nea.sharedbuild

import org.gradle.jvm.toolchain.JavaLanguageVersion

enum class MinecraftVersion(
	val versionName: String,
) {
	MC189("1.8.9"),
	MC11200("1.12"),
	MC11202("1.12.2"),
	MC1144("1.14.4"),
	MC11602("1.16.2"),
	MC11605("1.16.5"),
	MC12006("1.20.6"),
	MC121("1.21"),
	;

	val versionNumber = run {
		val parts = versionName.split('.').mapTo(mutableListOf()) { it.toInt() }
		if (parts.size == 2) parts.add(0)
		require(parts.size == 3)
		parts[0] * 10000 + parts[1] * 100 + parts[2]
	}
	val javaVersion: Int = when {
		versionNumber >= 12005 -> 21
		versionNumber >= 11800 -> 17
		versionNumber >= 11700 -> 16
		else -> 8
	}
	val javaLanguageVersion = JavaLanguageVersion.of(javaVersion)

}
