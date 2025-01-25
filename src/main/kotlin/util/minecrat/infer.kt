@file:OptIn(ExperimentalContracts::class)

package moe.nea.ultranotifier.util.minecrat

import moe.nea.ultranotifier.datamodel.HasCategorizedChatLine
import moe.nea.ultranotifier.event.ChattyHudLine
import net.minecraft.client.gui.hud.ChatHud
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

interface AccessorChatHud {
	val lineHeight_ultranotifier: Int
}

fun ChatHud.accessor(): AccessorChatHud {
	contract {
		returns() implies (this@accessor is AccessorChatHud)
	}
	return this as AccessorChatHud
}

val ChattyHudLine.category get() = (this as HasCategorizedChatLine).categorizedChatLine_ultraNotifier
