package moe.nea.ultranotifier.event

import moe.nea.ultranotifier.UltraNotifier

object UltraNotifierEvents {
	val eventBus =
//#if FORGE
//$$		net.minecraftforge.common.MinecraftForge.EVENT_BUS
//#else
		me.bush.eventbus.bus.EventBus { UltraNotifier.logger.warn("EventBus: $it") }
//#endif
	@JvmStatic
	fun <T : UltraEvent> post(event: T): T {
		UltraNotifier.logger.info("Posting $event")
		eventBus.post(event)
		return event
	}

	fun register(obj: Any) {
//#if FORGE
//$$		eventBus.register(obj)
//#else
		eventBus.subscribe(obj)
//#endif
	}
}


