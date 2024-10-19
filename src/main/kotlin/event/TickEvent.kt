package moe.nea.ultranotifier.event

//#if FABRIC
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//#endif

class TickEvent : UltraEvent(), UltraEvent.Silent<TickEvent> {

	companion object : SubscriptionTarget {
		override fun init() {
//#if FABRIC
			ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
				TickEvent().post()
			})
//#else
//$$			net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(object {
//$$				@UltraSubscribe
//$$				fun onForgeEvent(event: net.minecraftforge.event.TickEvent.ClientTickEvent) {
//$$					if (event.phase == net.minecraftforge.event.TickEvent.Phase.END)
//$$						TickEvent().post()
//$$				}
//$$			})
//#endif
		}
	}
}
