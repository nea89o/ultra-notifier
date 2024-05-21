package moe.nea.ultranotifier

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
}

abstract class UltraEvent :
//#if FORGE
//$$	net.minecraftforge.eventbus.api.Event()
//#else
	me.bush.eventbus.event.Event()
//#endif
{
//#if FORGE
//$$	override fun isCancelable(): Boolean {
//$$		return this.isCancellable()
//$$	}
//$$	fun isCancelled(): Boolean {
//$$		return isCanceled()
//$$	}
//$$	fun setCancelled(value: Boolean) {
//$$		setCanceled(value)
//$$	}
//#else
	override
//#endif

	fun isCancellable(): Boolean {
		return true
	}

//#if FORGE == 0
	override
//#endif
	fun cancel() {
		setCancelled(true)
	}

}


class ChatLineAddedEvent() : UltraEvent()
