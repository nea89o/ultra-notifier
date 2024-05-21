package moe.nea.ultranotifier


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
//#endif
}
