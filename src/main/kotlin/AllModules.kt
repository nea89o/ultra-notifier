package moe.nea.ultranotifier

import moe.nea.ultranotifier.commands.Commands
import moe.nea.ultranotifier.event.SubscriptionTarget

object AllModules {
	val allModules: List<SubscriptionTarget> = listOf(
		ChatStore,
		Commands
	)
}
