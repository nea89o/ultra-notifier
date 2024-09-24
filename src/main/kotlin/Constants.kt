package moe.nea.ultranotifier

// TODO: blossom this shit
object Constants {
	const val MOD_ID = "ultranotifier"
	const val VERSION = "1.0.0"

	enum class Platform {
		FORGE, FABRIC
	}

	val PLATFORM: Platform =
//#if FORGE
//$$		Platform.FORGE
//#else
		Platform.FABRIC
//#endif

	const val MINECRAFT_VERSION: String =
//#if MC == 10809
//$$		"1.8.9"
//#elseif MC == 12006
		"1.20.6"
//#elseif MC == 11404
//$$		"1.14.4"
//#elseif MC == 11202
//$$		"1.12.2"
//#elseif MC == 11605
//$$		"1.16.5"
//#elseif MC == 11602
//$$		"1.16.2"
//#endif
}
