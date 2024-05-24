package moe.nea.sharedbuild

private fun yarn(version: String): String =
	"net.fabricmc:yarn:${version}:v2"

enum class Versions(
	val projectName: String,
	val mappingStyle: String,
	val minecraftVersion: String,
	val mappingDependency: String,
	parentName: String?,
	val forgeDep: String?,
	val needsPack200: Boolean,
	val isBridge: Boolean,

	val fabricVersion: String? = null,
) {
	MC189("1.8.9", "srg", "1.8.9", "de.oceanlabs.mcp:mcp_stable:22-1.8.9@zip", "MC11404F", "net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9", true, false),
	MC11404F("1.14.4-forge", "srg", "1.14.4", "de.oceanlabs.mcp:mcp_stable:58-1.14.4@zip", "MC11404", "net.minecraftforge:forge:1.14.4-28.1.113", false, true),
	MC11404("1.14.4", "yarn", "1.14.4", yarn("1.14.4+build.1"), "MC12006", null, false, true, fabricVersion = "0.23.2+1.14"),
	MC12006("1.20.6", "yarn", "1.20.6", yarn("1.20.6+build.1"), null, null, false, false, fabricVersion = "0.99.0+1.20.6"),
	;

	val parent: Versions? by lazy {
		if (parentName == null) null
		else Versions.values().find { it.name == parentName }!!
	}

	val numericMinecraftVersion = run {
		require(minecraftVersion.count { it == '.' } == 2)
		val (a, b, c) = minecraftVersion.split(".").map { it.toInt() }
		a * 10000 + b * 100 + c
	}
	val projectPath get() = ":$projectName"

	companion object {
		init {
			values().forEach { it.parent }
		}
	}
}
