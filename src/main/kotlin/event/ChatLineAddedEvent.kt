package moe.nea.ultranotifier.event

import net.minecraft.text.Text

class ChatLineAddedEvent(val component: Text) : UltraEvent() {
	val string = component.string
}
