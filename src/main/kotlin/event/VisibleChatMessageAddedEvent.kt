package moe.nea.ultranotifier.event

import net.minecraft.client.gui.hud.ChatHudLine
import net.minecraft.text.Text

typealias ChattyHudLine =
	ChatHudLine
//#if MC < 1.20
//#if MC > 1.16
//$$	<Text>
//#endif
//#endif


data class VisibleChatMessageAddedEvent(
	val chatLine: ChattyHudLine,
) : UltraEvent()
