@file:OptIn(ExperimentalContracts::class)

package moe.nea.ultranotifier.util.minecrat

import moe.nea.ultranotifier.mixin.AccessorChatHud
import net.minecraft.client.gui.hud.ChatHud
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun ChatHud.accessor(): AccessorChatHud {
	contract {
		returns() implies (this@accessor is AccessorChatHud)
	}
	return this as AccessorChatHud
}
