package moe.nea.ultranotifier

import moe.nea.ultranotifier.commands.Commands
import moe.nea.ultranotifier.event.SubscriptionTarget
import moe.nea.ultranotifier.gui.ScreenUtil

object AllModules {
	val allModules: List<SubscriptionTarget> = listOf(
		ChatStore,
		Commands,
		ScreenUtil,
	)
}
