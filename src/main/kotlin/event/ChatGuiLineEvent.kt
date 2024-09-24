package moe.nea.ultranotifier.event

import net.minecraft.text.Text

class ChatGuiLineEvent(val component: Text) : UltraEvent() {
	val string =
//#if MC < 1.16
//$$		component.unformattedText // Why does remap not do this automatically? hello?
//#else
		component.string
//#endif

	override fun toString(): String {
		return "ChatLineAddedEvent($string)"
	}
}
