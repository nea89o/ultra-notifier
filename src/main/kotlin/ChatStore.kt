package moe.nea.ultranotifier

import moe.nea.ultranotifier.event.ChatGuiLineEvent
import moe.nea.ultranotifier.event.PacketChatLineEvent
import moe.nea.ultranotifier.event.SubscriptionTarget
import moe.nea.ultranotifier.event.UltraSubscribe
import moe.nea.ultranotifier.util.IdentityCharacteristics
import net.minecraft.text.Text

object ChatStore : SubscriptionTarget {

	data class ChatLine(
		val text: Text,
		var fromPacket: Boolean = false,
		var isDisplayed: Boolean = false,
	)

	val allLines = object : LinkedHashMap<IdentityCharacteristics<Text>, ChatLine>() {
		override fun removeEldestEntry(eldest: MutableMap.MutableEntry<IdentityCharacteristics<Text>, ChatLine>?): Boolean {
			return size > 500 // TODO: config
		}
	}

	fun insertChatLine(text: Text): ChatLine {
		return allLines.getOrPut(IdentityCharacteristics(text)) { ChatLine(text) }
	}

	@UltraSubscribe
	fun onMessageDisplayed(event: ChatGuiLineEvent) {
		insertChatLine(event.component).isDisplayed = true
	}

	@UltraSubscribe
	fun onMessageReceived(event: PacketChatLineEvent) {
		insertChatLine(event.component).fromPacket = true
	}
}
