package moe.nea.sharedbuild

private fun yarn(version: String): String =
	"net.fabricmc:yarn:${version}:v2"

enum class Versions(
	val projectName: String,
	val minecraftVersion: MinecraftVersion,
	val mappingDependency: String,
	val mappingStyle: MappingStyle,
	val forgeDep: String?,
	val fabricVersion: String? = null,
	parentName: String?,
) {
	MC189("1.8.9",
	      MinecraftVersion.MC189,
	      "de.oceanlabs.mcp:mcp_stable:22-1.8.9@zip",
	      MappingStyle.SEARGE,
	      "net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9",
	      null,
	      "MC11202"),
	MC11202("1.12.2",
	        MinecraftVersion.MC11202,
	        "de.oceanlabs.mcp:mcp_stable:39-1.12@zip",
	        MappingStyle.SEARGE,
	        "net.minecraftforge:forge:1.12.2-14.23.5.2847",
	        null,
	        "MC116Forge"),
	MC116Forge("1.16.2-forge",
	           MinecraftVersion.MC11602,
	           "official",
	           MappingStyle.SEARGE,
	           "net.minecraftforge:forge:1.16.2-33.0.61",
	           null,
	           "MC116Fabric"),
	MC116Fabric("1.16.2-fabric",
	            MinecraftVersion.MC11602,
	            yarn("1.16.2+build.47"),
	            MappingStyle.YARN,
	            null,
	            "0.42.0+1.16",
	            "MC12006"
	),
	MC12006(
		"1.20.6",
		MinecraftVersion.MC12006,
		yarn("1.20.6+build.1"),
		MappingStyle.YARN,
		null,
		fabricVersion = "0.99.0+1.20.6",
		null,
	),
	;

	val platformName = if (forgeDep == null) "fabric" else "forge"
	val universalCraft = "gg.essential:universalcraft-${minecraftVersion.versionName}-$platformName:363"
	val needsPack200 = forgeDep != null && minecraftVersion <= MinecraftVersion.MC11202
	val parent: Versions? by lazy {
		if (parentName == null) null
		else Versions.values().find { it.name == parentName }!!
	}

	val projectPath get() = ":$projectName"

	companion object {
		init {
			values().forEach { it.parent }
		}
	}
}
