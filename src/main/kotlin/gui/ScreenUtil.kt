package moe.nea.ultranotifier.gui

import gg.essential.universal.UScreen
import moe.nea.ultranotifier.event.SubscriptionTarget
import moe.nea.ultranotifier.event.TickEvent
import moe.nea.ultranotifier.event.UltraSubscribe
import net.minecraft.client.gui.screen.Screen

object ScreenUtil : SubscriptionTarget {
	var openScreen: Screen? = null

	@UltraSubscribe
	fun onTick(event: TickEvent) {
		openScreen?.let {
			UScreen.displayScreen(it)
			openScreen = null
		}
	}
}
