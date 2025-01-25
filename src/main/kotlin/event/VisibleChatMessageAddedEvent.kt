package moe.nea.ultranotifier.event

import net.minecraft.client.gui.hud.ChatHudLine


data class VisibleChatMessageAddedEvent(
	val chatLine: ChatHudLine,
) : UltraEvent()
