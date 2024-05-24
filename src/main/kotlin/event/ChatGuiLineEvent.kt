package moe.nea.ultranotifier.event

import net.minecraft.text.Text

class ChatGuiLineEvent(val component: Text) : UltraEvent() {
	val string = component.string
	override fun toString(): String {
		return "ChatLineAddedEvent($string)"
	}
}
