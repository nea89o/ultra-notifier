package moe.nea.ultranotifier.event

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

	fun post() {
		UltraNotifierEvents.post(this)
	}

}


