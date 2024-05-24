package moe.nea.ultranotifier

//#if FORGE
//$$import net.minecraftforge.fml.common.Mod
//$$
//#if MC == 10809
//$$import net.minecraftforge.fml.common.event.FMLInitializationEvent
//$$@Mod(modid = Constants.MOD_ID, version = Constants.VERSION, useMetadata = true)
//#else
//$$@Mod(Constants.MOD_ID)
//#endif
//$$class UltraNotifierEntryPoint {
//#if MC == 10809
//$$	@Mod.EventHandler
//$$	fun onInit(@Suppress("UNUSED_PARAMETER") event: FMLInitializationEvent) {
//#else
//$$	init {
//#endif
//$$	    UltraNotifier.onStartup()
//$$	}
//$$}
//#else
import net.fabricmc.api.ModInitializer
class UltraNotifierEntryPoint : ModInitializer {
	override fun onInitialize() {
		UltraNotifier.onStartup()
	}
}
//#endif
