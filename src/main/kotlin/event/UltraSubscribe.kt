package moe.nea.ultranotifier.event

typealias UltraSubscribe =
//#if FORGE
//$$	net.minecraftforge.eventbus.api.SubscribeEvent
//#else
	me.bush.eventbus.annotation.EventListener
//#endif
